package com.ucar.zkdoctor.service.instance;

import com.ucar.zkdoctor.pojo.bo.AddNewConfigFileBO;
import com.ucar.zkdoctor.pojo.bo.HostAndPort;
import com.ucar.zkdoctor.pojo.bo.InstanceInfoSearchBO;
import com.ucar.zkdoctor.pojo.po.InstanceInfo;
import com.ucar.zkdoctor.pojo.vo.InstanceDetailVO;
import com.ucar.zkdoctor.pojo.vo.UploadedJarFileVO;

import java.util.List;

/**
 * Description: 实例服务接口
 * Created on 2018/1/9 17:07
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public interface InstanceService {

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
     * 根据集实例Status获取实例详情信息，包括实例基本信息以及实例状态信息
     *
     * @param Status 实例Status
     * @return
     */
    List<InstanceDetailVO> getInstanceDetailVOByStatus(int Status);

    /**
     * 根据ip和port获取实例信息
     *
     * @param host 实例ip
     * @param port 实例port
     * @return
     */
    InstanceInfo getInstanceByHostAndPort(String host, int port);

    /**
     * 通过集群下的所有实例信息
     *
     * @param clusterId 集群id
     * @return
     */
    List<InstanceInfo> getInstancesByClusterId(int clusterId);

    /**
     * 通过实例status值获取所有实例信息
     *
     * @param status 实例status
     * @return
     */
    List<InstanceInfo> getInstancesByStatus(int status);


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
    List<InstanceInfo> getByMachineId(int machineId);

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
    boolean updateInstanceStatus(int id, int status);

    /**
     * 更新实例角色信息
     *
     * @param id             实例id
     * @param serverStateLag 实例角色信息
     * @return
     */
    boolean updateInstanceServerStateLag(int id, int serverStateLag);

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
     * 该实例列表是否有被占用的情况
     *
     * @param hostAndPortList 实例列表
     * @return
     */
    boolean existsInstances(List<HostAndPort> hostAndPortList);

    /**
     * 该实例是否有被占用的情况
     *
     * @param hostAndPort 实例ip：port
     * @return
     */
    boolean existsInstances(HostAndPort hostAndPort);

    /**
     * 批量增加所有实例
     *
     * @param clusterId       集群id
     * @param hostAndPortList 实例列表
     * @return
     */
    boolean batchinsertInstanceForCluster(int clusterId, List<HostAndPort> hostAndPortList);

    /**
     * 增加新实例到集群
     *
     * @param clusterId 集群id
     * @param host      新实例ip
     * @param port      新实例port
     * @return
     */
    boolean insertInstanceForCluster(int clusterId, String host, int port);

    /**
     * 增加新实例到集群
     *
     * @param clusterId    集群id
     * @param instanceInfo 实例信息
     */
    boolean insertInstanceForCluster(int clusterId, InstanceInfo instanceInfo);

    /**
     * 根据ip：port信息生成新的实例信息
     *
     * @param host 机器ip
     * @param port 机器port
     * @return
     */
    InstanceInfo generateNewInstanceInfo(String host, int port);

    /**
     * 根据实例id获取实例详情信息，包括实例基本信息以及实例状态信息
     *
     * @param instanceId 实例id
     * @return
     */
    InstanceDetailVO getInstanceDetailVOByInstanceId(int instanceId);

    /**
     * 根据集群id获取实例详情信息，包括实例基本信息以及实例状态信息
     *
     * @param clusterId 集群id
     * @return
     */
    List<InstanceDetailVO> getInstanceDetailVOByClusterId(int clusterId);

    /**
     * 移除实例相关信息
     *
     * @param instanceId 实例id
     * @return
     */
    boolean removeInstanceForCluster(int instanceId);

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
     * @param instanceId        实例id
     * @param connectionMonitor 连接收集开关
     * @return
     */
    boolean updateInstanceConnMonitor(int instanceId, boolean connectionMonitor);

    /**
     * 获取所有连接收集开关打开的实例信息
     *
     * @return
     */
    List<InstanceInfo> getAllConnMonitorInstance();

    /**
     * 获取实例zoo.cfg配置文件信息
     *
     * @param host 实例host
     * @return
     */
    String queryInstanceConfig(String host);

    /**
     * 修改zoo.cfg配置文件内容
     *
     * @param host               实例ip
     * @param zooConfFileContent 配置文件内容
     * @return
     */
    boolean instanceConfOps(String host, String zooConfFileContent);

    /**
     * 新增配置文件
     *
     * @param addNewConfigFileBO 新配置相关参数
     * @return
     */
    boolean addNewConfigFile(AddNewConfigFileBO addNewConfigFileBO);

    /**
     * 查询已上传的安装包Jar文件信息
     *
     * @return
     */
    List<UploadedJarFileVO> queryUploadedJarFile();

    /**
     * 升级zk服务，仅支持替换Jar包
     *
     * @param instanceId 实例id
     * @return
     */
    boolean updateServer(int instanceId);
}
