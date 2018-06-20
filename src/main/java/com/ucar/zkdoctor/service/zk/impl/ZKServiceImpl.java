package com.ucar.zkdoctor.service.zk.impl;

import com.alibaba.fastjson.JSONObject;
import com.ucar.zkdoctor.pojo.bo.CacheObject;
import com.ucar.zkdoctor.pojo.bo.HostAndPort;
import com.ucar.zkdoctor.pojo.po.ClusterInfo;
import com.ucar.zkdoctor.pojo.po.InstanceInfo;
import com.ucar.zkdoctor.pojo.vo.ZnodeDetailInfoVO;
import com.ucar.zkdoctor.pojo.vo.ZnodeTreeNodeVO;
import com.ucar.zkdoctor.service.cluster.ClusterService;
import com.ucar.zkdoctor.service.collection.CollectService;
import com.ucar.zkdoctor.service.instance.InstanceService;
import com.ucar.zkdoctor.service.machine.MachineOperationService;
import com.ucar.zkdoctor.service.machine.MachineService;
import com.ucar.zkdoctor.service.zk.CustomZKManager;
import com.ucar.zkdoctor.service.zk.ZKService;
import com.ucar.zkdoctor.util.config.ModifiableConfig;
import com.ucar.zkdoctor.util.constant.ClusterEnumClass.ClusterStatusEnum;
import com.ucar.zkdoctor.util.constant.InstanceEnumClass.InstanceStatusEnum;
import com.ucar.zkdoctor.util.constant.SymbolConstant;
import com.ucar.zkdoctor.util.constant.ZKServerEnumClass;
import com.ucar.zkdoctor.util.constant.protocol.MachineProtocol;
import com.ucar.zkdoctor.util.constant.protocol.ZKProtocol;
import com.ucar.zkdoctor.util.exception.SSHException;
import com.ucar.zkdoctor.util.ssh.SSHUtil;
import com.ucar.zkdoctor.util.tool.DateUtil;
import com.ucar.zkdoctor.util.tool.HessianSerializerUtils;
import com.ucar.zkdoctor.util.tool.IdempotentConfirmer;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Description: zk服务相关接口实现类
 * Created on 2018/1/16 10:27
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Service
public class ZKServiceImpl implements ZKService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZKServiceImpl.class);

    @Resource
    private InstanceService instanceService;

    @Resource
    private ClusterService clusterService;

    @Resource
    private MachineService machineService;

    @Resource
    private CollectService collectService;

    @Resource
    private MachineOperationService machineOperationService;

    /**
     * 动态扩容过程结果记录，key：clusterId，value：结果列表
     */
    private static final Map<Integer, CacheObject<List<String>>> DYNAMIC_EXPANSION_PEOCESS_MAP =
            new ConcurrentHashMap<Integer, CacheObject<List<String>>>();

    /**
     * 重启集群过程结果记录，key：clusterId，value：结果列表
     */
    private static final Map<Integer, CacheObject<List<String>>> RESTART_PEOCESS_MAP =
            new ConcurrentHashMap<Integer, CacheObject<List<String>>>();

    /**
     * 安装zk服务过程结果记录，key：'install'，value：结果列表
     */
    public static final Map<String, CacheObject<List<String>>> INSTALL_PEOCESS_MAP =
            new ConcurrentHashMap<String, CacheObject<List<String>>>();

    /**
     * 重启zk服务时，进行数据同步校验的路径
     */
    private static final String SYNC_CHECK_PATH = "/zkdoctor/restart_";

    /**
     * 涉及集群操作的线程池
     */
    private ExecutorService quorumExecutorService = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(5));

    /**
     * 执行zk服务安装的线程池
     */
    private ExecutorService installExecutorService = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(5));

    private CustomZKManager customZKManager = new CustomZKManager();

    @Override
    public int checkZKStatus(String host, int port) {
        // TODO
        return InstanceStatusEnum.RUNNING.getStatus();
    }

    @Override
    public boolean autoInstallServers(List<HostAndPort> hostList, String confFileContent, String installFileName,
                                      String installFileDir, String dataDir, String downloadSite) {
        return autoInstallServers(hostList, ModifiableConfig.zkInstallDir, confFileContent, installFileName,
                installFileDir, dataDir, downloadSite);
    }

    @Override
    public boolean autoInstallServers(List<HostAndPort> hostList, String remoteDir, String confFileContent,
                                      String installFileName, String installFileDir, String dataDir, String downloadSite) {
        return autoInstallServers(hostList, remoteDir, MachineProtocol.PATH + installFileName, confFileContent,
                installFileName, installFileDir, dataDir, downloadSite);
    }

    @Override
    public boolean autoInstallServers(List<HostAndPort> hostList, String remoteDir, String localServerFilePath, String confFileContent,
                                      String installFileName, String installFileDir, String dataDir, String downloadSite) {
        // 先判断之前的是否结束
        if (!isDeployOver()) {
            throw new RuntimeException("之前的部署过程正在进行，请稍后重试！");
        }
        installExecutorService.submit(new InstallServersCallable(hostList, remoteDir, localServerFilePath,
                confFileContent, installFileName, installFileDir, dataDir, downloadSite));
        return true;
    }

    @Override
    public boolean autoInstallServer(final HostAndPort hostAndPort, final String remoteDir, final String localServerFilePath, final String confFileContent,
                                     final String installFileName, final String installFileDir, final String dataDir, final String downloadSite) {
        // 部署过程缓存记录
        CacheObject<List<String>> newResultCache = ZKServiceImpl.INSTALL_PEOCESS_MAP.get("install");
        List<String> newResult = new LinkedList<String>();
        if (newResultCache != null && newResultCache.getObject() != null) {
            newResult = newResultCache.getObject();
        }
        try {
            // 安装文件是否在服务器上，如果不在将根据配置的地址下载到指定目录下，再进行推送
            File zkServerFile = new File(installFileDir + localServerFilePath);
            if (!zkServerFile.exists()) {
                newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在安装：服务器目录" +
                        installFileDir + "下无安装文件，正在准备下载...");
                LOGGER.warn("Scp local zkserver file to remote failed, there is no localServerFilePath {}.", localServerFilePath);
                // 下载安装文件
                String downloadSiteWeb = StringUtils.isEmpty(downloadSite) ? ModifiableConfig.installFileDownloadSite : downloadSite;
                boolean download = SSHUtil.downloadFile(installFileDir, downloadSiteWeb);
                newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在安装：服务器目录" +
                        installFileDir + "下无安装文件，下载结果" + download);
                zkServerFile = new File(installFileDir + localServerFilePath);
                if (!zkServerFile.exists()) {
                    newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在安装：下载文件路径不位于" +
                            installFileDir + "，请检查");
                    return false;
                }
            }
            newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在安装：正在" +
                    hostAndPort.toString() + "安装服务，请稍等...");
            // 服务安装操作
            boolean install = SSHUtil.installZKServer(hostAndPort.getHost(), remoteDir, zkServerFile.getAbsolutePath(),
                    installFileName, confFileContent, dataDir, String.valueOf(hostAndPort.getMyid()));
            if (!install) {
                newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在安装：在" +
                        hostAndPort.toString() + "安装服务失败");
                return false;
            }
            newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在安装：在" +
                    hostAndPort.toString() + "安装服务成功");
            // 启动服务
            newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在安装：正在" +
                    hostAndPort.toString() + "上启动zk服务...");
            boolean start = startZKServer(hostAndPort.getHost());
            if (start) {
                newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在安装：在" +
                        hostAndPort.toString() + "上启动zk服务成功");
            } else {
                newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "安装失败：在" +
                        hostAndPort.toString() + "上启动zk服务失败");
            }
            return start;
        } catch (Exception e) {
            newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在安装：在" +
                    hostAndPort.toString() + "上启动zk服务失败");
            return false;
        }
    }

    @Override
    public List<String> queryDeployProcess() {
        if (INSTALL_PEOCESS_MAP.containsKey("install")) {
            synchronized (INSTALL_PEOCESS_MAP) {
                if (INSTALL_PEOCESS_MAP.containsKey("install")) {
                    CacheObject<List<String>> result = INSTALL_PEOCESS_MAP.get("install");
                    if (result != null) {
                        return result.getObject();
                    }
                }
            }
        }
        return new ArrayList<String>();
    }

    @Override
    public boolean dynamicExpansion(final int clusterId, final String newServerConfig, final String newHost, final int newPort) {
        if (StringUtils.isBlank(newServerConfig)) {
            throw new RuntimeException("新服务器配置为空");
        }
        if (!isDynamicExpansionOver(clusterId)) { // 之前有没有完成的扩容操作
            throw new RuntimeException("该集群有正在进行中的扩容操作，请稍后尝试！");
        }
        Future<Boolean> future = quorumExecutorService.submit(new ExpansionCallable(clusterId, newServerConfig, newHost, newPort));
        try {
            return future.get(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.warn("Get dynamic expansion:{} result for cluster:{} failed.", newServerConfig, clusterId, e);
            throw new RuntimeException("获取扩容结果，发生中断异常！扩容过程将在后台进行");
        } catch (ExecutionException e) {
            LOGGER.warn("Get dynamic expansion:{} result for cluster:{} failed.", newServerConfig, clusterId, e);
            throw new RuntimeException("获取扩容结果，执行异常！扩容过程将在后台进行");
        } catch (TimeoutException e) {
            LOGGER.warn("Get dynamic expansion:{} result for cluster:{} failed.", newServerConfig, clusterId, e);
            throw new RuntimeException("获取扩容结果，发生超时异常！超时时间：60s，扩容过程将在后台进行");
        }
    }

    @Override
    public boolean dynamicExpansion(int instanceId, String ip, String newServerConfig) {
        // 1、增加新扩容的服务器配置
        boolean addNewConfigResult = addNewServerConfig(ip, newServerConfig);
        if (!addNewConfigResult) {
            LOGGER.error("[DynamicExpansion] Add new server config [{}] to instance {} failed.", newServerConfig, ip);
            return false;
        }

        // 2、重启服务器
        boolean restart = restartZKInstance(instanceId);
        if (!restart) {
            LOGGER.error("[DynamicExpansion] Restart instance {} failed.", ip);
            return false;
        }
        try {
            // 等待选主以及数据同步
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }

        // 3、判断集群服务是否正常
        return isQuorumServerRun(ip);
    }

    @Override
    public List<String> queryDynamicExpansionProcess(int clusterId) {
        if (DYNAMIC_EXPANSION_PEOCESS_MAP.containsKey(clusterId)) {
            synchronized (DYNAMIC_EXPANSION_PEOCESS_MAP) {
                if (DYNAMIC_EXPANSION_PEOCESS_MAP.containsKey(clusterId)) {
                    CacheObject<List<String>> result = DYNAMIC_EXPANSION_PEOCESS_MAP.get(clusterId);
                    if (result != null) {
                        return result.getObject();
                    }
                }
            }
        }
        return new ArrayList<String>();
    }

    @Override
    public boolean isQuorumServerRun(String host) {
        try {
            String statusResult = machineOperationService.executeShell(host, ZKProtocol.statusZKShell());
            if (StringUtils.isNotBlank(statusResult)) {
                return !statusResult.contains("Error contacting service");
            }
        } catch (Exception e) {
            LOGGER.error("Quorum server {} running failed!", host, e);
        }
        return false;
    }

    @Override
    public boolean addNewServerConfig(String ip, String newServerConfig) {
        if (StringUtils.isBlank(newServerConfig)) {
            return false;
        }
        try {
            String currentConfig = instanceService.queryInstanceConfig(ip);
            if (currentConfig == null) {
                LOGGER.warn("Current zk {} config is null in adding new server config.", ip);
                return false;
            }
            StringBuilder stringBuilder = new StringBuilder();
            String notes = "# Generated by automatic expansion";
            stringBuilder.append(currentConfig);
            stringBuilder.append(SymbolConstant.NEXT_LINE);
            stringBuilder.append(notes);
            stringBuilder.append(SymbolConstant.NEXT_LINE);
            stringBuilder.append(newServerConfig);
            return instanceService.instanceConfOps(ip, stringBuilder.toString());
        } catch (RuntimeException e) {
            LOGGER.warn("Add new Server config failed.", e);
            return false;
        }
    }

    @Override
    public boolean offLineCluster(int clusterId) {
        List<InstanceInfo> instanceInfoList = instanceService.getInstancesByClusterId(clusterId);
        if (CollectionUtils.isEmpty(instanceInfoList)) {
            return false;
        }
        for (InstanceInfo instanceInfo : instanceInfoList) {
            stopZKInstance(instanceInfo.getId());
        }
        // 下线集群操作，直接更改集群状态
        return clusterService.updateClusterStatus(clusterId, ClusterStatusEnum.OFFLINE.getStatus());
    }

    @Override
    public boolean restartQuorumServer(int clusterId, long sleepTimeMs) {
        if (!isRestartOver(clusterId)) { // 之前有没有完成的重启操作
            throw new RuntimeException("该集群有正在进行中的重启操作，请稍后尝试！");
        }

        Future<Boolean> future = quorumExecutorService.submit(new RestartCallable(clusterId, sleepTimeMs));
        try {
            return future.get(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.warn("Get restart quorum server result failed for cluster:{}.", clusterId, e);
            throw new RuntimeException("获取重启集群结果，发生中断异常！重启过程将在后台进行");
        } catch (ExecutionException e) {
            LOGGER.warn("Get restart quorum server result failed for cluster:{}.", clusterId, e);
            throw new RuntimeException("获取重启集群结果，执行异常！重启过程将在后台进行");
        } catch (TimeoutException e) {
            LOGGER.warn("Get restart quorum server result failed for cluster:{}.", clusterId, e);
            throw new RuntimeException("获取重启集群结果，发生超时异常！超时时间：60s，重启过程将在后台进行");
        }
    }

    @Override
    public List<String> queryRestartProcess(int clusterId) {
        if (RESTART_PEOCESS_MAP.containsKey(clusterId)) {
            synchronized (RESTART_PEOCESS_MAP) {
                if (RESTART_PEOCESS_MAP.containsKey(clusterId)) {
                    CacheObject<List<String>> result = RESTART_PEOCESS_MAP.get(clusterId);
                    if (result != null) {
                        return result.getObject();
                    }
                }
            }
        }
        return new ArrayList<String>();
    }

    @Override
    public boolean restartQuorumServer(int clusterId) {
        return restartQuorumServer(clusterId, 300);
    }

    @Override
    public boolean stopZKInstance(final int instanceId) {
        return zkInstanceOps(instanceId, null, new ZKInstanceOps() {
            @Override
            public boolean operate(String host, int port) {
                boolean stop = stopZKServer(host);
                if (stop) {
                    instanceService.updateInstanceStatus(instanceId, InstanceStatusEnum.OFFLINE.getStatus());
                }
                LOGGER.info("stopping zk server {}:{}... {}.", host, port, stop);
                return stop;
            }
        });
    }

    @Override
    public boolean restartZKInstance(final int instanceId) {
        // 直接进行重启
        return zkInstanceOps(instanceId, null, new ZKInstanceOps() {
            @Override
            public boolean operate(String host, int port) {
                boolean isZKRun = restartZKServer(host);
                if (isZKRun) {
                    instanceService.updateInstanceStatus(instanceId, InstanceStatusEnum.RUNNING.getStatus());
                }
                LOGGER.info("restarting zk server {}:{}... {}.", host, port, isZKRun);
                return isZKRun;
            }
        });
    }

    @Override
    public boolean isZKRun(final String host, final int port) {
        if (StringUtils.isBlank(host) || port < 0) {
            return false;
        }
        return new IdempotentConfirmer(2) { // 第一次失败，再重试一次
            @Override
            public boolean execute() {
                try {
                    Boolean isRunOk = checkInstanceRunOk(host, port);
                    return isRunOk != null && isRunOk;
                } catch (Exception e) {
                    LOGGER.error("Check zookeeper {}:{} run error: {}.", host, port, e.getMessage(), e);
                    // 等待10ms再次检测
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e1) {
                    }
                    return false;
                }
            }
        }.run();
    }

    @Override
    public Boolean checkInstanceRunOk(String host, int port) {
        String result = collectService.checkServerRunningNormal(host, port);
        if (result == null) {
            return null;
        } else {
            return "imok".equals(result);
        }
    }

    @Override
    public boolean isQuorumSynchronized(final InstanceInfo restartInst, final InstanceInfo checkInstance,
                                        final long sleepTimeMs) {
        // 检验集群是否正常，通过写、读数据来进行
        final String data = restartInst.getHost() + "_" + String.valueOf(System.currentTimeMillis());
        final String path = SYNC_CHECK_PATH + restartInst.getHost();
        // 在重启的节点上创建数据，只有不存在时才能成功
        boolean createNode = new IdempotentConfirmer(2) {
            @Override
            public boolean execute() {
                return customZKManager.createNotExistsNode(restartInst.getId(), restartInst.getHost(), restartInst.getPort(),
                        path, data.getBytes(), true);
            }
        }.run();
        LOGGER.info("Create test node {} in restarting {}:{} result is {}.",
                path, restartInst.getHost(), restartInst.getPort(), createNode);
        // 创建失败则认为失败
        if (!createNode) {
            return false;
        }
        try {
            Thread.sleep(sleepTimeMs);
        } catch (InterruptedException e) {
        }
        // 在随机另外一台节点上进行数据校验
        boolean checkNodeExists = new IdempotentConfirmer(2) {
            @Override
            public boolean execute() {
                boolean check = customZKManager.equalsZNodeData(checkInstance.getId(), checkInstance.getHost(), checkInstance.getPort(),
                        path, data.getBytes());
                if (!check) {
                    try {
                        Thread.sleep(sleepTimeMs);
                    } catch (InterruptedException e) {
                    }
                }
                return check;
            }
        }.run();
        LOGGER.info("Check test node {} in {}:{} while restarting {}:{} result is {}.",
                path, checkInstance.getHost(), checkInstance.getPort(), restartInst.getHost(), restartInst.getPort(), checkNodeExists);
        // 数据校验失败，则失败
        return checkNodeExists;
    }

    @Override
    public List<ZnodeTreeNodeVO> initZnodesTree(Integer clusterId) {
        return initNodesChildren(clusterId, CustomZKManager.PATH);
    }

    @Override
    public List<ZnodeTreeNodeVO> initNodesChildren(Integer clusterId, String parentPath) {
        InstanceInfo leaderOrStandaloneInstance = null;
        try {
            leaderOrStandaloneInstance = instanceService.getLeaderOrStandaloneInstance(clusterId);
        } catch (Exception e) {
            LOGGER.error("Get leader or standalone instance failed.", e);
            throw new RuntimeException("该集群leader或standalone角色实例获取失败，请检查集群各实例状态。");
        }
        if (leaderOrStandaloneInstance == null) {
            throw new RuntimeException("该集群没有leader或standalone角色实例，请检查后重试。");
        }
        List<String> initChildrenZnodes = customZKManager.getChildren(leaderOrStandaloneInstance.getId(),
                leaderOrStandaloneInstance.getHost(), leaderOrStandaloneInstance.getPort(), parentPath, false);
        if (CollectionUtils.isNotEmpty(initChildrenZnodes)) {
            return generateTreeNodes(initChildrenZnodes, parentPath);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean deleteZnodeAndChildren(Integer clusterId, String znode) {
        InstanceInfo leaderOrStandaloneInstance = instanceService.getLeaderOrStandaloneInstance(clusterId);
        if (leaderOrStandaloneInstance == null) {
            LOGGER.warn("Delete znode:{} and it's children znodes failed because leader/standalone server of cluster:{} is null.", znode, clusterId);
            return false;
        }
        return customZKManager.deleteZNodeAndChildren(leaderOrStandaloneInstance.getId(), leaderOrStandaloneInstance.getHost(),
                leaderOrStandaloneInstance.getPort(), znode);
    }

    @Override
    public ZnodeDetailInfoVO searchDataForZnode(Integer clusterId, String znode) {
        return searchDataForZnode(clusterId, znode, true);
    }

    @Override
    public ZnodeDetailInfoVO searchDataForZnode(Integer clusterId, String znode, boolean isStringSerializable) {
        InstanceInfo leaderOrStandaloneInstance = instanceService.getLeaderOrStandaloneInstance(clusterId);
        if (leaderOrStandaloneInstance == null) {
            LOGGER.warn("Search data for znode:{} failed because leader/standalone server of cluster:{} is null.", znode, clusterId);
            return new ZnodeDetailInfoVO();
        }
        Stat stat = new Stat();
        byte[] data = customZKManager.getData(leaderOrStandaloneInstance.getId(), leaderOrStandaloneInstance.getHost(),
                leaderOrStandaloneInstance.getPort(), znode, stat, false);
        ZnodeDetailInfoVO znodeDetailInfoVO = new ZnodeDetailInfoVO();
        znodeDetailInfoVO.setCzxid("0x" + Long.toHexString(stat.getCzxid()));
        znodeDetailInfoVO.setMzxid("0x" + Long.toHexString(stat.getMzxid()));
        znodeDetailInfoVO.setCtime(DateUtil.formatYYYYMMddHHMMss(new Date(stat.getCtime())));
        znodeDetailInfoVO.setMtime(DateUtil.formatYYYYMMddHHMMss(new Date(stat.getMtime())));
        znodeDetailInfoVO.setVersion(stat.getVersion());
        znodeDetailInfoVO.setCversion(stat.getCversion());
        znodeDetailInfoVO.setAversion(stat.getAversion());
        znodeDetailInfoVO.setEphemeralOwner("0x" + Long.toHexString(stat.getEphemeralOwner()));
        znodeDetailInfoVO.setDataLength(stat.getDataLength());
        znodeDetailInfoVO.setNumChildren(stat.getNumChildren());
        znodeDetailInfoVO.setPzxid("0x" + Long.toHexString(stat.getPzxid()));
        if (data != null) {
            if (isStringSerializable) {
                znodeDetailInfoVO.setData(new String(data));
            } else {
                Object dataResult = HessianSerializerUtils.deserialize(data);
                znodeDetailInfoVO.setData(JSONObject.toJSONString(dataResult));
            }
        }
        return znodeDetailInfoVO;
    }

    @Override
    public boolean updataZnodeData(Integer clusterId, String path, String data, int version) {
        InstanceInfo leaderOrStandaloneInstance = instanceService.getLeaderOrStandaloneInstance(clusterId);
        if (leaderOrStandaloneInstance == null) {
            LOGGER.warn("Updata data：{},version:{} for znode:{} failed because leader/standalone server of cluster:{} is null.",
                    data, version, path, clusterId);
            return false;
        }
        Stat stat = customZKManager.setData(leaderOrStandaloneInstance.getId(), leaderOrStandaloneInstance.getHost(),
                leaderOrStandaloneInstance.getPort(),
                path, data.getBytes(), version);
        return stat != null;
    }

    @Override
    public boolean createNewZnode(Integer clusterId, String path, String data, boolean createParentNeeded, String aclFlag) {
        InstanceInfo leaderOrStandaloneInstance = instanceService.getLeaderOrStandaloneInstance(clusterId);
        if (leaderOrStandaloneInstance == null) {
            LOGGER.warn("Create new znode:{} data：{},createParentNeeded:{} aclFlag:{} failed because leader/standalone server of cluster:{} is null.",
                    path, data, createParentNeeded, aclFlag, clusterId);
            return false;
        }
        List<ACL> aclList;
        if ("0".equals(aclFlag)) {
            aclList = ZooDefs.Ids.OPEN_ACL_UNSAFE;
        } else if ("1".equals(aclFlag)) {
            aclList = ZooDefs.Ids.CREATOR_ALL_ACL;
        } else if ("2".equals(aclFlag)) {
            aclList = ZooDefs.Ids.READ_ACL_UNSAFE;
        } else {
            throw new RuntimeException("节点ACL设置不存在，请检查！" + aclFlag);
        }
        return customZKManager.createNode(leaderOrStandaloneInstance.getId(), leaderOrStandaloneInstance.getHost(),
                leaderOrStandaloneInstance.getPort(), path, data.getBytes(), aclList, createParentNeeded);
    }

    @Override
    public boolean updateServer(String host) {
        File uploadJarFile = new File(ModifiableConfig.uploadFileDir + ModifiableConfig.uploadFileName);
        if (!uploadJarFile.exists()) {
            LOGGER.error("Update server:{} failed because jar file:{} does not exist.",
                    host, ModifiableConfig.uploadFileDir + ModifiableConfig.uploadFileName);
            throw new RuntimeException("升级Jar包不存在，请重新上传！");
        }
        try {
            // 备份原jar文件
            long timeStamp = new Date().getTime();
            String bakJarFileName = ModifiableConfig.uploadFileName + ".bak." + timeStamp;
            boolean backup = SSHUtil.backUpFile(host, ModifiableConfig.zkInstallDir,
                    ModifiableConfig.uploadFileName, bakJarFileName);
            if (!backup) {
                LOGGER.error("Update server:{} failed because backup jar file failed.", host);
                throw new RuntimeException("备份原jar文件失败，请重试！");
            }
            // 拷贝新jar文件
            boolean scpToRemote = SSHUtil.uploadJarFile(host, ModifiableConfig.zkInstallDir, uploadJarFile.getAbsolutePath());
            if (!scpToRemote) {
                LOGGER.error("Update server:{} failed because scp jar file to remote server failed.", host);
                throw new RuntimeException("推送升级包至远程服务器：" + host + "失败，请重试！");
            }
            // 重启zk服务
            boolean restart = restartZKServer(host);
            if (!restart) {
                LOGGER.error("Update server:{} failed because restart server failed.", host);
                throw new RuntimeException("重启zk服务失败，请重试！");
            }
            return true;
        } catch (SSHException e) {
            LOGGER.error("Update server:{} failed because scp jar file to remote server failed.", host, e);
            throw new RuntimeException("推送升级包至远程服务器：" + host + "失败，请重试！");
        }
    }

    /**
     * 将子节点列表转化为页面显示tree node格式
     *
     * @param children 子节点列表，不包含路径符号/
     * @param parent   父节点，格式如:/zookeeper/node1
     * @return
     */
    private List<ZnodeTreeNodeVO> generateTreeNodes(List<String> children, String parent) {
        List<ZnodeTreeNodeVO> znodeTreeNodeVOList = new ArrayList<ZnodeTreeNodeVO>();
        // 对返回的节点进行字典序排序
        Collections.sort(children, new Comparator<String>() {
            @Override
            public int compare(String str1, String str2) {
                return str1.compareToIgnoreCase(str2);
            }
        });
        // 获取的数据是否为根节点
        boolean isRoot = CustomZKManager.PATH.equals(parent);
        if (isRoot) {
            for (String node : children) {
                ZnodeTreeNodeVO znodeTreeNodeVO = new ZnodeTreeNodeVO();
                znodeTreeNodeVO.setTitle(CustomZKManager.PATH + node);
                znodeTreeNodeVO.setKey(CustomZKManager.PATH + node);
                znodeTreeNodeVOList.add(znodeTreeNodeVO);
            }
        } else {
            for (String node : children) {
                ZnodeTreeNodeVO znodeTreeNodeVO = new ZnodeTreeNodeVO();
                znodeTreeNodeVO.setTitle(CustomZKManager.PATH + node);
                znodeTreeNodeVO.setKey(parent + CustomZKManager.PATH + node);
                znodeTreeNodeVOList.add(znodeTreeNodeVO);
            }
        }
        return znodeTreeNodeVOList;
    }

    /**
     * zk实例运维操作，包括启动、停止、重启等
     *
     * @param instanceId        实例id
     * @param confirmZKRunOrNot 需要确认zk服务运行与否
     *                          true：确认zk服务运行，false：确认zk服务未运行
     * @param zkInstanceOps     实例运维操作回调接口
     * @return
     */
    private boolean zkInstanceOps(int instanceId, Boolean confirmZKRunOrNot, ZKInstanceOps zkInstanceOps) {
        InstanceInfo instanceInfo = instanceService.getInstanceInfoById(instanceId);
        if (instanceInfo == null) {
            return false;
        }
        boolean operable;
        if (confirmZKRunOrNot == null) { // 无需判断zk服务运行状况
            operable = true;
        } else if (confirmZKRunOrNot) { // 确认zk服务正常运行
            operable = instanceInfo.getStatus() == InstanceStatusEnum.RUNNING.getStatus() &&
                    isZKRun(instanceInfo.getHost(), instanceInfo.getPort());
        } else { // 确认zk服务未运行
            operable = instanceInfo.getStatus() != InstanceStatusEnum.RUNNING.getStatus() &&
                    !isZKRun(instanceInfo.getHost(), instanceInfo.getPort());
        }
        if (operable) {
            try {
                return zkInstanceOps.operate(instanceInfo.getHost(), instanceInfo.getPort());
            } catch (Exception e) {
                LOGGER.error("Operate zk instance {}:{} error:{}",
                        instanceInfo.getHost(), instanceInfo.getPort(), e.getMessage(), e);
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * zk实例运维操作接口
     */
    private interface ZKInstanceOps {
        boolean operate(String host, int port);
    }

    /**
     * 给定ip，启动zk服务
     *
     * @param host zk ip
     * @return
     */
    private boolean startZKServer(String host) {
        String result = machineOperationService.executeShell(host, ZKProtocol.startZKShell());
        boolean isStart = false;
        if (StringUtils.isNotBlank(result) && (result.contains("STARTED"))) {
            isStart = true;
        }
        return isStart;
    }

    /**
     * 给定ip，停止zk服务
     *
     * @param host zk ip
     * @return
     */
    private boolean stopZKServer(String host) {
        String result = machineOperationService.executeShell(host, ZKProtocol.stopZKShell());
        boolean isStop = false;
        // 正常停止或实例已停止状态，均认为服务停止
        if (StringUtils.isNotBlank(result) && (result.contains("STOPPED") || result.contains("no zookeeper to stop"))) {
            isStop = true;
        }
        return isStop;
    }

    /**
     * 给定ip，重启zk服务
     *
     * @param host zk ip
     * @return
     */
    private boolean restartZKServer(String host) {
        String result = machineOperationService.executeShell(host, ZKProtocol.restartZKShell());
        boolean isZKRun = false;
        if (StringUtils.isNotBlank(result) && result.contains("STARTED")) {
            isZKRun = true;
        }
        return isZKRun;
    }

    /**
     * 删除重启期间进行校验的节点信息
     *
     * @param leaderId         leader实例id
     * @param host             leader host
     * @param port             leader port
     * @param instanceInfoList 重启实例列表
     */
    private void deleteQuorumSynCheckNodes(int leaderId, String host, int port, List<InstanceInfo> instanceInfoList) {
        for (InstanceInfo instanceInfo : instanceInfoList) {
            String path = SYNC_CHECK_PATH + instanceInfo.getHost();
            boolean delete = customZKManager.deleteZNodeOnly(leaderId, host, port, path);
            if (!delete) {
                LOGGER.warn("Delete check test node {} exception, please do this manually.", path);
            }
        }
    }

    /**
     * 指定集群的扩容扩容是否结束
     *
     * @param clusterId 集群id
     * @return
     */
    private boolean isDynamicExpansionOver(int clusterId) {
        boolean over;
        if (DYNAMIC_EXPANSION_PEOCESS_MAP.containsKey(clusterId)) {
            synchronized (DYNAMIC_EXPANSION_PEOCESS_MAP) {
                if (DYNAMIC_EXPANSION_PEOCESS_MAP.containsKey(clusterId)) {
                    CacheObject<List<String>> result = DYNAMIC_EXPANSION_PEOCESS_MAP.get(clusterId);
                    if (result == null || result.getObject() == null || result.getObject().size() <= 0) {
                        DYNAMIC_EXPANSION_PEOCESS_MAP.remove(clusterId);
                        return true;
                    }
                    // 之前扩容过程是否结束
                    for (String resultLine : result.getObject()) {
                        if (!resultLine.contains("正在")) { // 说明之前扩容成功或扩容失败，清空缓存信息
                            DYNAMIC_EXPANSION_PEOCESS_MAP.remove(clusterId);
                            return true;
                        }
                    }
                    // 全部步骤均在扩容中
                    over = false;
                } else {
                    over = true;
                }
            }
        } else {
            over = true;
        }
        return over;
    }

    /**
     * 集群扩容操作
     */
    private class ExpansionCallable implements Callable<Boolean> {
        /**
         * 集群id
         */
        private Integer clusterId;

        /**
         * 新服务器配置，格式：server.id=host:quorumPort:electionPort:peerType
         */
        private String newServerConfig;

        /**
         * 新服务器ip
         */
        private String newHost;

        /**
         * 新服务器port
         */
        private Integer newPort;

        ExpansionCallable(Integer clusterId, String newServerConfig, String newHost, Integer newPort) {
            this.clusterId = clusterId;
            this.newServerConfig = newServerConfig;
            this.newHost = newHost;
            this.newPort = newPort;
        }

        @Override
        public Boolean call() throws Exception {
            final List<String> newResult = new LinkedList<String>();
            newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在准备扩容，请稍等...");
            CacheObject<List<String>> resultCache = new CacheObject<List<String>>(newResult, System.currentTimeMillis());
            synchronized (DYNAMIC_EXPANSION_PEOCESS_MAP) {
                DYNAMIC_EXPANSION_PEOCESS_MAP.put(clusterId, resultCache);
            }
            // 检查新服务器是否被占用
            InstanceInfo existInfo = instanceService.getInstanceByHostAndPort(newHost, newPort);
            if (existInfo != null) {
                LOGGER.warn("[DynamicExpansion] new server {}:{} is allocated for other cluster.",
                        newHost, newPort);
                newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "扩容失败：所填新服务host:port已经被占用。");
                return false;
            }
            // 获取当前集群与实例信息
            ClusterInfo clusterInfo = clusterService.getClusterInfoById(clusterId);
            if (clusterInfo == null) {
                newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "扩容失败：查询集群信息为null。");
                return false;
            }
            List<InstanceInfo> instanceInfoList = instanceService.getInstancesByClusterId(clusterId);
            if (CollectionUtils.isEmpty(instanceInfoList) || instanceInfoList.size() < 2) {
                newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "扩容失败：集群实例数量小于2，不能进行扩容操作。");
                return false;
            }

            newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在扩容：正在确认新服务器是否处于正常运行状态...");
            // 1、确认新服务器启动且正常加入到集群中
            if (!isQuorumServerRun(newHost)) {
                // 新服务器与集群之间连接异常（新服务器未启动或者服务异常），重启新服务器以确保新服务器连接到原集群上
                newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在扩容：新服务器未运行，正在启动新服务器...");
                boolean restartNewServer = restartZKServer(newHost);
                if (restartNewServer) {
                    try {
                        // 等待选主以及数据同步
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                    }
                    boolean isQuorumServerRun = isQuorumServerRun(newHost);
                    if (!isQuorumServerRun) {
                        LOGGER.error("[DynamicExpansion] new server {}:{} is not running.",
                                newHost, newPort);
                        newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "扩容失败：新服务器启动失败。");
                        return false;
                    } else {
                        newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在扩容：新服务器启动成功");
                    }
                } else { // 重启新服务器失败
                    LOGGER.error("[DynamicExpansion] restart new server {}:{} failed.",
                            newHost, newPort);
                    newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "扩容失败：启动新服务器失败。");
                    return false;
                }
            } else {
                newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在扩容：新服务器处于正常运行状态");
            }

            // 2、逐台更新配置（先follower，最后leader）并重启，判断重启后集群是否正常
            List<InstanceInfo> learnersInstance = new ArrayList<InstanceInfo>();
            InstanceInfo leaderInstance = null;
            for (InstanceInfo instanceInfo : instanceInfoList) {
                if (ZKServerEnumClass.ZKServerStateEnum.LEADER.getServerState() == instanceInfo.getServerStateLag()) {
                    if (leaderInstance == null) {
                        leaderInstance = instanceInfo;
                    } else {
                        LOGGER.warn("[DynamicExpansion] there is too many leaders in cluster {}.", clusterId);
                        newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "扩容失败：集群中检测到多个leader角色的实例。");
                        return false;
                    }
                } else {
                    learnersInstance.add(instanceInfo);
                }
            }
            if (leaderInstance == null) {
                LOGGER.warn("[DynamicExpansion] there is no leader in cluster {}.", clusterId);
                newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "扩容失败：集群中未检测到leader角色的实例。");
                return false;
            }
            // 扩容learner的配置并重启
            for (InstanceInfo instanceInfo : learnersInstance) {
                newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在扩容：正在扩容" +
                        ZKServerEnumClass.ZKServerStateEnum.getDescByServerState(instanceInfo.getServerStateLag()) +
                        "实例..." + instanceInfo.getHost() + ":" + instanceInfo.getPort());
                boolean expansion = dynamicExpansion(instanceInfo.getId(), instanceInfo.getHost(), newServerConfig);
                if (!expansion) {
                    LOGGER.error("[DynamicExpansion] dynamic expansion for learner instance {}:{} failed.",
                            instanceInfo.getHost(), instanceInfo.getPort());
                    String info = "在" + ZKServerEnumClass.ZKServerStateEnum.getDescByServerState(instanceInfo.getServerStateLag()) +
                            "实例 " + instanceInfo.getHost() + ":" + instanceInfo.getPort() + " 上扩容失败。";
                    newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "扩容失败：" + info);
                    return false;
                } else {
                    newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在扩容：在" +
                            ZKServerEnumClass.ZKServerStateEnum.getDescByServerState(instanceInfo.getServerStateLag()) +
                            "实例 " + instanceInfo.getHost() + ":" + instanceInfo.getPort() + " 上扩容成功");
                }
            }
            // 扩容leader的配置并重启
            newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在扩容：正在扩容leader实例..."
                    + leaderInstance.getHost() + ":" + leaderInstance.getPort());
            boolean leaderExpansion = dynamicExpansion(leaderInstance.getId(), leaderInstance.getHost(), newServerConfig);
            if (!leaderExpansion) {
                LOGGER.error("[DynamicExpansion] dynamic expansion for leader instance {}:{} failed.",
                        leaderInstance.getHost(), leaderInstance.getPort());
                String info = "在leader实例" + leaderInstance.getHost() + ":" + leaderInstance.getPort() + "上扩容失败。";
                newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "扩容失败：" + info);
                return false;
            } else {
                newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在扩容：在leader实例"
                        + leaderInstance.getHost() + ":" + leaderInstance.getPort() + "上扩容成功");
            }

            // 3、整个集群正常后，将新服务器加到该集群下管理
            newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在扩容：将新服务器" + newHost + "添加到机器列表中...");
            boolean addNewMachine = machineService.insertIfNotExistsMachine(newHost);
            if (!addNewMachine) {
                LOGGER.error("[DynamicExpansion] add new machine {} failed.", newHost);
                newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "扩容失败：将新服务器" + newHost + "添加到机器列表失败。");
                return false;
            } else {
                newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在扩容：将新服务器" + newHost + "添加到机器列表成功");
            }
            newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在扩容：将新服务器" + newHost + "添加到实例列表中...");
            boolean lastResult = instanceService.insertInstanceForCluster(clusterId, newHost, newPort);
            if (lastResult) {
                newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在扩容：将新服务器" + newHost + "添加到实例列表成功");
                newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "扩容成功！");
            } else {
                newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "扩容失败：将新服务器" + newHost + "添加到实例列表失败。");
            }
            return lastResult;
        }
    }

    /**
     * 集群重启操作是否结束
     *
     * @param clusterId 集群id
     * @return
     */
    private boolean isRestartOver(int clusterId) {
        boolean over;
        if (RESTART_PEOCESS_MAP.containsKey(clusterId)) {
            synchronized (RESTART_PEOCESS_MAP) {
                if (RESTART_PEOCESS_MAP.containsKey(clusterId)) {
                    CacheObject<List<String>> result = RESTART_PEOCESS_MAP.get(clusterId);
                    if (result == null || result.getObject() == null || result.getObject().size() <= 0) {
                        RESTART_PEOCESS_MAP.remove(clusterId);
                        return true;
                    }
                    // 之前重启过程是否结束
                    for (String resultLine : result.getObject()) {
                        if (!resultLine.contains("正在")) { // 说明之前重启成功或重启失败，清空缓存信息
                            RESTART_PEOCESS_MAP.remove(clusterId);
                            return true;
                        }
                    }
                    // 全部步骤均在重启中
                    over = false;
                } else {
                    over = true;
                }
            }
        } else {
            over = true;
        }
        return over;
    }

    /**
     * 重启操作
     */
    private class RestartCallable implements Callable<Boolean> {
        /**
         * 集群id
         */
        private Integer clusterId;

        /**
         * 验证数据同步时间间隔，单位：毫秒
         */
        private Long sleepTimeMs;

        RestartCallable(Integer clusterId, Long sleepTimeMs) {
            this.clusterId = clusterId;
            this.sleepTimeMs = sleepTimeMs;
        }

        @Override
        public Boolean call() throws Exception {
            List<String> newResult = new LinkedList<String>();
            newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在准备重启，请稍等...");
            CacheObject<List<String>> resultCache = new CacheObject<List<String>>(newResult, System.currentTimeMillis());
            synchronized (RESTART_PEOCESS_MAP) {
                RESTART_PEOCESS_MAP.put(clusterId, resultCache);
            }

            // 忽略已下线实例
            final List<InstanceInfo> instanceInfoList = instanceService.getAllOnLineInstancesByClusterId(clusterId);
            // 独立模式或者2个节点的集群，不支持重启功能
            if (CollectionUtils.isEmpty(instanceInfoList) || instanceInfoList.size() < 3) {
                LOGGER.info("Standalone or two server not supports restart, cluster id is {}.", clusterId);
                newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "重启失败：独立模式与2个节点的集群不支持重启");
                return false;
            }
            newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在重启：正在循环重启zk实例...");
            // 分别获取集群中的leader实例以及learner实例信息
            List<InstanceInfo> learnersInstance = new ArrayList<InstanceInfo>();
            InstanceInfo leaderInstance = null;
            for (InstanceInfo instanceInfo : instanceInfoList) {
                if (ZKServerEnumClass.ZKServerStateEnum.LEADER.getServerState() == instanceInfo.getServerStateLag()) {
                    if (leaderInstance == null) {
                        leaderInstance = instanceInfo;
                    } else {
                        LOGGER.warn("[Restart] there is too many leaders in cluster {}.", clusterId);
                        newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "重启失败：集群中检测到多个leader角色的实例。");
                        return false;
                    }
                } else {
                    learnersInstance.add(instanceInfo);
                }
            }
            if (leaderInstance == null) {
                LOGGER.warn("[Restart] there is no leader in cluster {}.", clusterId);
                newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "重启失败：集群中未检测到leader角色的实例。");
                return false;
            }
            boolean result = true;
            // 1、先重启learner
            for (int i = 0; i < learnersInstance.size(); i++) {
                InstanceInfo restartInst = learnersInstance.get(i);
                newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在重启：正在重启" +
                        ZKServerEnumClass.ZKServerStateEnum.getDescByServerState(restartInst.getServerStateLag()) +
                        restartInst.getHost() + ":" + restartInst.getPort() + "...");
                boolean restart = restartZKInstance(restartInst.getId());
                if (restart) {
                    newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在重启：重启" +
                            ZKServerEnumClass.ZKServerStateEnum.getDescByServerState(restartInst.getServerStateLag()) +
                            restartInst.getHost() + ":" + restartInst.getPort() + "成功");
                    InstanceInfo checkInstance = learnersInstance.get((i + 1) % learnersInstance.size());
                    newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在重启：检验重启实例与" +
                            checkInstance.getHost() + ":" + checkInstance.getPort() + "之间是否同步...");
                    // 重启实例数据不同步，可能写入失败或同步失败，均认为失败，重启过程结束
                    if (!isQuorumSynchronized(restartInst, checkInstance, sleepTimeMs)) {
                        newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "重启失败：检验重启实例与" +
                                checkInstance.getHost() + ":" + checkInstance.getPort() + "之间不同步");
                        result = false;
                        break;
                    } else {
                        newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在重启：检验重启实例与" +
                                checkInstance.getHost() + ":" + checkInstance.getPort() + "之间已经同步");
                    }
                } else {
                    newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "重启失败：重启实例" +
                            restartInst.getHost() + ":" + restartInst.getPort() + "失败");
                    // 一个重启失败，则直接退出，认为失败
                    result = false;
                    break;
                }
            }
            // 2、重启leader实例
            if (result) { // 其他实例重启成功后，再进行leader实例重启
                newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在重启：正在重启leader实例" +
                        leaderInstance.getHost() + ":" + leaderInstance.getPort() + "...");
                boolean restartLeader = restartZKInstance(leaderInstance.getId());
                if (restartLeader) {
                    newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在重启：重启leader实例" +
                            leaderInstance.getHost() + ":" + leaderInstance.getPort() + "成功");
                    InstanceInfo checkInstance = learnersInstance.get(0);
                    newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在重启：检验leader实例与" +
                            checkInstance.getHost() + ":" + checkInstance.getPort() + "之间是否同步...");
                    // 重启实例数据不同步，可能写入失败或同步失败，均认为失败，重启过程结束
                    if (!isQuorumSynchronized(leaderInstance, checkInstance, sleepTimeMs)) {
                        newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "重启失败：检验leader实例与" +
                                checkInstance.getHost() + ":" + checkInstance.getPort() + "之间不同步");
                        result = false;
                    } else {
                        newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在重启：检验重启实例与" +
                                checkInstance.getHost() + ":" + checkInstance.getPort() + "之间已经同步");
                    }
                } else {
                    newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "重启失败：重启leader实例" +
                            leaderInstance.getHost() + ":" + leaderInstance.getPort() + "失败");
                    result = false;
                }
            }
            if (result) {
                newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "重启成功！");
                final int leaderId = leaderInstance.getId();
                final String leaderHost = leaderInstance.getHost();
                final int leaderPort = leaderInstance.getPort();
                // 删除测试节点
                new Thread() {
                    @Override
                    public void run() {
                        deleteQuorumSynCheckNodes(leaderId, leaderHost, leaderPort, instanceInfoList);
                    }
                }.start();
            } else {
                newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "重启失败！");
            }
            return result;
        }
    }

    /**
     * 部署操作是否结束
     *
     * @return
     */
    private boolean isDeployOver() {
        boolean over;
        if (INSTALL_PEOCESS_MAP.containsKey("install")) {
            synchronized (INSTALL_PEOCESS_MAP) {
                if (INSTALL_PEOCESS_MAP.containsKey("install")) {
                    CacheObject<List<String>> result = INSTALL_PEOCESS_MAP.get("install");
                    if (result == null || result.getObject() == null || result.getObject().size() <= 0) {
                        INSTALL_PEOCESS_MAP.remove("install");
                        return true;
                    }
                    // 之前部署过程是否结束
                    for (String resultLine : result.getObject()) {
                        if (!resultLine.contains("正在")) { // 说明之前部署成功或部署失败，清空缓存信息
                            INSTALL_PEOCESS_MAP.remove("install");
                            return true;
                        }
                    }
                    // 全部步骤均在部署中
                    over = false;
                } else {
                    over = true;
                }
            }
        } else {
            over = true;
        }
        return over;
    }

    /**
     * 自动部署服务
     */
    private class InstallServersCallable implements Callable<Boolean> {

        /**
         * 目标服务器列表
         */
        private List<HostAndPort> hostList;

        /**
         * 远程服务器安装目录（注意：路径结尾没有/）
         */
        private String remoteDir;

        /**
         * 本地安装文件目录以及文件名
         */
        private String localServerFilePath;

        /**
         * 配置
         */
        private String confFileContent;

        /**
         * 安装包名称
         */
        private String installFileName;

        /**
         * 安装包存放在服务器的目录
         */
        private String installFileDir;

        /**
         * dataDir
         */
        private String dataDir;

        /**
         * 安装文件下载路径
         */
        private String downloadSite;

        InstallServersCallable(List<HostAndPort> hostList, String remoteDir, String localServerFilePath, String confFileContent,
                               String installFileName, String installFileDir, String dataDir, String downloadSite) {
            this.hostList = hostList;
            this.remoteDir = remoteDir;
            this.localServerFilePath = localServerFilePath;
            this.confFileContent = confFileContent;
            this.installFileName = installFileName;
            this.installFileDir = installFileDir;
            this.dataDir = dataDir;
            this.downloadSite = downloadSite;
        }


        @Override
        public Boolean call() throws Exception {
            // 部署过程记录
            final List<String> newResult = new LinkedList<String>();
            CacheObject<List<String>> resultCache = new CacheObject<List<String>>(newResult, System.currentTimeMillis());
            synchronized (INSTALL_PEOCESS_MAP) {
                INSTALL_PEOCESS_MAP.put("install", resultCache);
            }
            // 依次部署服务
            for (HostAndPort host : hostList) {
                boolean autoInstall = autoInstallServer(host, remoteDir, localServerFilePath, confFileContent,
                        installFileName, installFileDir, dataDir, downloadSite);
                if (!autoInstall) {
                    newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "部署失败：" + host.toString() + "部署失败");
                    return false;
                } else {
                    newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在部署：" + host.toString() + "部署成功");
                }
            }
            newResult.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "部署成功!");
            return true;
        }
    }
}
