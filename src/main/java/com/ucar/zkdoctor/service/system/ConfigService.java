package com.ucar.zkdoctor.service.system;

import com.ucar.zkdoctor.pojo.po.SysConfig;

import java.util.List;

/**
 * Description: 系统相关配置服务接口
 * Created on 2018/1/23 13:56
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public interface ConfigService {

    /**
     * 系统初始化的时候，加载指定系统配置项
     */
    void initConfig();

    /**
     * 保存新配置信息
     *
     * @param sysConfig 配置信息
     * @return
     */
    boolean insertSysConfig(SysConfig sysConfig);

    /**
     * 获取指定配置信息
     *
     * @param configName 配置名称
     * @return
     */
    SysConfig getSysConfigByName(String configName);

    /**
     * 获取所有配置信息
     *
     * @return
     */
    List<SysConfig> getAllSysConfig();

    /**
     * 删除某项配置信息
     *
     * @param configName 配置名称
     * @return
     */
    boolean deleteSystemByName(String configName);

    /**
     * 获取所有配置信息，包含默认配置信息
     *
     * @return
     */
    List<SysConfig> getAllSysConfigIncludeDefault();

}
