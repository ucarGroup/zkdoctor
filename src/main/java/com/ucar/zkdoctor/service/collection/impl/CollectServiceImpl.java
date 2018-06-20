package com.ucar.zkdoctor.service.collection.impl;

import com.alibaba.fastjson.JSONObject;
import com.ucar.zkdoctor.pojo.bo.HostAndPort;
import com.ucar.zkdoctor.pojo.dto.ConnectionInfoDTO;
import com.ucar.zkdoctor.pojo.dto.ServerStateInfoDTO;
import com.ucar.zkdoctor.pojo.dto.ZKServerConfigDTO;
import com.ucar.zkdoctor.pojo.po.ClientInfo;
import com.ucar.zkdoctor.pojo.po.ClusterState;
import com.ucar.zkdoctor.pojo.po.InstanceInfo;
import com.ucar.zkdoctor.pojo.po.InstanceState;
import com.ucar.zkdoctor.pojo.po.InstanceStateLog;
import com.ucar.zkdoctor.pojo.po.MachineInfo;
import com.ucar.zkdoctor.pojo.po.MachineState;
import com.ucar.zkdoctor.pojo.po.MachineStateLog;
import com.ucar.zkdoctor.service.cluster.ClusterStateService;
import com.ucar.zkdoctor.service.collection.CollectService;
import com.ucar.zkdoctor.service.instance.InstanceService;
import com.ucar.zkdoctor.service.instance.InstanceStateService;
import com.ucar.zkdoctor.service.machine.MachineService;
import com.ucar.zkdoctor.service.machine.MachineStateService;
import com.ucar.zkdoctor.service.schedule.SchedulerService;
import com.ucar.zkdoctor.util.config.ConfigUtil;
import com.ucar.zkdoctor.util.constant.FourLetterCommand;
import com.ucar.zkdoctor.util.constant.InstanceEnumClass.InstanceStatusEnum;
import com.ucar.zkdoctor.util.constant.SchedulerConstant;
import com.ucar.zkdoctor.util.constant.SymbolConstant;
import com.ucar.zkdoctor.util.constant.ZKServerEnumClass.ZKServerStateEnum;
import com.ucar.zkdoctor.util.constant.protocol.MachineProtocol;
import com.ucar.zkdoctor.util.exception.SSHException;
import com.ucar.zkdoctor.util.parser.ConnectionReaderParser;
import com.ucar.zkdoctor.util.parser.ServerStateReaderParser;
import com.ucar.zkdoctor.util.parser.StringReaderParser;
import com.ucar.zkdoctor.util.parser.ZKServerConfigReaderParser;
import com.ucar.zkdoctor.util.ssh.SSHUtil;
import com.ucar.zkdoctor.util.thread.NamedThreadFactory;
import com.ucar.zkdoctor.util.tool.CommandHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.quartz.JobKey;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description: 信息收集服务接口实现类
 * Created on 2018/1/23 11:36
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Service("collectService")
public class CollectServiceImpl implements CollectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CollectServiceImpl.class);

    @Resource
    private ClusterStateService clusterStateService;

    @Resource
    private InstanceService instanceService;

    @Resource
    private InstanceStateService instanceStateService;

    @Resource
    private MachineService machineService;

    @Resource
    private MachineStateService machineStateService;

    @Resource
    private SchedulerService schedulerService;

    /**
     * 定时务线程池
     */
    private static ThreadPoolExecutor schedulerPool = new ThreadPoolExecutor(
            128, 256, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1000),
            new NamedThreadFactory("quartz-scheduler", true));

    @Override
    public boolean deployZKCollection(int clusterId) {
        JobKey jobKey = JobKey.jobKey(SchedulerConstant.ZK_COLLECT_JOB_NAME, SchedulerConstant.ZK_COLLECT_JOB_GROUP);
        return deployZKCollection(clusterId, jobKey);
    }

    @Override
    public boolean unDeployZKCollection(int clusterId) {
        TriggerKey triggerKey = TriggerKey.triggerKey(SchedulerConstant.ZK_COLLECT_TRIGGER_NAME,
                SchedulerConstant.ZK_COLLECT_TRIGGER_GROUP + clusterId);
        return schedulerService.unscheduleJob(triggerKey);
    }

    @Override
    public boolean deployZKCollection(int clusterId, JobKey jobKey) {
        // 记录集群id信息
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put(SchedulerConstant.CLUSTER_KEY, clusterId);
        TriggerKey triggerKey = TriggerKey.triggerKey(SchedulerConstant.ZK_COLLECT_TRIGGER_NAME, SchedulerConstant.ZK_COLLECT_TRIGGER_GROUP + clusterId);
        return schedulerService.deployJobByCron(jobKey, triggerKey, dataMap, SchedulerConstant.ZK_COLLECT_JOB_CRON, false);
    }

    @Override
    public void collectZKInfo(final int clusterId) {
        schedulerPool.submit(new Runnable() {
            @Override
            public void run() {
                // 不收集已下线实例
                List<InstanceInfo> instanceInfoList = instanceService.getAllOnLineInstancesByClusterId(clusterId);
                if (CollectionUtils.isEmpty(instanceInfoList)) {
                    return;
                }
                List<InstanceState> instanceStateList = new ArrayList<InstanceState>();
                List<InstanceStateLog> instanceStateLogList = new ArrayList<InstanceStateLog>();
                Date createTime = new Date();
                for (InstanceInfo instanceInfo : instanceInfoList) {
                    // 收集mntr四字命令结果数据
                    ServerStateInfoDTO serverStateInfoDTO = collectServerStateInfo(instanceInfo.getHost(), instanceInfo.getPort());
                    InstanceState instanceState;
                    if (serverStateInfoDTO != null) {
                        // 重置统计结果，srst四字命令
                        resetServerStatistic(instanceInfo.getHost(), instanceInfo.getPort());
                        instanceState = getInstanceStateFromServerInfo(clusterId, instanceInfo.getId(),
                                new HostAndPort(instanceInfo.getHost(), instanceInfo.getPort()).toString(), serverStateInfoDTO);
                        // 如果实例处于异常或者未运行状态，那么更新实例当前状态
                        if (instanceInfo.getStatus() == InstanceStatusEnum.EXCEPTION.getStatus() ||
                                instanceInfo.getStatus() == InstanceStatusEnum.NOT_RUNNING.getStatus()) {
                            instanceService.updateInstanceStatus(instanceInfo.getId(), InstanceStatusEnum.RUNNING.getStatus());
                        }
                    } else { // 实例可能异常状态，更新实例状态信息
                        instanceState = getInstanceStateFromServerInfo(clusterId, instanceInfo.getId(),
                                new HostAndPort(instanceInfo.getHost(), instanceInfo.getPort()).toString(), new ServerStateInfoDTO());
                        // 如果实例处于未运行状态，那么更新实例当前状态
                        if (instanceInfo.getStatus() == InstanceStatusEnum.RUNNING.getStatus()) {
                            instanceService.updateInstanceStatus(instanceInfo.getId(), InstanceStatusEnum.EXCEPTION.getStatus());
                        }
                    }
                    instanceState.setRunOk(checkInstanceRunOk(instanceInfo.getHost(), instanceInfo.getPort()));
                    instanceState.setCreateTime(createTime);
                    instanceState.setModifyTime(createTime);
                    instanceStateList.add(instanceState);
                    InstanceStateLog instanceStateLog = getInstanceStateLogFromState(instanceState);
                    instanceStateLogList.add(instanceStateLog);
                    // 实例角色如果发生改变，则更新实例角色信息
                    if (instanceState.getServerStateLag() != null && !instanceState.getServerStateLag().equals(instanceInfo.getServerStateLag())) {
                        instanceService.updateInstanceServerStateLag(instanceInfo.getId(), instanceState.getServerStateLag());
                    }
                }
                // 保存实例以及集群状态信息
                for (InstanceState instanceState : instanceStateList) {
                    try {
                        instanceStateService.mergeInstanceState(instanceState);
                    } catch (Exception e) {
                        LOGGER.error("Merge instance state failed.", e);
                    }
                }
                try {
                    instanceStateService.batchInsertInstanceStateLogs(instanceStateLogList);
                } catch (Exception e) {
                    LOGGER.error("Batch save instance state failed.", e);
                }
                mergeClusterStats(clusterId, createTime, instanceStateList);
            }
        });
    }

    @Override
    public ServerStateInfoDTO collectServerStateInfo(String host, int port) {
        try {
            return CommandHelper.doCommand(host, port, FourLetterCommand.mntr, new ServerStateReaderParser());
        } catch (Exception e) {
            LOGGER.error("Collect {}:{} ServerStateInfoDTO with 'mntr' error!", host, port, e);
            return null;
        }
    }

    @Override
    public ZKServerConfigDTO collectConfigInfo(String host, int port) {
        try {
            return CommandHelper.doCommand(host, port, FourLetterCommand.conf, new ZKServerConfigReaderParser());
        } catch (Exception e) {
            LOGGER.error("Collect {}:{} ZKServerConfigDTO with 'conf' error!", host, port, e);
            return null;
        }
    }

    @Override
    public List<ConnectionInfoDTO> collectConnectionInfo(String host, int port) {
        try {
            return CommandHelper.doCommand(host, port, FourLetterCommand.cons, new ConnectionReaderParser());
        } catch (Exception e) {
            LOGGER.error("Collect {}:{} ConnectionInfoDTO with 'cons' error!", host, port, e);
            return null;
        }
    }

    @Override
    public boolean resetConnectionInfo(String host, int port) {
        try {
            String result = CommandHelper.doCommand(host, port, FourLetterCommand.crst, new StringReaderParser());
            if (result == null || !"Connection stats reset.".equals(result)) {
                LOGGER.error("Reset connection info {}:{} with 'crst' failed, result is {}.", host, port, result);
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            LOGGER.error("Reset connection info {}:{} with 'crst' failed", host, port, e);
            return false;
        }
    }

    @Override
    public boolean resetServerStatistic(String host, int port) {
        try {
            String result = CommandHelper.doCommand(host, port, FourLetterCommand.srst, new StringReaderParser());
            if (result == null || !"Server stats reset.".equals(result)) {
                LOGGER.error("Reset Server stats {}:{} with 'srst' failed, result is {}.", host, port, result);
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            LOGGER.error("Reset Server stats {}:{} with 'srst' failed", host, port, e);
            return false;
        }
    }

    @Override
    public String checkServerRunningNormal(String host, int port) {
        try {
            return CommandHelper.doCommand(host, port, FourLetterCommand.ruok, new StringReaderParser());
        } catch (Exception e) {
            LOGGER.error("collect ip:{},port:{} running normal error!", host, port, e);
            return null;
        }
    }

    @Override
    public Boolean checkInstanceRunOk(String host, int port) {
        String result = checkServerRunningNormal(host, port);
        if (result == null) { // 可能由于网络或者zkdoctor本身系统问题，导致收集信息为空，此时记录结果为null
            LOGGER.warn("Check instance {}:{} run ok, the result is NULL.", host, port);
            return null;
        } else {
            if ("imok".equals(result)) {
                return true;
            } else {
                LOGGER.warn("Check instance {}:{} run ok, the result is {}.", host, port, result);
                return false;
            }
        }
    }

    @Override
    public boolean cleanCollectData() {
        Date now = new Date();
        // 1、清理集群历史数据
        int clusterStateLogKeepDays = ConfigUtil.getClusterStateLogKeepDays();
        Date clusterStateLogStartDate = DateUtils.addDays(now, -clusterStateLogKeepDays);
        cleanClusterStateData(clusterStateLogStartDate);

        // 2、清理实例历史数据
        int instanceStateLogKeepDays = ConfigUtil.getInstanceStateLogKeepDays();
        Date instanceStateLogStartDate = DateUtils.addDays(now, -instanceStateLogKeepDays);
        cleanInstanceStateData(instanceStateLogStartDate);

        // 3、清理机器历史数据
        int machineStateLogKeepDays = ConfigUtil.getMachineStateLogKeepDays();
        Date machineStateLogStartDate = DateUtils.addDays(now, -machineStateLogKeepDays);
        cleanMachineStateData(machineStateLogStartDate);

        // 4、清理实例连接信息历史数据
        int clientConnectionsKeepDays = ConfigUtil.getClientConnectionsKeepDays();
        Date clientConnectionsStartDate = DateUtils.addDays(now, -clientConnectionsKeepDays);
        cleanClientConnectionsData(clientConnectionsStartDate);
        return true;
    }

    @Override
    public void cleanClusterStateData(final Date endDate) {
        Long delCount = clusterStateService.cleanClusterStateLogCount(endDate);
        if (delCount == null) {
            return;
        }
        cleanUpCollectData(delCount, endDate, new CleanCollectData<Date>() {
            @Override
            public void clean(Date endDate) {
                clusterStateService.cleanClusterStateLogData(endDate);
            }
        });
        LOGGER.info("Clean cluster state historical data, del count is " + delCount);
    }

    @Override
    public void cleanInstanceStateData(Date endDate) {
        Long delCount = instanceStateService.cleanInstanceStateLogCount(endDate);
        if (delCount == null) {
            return;
        }
        cleanUpCollectData(delCount, endDate, new CleanCollectData<Date>() {
            @Override
            public void clean(Date endDate) {
                instanceStateService.cleanInstanceStateLogData(endDate);
            }
        });
        LOGGER.info("Clean instance state historical data, del count is " + delCount);
    }

    @Override
    public void cleanMachineStateData(Date endDate) {
        Long delCount = machineStateService.cleanMachineStateLogCount(endDate);
        if (delCount == null) {
            return;
        }
        cleanUpCollectData(delCount, endDate, new CleanCollectData<Date>() {
            @Override
            public void clean(Date endDate) {
                machineStateService.cleanMachineStateLogData(endDate);
            }
        });
        LOGGER.info("Clean machine state historical data, del count is " + delCount);
    }

    @Override
    public void cleanClientConnectionsData(Date endDate) {
        Long delCount = instanceStateService.cleanClientConnectionsCount(endDate);
        if (delCount == null) {
            return;
        }
        cleanUpCollectData(delCount, endDate, new CleanCollectData<Date>() {
            @Override
            public void clean(Date endDate) {
                instanceStateService.cleanClientConnectionsData(endDate);
            }
        });
        LOGGER.info("Clean client connections historical data, del count is " + delCount);
    }

    @Override
    public void collectAllMachineState() {
        List<MachineInfo> machineInfoList = machineService.getAllMonitorMachine();
        if (CollectionUtils.isEmpty(machineInfoList)) {
            return;
        }
        for (final MachineInfo machineInfo : machineInfoList) {
            schedulerPool.submit(new Runnable() {
                @Override
                public void run() {
                    MachineState machineState = new MachineState();
                    machineState.setMachineId(machineInfo.getId());
                    machineState.setHost(machineInfo.getHost());
                    MachineStateLog machineStateLog = new MachineStateLog();
                    machineStateLog.setMachineId(machineInfo.getId());
                    machineStateLog.setHost(machineInfo.getHost());
                    if (collectMachineState(machineInfo.getHost(), machineState, machineStateLog)) {
                        if (machineInfo.getAvailable() == null || !machineInfo.getAvailable()) { // 更新机器为可用状态
                            machineService.updateMachineAvailable(machineInfo.getId(), true);
                        }
                        try {
                            machineStateService.mergeMachineState(machineState);
                        } catch (Exception e) {
                            LOGGER.warn("Merge machine state of {} failed.", machineInfo.getHost());
                        }
                        try {
                            machineStateService.insertMachineStateLogs(machineStateLog);
                        } catch (Exception e) {
                            LOGGER.warn("Save machine state log of {} failed.", machineInfo.getHost());
                        }
                    } else { // 说明ssh到机器异常，更新当前机器为不可用
                        if (machineInfo.getAvailable() == null || machineInfo.getAvailable()) {
                            machineService.updateMachineAvailable(machineInfo.getId(), false);
                        }
                    }
                }
            });
        }
    }

    @Override
    public boolean collectMachineState(String host, MachineState machineState, MachineStateLog machineStateLog) {
        try {
            Map<String, String> sshResult = SSHUtil.collectMachineState(host);
            Date now = new Date();
            machineState.setCreateTime(now);
            machineState.setModifyTime(now);

            machineStateLog.setCreateTime(now);

            // 收集机器cpu、内存、负载的情况
            String sarResult = sshResult.get(MachineProtocol.SAR);
            if (sarResult != null) {
                collectSARResult(machineStateLog, machineState, sarResult);
            }

            // 收集机器磁盘使用率情况
            String dfResult = sshResult.get(MachineProtocol.DF);
            if (dfResult != null) {
                collectDFResult(machineStateLog, machineState, dfResult);
            }

            // 收集机器流量情况
            String iftopResult = sshResult.get(MachineProtocol.IFTOP);
            if (iftopResult != null) {
                collectIftopResult(machineStateLog, machineState, iftopResult);
            }
        } catch (SSHException e) {
            LOGGER.warn("Collect machine {} state failed.", host, e);
            return false;
        }
        return true;
    }

    @Override
    public void collectAllInstanceConnections() {
        List<InstanceInfo> instanceInfoList = instanceService.getAllConnMonitorInstance();
        if (CollectionUtils.isEmpty(instanceInfoList)) {
            return;
        }
        final Date createTime = new Date();
        for (final InstanceInfo instanceInfo : instanceInfoList) {
            schedulerPool.submit(new Runnable() {
                @Override
                public void run() {
                    List<ConnectionInfoDTO> connectionInfoDTOList = collectConnectionInfo(instanceInfo.getHost(), instanceInfo.getPort());
                    if (CollectionUtils.isEmpty(connectionInfoDTOList)) {
                        return;
                    }
                    // 重置连接统计信息
                    resetConnectionInfo(instanceInfo.getHost(), instanceInfo.getPort());
                    // 处理并保存连接信息
                    List<ClientInfo> clientInfoList = new ArrayList<ClientInfo>();
                    for (ConnectionInfoDTO connectionInfoDTO : connectionInfoDTOList) {
                        ClientInfo clientInfo = new ClientInfo();
                        clientInfo.setClusterId(instanceInfo.getClusterId());
                        clientInfo.setInstanceId(instanceInfo.getId());
                        clientInfo.setClientIp(connectionInfoDTO.getIp());
                        clientInfo.setClientPort(connectionInfoDTO.getPort());
                        clientInfo.setSid(connectionInfoDTO.getSid());
                        clientInfo.setQueued(connectionInfoDTO.getQueued());
                        clientInfo.setRecved(connectionInfoDTO.getRecved());
                        clientInfo.setSent(connectionInfoDTO.getSent());
                        clientInfo.setEst(connectionInfoDTO.getEst());
                        clientInfo.setToTime(connectionInfoDTO.getTo());
                        clientInfo.setLcxid(connectionInfoDTO.getLcxid());
                        clientInfo.setLzxid(connectionInfoDTO.getLzxid());
                        clientInfo.setLresp(connectionInfoDTO.getLresp());
                        clientInfo.setLlat(connectionInfoDTO.getLlat());
                        clientInfo.setMinlat(connectionInfoDTO.getMinlat());
                        clientInfo.setAvglat(connectionInfoDTO.getAvglat());
                        clientInfo.setMaxlat(connectionInfoDTO.getMaxlat());
                        clientInfo.setCreateTime(createTime);
                        clientInfoList.add(clientInfo);
                    }
                    try {
                        instanceStateService.batchInsertClientConnections(clientInfoList);
                    } catch (Exception e) {
                        LOGGER.warn("Batch insert instance {}:{} client connections failed.", instanceInfo.getHost(), instanceInfo.getPort());
                    }
                }
            });
        }
    }

    /**
     * 收集机器cpu、内存、负载信息
     * sarResult结果示例：
     * Average:        CPU     %user     %nice   %system   %iowait    %steal     %idle
     * Average:        all      0.08      0.00      0.08      0.00      0.00     99.83
     * Average:          0      0.00      0.00      0.00      0.00      0.00    100.00
     * Average:          1      0.00      0.00      0.00      0.00      0.00    100.00
     * Average:          2      0.33      0.00      0.00      0.00      0.00     99.67
     * Average:          3      0.33      0.00      0.33      0.00      0.00     99.34
     * Average:    kbmemfree kbmemused  %memused kbbuffers  kbcached  kbcommit   %commit
     * Average:      2848228   1075936     27.42    255584    521360    395304      4.87
     * Average:      runq-sz  plist-sz   ldavg-1   ldavg-5  ldavg-15
     * Average:            0       186      0.08      0.03      0.0
     *
     * @param machineStateLog 机器历史状态信息
     * @param machineState    机器状态信息
     * @return
     */
    private boolean collectSARResult(MachineStateLog machineStateLog, MachineState machineState, String sarResult) {
        String[] results = sarResult.split(SymbolConstant.NEXT_LINE);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        for (int i = 0; i < results.length; ++i) {
            try {
                if (results[i].contains(MachineProtocol.CPU_USER_STRING)) {
                    // 1、CPU相关，分别取：
                    // %user：  用户级别下消耗的CPU时间的比例
                    // %nice：  nice优先级用户级集群程序消耗的CPU时间比例
                    // %system：系统核心级别下消耗的CPU时间的比例
                    // %iowait：等待I/O操作占用CPU总时间的百分比
                    // %idle：  CPU空闲时间占用CPU总时间的百分比
                    String[] cpu = results[++i].split(SymbolConstant.ANY_BLANK);
                    if (cpu.length == 8) {
                        machineStateLog.setCpuUserPercent(cpu[2]);
                        machineStateLog.setCpuNicePercent(cpu[3]);
                        machineStateLog.setCpuSysPercent(cpu[4]);
                        machineStateLog.setIoWaitPercent(cpu[5]);
                        machineStateLog.setIdlePercent(cpu[7]);
                        // 保存机器CPU使用率 = user + nice + sys
                        double cpuUsePercent = Double.parseDouble(cpu[2]) + Double.parseDouble(cpu[3]) + Double.parseDouble(cpu[4]);
                        machineState.setCpuUsage(decimalFormat.format(cpuUsePercent));
                        machineStateLog.setCpuUsage(machineState.getCpuUsage());
                    }
                    ++i;
                    // 取每个核CPU信息 单核cpu使用率
                    StringBuilder cpuDetail = new StringBuilder();
                    while (!results[i].contains(MachineProtocol.MEM_USED_STRING)
                            && !results[i].contains(MachineProtocol.LDAVG_STRING) && i < results.length) {
                        String[] singleCpu = results[i++].split(SymbolConstant.ANY_BLANK);
                        if (singleCpu.length == 8) {
                            try {
                                String cpuUserPercent = singleCpu[2];
                                String cpuNicePercent = singleCpu[3];
                                String cpuSysPercent = singleCpu[4];
                                if (StringUtils.isNotBlank(cpuUserPercent) && StringUtils.isNotBlank(cpuSysPercent)
                                        && StringUtils.isNotBlank(cpuNicePercent)) {
                                    cpuDetail.append(decimalFormat.format(Double.parseDouble(cpuUserPercent)
                                            + Double.parseDouble(cpuSysPercent) + Double.parseDouble(cpuNicePercent)));
                                    cpuDetail.append(SymbolConstant.BLANK);
                                }
                            } catch (Exception e) {
                                LOGGER.warn("Collect {} single cpu info failed. ", machineState.getHost(), e);
                            }
                        }
                    }
                    if (cpuDetail.length() <= 500) { // 长度限制，当总长度大于500时，不进行统计
                        machineStateLog.setCpuSingleUsage(cpuDetail.toString());
                    }
                    --i;
                } else if (results[i].contains(MachineProtocol.MEM_USED_STRING)) {
                    // 2、内存相关，内存使用率 = 1 - (memFree + buffers + cached) / (memFree + memUsed)
                    String[] memory = results[++i].split(SymbolConstant.ANY_BLANK);
                    if (memory.length >= 8) {
                        machineStateLog.setMemoryFree(memory[1]);
                        double memoryTotal = Double.parseDouble(memory[1]) + Double.parseDouble(memory[2]);
                        machineStateLog.setMemoryTotal(decimalFormat.format(memoryTotal));
                        machineStateLog.setBuffers(memory[4]);
                        machineStateLog.setCached(memory[5]);
                        // 保存机器内存使用率
                        double memoryUsage = 100 * (1 - (Double.parseDouble(memory[1]) + Double.parseDouble(memory[4]) + Double.parseDouble(memory[5]))
                                / memoryTotal);
                        machineState.setMemoryUsage(decimalFormat.format(memoryUsage));
                        machineState.setMemoryFree(memory[1]);
                        machineState.setMemoryTotal(machineStateLog.getMemoryTotal());
                        machineStateLog.setMemoryUsage(machineState.getMemoryUsage());
                    }
                } else if (results[i].contains(MachineProtocol.LDAVG_STRING)) {
                    // 3、平均负载，取最后1分钟的系统平均负载
                    String[] loadAverage = results[++i].split(SymbolConstant.ANY_BLANK);
                    if (loadAverage.length == 6) {
                        machineStateLog.setAvgLoad(loadAverage[3]);
                        machineState.setAvgLoad(loadAverage[3]);
                    }
                }
            } catch (Exception e) {
                LOGGER.warn("Collect machine {} sar result failed.", machineState.getHost(), e);
            }
        }
        return true;
    }

    /**
     * 收集机器磁盘信息
     * dfResult结果示例：
     * / 3993.6 570.0 16 -/dev/shm 1945.6  0 -/home 3993.6 8.3 1 -/usr 24576.0 1945.6 9 -/var 3993.6 220.0 6 -
     * Filesystem      Size  Used Avail Use% Mounted on
     * /dev/vda1       3.9G  570M  3.1G  16% /
     * tmpfs           1.9G     0  1.9G   0% /dev/shm
     * /dev/vda2       3.9G  8.3M  3.7G   1% /home
     * /dev/vda5        24G  1.9G   21G   9% /usr
     * /dev/vda3       3.9G  220M  3.4G   6% /var
     *
     * @param machineStateLog 机器历史状态信息
     * @param machineState    机器状态信息
     * @return
     */
    private boolean collectDFResult(MachineStateLog machineStateLog, MachineState machineState, String dfResult) {
        String[] diskUsageDetail = dfResult.split(SymbolConstant.NEXT_LINE);
        // 格式为：mounted Size used Use% - mounted Size used Use%，单位为MB
        StringBuilder diskUsage = new StringBuilder();
        StringBuilder diskFreePercent = new StringBuilder();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        for (int i = 1; i < diskUsageDetail.length; ++i) {
            String[] disk = diskUsageDetail[i].split(SymbolConstant.ANY_BLANK);
            if (disk.length == 6) {
                try {
                    String usePercent = disk[4].replace(SymbolConstant.PERCENT, StringUtils.EMPTY);
                    // 磁盘空闲率相关统计
                    diskFreePercent.append(disk[5]).append(SymbolConstant.BLANK);
                    diskFreePercent.append(decimalFormat.format(100 - Double.parseDouble(usePercent))).
                            append(SymbolConstant.BLANK).append(SymbolConstant.DASH);

                    String dataDiskUsed = null;
                    String dataDiskTotal = null;
                    diskUsage.append(disk[5]).append(SymbolConstant.BLANK);
                    // Size 单位：GB
                    if (disk[1].contains(MachineProtocol.SIZE_G)) {
                        dataDiskTotal = disk[1].replace(MachineProtocol.SIZE_G, StringUtils.EMPTY);
                    } else if (disk[1].contains(MachineProtocol.SIZE_M)) {
                        dataDiskTotal = decimalFormat.format(Double.parseDouble(disk[1].replace(MachineProtocol.SIZE_M, StringUtils.EMPTY)) / SymbolConstant.BASE_UNIT);
                    }
                    diskUsage.append(dataDiskTotal);
                    diskUsage.append(SymbolConstant.BLANK);
                    // Used 单位：GB
                    if (disk[2].contains(MachineProtocol.SIZE_G)) {
                        dataDiskUsed = disk[2].replace(MachineProtocol.SIZE_G, StringUtils.EMPTY);
                    } else if (disk[2].contains(MachineProtocol.SIZE_M)) {
                        dataDiskUsed = decimalFormat.format(Double.parseDouble(disk[2].replace(MachineProtocol.SIZE_M, StringUtils.EMPTY)) / SymbolConstant.BASE_UNIT);
                    }
                    diskUsage.append(dataDiskUsed);
                    // Use% 单位%
                    diskUsage.append(SymbolConstant.BLANK).append(usePercent).append(SymbolConstant.BLANK); // Mounted on，以配置的数据目录为准
                    if (dfResult.contains(ConfigUtil.getDataDirConfig()) && disk[5].equals(ConfigUtil.getDataDirConfig())) {
                        machineState.setDiskUsage(usePercent);
                        machineStateLog.setDiskUsage(usePercent);
                        machineState.setDataDiskTotal(String.valueOf(dataDiskTotal));
                        machineState.setDataDiskUsed(String.valueOf(dataDiskUsed));
                    }
                } catch (Exception e) {
                    LOGGER.warn("Collect machine {} df result failed.", machineState.getHost(), e);
                }
            }
            diskUsage.append(SymbolConstant.DASH);
        }
        machineStateLog.setDiskSituation(diskUsage.toString());
        machineStateLog.setDiskFreePercent(diskFreePercent.toString());
        return true;
    }

    /**
     * 收集机器流量信息
     * iftopResult结果示例：
     * Listening on eth0
     * # Host name (port/service if enabled)            last 2s   last 10s   last 40s cumulative
     * --------------------------------------------------------------------------------------------
     * 1 127.0.0.1                           =>       746B       746B       746B     1.46KB
     * 127.0.0.1                            <=     1.04KB     1.04KB     1.04KB     2.08KB
     * 2 127.0.0.1                           =>       107B       107B       107B       214B
     * 127.0.0.1                              <=       283B       283B       283B       566B
     * 3 127.0.0.1                           =>       110B       110B       110B       220B
     * 127.0.0.1                             <=       126B       126B       126B       252B
     * 4 127.0.0.1                           =>        46B        46B        46B        92B
     * 127.0.0.1                             <=        30B        30B        30B        60B
     * --------------------------------------------------------------------------------------------
     * Total send rate:                                     0.99KB     0.99KB     0.99KB
     * Total receive rate:                                  1.47KB     1.47KB     1.47KB
     * Total send and receive rate:                         2.45KB     2.45KB     2.45KB
     * --------------------------------------------------------------------------------------------
     * Peak rate (sent/received/total):                     0.99KB     1.47KB     2.45KB
     * Cumulative (sent/received/total):                    1.97KB     2.93KB     4.91KB
     * ============================================================================================
     *
     * @param machineStateLog 机器历史状态信息
     * @param machineState    机器状态信息
     * @return
     */
    private boolean collectIftopResult(MachineStateLog machineStateLog, MachineState machineState, String iftopResult) {
        String[] netTraffic = iftopResult.split(SymbolConstant.NEXT_LINE);
        List<Map<String, Double>> netFlowInList = new LinkedList<Map<String, Double>>();
        List<Map<String, Double>> netFlowOutList = new LinkedList<Map<String, Double>>();
        double totalReceive = 0;
        double totalSend = 0;
        for (int i = 0; i < netTraffic.length; i++) {
            try {
                // 跳过分隔符
                if (netTraffic[i].contains(MachineProtocol.DASH_SEPERATOR) || netTraffic[i].contains(MachineProtocol.EQUAL_SEPERATOR)) {
                    continue;
                }
                String[] trafficDetail = netTraffic[i].trim().split(SymbolConstant.ANY_BLANK);
                // 机器发送流量
                if (trafficDetail.length == 7 && netTraffic[i].contains(MachineProtocol.TRAFFIC_SEND)) {
                    ++i;
                    // 机器接收流量
                    String[] receiveTraffic = netTraffic[i].trim().split(SymbolConstant.ANY_BLANK);
                    if (receiveTraffic.length == 6 && netTraffic[i].contains(MachineProtocol.TRAFFIC_RECEIVE)) {
                        Map<String, Double> send = new HashMap<String, Double>();
                        send.put(receiveTraffic[0], transformTrafficToKByte(trafficDetail[4]));
                        netFlowOutList.add(send);
                        Map<String, Double> receive = new HashMap<String, Double>();
                        receive.put(receiveTraffic[0], transformTrafficToKByte(receiveTraffic[3]));
                        netFlowInList.add(receive);
                    }
                } else if (netTraffic[i].contains(MachineProtocol.TOTAL_SEND)) { // 总发送流量
                    if (trafficDetail.length == 6) {
                        totalSend = transformTrafficToKByte(trafficDetail[4]);
                    }
                } else if (netTraffic[i].contains(MachineProtocol.TOTAL_RECEIVE)) { // 总接收流量
                    if (trafficDetail.length == 6) {
                        totalReceive = transformTrafficToKByte(trafficDetail[4]);
                    }
                } else if (netTraffic[i].contains(MachineProtocol.TOTAL_SEND_RECEIVE)) { // 总流量
                    if (trafficDetail.length == 8) {
                        machineState.setNetTraffic(String.valueOf(transformTrafficToKByte(trafficDetail[6])));
                    }
                }
            } catch (Exception e) {
                LOGGER.warn("Collect machine {} iftop result failed.", machineState.getHost(), e);
            }
        }
        if (netFlowInList.size() > 0) {
            machineStateLog.setNetFlowIn(JSONObject.toJSONString(netFlowInList));
        }
        if (netFlowOutList.size() > 0) {
            machineStateLog.setNetFlowOut(JSONObject.toJSONString(netFlowOutList));
        }
        machineStateLog.setNetTraffic(totalReceive + SymbolConstant.BLANK + totalSend);
        return true;
    }

    /**
     * 将获取的流量数据转化为KB单位数据返回
     *
     * @param traffic 流量数据，包含单位信息
     * @return
     */
    private double transformTrafficToKByte(String traffic) {
        if (StringUtils.isBlank(traffic)) {
            return 0;
        }
        try {
            if (traffic.contains(MachineProtocol.SIZE_KB)) { // 单位为：KB
                return Double.parseDouble(traffic.replace(MachineProtocol.SIZE_KB, StringUtils.EMPTY));
            } else if (traffic.contains(MachineProtocol.SIZE_MB)) { // 单位为：MB
                return Double.parseDouble(traffic.replace(MachineProtocol.SIZE_MB, StringUtils.EMPTY))
                        * SymbolConstant.BASE_UNIT;
            } else if (traffic.contains(MachineProtocol.SIZE_GB)) { // 单位为：GB
                return Double.parseDouble(traffic.replace(MachineProtocol.SIZE_GB, StringUtils.EMPTY))
                        * SymbolConstant.BASE_UNIT * SymbolConstant.BASE_UNIT;
            } else { // 单位为：B
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                double trafficValue = Double.parseDouble(traffic.replace(MachineProtocol.SIZE_B, StringUtils.EMPTY)) / SymbolConstant.BASE_UNIT;
                return Double.parseDouble(decimalFormat.format(trafficValue));
            }
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 根据服务端返回数据，生成实例状态信息
     *
     * @param clusterId          集群id
     * @param instanceId         实例id
     * @param hostInfo           实例ip：port
     * @param serverStateInfoDTO 服务端返回数据
     * @return
     */
    private InstanceState getInstanceStateFromServerInfo(int clusterId, int instanceId, String hostInfo,
                                                         ServerStateInfoDTO serverStateInfoDTO) {
        InstanceState instanceState = new InstanceState();
        instanceState.setInstanceId(instanceId);
        instanceState.setClusterId(clusterId);
        instanceState.setHostInfo(hostInfo);
        instanceState.setVersion(serverStateInfoDTO.getZk_version());
        instanceState.setAvgLatency(serverStateInfoDTO.getZk_avg_latency());
        instanceState.setMaxLatency(serverStateInfoDTO.getZk_max_latency());
        instanceState.setMinLatency(serverStateInfoDTO.getZk_min_latency());
        instanceState.setReceived(serverStateInfoDTO.getZk_packets_received());
        instanceState.setSent(serverStateInfoDTO.getZk_packets_sent());
        instanceState.setCurrConnections(serverStateInfoDTO.getZk_num_alive_connections());
        instanceState.setCurrOutstandings(serverStateInfoDTO.getZk_outstanding_requests());
        instanceState.setServerStateLag(ZKServerStateEnum.getServerStateByDesc(serverStateInfoDTO.getZk_server_state()));
        instanceState.setCurrZnodeCount(serverStateInfoDTO.getZk_znode_count());
        instanceState.setCurrWatchCount(serverStateInfoDTO.getZk_watch_count());
        instanceState.setCurrEphemeralsCount(serverStateInfoDTO.getZk_ephemerals_count());
        instanceState.setApproximateDataSize(serverStateInfoDTO.getZk_approximate_data_size());
        instanceState.setOpenFileDescriptorCount(serverStateInfoDTO.getZk_open_file_descriptor_count());
        instanceState.setMaxFileDescriptorCount(serverStateInfoDTO.getZk_max_file_descriptor_count());
        instanceState.setFollowers(serverStateInfoDTO.getZk_followers());
        instanceState.setSyncedFollowers(serverStateInfoDTO.getZk_synced_followers());
        instanceState.setPendingSyncs(serverStateInfoDTO.getZk_pending_syncs());
        return instanceState;
    }

    /**
     * 根据实例状态信息，生成实例历史状态信息
     *
     * @param instanceState 实例状态信息
     * @return
     */
    private InstanceStateLog getInstanceStateLogFromState(InstanceState instanceState) {
        InstanceStateLog instanceStateLog = new InstanceStateLog();
        instanceStateLog.setInstanceId(instanceState.getInstanceId());
        instanceStateLog.setHostInfo(instanceState.getHostInfo());
        instanceStateLog.setClusterId(instanceState.getClusterId());
        instanceStateLog.setVersion(instanceState.getVersion());
        instanceState.setLeaderId(instanceState.getLeaderId());
        instanceStateLog.setAvgLatency(instanceState.getAvgLatency());
        instanceStateLog.setMaxLatency(instanceState.getMaxLatency());
        instanceStateLog.setMinLatency(instanceState.getMinLatency());
        instanceStateLog.setReceived(instanceState.getReceived());
        instanceStateLog.setSent(instanceState.getSent());
        instanceStateLog.setConnections(instanceState.getCurrConnections());
        instanceStateLog.setZnodeCount(instanceState.getCurrZnodeCount());
        instanceStateLog.setWatchCount(instanceState.getCurrWatchCount());
        instanceStateLog.setEphemeralsCount(instanceState.getCurrEphemeralsCount());
        instanceStateLog.setOutstandings(instanceState.getCurrOutstandings());
        instanceStateLog.setApproximateDataSize(instanceState.getApproximateDataSize());
        instanceStateLog.setOpenFileDescriptorCount(instanceState.getOpenFileDescriptorCount());
        instanceStateLog.setMaxFileDescriptorCount(instanceState.getMaxFileDescriptorCount());
        instanceStateLog.setServerStateLag(instanceState.getServerStateLag());
        instanceStateLog.setFollowers(instanceState.getFollowers());
        instanceStateLog.setSyncedFollowers(instanceState.getSyncedFollowers());
        instanceStateLog.setPendingSyncs(instanceState.getPendingSyncs());
        instanceStateLog.setRunOk(instanceState.getRunOk());
        instanceStateLog.setCreateTime(instanceState.getCreateTime());
        return instanceStateLog;
    }

    /**
     * 保存集群状态历史情况
     *
     * @param clusterId         集群id
     * @param createTime        收集时间
     * @param instanceStateList 实例状态列表
     */
    private void mergeClusterStats(int clusterId, Date createTime, List<InstanceState> instanceStateList) {
        ClusterState clusterState = new ClusterState();
        clusterState.setClusterId(clusterId);
        clusterState.setInstanceNumber(instanceStateList.size());
        clusterState.setModifyTime(createTime);
        clusterState.setCreateTime(createTime);
        for (InstanceState instanceStats : instanceStateList) {
            // leader或者standalone模式，集群数据只统计leader或者standalone节点
            if (ZKServerStateEnum.LEADER.getServerState() == instanceStats.getServerStateLag()
                    || ZKServerStateEnum.STANDALONE.getServerState() == instanceStats.getServerStateLag()) {
                clusterState.setZnodeCount(instanceStats.getCurrZnodeCount());
                clusterState.setEphemerals(instanceStats.getCurrEphemeralsCount());
                clusterState.setApproximateDataSize(instanceStats.getApproximateDataSize());
            }
            // 平均延时记录实例最大值
            if (instanceStats.getAvgLatency() != null &&
                    (clusterState.getAvgLatencyMax() == null || instanceStats.getAvgLatency() > clusterState.getAvgLatencyMax())) {
                clusterState.setAvgLatencyMax(instanceStats.getAvgLatency());
            }
            // 最大延时记录实例最大值
            if (instanceStats.getMaxLatency() != null &&
                    (clusterState.getMaxLatencyMax() == null || instanceStats.getMaxLatency() > clusterState.getMaxLatencyMax())) {
                clusterState.setMaxLatencyMax(instanceStats.getMaxLatency());
            }
            // 最小延时记录实例最大值
            if (instanceStats.getMinLatency() != null &&
                    (clusterState.getMinLatencyMax() == null || instanceStats.getMinLatency() > clusterState.getMinLatencyMax())) {
                clusterState.setMinLatencyMax(instanceStats.getMinLatency());
            }
            // 收包数统计总数
            if (instanceStats.getReceived() != null) {
                if (clusterState.getReceivedTotal() == null) {
                    clusterState.setReceivedTotal(instanceStats.getReceived());
                } else {
                    clusterState.setReceivedTotal(clusterState.getReceivedTotal() + instanceStats.getReceived());
                }
            }
            // 发包数统计总数
            if (instanceStats.getSent() != null) {
                if (clusterState.getSentTotal() == null) {
                    clusterState.setSentTotal(instanceStats.getSent());
                } else {
                    clusterState.setSentTotal(clusterState.getSentTotal() + instanceStats.getSent());
                }
            }
            // 连接数统计总数
            if (instanceStats.getCurrConnections() != null) {
                if (clusterState.getConnectionTotal() == null) {
                    clusterState.setConnectionTotal(instanceStats.getCurrConnections());
                } else {
                    clusterState.setConnectionTotal(clusterState.getConnectionTotal() + instanceStats.getCurrConnections());
                }
            }
            // watcher数统计总数
            if (instanceStats.getCurrWatchCount() != null) {
                if (clusterState.getWatcherTotal() == null) {
                    clusterState.setWatcherTotal(instanceStats.getCurrWatchCount());
                } else {
                    clusterState.setWatcherTotal(clusterState.getWatcherTotal() + instanceStats.getCurrWatchCount());
                }
            }
            // 堆积请求数统计总数
            if (instanceStats.getCurrOutstandings() != null) {
                if (clusterState.getOutstandingTotal() == null) {
                    clusterState.setOutstandingTotal(instanceStats.getCurrOutstandings());
                } else {
                    clusterState.setOutstandingTotal(clusterState.getOutstandingTotal() + instanceStats.getCurrOutstandings());
                }
            }
            // 打开文件描述符数量取集群中的最大值
            if (instanceStats.getOpenFileDescriptorCount() != null) {
                if (clusterState.getOpenFileDescriptorCountTotal() == null ||
                        instanceStats.getOpenFileDescriptorCount() > clusterState.getOpenFileDescriptorCountTotal()) {
                    clusterState.setOpenFileDescriptorCountTotal(instanceStats.getOpenFileDescriptorCount());
                }
            }
        }
        try {
            clusterStateService.mergeClusterState(clusterState);
        } catch (Exception e) {
            LOGGER.error("Merge cluster state failed.", e);
        }
        try {
            clusterStateService.insertClusterStateLogs(clusterState);
        } catch (Exception e) {
            LOGGER.error("Save cluster state log failed.", e);
        }
    }

    /**
     * 删除历史统计数据，每次删除1000条，睡眠10ms
     *
     * @param delCount            需要删除的数据总量
     * @param endDate             删除该日期之前的数据
     * @param cleanHistoricalData 清除数据的接口
     * @param <T>                 日期类型，Date或Long
     */
    private <T> void cleanUpCollectData(final long delCount, final T endDate, final CleanCollectData cleanHistoricalData) {
        schedulerPool.submit(new Runnable() {
            @Override
            public void run() {
                if (delCount > 0) {
                    int forCount = (int) (delCount / 1000) + 1;
                    for (int i = 1; i <= forCount; i++) {
                        try {
                            cleanHistoricalData.clean(endDate);
                            try {
                                Thread.sleep(10L);
                            } catch (InterruptedException e) {
                            }
                        } catch (Exception e) {
                            LOGGER.warn("clean up historical data error, for count :" + i);
                        }
                    }
                    LOGGER.info("total clean for loop count is " + forCount);
                }
            }
        });
    }

    /**
     * 清除数据接口
     *
     * @param <T> 日期类型
     */
    private interface CleanCollectData<T> {
        void clean(T endDate);
    }
}
