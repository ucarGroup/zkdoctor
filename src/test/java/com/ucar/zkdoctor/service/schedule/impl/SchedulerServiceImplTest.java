package com.ucar.zkdoctor.service.schedule.impl;

import com.ucar.zkdoctor.BaseTest;
import com.ucar.zkdoctor.pojo.po.TriggerInfo;
import com.ucar.zkdoctor.service.schedule.SchedulerService;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description: 定时任务测试类
 * Created on 2018/2/5 17:09
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class SchedulerServiceImplTest extends BaseTest {

    @Resource
    private SchedulerService schedulerService;

    @Test
    public void getTrigger() throws Exception {
        List<TriggerInfo> triggerInfoList = schedulerService.getAllTriggers();
        if (CollectionUtils.isNotEmpty(triggerInfoList)) {
            for (TriggerInfo triggerInfo : triggerInfoList) {
                System.out.println(triggerInfo);
            }
        } else {
            System.out.println("triggerInfoList is NULL");
        }
    }

    @Test
    public void testGetTriggersByNameOrGroup() throws Exception {
        List<TriggerInfo> triggerInfoList = schedulerService.getTriggersByNameOrGroup("clean");
        if (CollectionUtils.isNotEmpty(triggerInfoList)) {
            for (TriggerInfo triggerInfo : triggerInfoList) {
                System.out.println(triggerInfo);
            }
        } else {
            System.out.println("triggerInfoList is NULL");
        }
    }
}