package com.ucar.zkdoctor.service.zk;

import com.ucar.zkdoctor.pojo.bo.HostAndPort;
import com.ucar.zkdoctor.pojo.po.InstanceInfo;
import com.ucar.zkdoctor.pojo.vo.ZnodeDetailInfoVO;
import com.ucar.zkdoctor.pojo.vo.ZnodeTreeNodeVO;

import java.util.List;

/**
 * Description: zk服务相关接口
 * Created on 2018/1/16 10:27
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public interface ZKService {

    /**
     * 检测当前zk服务的状态
     *
     * @param host 服务器host
     * @param port 服务器port
     * @return
     */
    int checkZKStatus(String host, int port);

    /**
     * 自动在目标服务器上安装zk服务
     *
     * @param hostList        目标服务器列表
     * @param confFileContent 配置
     * @param installFileName 安装包名称
     * @param installFileDir  安装包存放在服务器的目录
     * @param dataDir         dataDir
     * @param downloadSite    安装文件下载路径
     * @return
     */
    boolean autoInstallServers(List<HostAndPort> hostList, String confFileContent, String installFileName, String installFileDir,
                               String dataDir, String downloadSite);

    /**
     * 自动在目标服务器上安装zk服务
     *
     * @param hostList        目标服务器列表
     * @param remoteDir       远程服务器安装目录（注意：路径结尾没有/）
     * @param confFileContent 配置
     * @param installFileName 安装包名称
     * @param installFileDir  安装包存放在服务器的目录
     * @param dataDir         dataDir
     * @param downloadSite    安装文件下载路径
     * @return
     */
    boolean autoInstallServers(List<HostAndPort> hostList, String remoteDir, String confFileContent, String installFileName,
                               String installFileDir, String dataDir, String downloadSite);

    /**
     * 自动在目标服务器上安装zk服务
     *
     * @param hostList            目标服务器列表
     * @param remoteDir           远程服务器安装目录（注意：路径结尾没有/）
     * @param localServerFilePath 本地安装文件目录以及文件名
     * @param confFileContent     配置
     * @param installFileName     安装包名称
     * @param installFileDir      安装包存放在服务器的目录
     * @param dataDir             dataDir
     * @param downloadSite        安装文件下载路径
     * @return
     */
    boolean autoInstallServers(List<HostAndPort> hostList, String remoteDir, String localServerFilePath, String confFileContent,
                               String installFileName, String installFileDir, String dataDir, String downloadSite);

    /**
     * 自动在目标服务器上安装zk服务
     *
     * @param host                目标服务器地址
     * @param remoteDir           远程服务器安装目录（注意：路径结尾没有/）
     * @param localServerFilePath 本地安装文件目录以及文件名
     * @param confFileContent     配置
     * @param installFileName     安装包名称
     * @param installFileDir      安装包存放在服务器的目录
     * @param dataDir             dataDir
     * @param downloadSite        安装文件下载路径
     * @return
     */
    boolean autoInstallServer(HostAndPort host, String remoteDir, String localServerFilePath, String confFileContent,
                              String installFileName, String installFileDir, String dataDir, String downloadSite);

    /**
     * 获取集群部署过程数据
     *
     * @return
     */
    List<String> queryDeployProcess();

    /**
     * 集群动态扩容
     *
     * @param clusterId       集群id
     * @param newServerConfig 新服务器配置，格式：server.id=host:quorumPort:electionPort:peerType
     * @param newHost         新服务器ip
     * @param newPort         新服务器port
     * @return
     */
    boolean dynamicExpansion(int clusterId, String newServerConfig, String newHost, int newPort);

    /**
     * 动态扩容
     *
     * @param instanceId      待实例id
     * @param ip              实例ip
     * @param newServerConfig 新服务器配置，格式：server.id=host:quorumPort:electionPort:peerType
     * @return
     */
    boolean dynamicExpansion(int instanceId, String ip, String newServerConfig);

    /**
     * 查询该集群扩容过程结果记录信息
     *
     * @param clusterId 集群id
     * @return
     */
    List<String> queryDynamicExpansionProcess(int clusterId);

    /**
     * 判断集群中的某个server是否处于正常运行状态
     *
     * @param host server ip
     * @return
     */
    boolean isQuorumServerRun(String host);

    /**
     * 服务器增加新的配置
     *
     * @param ip              目标服务器ip
     * @param newServerConfig 新服务器配置，格式：server.id=host:quorumPort:electionPort:peerType
     * @return
     */
    boolean addNewServerConfig(String ip, String newServerConfig);

    /**
     * 下线zk集群，直接停止集群服务，并不解除集群与实例的关系。直接更新状态为已下线
     *
     * @param clusterId 集群id
     * @return
     */
    boolean offLineCluster(int clusterId);

    /**
     * 重启zk集群
     *
     * @param clusterId   集群id
     * @param sleepTimeMs 验证数据同步时间间隔，单位：毫秒
     * @return
     */
    boolean restartQuorumServer(int clusterId, long sleepTimeMs);

    /**
     * 获取集群重启结果记录信息
     *
     * @param clusterId 集群id
     * @return
     */
    List<String> queryRestartProcess(int clusterId);

    /**
     * 重启zk集群
     *
     * @param clusterId 集群id
     * @return
     */
    boolean restartQuorumServer(int clusterId);

    /**
     * 停止已有的zk实例
     *
     * @param instanceId 实例id
     * @return
     */
    boolean stopZKInstance(int instanceId);

    /**
     * 重启已有的zk实例
     *
     * @param instanceId 实例id
     * @return
     */
    boolean restartZKInstance(int instanceId);

    /**
     * zk服务是否正常运行，判断两次
     *
     * @param host zk ip
     * @param port zk port
     * @return
     */
    boolean isZKRun(String host, int port);

    /**
     * 实例是否正常运行
     *
     * @param host zk ip
     * @param port zk port
     * @return
     */
    Boolean checkInstanceRunOk(String host, int port);

    /**
     * 重启后，整个集群是否运行正常
     *
     * @param restartInst   重启的实例
     * @param checkInstance 选择进行校验数据同步的实例
     * @param sleepTimeMs   验证数据同步时间间隔，单位：毫秒
     * @return
     */
    boolean isQuorumSynchronized(InstanceInfo restartInst, InstanceInfo checkInstance, long sleepTimeMs);

    /**
     * 获取指定集群下，获取所有/下的节点信息
     *
     * @param clusterId 集群id
     * @return
     */
    List<ZnodeTreeNodeVO> initZnodesTree(Integer clusterId);

    /**
     * 获取指定集群下，获取所有指定路径下的子节点信息（仅包含一层子节点）
     *
     * @param clusterId  集群id
     * @param parentPath 父节点路径
     * @return
     */
    List<ZnodeTreeNodeVO> initNodesChildren(Integer clusterId, String parentPath);

    /**
     * 删除节点以及其子节点
     *
     * @param clusterId 集群id
     * @param znode     节点路径
     * @return
     */
    boolean deleteZnodeAndChildren(Integer clusterId, String znode);

    /**
     * 查询znode节点数据信息，默认数据选择string序列化方式
     *
     * @param clusterId 集群id
     * @param znode     节点路径
     * @return
     */
    ZnodeDetailInfoVO searchDataForZnode(Integer clusterId, String znode);

    /**
     * 查询znode节点数据信息。目前只支持string序列化方式以及hessian序列化方式，可扩展
     *
     * @param clusterId            集群id
     * @param znode                节点路径
     * @param isStringSerializable 是否使用string序列化方式序列化数据，true：string序列化，false：hessian序列化
     * @return
     */
    ZnodeDetailInfoVO searchDataForZnode(Integer clusterId, String znode, boolean isStringSerializable);

    /**
     * 更新节点数据信息
     *
     * @param clusterId 集群id
     * @param path      节点路径
     * @param data      数据内容，目前只支持string类型
     * @param version   版本号，默认为-1
     * @return
     */
    boolean updataZnodeData(Integer clusterId, String path, String data, int version);

    /**
     * 创建新节点
     *
     * @param clusterId          集群id
     * @param path               新节点路径
     * @param data               数据
     * @param createParentNeeded 是否创建不存在的副接待你
     * @param aclFlag            ACL，0:1:2：
     * @return
     */
    boolean createNewZnode(Integer clusterId, String path, String data, boolean createParentNeeded, String aclFlag);

    /**
     * 升级zk服务，替换jar包，重启实例
     *
     * @param host 实例ip
     * @return
     */
    boolean updateServer(String host);
}
