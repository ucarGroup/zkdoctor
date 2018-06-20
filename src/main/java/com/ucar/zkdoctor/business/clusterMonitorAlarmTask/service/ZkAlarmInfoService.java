package com.ucar.zkdoctor.business.clusterMonitorAlarmTask.service;

import com.ucar.zkdoctor.business.clusterMonitorAlarmTask.base.service.BaseService;
import com.ucar.zkdoctor.business.clusterMonitorAlarmTask.pojo.ZkAlarmInfo;
import com.ucar.zkdoctor.web.ConResult;

import java.util.List;

public interface ZkAlarmInfoService extends BaseService {
    int deleteByPrimaryKey(Integer id);

    ConResult insert(ZkAlarmInfo record);

    ConResult insertZkAlarmInfo(ZkAlarmInfo record);

    ZkAlarmInfo selectByPrimaryKey(Integer id);

    List<ZkAlarmInfo> selectAll();

    ConResult updateByPrimaryKey(ZkAlarmInfo record);

    ConResult getAllZkAlarmInfo();

    ConResult getClusterInstanceCollectInfo();


}
