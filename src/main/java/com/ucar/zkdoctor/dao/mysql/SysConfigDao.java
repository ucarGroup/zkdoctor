package com.ucar.zkdoctor.dao.mysql;

import com.ucar.zkdoctor.pojo.po.SysConfig;

import java.util.List;

/**
 * Description: 配置信息Dao
 * Created on 2018/1/23 14:02
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public interface SysConfigDao {
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
    boolean deleteSysConfigByName(String configName);
}
