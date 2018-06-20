package com.ucar.zkdoctor.dao.mysql;

import com.ucar.zkdoctor.pojo.bo.ClusterInfoSearchBO;
import com.ucar.zkdoctor.pojo.po.ClusterInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description: 集群信息Dao
 * Created on 2018/1/9 11:01
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public interface ClusterInfoDao {

    /**
     * 保存新集群信息
     *
     * @param clusterInfo 集群信息
     * @return
     */
    boolean insertClusterInfo(ClusterInfo clusterInfo);

    /**
     * 通过集群id获取集群信息
     *
     * @param id 集群id
     * @return
     */
    ClusterInfo getClusterInfoById(int id);

    /**
     * 通过名字获取集群信息
     *
     * @param clusterName 集群名称
     * @return
     */
    ClusterInfo getClusterInfoByClusterName(String clusterName);

    /**
     * 获取所有集群信息
     *
     * @return
     */
    List<ClusterInfo> getAllClusterInfos();

    /**
     * 获取所有监控中的集群信息，包括运行正常以及异常的集群
     *
     * @return
     */
    List<ClusterInfo> getAllMonitoringClusterInfos();

    /**
     * 获取集群总数
     *
     * @return
     */
    int getClusterInfosTotalCount();

    /**
     * 获取所有符合条件的实例信息
     *
     * @param clusterInfoSearchBO 查询条件
     * @return
     */
    List<ClusterInfo> getAllClusterInfosByParams(ClusterInfoSearchBO clusterInfoSearchBO);

    /**
     * 修改集群信息
     *
     * @param clusterInfo 集群信息
     * @return
     */
    boolean updateClusterInfo(ClusterInfo clusterInfo);

    /**
     * 修改集群状态
     *
     * @param id     集群id
     * @param status 集群状态
     * @return
     */
    boolean updateClusterStatus(@Param("id") int id, @Param("status") int status);

    /**
     * 根据集群id删除集群信息
     *
     * @param id 集群id
     * @return
     */
    boolean deleteClusterInfoByClusterId(int id);

    /**
     * 根据集群名称删除集群信息
     *
     * @param clusterName 集群名称
     * @return
     */
    boolean deleteClusterInfoByClusterName(String clusterName);
}
