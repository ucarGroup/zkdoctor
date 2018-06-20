package com.ucar.zkdoctor.business.clusterMonitorAlarmTask.dao;

import com.ucar.zkdoctor.business.clusterMonitorAlarmTask.pojo.ZkAlarmInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface ZkAlarmInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ZkAlarmInfo record);

    int insertZkAlarmInfo(ZkAlarmInfo record);

    ZkAlarmInfo selectByPrimaryKey(Integer id);

    List<ZkAlarmInfo> selectAll();

    int updateByPrimaryKey(ZkAlarmInfo record);

    List<HashMap<String, Object>> selectAllContainName(String alarmTime);

    int selectClusterTotal(@Param("status") Integer status);

    int selectInstanceTotal(@Param("status") Integer status);
}
