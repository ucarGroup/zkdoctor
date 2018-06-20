package com.ucar.zkdoctor.dao.mysql;

import com.ucar.zkdoctor.pojo.bo.InstanceInfoSearchBO;
import com.ucar.zkdoctor.pojo.po.InstanceInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description: 实例信息Dao
 * Created on 2018/1/9 11:01
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public interface InstanceInfoDao {

    /**
     * 保存新实例信息
     *
     * @param instanceInfo 实例信息
     * @return
     */
    boolean insertInstance(InstanceInfo instanceInfo);

    /**
     * 通过实例id获取实例信息
     *
     * @param id 实例id
     * @return
     */
    InstanceInfo getInstanceInfoById(int id);


    /**
     * 根据ip和port获取实例信息
     *
     * @param host 实例ip
     * @param port 实例port
     * @return
     */
    InstanceInfo getInstanceByHostAndPort(@Param("host") String host, @Param("port") int port);

    /**
     * 通过集群下的所有实例信息
     *
     * @param clusterId 集群id
     * @return
     */
    List<InstanceInfo> getInstancesByClusterId(int clusterId);

    /**
     * 通过实例status获取实例信息
     *
     * @param status 实例status
     * @return
     */
    List<InstanceInfo> getInstanceInfoByStatus(int status);

    /**
     * 获取集群所有未下线的实例信息
     *
     * @param clusterId 集群id
     * @return
     */
    List<InstanceInfo> getAllOnLineInstancesByClusterId(int clusterId);

    /**
     * 获取所有实例信息
     *
     * @return
     */
    List<InstanceInfo> getAllInstances();

    /**
     * 获取机器上的所有实例信息
     *
     * @param machineId 机器id
     * @return
     */
    List<InstanceInfo> getInstancesByMachineId(int machineId);

    /**
     * 获取所有符合条件的实例信息
     *
     * @param instanceInfoSearchBO 查询条件
     * @return
     */
    List<InstanceInfo> getAllInstancesByParams(InstanceInfoSearchBO instanceInfoSearchBO);

    /**
     * 更新实例信息
     *
     * @param instanceInfo 实例信息
     * @return
     */
    boolean updateInstance(InstanceInfo instanceInfo);

    /**
     * 更新实例状态信息
     *
     * @param id     实例id
     * @param status 实例状态
     * @return
     */
    boolean updateInstanceStatus(@Param("id") int id, @Param("status") int status);

    /**
     * 更新实例角色信息
     *
     * @param id             实例id
     * @param serverStateLag 实例角色信息
     * @return
     */
    boolean updateInstanceServerStateLag(@Param("id") int id, @Param("serverStateLag") int serverStateLag);

    /**
     * 通过实例id删除实例信息
     *
     * @param id 实例id
     * @return
     */
    boolean deleteInstanceById(int id);

    /**
     * 删除集群下的所有实例信息
     *
     * @param clusterId 集群id
     * @return
     */
    boolean deleteInstancesByClusterId(int clusterId);

    /**
     * 获取集群实例总数
     *
     * @param clusterId 集群id
     * @return
     */
    int getInstanceCountByClusterId(int clusterId);

    /**
     * 获取实例总数
     *
     * @return
     */
    int getInstanceTotalCount();

    /**
     * 获取集群下所有正常状态的实例信息
     *
     * @param clusterId 集群id
     * @return
     */
    List<InstanceInfo> getNormalInstancesByClusterId(int clusterId);

    /**
     * 获取所有监控中的实例数
     *
     * @return
     */
    int getMonitoringCount();

    /**
     * 获取集群中的leader节点或standalone节点
     *
     * @param clusterId 集群id
     * @return
     */
    InstanceInfo getLeaderOrStandaloneInstance(int clusterId);

    /**
     * 更新实例连接收集开关
     *
     * @param id                实例id
     * @param connectionMonitor 连接收集开关
     * @return
     */
    boolean updateInstanceConnMonitor(@Param("id") int id, @Param("connectionMonitor") boolean connectionMonitor);

    /**
     * 获取所有连接收集开关打开的实例信息
     *
     * @return
     */
    List<InstanceInfo> getAllConnMonitorInstance();
}
