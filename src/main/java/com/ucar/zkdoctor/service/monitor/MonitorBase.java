package com.ucar.zkdoctor.service.monitor;

import com.ucar.zkdoctor.pojo.po.ClusterInfo;
import com.ucar.zkdoctor.pojo.po.ClusterState;
import com.ucar.zkdoctor.pojo.po.MachineInfo;
import com.ucar.zkdoctor.pojo.po.MachineState;
import com.ucar.zkdoctor.pojo.po.MonitorIndicator;
import com.ucar.zkdoctor.pojo.po.MonitorTask;
import com.ucar.zkdoctor.service.cluster.ClusterService;
import com.ucar.zkdoctor.service.cluster.ClusterStateService;
import com.ucar.zkdoctor.service.instance.InstanceService;
import com.ucar.zkdoctor.service.instance.InstanceStateService;
import com.ucar.zkdoctor.service.machine.MachineService;
import com.ucar.zkdoctor.service.machine.MachineStateService;
import com.ucar.zkdoctor.util.constant.ClusterEnumClass.ClusterStatusEnum;
import com.ucar.zkdoctor.util.thread.NamedThreadFactory;
import com.ucar.zkdoctor.util.tool.DateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description: 监控基类
 * Created on 2018/2/6 11:16
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Service
public class MonitorBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorBase.class);

    @Resource
    protected MonitorService monitorService;

    @Resource
    protected ClusterService clusterService;

    @Resource
    protected ClusterStateService clusterStateService;

    @Resource
    protected InstanceService instanceService;

    @Resource
    protected InstanceStateService instanceStateService;

    @Resource
    protected MachineService machineService;

    @Resource
    protected MachineStateService machineStateService;

    /**
     * 当前集群最新状态信息
     */
    protected static Map<Integer, ClusterState> currClusterStates = new ConcurrentHashMap<Integer, ClusterState>();

    /**
     * 当前机器最新状态信息
     */
    protected static Map<String, MachineState> currMachineStates = new ConcurrentHashMap<String, MachineState>();

    /**
     * 所有监控对象map，初始化时，将所有监控对象信息初始化到该map中
     */
    public static Map<String, MonitorBase> monitorServiceMap = new ConcurrentHashMap<String, MonitorBase>();

    /**
     * 监控任务线程池
     */
    protected static ThreadPoolExecutor monitorThreadPool = new ThreadPoolExecutor(
            32, 64, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1000),
            new NamedThreadFactory("monitor", true));


    /**
     * 监控，具体监控由子类实现
     */
    public void monitor() {
    }

    /**
     * 监控执行入口
     */
    public void execute() {
        // 更新集群状态信息
        updateCurrClusterStates();
        // 更新机器状态信息
        updateCurrMachineStates();
        for (final Map.Entry<String, MonitorBase> monitorServiceEntry : MonitorBase.monitorServiceMap.entrySet()) {
            monitorThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        monitorServiceEntry.getValue().monitor();
                    } catch (Exception e) {
                        LOGGER.warn("Monitor failed.", e);
                    }
                }
            });
        }
    }

    /**
     * 更新集群状态信息
     */
    private void updateCurrClusterStates() {
        if (!currClusterStates.isEmpty()) {
            synchronized (currClusterStates) {
                if (!currClusterStates.isEmpty())
                    // 清空之前状态信息
                    currMachineStates.clear();
            }
        }
        List<ClusterInfo> clusterInfoList = clusterService.getAllClusterInfos();
        if (CollectionUtils.isEmpty(clusterInfoList)) {
            return;
        }
        synchronized (currClusterStates) {
            Date beforeTime = DateUtil.getIntervalDate(new Date(), -2 * 60 * 1000);
            for (ClusterInfo clusterInfo : clusterInfoList) {
                ClusterState clusterState = clusterStateService.getClusterStateByClusterId(clusterInfo.getId());
                if (clusterState == null) {
                    continue;
                }
                // 状态信息收集时间在2分钟以内
                if ((clusterState.getModifyTime() == null && clusterState.getCreateTime() != null && clusterState.getCreateTime().after(beforeTime)) ||
                        (clusterState.getModifyTime() != null && clusterState.getModifyTime().after(beforeTime))) {
                    // 保存最新集群状态信息
                    currClusterStates.put(clusterState.getClusterId(), clusterState);
                }
            }
        }
    }

    /**
     * 更新机器状态信息
     */
    private void updateCurrMachineStates() {
        if (!currMachineStates.isEmpty()) {
            synchronized (currMachineStates) {
                if (!currMachineStates.isEmpty())
                    // 清空之前状态信息
                    currMachineStates.clear();
            }
        }
        // 没有监控中的机器信息，则直接返回
        List<MachineInfo> machineInfoList = machineService.getAllMonitorMachine();
        if (CollectionUtils.isEmpty(machineInfoList)) {
            return;
        }
        synchronized (currMachineStates) {
            Date beforeTime = DateUtil.getIntervalDate(new Date(), -2 * 60 * 1000);
            for (MachineInfo machineInfo : machineInfoList) {
                MachineState machineState = machineStateService.getMachineStateByMachineId(machineInfo.getId());
                if (machineState == null) {
                    continue;
                }
                // 状态信息收集时间在2分钟以内
                if ((machineState.getModifyTime() == null && machineState.getCreateTime() != null &&
                        machineState.getCreateTime().after(beforeTime)) ||
                        (machineState.getModifyTime() != null && machineState.getModifyTime().after(beforeTime))) {
                    // 保存最新机器状态信息
                    currMachineStates.put(machineState.getHost(), machineState);
                }
            }
        }
    }

    /**
     * 具体执行监控方法
     *
     * @param monitorServiceName 监控服务名称，默认只类名
     * @param monitor            监控接口
     */
    protected void specifyMonitor(String monitorServiceName, final Monitor monitor) {
        // 获取监控指标信息
        final MonitorIndicator monitorIndicator = monitorService.getIndicatorByClassName(monitorServiceName);
        if (monitorIndicator == null || monitorIndicator.getSwitchOn() == null || !monitorIndicator.getSwitchOn()) {
            return;
        }
        // 获取其下所有开启的监控任务
        List<MonitorTask> monitorTaskList = monitorService.getTasksByParams(monitorIndicator.getId(), null, true);
        if (CollectionUtils.isEmpty(monitorTaskList)) {
            return;
        }
        for (final MonitorTask monitorTask : monitorTaskList) {
            monitorThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    monitor.monitor(monitorTask, monitorIndicator);
                }
            });
        }
    }

    /**
     * 监控接口
     */
    protected interface Monitor {
        void monitor(MonitorTask monitorTask, MonitorIndicator monitorIndicator);
    }

    /**
     * 系统初始化监控指标基本信息，由各个监控子类自行设置各自的信息
     * 如果初始化反射获取监控类信息没有初始化相关bean，则先赋值，防止空指针异常
     */
    public void init(MonitorService monitorService, ClusterService clusterService,
                     ClusterStateService clusterStateService, InstanceService instanceService, InstanceStateService instanceStateService,
                     MachineService machineService, MachineStateService machineStateService) {
        if (this.monitorService == null && monitorService != null) {
            this.monitorService = monitorService;
        }
        if (this.clusterService == null && clusterService != null) {
            this.clusterService = clusterService;
        }
        if (this.clusterStateService == null && clusterStateService != null) {
            this.clusterStateService = clusterStateService;
        }
        if (this.instanceService == null && instanceService != null) {
            this.instanceService = instanceService;
        }
        if (this.instanceStateService == null && instanceStateService != null) {
            this.instanceStateService = instanceStateService;
        }
        if (this.machineService == null && machineService != null) {
            this.machineService = machineService;
        }
        if (this.machineStateService == null && machineStateService != null) {
            this.machineStateService = machineStateService;
        }
    }

    /**
     * 同步监控指标信息
     *
     * @param monitorIndicator 监控指标信息
     */
    protected void synMonitorInfo(MonitorIndicator monitorIndicator) {
        if (monitorIndicator == null) {
            return;
        }
        MonitorIndicator monitorIndicatorDB = monitorService.getIndicatorByClassName(monitorIndicator.getClassName());
        if (monitorIndicatorDB != null) {
            return;
        }
        try {
            LOGGER.info("Init monitor inidicator:{} to DB.", monitorIndicator.getClassName());
            monitorService.insertIndicator(monitorIndicator);
            List<ClusterInfo> clusterInfoList = clusterService.getAllClusterInfos();
            if (CollectionUtils.isNotEmpty(clusterInfoList)) {
                for (ClusterInfo clusterInfo : clusterInfoList) {
                    MonitorTask monitorTask = new MonitorTask();
                    monitorTask.setIndicatorId(monitorIndicator.getId());
                    monitorTask.setClusterId(clusterInfo.getId());
                    monitorTask.setAlertValue(monitorIndicator.getDefaultAlertValue());
                    monitorTask.setAlertInterval(monitorIndicator.getDefaultAlertInterval());
                    monitorTask.setAlertFrequency(monitorIndicator.getDefaultAlertFrequency());
                    monitorTask.setAlertForm(monitorIndicator.getDefaultAlertForm());
                    // 只有正常运行中的集群，以设置的开关为主，其余则创建任务，但是开关关闭
                    monitorTask.setSwitchOn(ClusterStatusEnum.RUNNING.getStatus().equals(clusterInfo.getStatus())
                            & monitorIndicator.getSwitchOn());
                    monitorService.insertTask(monitorTask);
                }
            }
            LOGGER.info("Synchronize monitor indicator and task success. the monitor indicator is {}.",
                    monitorIndicator.getIndicatorName());
        } catch (Exception e) {
            LOGGER.error("Synchronize monitor indicator and task error!  the monitor indicator is {}.",
                    monitorIndicator.getIndicatorName(), e);
        }
    }
}
