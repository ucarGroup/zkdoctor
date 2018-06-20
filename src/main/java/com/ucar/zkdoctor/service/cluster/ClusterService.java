package com.ucar.zkdoctor.service.cluster;

import com.ucar.zkdoctor.pojo.bo.ClusterInfoSearchBO;
import com.ucar.zkdoctor.pojo.po.ClusterAlarmUser;
import com.ucar.zkdoctor.pojo.po.ClusterInfo;
import com.ucar.zkdoctor.pojo.po.User;
import com.ucar.zkdoctor.pojo.vo.ClusterDetailVO;
import com.ucar.zkdoctor.pojo.vo.ClusterListVO;

import java.util.List;

/**
 * Description: 集群服务接口
 * Created on 2018/1/9 17:06
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public interface ClusterService {

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
    boolean updateClusterStatus(int id, int status);

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

    /**
     * 查询首页展示的集群信息
     *
     * @param clusterInfoSearchBO 查询条件
     * @return
     */
    List<ClusterListVO> searchForClusterListVO(ClusterInfoSearchBO clusterInfoSearchBO);

    /**
     * 通过集群id获取集群信息以及状态信息
     *
     * @param clusterId 集群id
     * @return
     */
    ClusterDetailVO getClusterDetailVOByClusterId(int clusterId);

    /**
     * 获取集群下配置的用户信息，用于报警使用
     *
     * @param clusterId 集群id
     * @return
     */
    List<User> clusterAlarmUsers(int clusterId);

    /**
     * 插入新的集群用户信息
     *
     * @param clusterAlarmUser 集群与用户对应信息
     * @return
     */
    boolean addClusterAlarmUser(ClusterAlarmUser clusterAlarmUser);

    /**
     * 获取集群下的所有报警用户信息
     *
     * @param clusterId 集群id
     * @return
     */
    List<ClusterAlarmUser> getUserIdsByClusterId(int clusterId);

    /**
     * 获取用户对应的集群信息
     *
     * @param userId 用户id
     * @return
     */
    List<ClusterAlarmUser> getClusterIdsByUserId(int userId);

    /**
     * 删除集群与用户对应关系
     *
     * @param clusterId 集群id
     * @param userId    用户id
     * @return
     */
    boolean deleteAlarmUser(int clusterId, int userId);

    /**
     * 创建集群
     *
     * @param clusterInfo       集群基本信息
     * @param newClusterServers server实例信息
     * @return
     */
    boolean addNewCluster(ClusterInfo clusterInfo, String newClusterServers);

    /**
     * 删除某个用户的所有报警设置
     *
     * @param userId 用户id
     * @return
     */
    boolean deleteAllAlarmUser(int userId);
}
