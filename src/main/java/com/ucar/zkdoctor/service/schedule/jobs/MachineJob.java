package com.ucar.zkdoctor.service.schedule.jobs;

import com.ucar.zkdoctor.service.collection.CollectService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * Description: 机器监控定时任务，每一分钟执行一次
 * Created on 2018/2/22 9:24
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class MachineJob extends BaseJob {

    private static final long serialVersionUID = 9107115062681338556L;

    private static final Logger LOGGER = LoggerFactory.getLogger(MachineJob.class);

    @Resource
    private CollectService collectService;

    @Override
    public void action(JobExecutionContext context) {
        try {
            collectService.collectAllMachineState();
        } catch (Exception e) {
            LOGGER.error("MachineJob execute failed.", e);
        }
    }
}
