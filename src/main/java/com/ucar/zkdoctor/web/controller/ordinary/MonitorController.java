package com.ucar.zkdoctor.web.controller.ordinary;

import com.ucar.zkdoctor.business.clusterMonitorAlarmTask.service.ZkMonitorTaskService;
import com.ucar.zkdoctor.pojo.bo.MonitorIndicatorSearchBO;
import com.ucar.zkdoctor.pojo.bo.MonitorTaskSearchBO;
import com.ucar.zkdoctor.pojo.po.MonitorIndicator;
import com.ucar.zkdoctor.pojo.po.MonitorTask;
import com.ucar.zkdoctor.pojo.vo.PageVO;
import com.ucar.zkdoctor.service.monitor.MonitorService;
import com.ucar.zkdoctor.web.ConResult;
import com.ucar.zkdoctor.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 监控相关功能Controller
 * Created on 2018/2/7 11:51
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Controller
@RequestMapping("/monitor")
public class MonitorController extends BaseController {

    @Resource
    private MonitorService monitorService;

    @Autowired
    private ZkMonitorTaskService zkMonitorTaskService;

    /**
     * 查询相关监控指标
     *
     * @param monitorIndicatorSearchBO 监控指标查询条件
     * @return
     */
    @RequestMapping("/indicator/query")
    @ResponseBody
    public ConResult doQueryIndicator(MonitorIndicatorSearchBO monitorIndicatorSearchBO) {
        List<MonitorIndicator> monitorIndicatorList = monitorService.getIndicatorsByParams(monitorIndicatorSearchBO);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", monitorIndicatorList == null ? new ArrayList<MonitorIndicator>() : monitorIndicatorList);
        PageVO pageVO = new PageVO();
        if (monitorIndicatorList != null) {
            pageVO.setTotal(monitorIndicatorList.size());
        }
        result.put("page", pageVO);
        return ConResult.success(result);
    }

    /**
     * 查询相关监控任务
     *
     * @param monitorTaskSearchBO 监控任务查询条件
     * @return
     */
    @RequestMapping("/tasks/query")
    @ResponseBody
    public ConResult doQueryTask(MonitorTaskSearchBO monitorTaskSearchBO) {
        List<MonitorTask> monitorTaskList = monitorService.getTasksByParams(monitorTaskSearchBO);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", monitorTaskList == null ? new ArrayList<MonitorTask>() : monitorTaskList);
        PageVO pageVO = new PageVO();
        if (monitorTaskList != null) {
            pageVO.setTotal(monitorTaskList.size());
        }
        result.put("page", pageVO);
        return ConResult.success(result);
    }

    /**
     * 更新指定监控任务开关信息
     *
     * @param id       监控任务id
     * @param switchOn 开关
     * @return
     */
    @RequestMapping("/task/updateSwitchOn")
    @ResponseBody
    public ConResult doUpdateSwitchOnTask(Integer id, Boolean switchOn) {
        if (id == null || switchOn == null) {
            return ConResult.fail("任务id或开关信息为NULL，请重新尝试。");
        }
        boolean update = monitorService.updateTaskSwitchOn(id, switchOn);
        if (update) {
            return ConResult.success();
        } else {
            return ConResult.fail("修改失败或集群未监控/下线或对应监控指标关闭，请检查后重试");
        }
    }

    /**
     * 更新监控任务信息
     *
     * @param monitorTask 监控任务信息
     * @return
     */
    @RequestMapping("/task/updateTask")
    @ResponseBody
    public ConResult doUpdateTask(MonitorTask monitorTask) {
        if (monitorTask == null) {
            return ConResult.fail("任务信息为NULL，请重新尝试。");
        }
        boolean update = monitorService.updateTask(monitorTask);
        if (update) {
            return new ConResult(true, "修改监控任务信息成功！", null);
        } else {
            return ConResult.fail("修改监控任务信息失败，请重新尝试。");
        }
    }

    /**
     * 集群监控报警任务信息查询
     *
     * @param monitorIndicatorSearchBO 监控指标查询条件
     * @return
     */
    @RequestMapping(value = "/clusterMonitorAlarmTaskList", method = RequestMethod.GET)
    @ResponseBody
    public ConResult clusterMonitorAlarmTaskList(MonitorIndicatorSearchBO monitorIndicatorSearchBO) {
        List<MonitorIndicator> monitorIndicatorList = monitorService.getIndicatorsByParams(monitorIndicatorSearchBO);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", monitorIndicatorList == null ? new ArrayList<MonitorIndicator>() : monitorIndicatorList);
        PageVO pageVO = new PageVO();
        if (monitorIndicatorList != null) {
            pageVO.setTotal(monitorIndicatorList.size());
        }
        result.put("page", pageVO);
        return ConResult.success(result);
    }

    @RequestMapping(value = "/getZkMonitorTaskDataByClusterId")
    @ResponseBody
    public ConResult getZkMonitorTaskDataByClusterId(@RequestParam(value = "clusterId") Integer clusterId) {
        return zkMonitorTaskService.selectByClusterId(clusterId);
    }

}
