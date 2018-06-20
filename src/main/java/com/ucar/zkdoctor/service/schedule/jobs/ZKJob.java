package com.ucar.zkdoctor.service.schedule.jobs;

import com.ucar.zkdoctor.service.collection.CollectService;
import com.ucar.zkdoctor.util.constant.SchedulerConstant;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * Description: 实例以及集群zk服务端信息收集定时任务，一分钟执行一次
 * Created on 2018/1/28 22:03
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ZKJob extends BaseJob {

    private static final long serialVersionUID = -5377061873474758885L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ZKJob.class);

    @Resource
    private CollectService collectService;

    @Override
    public void action(JobExecutionContext context) {
        try {
            JobDataMap dataMap = context.getMergedJobDataMap();
            int clusterId = dataMap.getInt(SchedulerConstant.CLUSTER_KEY);
            collectService.collectZKInfo(clusterId);
        } catch (Exception e) {
            LOGGER.error("ZKJob execute failed.", e);
        }
    }
}

