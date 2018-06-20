package com.ucar.zkdoctor.web.controller.manager;

import com.ucar.zkdoctor.business.clusterMonitorAlarmTask.pojo.ZkMonitorTask;
import com.ucar.zkdoctor.business.clusterMonitorAlarmTask.service.ZkMonitorTaskService;
import com.ucar.zkdoctor.pojo.po.MonitorIndicator;
import com.ucar.zkdoctor.service.monitor.MonitorService;
import com.ucar.zkdoctor.web.ConResult;
import com.ucar.zkdoctor.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Description: 监控指标相关管理员操作Controller
 * Created on 2018/2/7 11:51
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Controller
@RequestMapping("/manage/monitor")
public class MonitorManageController extends BaseController {

    @Resource
    private MonitorService monitorService;
    @Autowired
    private ZkMonitorTaskService zkMonitorTaskService;

    /**
     * 更新指定监控指标默认开关信息
     *
     * @param id       监控指标id
     * @param switchOn 开关
     * @return
     */
    @RequestMapping("/indicator/updateSwitchOn")
    @ResponseBody
    public ConResult doUpdateSwitchOnIndicator(Integer id, Boolean switchOn) {
        if (id == null || switchOn == null) {
            return ConResult.fail("监控指标id或开关信息为NULL，请重新尝试。");
        }
        boolean update = monitorService.updateIndicatorSwitchOn(id, switchOn);
        if (update) {
            return ConResult.success();
        } else {
            return ConResult.fail("修改监控指标默认开关信息失败，请重新尝试。");
        }
    }

    /**
     * 更新监控指标相关信息
     *
     * @param monitorIndicator 监控指标
     * @return
     */
    @RequestMapping("/indicator/updateIndicator")
    @ResponseBody
    public ConResult doUpdateIndicator(MonitorIndicator monitorIndicator) {
        if (monitorIndicator == null) {
            return ConResult.fail("监控指标信息为NULL，请重新尝试。");
        }
        boolean update = monitorService.updateIndicator(monitorIndicator);
        if (update) {
            return new ConResult(true, "修改监控指标信息成功！", null);
        } else {
            return ConResult.fail("修改监控指标信息失败，请重新尝试。");
        }
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
