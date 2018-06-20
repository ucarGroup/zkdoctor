package com.ucar.zkdoctor.service.schedule.jobs;

import com.ucar.zkdoctor.service.monitor.MonitorBase;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * Description: 监控定时任务
 * Created on 2018/2/6 16:24
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class MonitorJob extends BaseJob {

    private static final long serialVersionUID = -7416220048861930099L;

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorJob.class);

    @Resource
    private MonitorBase monitorBase;

    @Override
    public void action(JobExecutionContext context) {
        try {
            monitorBase.execute();
        } catch (Exception e) {
            LOGGER.error("MonitorJob execute failed.", e);
        }
    }
}
