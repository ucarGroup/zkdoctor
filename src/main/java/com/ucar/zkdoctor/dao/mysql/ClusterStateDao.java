package com.ucar.zkdoctor.dao.mysql;

import com.ucar.zkdoctor.pojo.po.ClusterState;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Description: 集群状态Dao
 * Created on 2018/1/9 11:03
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public interface ClusterStateDao {

    /**
     * 插入新的集群状态信息
     *
     * @param clusterState 集群状态
     * @return
     */
    boolean insertClusterState(ClusterState clusterState);

    /**
     * 通过集群id获取最新集群状态信息
     *
     * @param clusterId 集群id
     * @return
     */
    ClusterState getClusterStateByClusterId(int clusterId);

    /**
     * 删除某集群状态信息
     *
     * @param clusterId 集群id
     * @return
     */
    boolean deleteClusterStateByClusterId(int clusterId);

    /**
     * 批量写入集群运行状态历史记录
     *
     * @param clusterStateList 集群状态信息
     * @return
     */
    boolean batchInsertClusterStateLogs(List<ClusterState> clusterStateList);

    /**
     * 插入某条集群运行状态记录
     *
     * @param clusterState 集群状态信息
     * @return
     */
    boolean insertClusterStateLogs(ClusterState clusterState);

    /**
     * 获取某集群的历史状态信息
     *
     * @param clusterId 集群id
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return
     */
    List<ClusterState> getClusterStateLogByClusterId(@Param("clusterId") int clusterId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 删除此时间之前的所有集群状态历史数据
     *
     * @param endDate 此时间之前的数据将删除
     * @return
     */
    boolean cleanClusterStateLogData(Date endDate);

    /**
     * 需要删除的集群状态历史数据记录的数量
     *
     * @param endDate 获取此时间之前的所有数据条数
     * @return
     */
    Long cleanClusterStateLogCount(Date endDate);

}
