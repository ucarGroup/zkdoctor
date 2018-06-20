package com.ucar.zkdoctor.dao.mysql;

import com.ucar.zkdoctor.pojo.po.TriggerInfo;

import java.util.List;

/**
 * Description: 定时任务Dao
 * Created on 2018/2/5 16:58
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public interface QuartzDao {

    /**
     * 获取所有定时任务详细信息
     *
     * @return
     */
    List<TriggerInfo> getAllTriggers();

    /**
     * 通过名称或组查询定时任务信息
     *
     * @param queryString 查询信息字符串
     * @return
     */
    List<TriggerInfo> searchTriggerByNameOrGroup(String queryString);
}
