package com.ucar.zkdoctor.service.monitor.impl;

import com.ucar.zkdoctor.BaseTest;
import com.ucar.zkdoctor.pojo.po.MonitorIndicator;
import com.ucar.zkdoctor.pojo.po.MonitorTask;
import com.ucar.zkdoctor.service.monitor.MonitorService;
import com.ucar.zkdoctor.util.constant.CommonEnumClass;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description: 监控服务测试类
 * Created on 2018/2/6 13:44z
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class MonitorServiceImplTest extends BaseTest {

    @Resource
    private MonitorService monitorService;

    @Test
    public void initMonitor() throws Exception {
        monitorService.initMonitor();
    }

    @Test
    public void testInsertIndicator() throws Exception {
        MonitorIndicator monitorIndicator = new MonitorIndicator();
        monitorIndicator.setIndicatorName("测试监控");
        monitorIndicator.setClassName(this.getClass().getSimpleName());
        monitorIndicator.setDefaultAlertValue("100000");
        monitorIndicator.setDefaultAlertInterval(30);
        monitorIndicator.setDefaultAlertFrequency(5);
        monitorIndicator.setDefaultAlertForm(CommonEnumClass.AlertForm.MAIL.getAlert());
        monitorIndicator.setSwitchOn(true);
        monitorIndicator.setAlertValueUnit("个");
        boolean result = monitorService.insertIndicator(monitorIndicator);
        System.out.println("result is " + result + "," + monitorIndicator);
    }

    @Test
    public void testUpdateIndicator() throws Exception {
        MonitorIndicator monitorIndicator = monitorService.getIndicatorByIndicatorId(1);
        if (monitorIndicator != null) {
            System.out.println(monitorIndicator);
            monitorIndicator.setDefaultAlertValue("222");
            boolean result = monitorService.updateIndicator(monitorIndicator);
            System.out.println("result is " + result + "," + monitorService.getIndicatorByIndicatorId(1));
        }
    }

    @Test
    public void testUpdateTaskSwitchOn() throws Exception {
        boolean result = monitorService.updateTaskSwitchOn(1, false);
        System.out.println("result is " + result);
    }

    @Test
    public void testUpdateIndicatorSwitchOn() throws Exception {
        boolean result = monitorService.updateIndicatorSwitchOn(1, false);
        System.out.println("result is " + result);
    }

    @Test
    public void testGetIndicatorByClassName() throws Exception {
        MonitorIndicator monitorIndicator = monitorService.getIndicatorByClassName(this.getClass().getSimpleName());
        if (monitorIndicator != null) {
            System.out.println(monitorIndicator);
        } else {
            System.out.println("Result is NULL.");
        }
    }

    @Test
    public void testGetIndicatorsByParams() throws Exception {
        List<MonitorIndicator> indicators = monitorService.getIndicatorsByParams(null);
        if (CollectionUtils.isNotEmpty(indicators)) {
            for (MonitorIndicator indicator : indicators) {
                System.out.println(indicator);
            }
        } else {
            System.out.println("Result is NULL.");
        }
    }

    @Test
    public void testDeleteIndicatorByIndicatorId() throws Exception {
        boolean result = monitorService.deleteIndicatorByIndicatorId(1);
        System.out.println("result is " + result);
    }

    @Test
    public void testInsertTask() throws Exception {
        MonitorTask monitorTask = new MonitorTask();
        monitorTask.setIndicatorId(1);
        monitorTask.setClusterId(2);
        monitorTask.setAlertForm(CommonEnumClass.AlertForm.MAIL.getAlert());
        monitorTask.setAlertFrequency(10);
        monitorTask.setAlertInterval(1);
        monitorTask.setAlertValue("1000");
        monitorTask.setSwitchOn(true);
        boolean result = monitorService.insertTask(monitorTask);
        System.out.println("result is " + result + "," + monitorTask);
    }

    @Test
    public void testUpdateTask() throws Exception {
        MonitorTask monitorTask = monitorService.getTaskByTaskId(1);
        if (monitorTask != null) {
            System.out.println(monitorTask);
            monitorTask.setSwitchOn(false);
            boolean result = monitorService.updateTask(monitorTask);
            System.out.println("result is " + result + "," + monitorService.getTaskByTaskId(1));
        }
    }

    @Test
    public void testGetTasksByParams() throws Exception {
        List<MonitorTask> tasks = monitorService.getTasksByParams(null);
        if (CollectionUtils.isNotEmpty(tasks)) {
            for (MonitorTask monitorTask : tasks) {
                System.out.println(monitorTask);
            }
        } else {
            System.out.println("Result is NULL.");
        }
    }

    @Test
    public void testGetTasksByParams1() throws Exception {
        List<MonitorTask> tasks = monitorService.getTasksByParams(1, 1, true);
        if (CollectionUtils.isNotEmpty(tasks)) {
            for (MonitorTask monitorTask : tasks) {
                System.out.println(monitorTask);
            }
        } else {
            System.out.println("Result is NULL.");
        }
    }

    @Test
    public void testDeleteTaskByTaskId() throws Exception {
        boolean result = monitorService.deleteTaskByTaskId(1);
        System.out.println("result is " + result);
    }

    @Test
    public void testDeleteTaskByIndicatorId() throws Exception {
        boolean result = monitorService.deleteTaskByIndicatorId(1);
        System.out.println("result is " + result);
    }
}