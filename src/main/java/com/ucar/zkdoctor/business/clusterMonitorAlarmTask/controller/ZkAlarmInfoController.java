package com.ucar.zkdoctor.business.clusterMonitorAlarmTask.controller;

import com.ucar.zkdoctor.business.clusterMonitorAlarmTask.pojo.ZkAlarmInfo;
import com.ucar.zkdoctor.business.clusterMonitorAlarmTask.service.ZkAlarmInfoService;
import com.ucar.zkdoctor.web.ConResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/alarmInfo")
public class ZkAlarmInfoController {

    @Autowired
    private ZkAlarmInfoService zkAlarmInfoService;

    @RequestMapping(value = "/getAllZkAlarmInfo", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ConResult getAllZkAlarmInfo() {
        return zkAlarmInfoService.getAllZkAlarmInfo();
    }

    /**
     * 插入报警相关信息
     *
     * @param zkAlarmInfo 报警信息对象
     * @return
     */
    @RequestMapping(value = "/insertZkAlarmInfo")
    @ResponseBody
    public ConResult insertZkAlarmInfo(ZkAlarmInfo zkAlarmInfo) {
        return zkAlarmInfoService.insertZkAlarmInfo(zkAlarmInfo);
    }

    /**
     * 获得集群监控相关指标汇总信息
     *
     * @return
     */
    @RequestMapping(value = "/getCollectInfo")
    @ResponseBody
    public ConResult getCollectInfo() {
        return zkAlarmInfoService.getClusterInstanceCollectInfo();
    }
}
