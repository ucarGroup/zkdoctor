package com.ucar.zkdoctor.service.schedule.jobs;

import com.ucar.zkdoctor.service.collection.CollectService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * Description: 每天晚上1点，根据配置的保存天数信息，清除历史统计数据
 * Created on 2018/1/30 9:03
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class CleanCollectDataJob extends BaseJob {

    private static final long serialVersionUID = 357426219319968381L;

    private static final Logger LOGGER = LoggerFactory.getLogger(CleanCollectDataJob.class);

    @Resource
    private CollectService collectService;

    @Override
    public void action(JobExecutionContext context) {
        try {
            collectService.cleanCollectData();
        } catch (Exception e) {
            LOGGER.error("CleanCollectDataJob execute failed.", e);
        }
    }
}
