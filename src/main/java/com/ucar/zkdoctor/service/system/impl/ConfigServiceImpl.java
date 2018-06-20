package com.ucar.zkdoctor.service.system.impl;

import com.ucar.zkdoctor.dao.mysql.SysConfigDao;
import com.ucar.zkdoctor.pojo.po.SysConfig;
import com.ucar.zkdoctor.service.system.ConfigService;
import com.ucar.zkdoctor.util.config.ModifiableConfig;
import com.ucar.zkdoctor.util.tool.ReflectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 系统相关配置服务接口实现类
 * Created on 2018/1/23 13:56
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Service("configService")
public class ConfigServiceImpl implements ConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigServiceImpl.class);

    @Resource
    private SysConfigDao sysConfigDao;

    /**
     * 系统启动时，首次加载可变更的配置字段
     */
    private static final List<Field> MODIFIABLE_CONFIG_FIELDS = new ArrayList<Field>();

    /**
     * 系统启动时，首次加载可变更的系统配置默认信息
     */
    private static volatile Map<String, SysConfig> initSysConfigMap = new HashMap<String, SysConfig>();

    @Override
    public void initConfig() {
        ReflectUtils.getPublicFields(ModifiableConfig.class, MODIFIABLE_CONFIG_FIELDS);
        if (MODIFIABLE_CONFIG_FIELDS.size() < 1) {
            LOGGER.info("Init modifiable configs: the field of ModifiableConfig is empty.");
            return;
        }
        for (Field field : MODIFIABLE_CONFIG_FIELDS) {
            // 有单独的配置，则直接加载，如果没有进行配置，则走ModifiableConfig中的默认值
            SysConfig sysConfig = sysConfigDao.getSysConfigByName(field.getName());
            Object defaultValue = null;
            try {
                defaultValue = field.get(ModifiableConfig.class);
            } catch (IllegalAccessException e) {
                LOGGER.warn("Get default value of {} failed.",
                        sysConfig != null ? sysConfig.getConfigName() : "", e);
            }
            if (sysConfig != null) {
                try {
                    ReflectUtils.setField(ModifiableConfig.class, field, sysConfig.getConfigValue());
                    LOGGER.info("Init modifiable configs of filed {} to {} in class ModifiableConfig.",
                            field.getName(), sysConfig.getConfigValue());
                } catch (IllegalAccessException e) {
                    LOGGER.warn("Init modifiable configs of filed {} in class ModifiableConfig failed, DB info is {}.",
                            field.getName(), sysConfig, e);
                }
            } else {
                sysConfig = new SysConfig();
                sysConfig.setConfigName(field.getName());
            }
            if (defaultValue != null) {
                sysConfig.setDefaultConfigValue(String.valueOf(defaultValue));
            }
            // 加载配置值的描述信息
            Object configDesc = null;
            try {
                Field descField = ReflectUtils.getField(ModifiableConfig.class, field.getName() + "Desc");
                if (descField != null) {
                    descField.setAccessible(true);
                    configDesc = descField.get(ModifiableConfig.class);
                }
            } catch (SecurityException e) {
                LOGGER.warn("Init modifiable config description of filed {}Desc in class ModifiableConfig failed.",
                        field.getName(), e);
            } catch (IllegalAccessException e) {
                LOGGER.warn("Init modifiable config description of filed {}Desc in class ModifiableConfig failed.",
                        field.getName(), e);
            }
            if (configDesc != null) {
                sysConfig.setConfigDesc(String.valueOf(configDesc));
            }
            initSysConfigMap.put(field.getName(), sysConfig);
        }
    }

    @Override
    public boolean insertSysConfig(SysConfig sysConfig) {
        if (sysConfig == null) {
            return false;
        }
        boolean insertOrUpdate = sysConfigDao.insertSysConfig(sysConfig);
        if (insertOrUpdate) {
            // 同时更新缓存数据
            Field configField = ReflectUtils.getField(ModifiableConfig.class, sysConfig.getConfigName());
            if (configField != null) {
                try {
                    ReflectUtils.setField(ModifiableConfig.class, configField, sysConfig.getConfigValue());
                    LOGGER.info("Insert or update the value filed {} to {} in class ModifiableConfig.",
                            sysConfig.getConfigName(), sysConfig.getConfigValue());
                    return true;
                } catch (IllegalAccessException e) {
                    LOGGER.warn("Insert or update the value filed {} to {} in class ModifiableConfig failed.",
                            sysConfig.getConfigName(), sysConfig.getConfigValue(), e);
                    throw new RuntimeException("更新缓存配置值失败，请重试");
                }
            } else {
                throw new RuntimeException("查询配置值字段失败，请重试");
            }
        } else {
            throw new RuntimeException("更新配置值失败，请重试");
        }
    }

    @Override
    public SysConfig getSysConfigByName(String configName) {
        return sysConfigDao.getSysConfigByName(configName);
    }

    @Override
    public List<SysConfig> getAllSysConfig() {
        return sysConfigDao.getAllSysConfig();
    }

    @Override
    public boolean deleteSystemByName(String configName) {
        boolean deleteConfig = sysConfigDao.deleteSysConfigByName(configName);
        if (deleteConfig) {
            // 同时更新缓存数据
            Field configField = ReflectUtils.getField(ModifiableConfig.class, configName);
            if (configField != null) {
                SysConfig sysConfig = initSysConfigMap.get(configName);
                // 初始化未加载到默认值
                if (sysConfig == null) {
                    LOGGER.warn("Delete the value field {}.However the init config is null, failing to restore the default value.",
                            configName);
                    throw new RuntimeException("无初始化缓存配置信息，请重试或重启系统");
                }
                // 重置缓存配置值
                initSysConfigMap.get(configName).setConfigValue(null);
                try {
                    // 缓存恢复默认配置
                    ReflectUtils.setField(ModifiableConfig.class, configField, sysConfig.getDefaultConfigValue());
                    LOGGER.info("Delete the value filed {}.", configName);
                    return true;
                } catch (IllegalAccessException e) {
                    LOGGER.warn("Delete the value filed {}.", configName);
                    throw new RuntimeException("删除缓存配置值失败，请重试");
                }
            } else {
                throw new RuntimeException("查询配置值字段失败，请重试");
            }
        } else {
            throw new RuntimeException("删除配置值失败，请检查是否对配置值进行了配置");
        }
    }

    @Override
    public List<SysConfig> getAllSysConfigIncludeDefault() {
        // 以初始化的配置信息为准
        List<SysConfig> sysConfigList = new ArrayList<SysConfig>();
        for (Map.Entry<String, SysConfig> sysConfigEntry : initSysConfigMap.entrySet()) {
            SysConfig sysConfig = getSysConfigByName(sysConfigEntry.getKey());
            if (sysConfig == null) { // 直接取初始化数据
                // 数据库中没有指定配置，则初始化配置值信息为空
                sysConfigEntry.getValue().setConfigValue(null);
                sysConfig = sysConfigEntry.getValue();
            } else { // 进行过更新，加载默认值
                sysConfig.setDefaultConfigValue(sysConfigEntry.getValue().getDefaultConfigValue());
            }
            // 加载配置描述
            sysConfig.setConfigDesc(sysConfigEntry.getValue().getConfigDesc());
            sysConfigList.add(sysConfig);
        }
        return sysConfigList;
    }
}
