package com.ucar.zkdoctor.service.schedule.jobs;

import com.ucar.zkdoctor.service.collection.CollectService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * Description: 实例信息收集定时任务，每一分钟执行一次
 * Created on 2018/2/23 11:21
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class InstanceConnectionCollectJob extends BaseJob {

    private static final long serialVersionUID = -1210837346182355771L;

    private static final Logger LOGGER = LoggerFactory.getLogger(InstanceConnectionCollectJob.class);

    @Resource
    private CollectService collectService;

    @Override
    public void action(JobExecutionContext context) {
        try {
            collectService.collectAllInstanceConnections();
        } catch (Exception e) {
            LOGGER.error("InstanceConnectionCollectJob execute failed.", e);
        }
    }
}
