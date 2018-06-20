package com.ucar.zkdoctor.business.clusterMonitorAlarmTask.controller;

import com.ucar.zkdoctor.business.clusterMonitorAlarmTask.pojo.ZkMonitorTask;
import com.ucar.zkdoctor.business.clusterMonitorAlarmTask.service.ZkMonitorTaskService;
import com.ucar.zkdoctor.web.ConResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/monitorAlarm")
public class ZkMonitorTaskController {
    @Autowired
    private ZkMonitorTaskService zkMonitorTaskService;

    @RequestMapping(value = "/getZkMonitorTaskList", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ConResult getZkMonitorTaskList() {
        return zkMonitorTaskService.getZkMonitorTaskList();
    }

    @RequestMapping(value = "/getAllZkMonitorTask", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ConResult getAllZkMonitorTask() {
        return zkMonitorTaskService.getAllZkMonitorTask();
    }

    @RequestMapping(value = "/getAllZkMonitorTaskData")
    @ResponseBody
    public List<ZkMonitorTask> getAllZkMonitorTaskData() {
        return zkMonitorTaskService.selectAll();
    }

    @RequestMapping(value = "/getZkMonitorTaskDataByClusterId")
    @ResponseBody
    public ConResult getZkMonitorTaskDataByClusterId(@RequestParam(value = "clusterId") Integer clusterId) {
        return zkMonitorTaskService.selectByClusterId(clusterId);
    }

    /**
     * 修改指定监控任务默认开关信息
     *
     * @param monitorTaskId 监控任务id
     * @param switchOn      开关。监控任务是否开启，0：关闭，1：开启
     * @return
     */
    @RequestMapping("/updateSwitchOn")
    @ResponseBody
    public ConResult updateSwitchOn(
            @RequestParam(value = "monitorTaskId") Integer monitorTaskId,
            @RequestParam(value = "switchOn") Boolean switchOn) {
        ZkMonitorTask zkMonitorTask = new ZkMonitorTask();
        zkMonitorTask.setId(monitorTaskId);
        zkMonitorTask.setSwitchOn(switchOn);
        return zkMonitorTaskService.updateMonitorTaskSwitchOn(zkMonitorTask);
    }

    /**
     * 更新监控任务相关信息
     *
     * @param zkMonitorTask 监控任务对象
     * @return
     */

    @RequestMapping(value = "/updateMonitorTask")
    @ResponseBody
    public ConResult updateMonitorTask(ZkMonitorTask zkMonitorTask) {
        return zkMonitorTaskService.updateByPrimaryKey(zkMonitorTask);
    }
}
