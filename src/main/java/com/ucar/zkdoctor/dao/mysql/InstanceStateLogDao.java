package com.ucar.zkdoctor.dao.mysql;

import com.ucar.zkdoctor.pojo.bo.InstanceStateLogSearchBO;
import com.ucar.zkdoctor.pojo.po.InstanceStateLog;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Description: 实例状态历史记录Dao
 * Created on 2018/1/9 17:04
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public interface InstanceStateLogDao {

    /**
     * 批量写入实例运行状态记录
     *
     * @param instanceStateLogList 实例状态信息
     * @return
     */
    boolean batchInsertInstanceStateLogs(List<InstanceStateLog> instanceStateLogList);

    /**
     * 插入某条实例运行状态记录
     *
     * @param instanceStateLog 实例状态信息
     * @return
     */
    boolean insertInstanceStateLogs(InstanceStateLog instanceStateLog);

    /**
     * 获取符合条件的实例状态记录，集群汇总信息
     *
     * @param instanceStateLogSearchBO 查询条件
     * @return
     */
    List<InstanceStateLog> getInstanceStateLogByClusterParams(InstanceStateLogSearchBO instanceStateLogSearchBO);

    /**
     * 获取某实例的历史状态信息
     *
     * @param instanceId 实例id
     * @param startDate  开始时间
     * @param endDate    结束时间
     * @return
     */
    List<InstanceStateLog> getInstanceStateLogByInstance(@Param("instanceId") int instanceId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 获取某集群下所有实例的历史状态信息
     *
     * @param clusterId 集群id
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return
     */
    List<InstanceStateLog> getInstanceStateLogByCluster(@Param("clusterId") int clusterId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 删除此时间之前的所有实例状态历史数据
     *
     * @param endDate 此时间之前的数据将删除
     * @return
     */
    boolean cleanInstanceStateLogData(Date endDate);

    /**
     * 需要删除的实例状态历史数据记录的数量
     *
     * @param endDate 获取此时间之前的所有数据条数
     * @return
     */
    Long cleanInstanceStateLogCount(Date endDate);
}
