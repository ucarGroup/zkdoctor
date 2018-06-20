package com.ucar.zkdoctor.business.clusterMonitorAlarmTask.dao;

import com.ucar.zkdoctor.business.clusterMonitorAlarmTask.pojo.ZkMonitorTask;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface ZkMonitorTaskMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ZkMonitorTask record);

    ZkMonitorTask selectByPrimaryKey(Integer id);

    List<ZkMonitorTask> selectAll();

    int updateByPrimaryKey(ZkMonitorTask record);

    int updateMonitorTaskSwitchOn(ZkMonitorTask record);

    List<HashMap<String, Object>> selectAllContainName();

    List<HashMap<String, Object>> selectByClusterId(Integer clusterId);


}
