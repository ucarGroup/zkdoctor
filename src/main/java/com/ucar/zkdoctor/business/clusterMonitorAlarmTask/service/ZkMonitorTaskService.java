package com.ucar.zkdoctor.business.clusterMonitorAlarmTask.service;

import com.ucar.zkdoctor.business.clusterMonitorAlarmTask.base.service.BaseService;
import com.ucar.zkdoctor.business.clusterMonitorAlarmTask.pojo.ZkMonitorTask;
import com.ucar.zkdoctor.web.ConResult;

import java.util.List;

public interface ZkMonitorTaskService extends BaseService {
    int deleteByPrimaryKey(Integer id);

    int insert(ZkMonitorTask record);

    ZkMonitorTask selectByPrimaryKey(Integer id);

    List<ZkMonitorTask> selectAll();

    ConResult updateByPrimaryKey(ZkMonitorTask record);

    ConResult updateMonitorTaskSwitchOn(ZkMonitorTask record);

    ConResult getZkMonitorTaskList();

    ConResult selectByClusterId(Integer clusterId);

    ConResult getAllZkMonitorTask();
}
