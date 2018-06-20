package com.ucar.zkdoctor.service.schedule.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Description: 定时任务基类，记录job执行时间
 * Created on 2018/1/28 22:07
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public abstract class BaseJob implements Job, Serializable {

    private static final long serialVersionUID = 3796069056795105260L;

    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseJob.class);

    // 定时任务逻辑
    public abstract void action(JobExecutionContext context);

    /**
     * 统计时间
     *
     * @param context
     * @throws org.quartz.JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        long start = System.currentTimeMillis();
        this.action(context);
        long end = System.currentTimeMillis();
        LOGGER.info("Job: {}, trigger: {}, cost: {} ms", context.getJobDetail().getKey(),
                context.getTrigger().getKey(), (end - start));
    }
}
