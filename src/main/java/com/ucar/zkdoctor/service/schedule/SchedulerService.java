package com.ucar.zkdoctor.service.schedule;

import com.ucar.zkdoctor.pojo.po.TriggerInfo;
import org.quartz.JobKey;
import org.quartz.TriggerKey;

import java.util.List;
import java.util.Map;

/**
 * Description: 定时任务
 * Created on 2018/1/16 10:55
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public interface SchedulerService {

    /**
     * 移除定时任务
     *
     * @param triggerKey
     * @return
     */
    boolean unscheduleJob(TriggerKey triggerKey);

    /**
     * 根据cron部署Job
     *
     * @param jobKey
     * @param triggerKey
     * @param dataMap
     * @param cron
     * @param replace
     * @return
     */
    boolean deployJobByCron(JobKey jobKey, TriggerKey triggerKey, Map<String, Object> dataMap, String cron, boolean replace);

    /**
     * 获取所有trigger
     *
     * @return
     */
    List<TriggerInfo> getAllTriggers();

    /**
     * 模糊查询trigger
     *
     * @return
     */
    List<TriggerInfo> getTriggersByNameOrGroup(String query);

    /**
     * 暂停trigger
     *
     * @param triggerKey triggerKey
     * @return
     */
    boolean pauseTrigger(TriggerKey triggerKey);

    /**
     * 恢复trigger
     *
     * @param triggerKey triggerKey
     * @return
     */
    boolean resumeTrigger(TriggerKey triggerKey);

    /**
     * 删除定时任务
     *
     * @param triggerKey triggerKey
     * @return
     */
    boolean removeTrigger(TriggerKey triggerKey);
}
