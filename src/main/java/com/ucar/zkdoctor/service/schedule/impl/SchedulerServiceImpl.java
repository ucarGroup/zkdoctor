package com.ucar.zkdoctor.service.schedule.impl;

import com.ucar.zkdoctor.dao.mysql.QuartzDao;
import com.ucar.zkdoctor.pojo.po.TriggerInfo;
import com.ucar.zkdoctor.service.schedule.SchedulerService;
import com.ucar.zkdoctor.util.constant.SchedulerConstant;
import org.apache.commons.collections4.CollectionUtils;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Description: TODO
 * Created on 2018/1/16 10:55
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Service("schedulerService")
public class SchedulerServiceImpl implements SchedulerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerServiceImpl.class);

    @Resource
    private Scheduler scheduler;

    @Resource
    private QuartzDao quartzDao;

    @Override
    public boolean unscheduleJob(TriggerKey triggerKey) {
        try {
            boolean opResult = scheduler.checkExists(triggerKey);
            if (opResult) {
                opResult = scheduler.unscheduleJob(triggerKey);
            } else {
                return true;
            }
            return opResult;
        } catch (SchedulerException e) {
            LOGGER.error("Unschedule job trigger group {}, trigger name {} failed.",
                    triggerKey.getGroup(), triggerKey.getName(), e);
            return false;
        }
    }

    @Override
    public boolean deployJobByCron(JobKey jobKey, TriggerKey triggerKey, Map<String, Object> dataMap, String cron, boolean replace) {
        if (jobKey == null || triggerKey == null || !CronExpression.isValidExpression(cron)) {
            return false;
        }
        try {
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null) {
                LOGGER.error("JobKey {}:{} is not exist", jobKey.getGroup(), jobKey.getName());
                return false;
            }
            fireCronTrigger(triggerKey, jobDetail, cron, replace, dataMap);
        } catch (SchedulerException e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }

        return true;
    }

    private boolean fireCronTrigger(TriggerKey triggerKey, JobDetail jobDetail, String cron, boolean replace, Map<String, Object> dataMap) {
        try {
            boolean isExists = scheduler.checkExists(triggerKey);
            if (isExists) {
                if (replace) {
                    LOGGER.warn("Fire corn trigger: replace trigger={}:{}.", triggerKey.getGroup(), triggerKey.getName());
                    scheduler.unscheduleJob(triggerKey);
                } else {
                    LOGGER.info("Fire corn trigger: exist trigger={}:{}.", triggerKey.getGroup(), triggerKey.getName());
                    return false;
                }
            }
            // 创建触发器，一分钟后开始执行
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .forJob(jobDetail)
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                    .startAt(new Date(System.currentTimeMillis() + 60000))
                    .build();
            if (dataMap != null && dataMap.size() > 0) {
                trigger.getJobDataMap().putAll(dataMap);
            }
            scheduler.scheduleJob(trigger);
        } catch (SchedulerException e) {
            LOGGER.error("Fire corn trigger failed.", e);
            return false;
        }
        return true;
    }

    @Override
    public List<TriggerInfo> getAllTriggers() {
        return appendDescForTrigger(quartzDao.getAllTriggers());
    }

    @Override
    public List<TriggerInfo> getTriggersByNameOrGroup(String query) {
        return appendDescForTrigger(quartzDao.searchTriggerByNameOrGroup(query));
    }

    @Override
    public boolean pauseTrigger(TriggerKey triggerKey) {
        try {
            boolean exists = scheduler.checkExists(triggerKey);
            if (exists) {
                scheduler.pauseTrigger(triggerKey);
                return true;
            }
        } catch (SchedulerException e) {
            LOGGER.error("Pause Trigger:{}-{} failed.", triggerKey.getGroup(), triggerKey.getName(), e);
        }
        return false;
    }

    @Override
    public boolean resumeTrigger(TriggerKey triggerKey) {
        try {
            boolean exists = scheduler.checkExists(triggerKey);
            if (exists) {
                scheduler.resumeTrigger(triggerKey);
                return true;
            }
            LOGGER.error("Resume Trigger:{}-{} failed because triggerKey not exists.", triggerKey.getGroup(), triggerKey.getName());
            return false;
        } catch (Exception e) {
            LOGGER.error("Resume Trigger:{}-{} failed.", triggerKey.getGroup(), triggerKey.getName(), e);
            return false;
        }
    }

    @Override
    public boolean removeTrigger(TriggerKey triggerKey) {
        try {
            boolean exists = scheduler.checkExists(triggerKey);
            if (exists) {
                return scheduler.unscheduleJob(triggerKey);
            }
        } catch (SchedulerException e) {
            LOGGER.error("Remove Trigger:{}-{} failed.", triggerKey.getGroup(), triggerKey.getName(), e);
        }
        return false;
    }

    /**
     * 增加定时任务触发器的描述信息
     *
     * @param triggerInfoList 触发器信息列表
     * @return
     */
    private List<TriggerInfo> appendDescForTrigger(List<TriggerInfo> triggerInfoList) {
        if (CollectionUtils.isNotEmpty(triggerInfoList)) {
            for (TriggerInfo triggerInfo : triggerInfoList) {
                if (SchedulerConstant.ZK_COLLECT_TRIGGER_NAME.equals(triggerInfo.getTriggerName())) {
                    triggerInfo.setDescription(SchedulerConstant.ZK_COLLECT_JOB_DESC);
                } else if (SchedulerConstant.ZK_CLEAN_TRIGGER_NAME.equals(triggerInfo.getTriggerName())) {
                    triggerInfo.setDescription(SchedulerConstant.ZK_CLEAN_JOB_DESC);
                } else if (SchedulerConstant.ZK_MONITOR_TRIGGER_NAME.equals(triggerInfo.getTriggerName())) {
                    triggerInfo.setDescription(SchedulerConstant.ZK_MONITOR_JOB_DESC);
                } else if (SchedulerConstant.MACHINE_COLLECT_TRIGGER_NAME.equals(triggerInfo.getTriggerName())) {
                    triggerInfo.setDescription(SchedulerConstant.MACHINE_COLLECT_JOB_DESC);
                } else if (SchedulerConstant.INSTANCE_CONN_COLLECT_TRIGGER_NAME.equals(triggerInfo.getTriggerName())) {
                    triggerInfo.setDescription(SchedulerConstant.INSTANCE_CONN_COLLECT_JOB_DESC);
                }
            }
        }
        return triggerInfoList;
    }
}