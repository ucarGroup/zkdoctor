package com.ucar.zkdoctor.service.monitor.impl;

import com.ucar.zkdoctor.business.clusterMonitorAlarmTask.pojo.ZkAlarmInfo;
import com.ucar.zkdoctor.business.clusterMonitorAlarmTask.service.ZkAlarmInfoService;
import com.ucar.zkdoctor.dao.mysql.MonitorIndicatorDao;
import com.ucar.zkdoctor.dao.mysql.MonitorTaskDao;
import com.ucar.zkdoctor.pojo.bo.CacheObject;
import com.ucar.zkdoctor.pojo.bo.MonitorIndicatorSearchBO;
import com.ucar.zkdoctor.pojo.bo.MonitorTaskSearchBO;
import com.ucar.zkdoctor.pojo.po.ClusterInfo;
import com.ucar.zkdoctor.pojo.po.ClusterState;
import com.ucar.zkdoctor.pojo.po.InstanceState;
import com.ucar.zkdoctor.pojo.po.MachineState;
import com.ucar.zkdoctor.pojo.po.MonitorIndicator;
import com.ucar.zkdoctor.pojo.po.MonitorTask;
import com.ucar.zkdoctor.service.cluster.ClusterService;
import com.ucar.zkdoctor.service.cluster.ClusterStateService;
import com.ucar.zkdoctor.service.instance.InstanceService;
import com.ucar.zkdoctor.service.instance.InstanceStateService;
import com.ucar.zkdoctor.service.machine.MachineService;
import com.ucar.zkdoctor.service.machine.MachineStateService;
import com.ucar.zkdoctor.service.monitor.AlertService;
import com.ucar.zkdoctor.service.monitor.MonitorBase;
import com.ucar.zkdoctor.service.monitor.MonitorService;
import com.ucar.zkdoctor.util.constant.ClusterEnumClass;
import com.ucar.zkdoctor.util.constant.ZKServerEnumClass;
import com.ucar.zkdoctor.util.tool.DateUtil;
import com.ucar.zkdoctor.util.tool.ReflectUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: 监控服务接口实现类
 * Created on 2018/2/5 16:13
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Service("monitorService")
public class MonitorServiceImpl implements MonitorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorServiceImpl.class);

    /**
     * 所有监控类所在包位置
     * 所有监控类继承MonitorBase类，并在com.ucar.zkdoctor.service.monitor.detail包下
     * 新增监控只需满足以上条件，并重写monitor()与init()方法，即可自动添加至监控
     */
    private static final String BASE_MONITOR_CLASS_PACKAGE = "com.ucar.zkdoctor.service.monitor.detail";

    @Resource
    private MonitorIndicatorDao monitorIndicatorDao;

    @Resource
    private MonitorTaskDao monitorTaskDao;

    @Resource
    private MonitorService monitorService;

    @Resource
    private ClusterService clusterService;

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

    @Resource
    private AlertService alertService;

    @Autowired
    private ZkAlarmInfoService zkAlarmInfoService;

    /**
     * 报警信息保存，用于判断是否需要再次进行报警
     * key：taskId-instanceId（实例级监控）或taskId（集群级监控），用于区分是哪一项发生报警
     * value：发生超过阈值情况的次数
     */
    private static final Map<String, CacheObject<Integer>> ALERT_INFO_MAP = new ConcurrentHashMap<String, CacheObject<Integer>>();

    /**
     * 存储实例状态信息，上一次该实例的角色
     * key：实例id
     * value：zk角色，参考ZKServerStateEnum
     */
    private static final Map<Integer, String> SERVER_STATE_INFO_MAP = new ConcurrentHashMap<Integer, String>();

    /**
     * 默认报警信息缓存大小
     */
    private static final Integer ALERT_CACHE_SIZE = 10000;

    @Override
    public void initMonitor() {
        Set<Class<? extends MonitorBase>> monitorClasses = ReflectUtils.getMonitorClassesSet(BASE_MONITOR_CLASS_PACKAGE);
        if (CollectionUtils.isEmpty(monitorClasses)) {
            LOGGER.info("Init monitor classes empty.");
            return;
        }
        for (Class<?> monitorClass : monitorClasses) {
            try {
                LOGGER.info("Init monitor class:{}.", monitorClass.getName());
                MonitorBase monitorBase = (MonitorBase) monitorClass.newInstance();
                MonitorBase.monitorServiceMap.put(monitorClass.getSimpleName(), monitorBase);
                monitorBase.init(monitorService, clusterService, clusterStateService, instanceService, instanceStateService,
                        machineService, machineStateService);
            } catch (Exception e) {
                LOGGER.warn("Instantiation for monitorClass:{} failed.", monitorClass.getSimpleName(), e);
            }
        }
    }

    @Override
    public boolean insertIndicator(MonitorIndicator monitorIndicator) {
        if (monitorIndicator == null) {
            return false;
        }
        try {
            return monitorIndicatorDao.insertIndicator(monitorIndicator);
        } catch (Exception e) {
            LOGGER.error("Insert new monitorIndicator {} failed.", monitorIndicator, e);
            return false;
        }
    }

    @Override
    public boolean updateIndicator(MonitorIndicator monitorIndicator) {
        if (monitorIndicator == null) {
            return false;
        }
        monitorIndicator.setModifyTime(new Date());
        return monitorIndicatorDao.updateIndicator(monitorIndicator);
    }

    @Override
    public boolean updateIndicatorSwitchOn(int indicatorId, boolean switchOn) {
        boolean updateSwitchOn = monitorIndicatorDao.updateIndicatorSwitchOn(indicatorId, switchOn);
        if (!switchOn) { // 关闭监控指标同时直接关闭所有处于开启状态的监控任务
            List<MonitorTask> monitorTaskList = getTasksByParams(indicatorId, null, true);
            if (CollectionUtils.isNotEmpty(monitorTaskList)) {
                for (MonitorTask monitorTask : monitorTaskList) {
                    updateTaskSwitchOn(monitorTask.getId(), false);
                }
            }
        }
        return updateSwitchOn;
    }

    @Override
    public MonitorIndicator getIndicatorByIndicatorId(int id) {
        return monitorIndicatorDao.getIndicatorByIndicatorId(id);
    }

    @Override
    public MonitorIndicator getIndicatorByClassName(String className) {
        return monitorIndicatorDao.getIndicatorByClassName(className);
    }

    @Override
    public List<MonitorIndicator> getIndicatorsByParams(MonitorIndicatorSearchBO monitorIndicatorSearchBO) {
        return monitorIndicatorDao.getIndicatorsByParams(monitorIndicatorSearchBO);
    }

    @Override
    public boolean deleteIndicatorByIndicatorId(int id) {
        return monitorIndicatorDao.deleteIndicatorByIndicatorId(id);
    }

    @Override
    public boolean insertTask(MonitorTask monitorTask) {
        if (monitorTask == null) {
            return false;
        }
        try {
            return monitorTaskDao.insertTask(monitorTask);
        } catch (Exception e) {
            LOGGER.error("Insert new monitorTask {} failed.", monitorTask, e);
            return false;
        }
    }

    @Override
    public boolean batchSaveMonitorTask(int clusterId) {
        List<MonitorIndicator> monitorIndicatorList = getIndicatorsByParams(null);
        if (CollectionUtils.isEmpty(monitorIndicatorList)) {
            LOGGER.info("Batch save monitor tasks for cluster {}, the size of indicators is empty.", clusterId);
            return true;
        }
        for (MonitorIndicator monitorIndicator : monitorIndicatorList) {
            List<MonitorTask> monitorTaskList = getTasksByParams(monitorIndicator.getId(), clusterId, null);
            if (CollectionUtils.isNotEmpty(monitorTaskList)) {
                LOGGER.info("Batch save monitor tasks for cluster {}, the task exists.", clusterId);
                continue;
            }
            MonitorTask monitorTask = new MonitorTask();
            monitorTask.setIndicatorId(monitorIndicator.getId());
            monitorTask.setClusterId(clusterId);
            monitorTask.setAlertValue(monitorIndicator.getDefaultAlertValue());
            monitorTask.setAlertInterval(monitorIndicator.getDefaultAlertInterval());
            monitorTask.setAlertFrequency(monitorIndicator.getDefaultAlertFrequency());
            monitorTask.setAlertForm(monitorIndicator.getDefaultAlertForm());
            monitorTask.setSwitchOn(monitorIndicator.getSwitchOn());
            boolean insert = insertTask(monitorTask);
            if (!insert) {
                LOGGER.info("Batch save monitor tasks for cluster {}, save indicator {} for cluster task failed.",
                        clusterId, monitorIndicator.getIndicatorName());
            }
        }
        return true;
    }

    @Override
    public boolean updateTask(MonitorTask monitorTask) {
        if (monitorTask == null) {
            return false;
        }
        monitorTask.setModifyTime(new Date());
        return monitorTaskDao.updateTask(monitorTask);
    }

    @Override
    public boolean updateTaskSwitchOn(int taskId, boolean switchOn) {
        MonitorTask monitorTask = getTaskByTaskId(taskId);
        if (monitorTask == null) {
            return false;
        }
        return updateTaskSwitchOn(monitorTask.getIndicatorId(), taskId, switchOn);
    }

    @Override
    public void updateClusterTaskSwitchOn(int clusterId, boolean switchOn) {
        LOGGER.info("Update cluster {} all task switchOn to {}.", clusterId, switchOn);
        List<MonitorTask> monitorTaskList = getTasksByParams(null, clusterId, !switchOn);
        if (CollectionUtils.isEmpty(monitorTaskList)) {
            return;
        }
        for (MonitorTask monitorTask : monitorTaskList) {
            updateTaskSwitchOn(monitorTask.getIndicatorId(), monitorTask.getId(), switchOn);
        }
    }

    @Override
    public boolean updateTaskSwitchOn(int indicatorId, int taskId, boolean switchOn) {
        MonitorIndicator monitorIndicator = getIndicatorByIndicatorId(indicatorId);
        if (monitorIndicator == null) {
            return false;
        }
        MonitorTask monitorTask = getTaskByTaskId(taskId);
        if (monitorTask == null) {
            return false;
        }
        // 集群下线或者未监控状态，不能修改监控开关为开
        ClusterInfo clusterInfo = clusterService.getClusterInfoById(monitorTask.getClusterId());
        if (ClusterEnumClass.ClusterStatusEnum.NOT_MONITORED.getStatus().equals(clusterInfo.getStatus()) ||
                ClusterEnumClass.ClusterStatusEnum.OFFLINE.getStatus().equals(clusterInfo.getStatus())) {
            LOGGER.info("Update task switchOn to {}, but the cluster is offline or not monitored in fact.", switchOn);
            return false;
        }
        // 监控指标开关关闭，则监控任务无法开启
        if (monitorIndicator.getSwitchOn() == null || !monitorIndicator.getSwitchOn()) {
            LOGGER.info("Update task switchOn to {}, but the switch of indicator is off in fact.", switchOn);
            return false;
        } else {
            return monitorTaskDao.updateTaskSwitchOn(taskId, switchOn);
        }
    }

    @Override
    public MonitorTask getTaskByTaskId(int id) {
        return monitorTaskDao.getTaskByTaskId(id);
    }

    @Override
    public List<MonitorTask> getTasksByParams(Integer indicatorId, Integer clusterId, Boolean switchOn) {
        MonitorTaskSearchBO monitorTaskSearchBO = new MonitorTaskSearchBO();
        monitorTaskSearchBO.setIndicatorId(indicatorId);
        monitorTaskSearchBO.setClusterId(clusterId);
        monitorTaskSearchBO.setSwitchOn(switchOn);
        return getTasksByParams(monitorTaskSearchBO);
    }

    @Override
    public List<MonitorTask> getTasksByParams(MonitorTaskSearchBO monitorTaskSearchBO) {
        return monitorTaskDao.getTasksByParams(monitorTaskSearchBO);
    }

    @Override
    public boolean deleteTaskByTaskId(int id) {
        return monitorTaskDao.deleteTaskByTaskId(id);
    }

    @Override
    public boolean deleteTaskByIndicatorId(int indicatorId) {
        return monitorTaskDao.deleteByIndicatorId(indicatorId);
    }

    @Override
    public void monitorClusterState(ClusterState clusterState, Object currentValue, MonitorTask monitorTask, String alertInfo, String alertValueUnit) {
        if (currentValue == null || monitorTask == null) {
            return;
        }
        try {
            long current = Long.parseLong(String.valueOf(currentValue));
            long alertValue = Long.parseLong(monitorTask.getAlertValue());
            if (current >= alertValue) { // 超过报警阀值则根据条件，判断是否进行报警
                boolean alert = monitorAlert(monitorTask, null);
                if (alert) { // 报警
                    Date time = clusterState.getModifyTime() != null ? clusterState.getModifyTime() : clusterState.getCreateTime();
                    alertService.alert(clusterState.getClusterId(), null, String.valueOf(currentValue), monitorTask.getAlertValue(),
                            monitorTask.getAlertForm(), alertInfo, alertValueUnit, DateUtil.formatYYYYMMddHHMMss(time), true);
                    addAlarmInfo(clusterState.getClusterId(), null, String.valueOf(currentValue), monitorTask.getAlertValue(),
                            monitorTask.getAlertForm(), alertInfo, alertValueUnit, DateUtil.formatYYYYMMddHHMMss
                                    (time), null, true);

                }
            }
        } catch (Exception e) {
            LOGGER.error("monitor task error ! clusterId is {}.", monitorTask.getClusterId(), e);
        }
    }

    @Override
    public void monitorInstanceState(InstanceState instanceState, Object currentValue, MonitorTask monitorTask,
                                     String alertInfo, String alertValueUnit, Boolean isValueThresold) {
        if (monitorTask == null) {
            return;
        }
        try {
            int instanceId = instanceState.getInstanceId();
            if (isValueThresold == null) { // 特殊情况，与自己之前的值进行对比，如果有变化则进行报警
                String currentState = currentValue == null ? "null" :
                        ZKServerEnumClass.ZKServerStateEnum.getDescByServerState(Integer.parseInt(String.valueOf(currentValue)));
                if (SERVER_STATE_INFO_MAP.containsKey(instanceId)) {
                    String preState;
                    synchronized (SERVER_STATE_INFO_MAP) {
                        if (SERVER_STATE_INFO_MAP.containsKey(instanceId)) {
                            preState = SERVER_STATE_INFO_MAP.get(instanceId);
                        } else { // 之前没有该实例信息，则保存
                            SERVER_STATE_INFO_MAP.put(instanceId, currentState);
                            return;
                        }
                    }
                    // 表示可能出现异常，保存当前信息，并报警：
                    // 1、当前未检测出的实例角色状态
                    // 2、实例恢复角色状态信息
                    // 3、前后角色状态不一致
                    // 另外，不进行报警的情况：前后状态一致，不更新数据，不进行报警
                    // 1、前后都未检测出实例角色状态，说明之前已经发生过报警
                    // 2、前后角色状态一致
                    if (!currentState.equals(preState)) {
                        synchronized (SERVER_STATE_INFO_MAP) {
                            SERVER_STATE_INFO_MAP.put(instanceId, currentState);
                        }
                        alertService.alert(instanceState.getClusterId(), instanceId, currentState, preState,
                                monitorTask.getAlertForm(), alertInfo, alertValueUnit, DateUtil.formatYYYYMMddHHMMss(new Date()), isValueThresold);
                        addAlarmInfo(instanceState.getClusterId(), instanceId, null, null, monitorTask.getAlertForm(),
                                alertInfo, alertValueUnit, DateUtil.formatYYYYMMddHHMMss(new Date()), null, null);

                    }
                } else { // 之前未保存该实例状态信息，则保存
                    synchronized (SERVER_STATE_INFO_MAP) {
                        SERVER_STATE_INFO_MAP.put(instanceId, currentState);
                    }
                }
            } else if (isValueThresold) { // 超过阈值情况的监控
                if (currentValue != null) {
                    long current = Long.parseLong(String.valueOf(currentValue));
                    long alertValue = Long.parseLong(monitorTask.getAlertValue());
                    if (current >= alertValue) { // 超过报警阀值则根据条件，判断是否进行报警
                        boolean alert = monitorAlert(monitorTask, instanceId);
                        if (alert) { // 报警
                            Date time = instanceState.getModifyTime() != null ? instanceState.getModifyTime() : instanceState.getCreateTime();
                            alertService.alert(instanceState.getClusterId(), instanceId, String.valueOf(currentValue),
                                    monitorTask.getAlertValue(), monitorTask.getAlertForm(), alertInfo, alertValueUnit,
                                    DateUtil.formatYYYYMMddHHMMss(time), isValueThresold);
                            addAlarmInfo(instanceState.getClusterId(), instanceId, String.valueOf(currentValue), monitorTask.getAlertValue(),
                                    monitorTask.getAlertForm(), alertInfo, alertValueUnit, DateUtil
                                            .formatYYYYMMddHHMMss(time), null, true);
                        }
                    }
                }
            } else { // 不为某项指定值情况的监控
                String current = currentValue == null ? null : String.valueOf(currentValue);
                String alertValue = monitorTask.getAlertValue();
                if (alertValue != null && !alertValue.equals(current)) {
                    boolean alert = monitorAlert(monitorTask, instanceId);
                    if (alert) { // 报警
                        Date time = instanceState.getModifyTime() != null ? instanceState.getModifyTime() : instanceState.getCreateTime();
                        alertService.alert(instanceState.getClusterId(), instanceId, current, alertValue,
                                monitorTask.getAlertForm(), alertInfo, alertValueUnit, DateUtil.formatYYYYMMddHHMMss(time), isValueThresold);

                        addAlarmInfo(instanceState.getClusterId(), instanceId, current, alertValue,
                                monitorTask.getAlertForm(), alertInfo, alertValueUnit, DateUtil.formatYYYYMMddHHMMss
                                        (time), null, false);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("monitor instance task error ! clusterId is {}, instance is {}.",
                    monitorTask.getClusterId(), instanceState.getHostInfo(), e);
        }
    }

    @Override
    public void monitorMachineState(MachineState machineState, Object currentValue, MonitorTask monitorTask,
                                    String alertInfo, String alertValueUnit) {
        if (currentValue == null || monitorTask == null) {
            return;
        }
        try {
            double current = Double.parseDouble(String.valueOf(currentValue));
            double alertValue = Double.parseDouble(monitorTask.getAlertValue());
            if (current >= alertValue) { // 超过报警阀值则根据条件，判断是否进行报警
                boolean alert = monitorAlert(monitorTask, machineState.getMachineId());
                if (alert) { // 报警
                    Date time = machineState.getModifyTime() != null ? machineState.getModifyTime() : machineState.getCreateTime();
                    alertService.alert(monitorTask.getClusterId(), machineState.getMachineId(), String.valueOf(currentValue), monitorTask.getAlertValue(),
                            monitorTask.getAlertForm(), alertInfo, alertValueUnit, DateUtil.formatYYYYMMddHHMMss(time));
                    addAlarmInfo(monitorTask.getClusterId(), null, String.valueOf(currentValue), monitorTask.getAlertValue(),
                            monitorTask.getAlertForm(), alertInfo, alertValueUnit, DateUtil.formatYYYYMMddHHMMss
                                    (time), machineState.getMachineId(), true);

                }
            }
        } catch (Exception e) {
            LOGGER.error("monitor machine task error ! clusterId is {}, machine is {}.",
                    monitorTask.getClusterId(), machineState.getHost(), e);
        }
    }

    /**
     * 报警处理
     *
     * @param monitorTask 监控任务
     * @param assistId    实例id/机器id（若为集群级监控，该项为null）
     */
    private boolean monitorAlert(MonitorTask monitorTask, Integer assistId) {
        String alertKey = getAlertKey(monitorTask.getId(), assistId);
        // 判断缓存大小，超过设置值，则先清空本任务的过期缓存信息
        if (ALERT_INFO_MAP.size() > ALERT_CACHE_SIZE) {
            synchronized (ALERT_INFO_MAP) {
                if (ALERT_INFO_MAP.size() > ALERT_CACHE_SIZE) {
                    // 需要移除过期的数据，注：特殊情况，如都没有过期，且数量已经达到最大值，那么该步骤无效
                    for (Map.Entry<String, CacheObject<Integer>> entry : ALERT_INFO_MAP.entrySet()) {
                        int taskId = getTaskIdFromAlertKey(entry.getKey());
                        MonitorTask task = monitorService.getTaskByTaskId(taskId);
                        if (task == null) {
                            ALERT_INFO_MAP.remove(entry.getKey());
                            LOGGER.info("ALERT_INFO_MAP size is over {}, now is {}. task is null so remove {}...",
                                    ALERT_CACHE_SIZE, ALERT_INFO_MAP.size(), entry.getKey());
                        } else if (task.getAlertInterval() != null) {
                            CacheObject<Integer> alertInfo = ALERT_INFO_MAP.get(entry.getKey());
                            if (alertInfo != null && alertInfo.expired(task.getAlertInterval() * 60)) {
                                ALERT_INFO_MAP.remove(entry.getKey());
                                LOGGER.info("ALERT_INFO_MAP size is over {}, now is {}. removing {}...",
                                        ALERT_CACHE_SIZE, ALERT_INFO_MAP.size(), entry.getKey());
                            }
                        }
                    }
                }
            }
        }
        // 截止到当前时间，总共发生过几次报警情况，用于与阈值进行比较，判断是否进行报警
        int currentFrequency;
        if (ALERT_INFO_MAP.containsKey(alertKey)) {
            synchronized (ALERT_INFO_MAP) {
                if (ALERT_INFO_MAP.containsKey(alertKey)) {
                    // 之前发生过报警情况，先判断是否超过设定的阈值信息
                    CacheObject<Integer> preAlertInfo = ALERT_INFO_MAP.get(alertKey);
                    if (preAlertInfo.expired(monitorTask.getAlertInterval() * 60)) { // 之前的数据已经过期，则重新加入
                        currentFrequency = 1;
                    } else {
                        currentFrequency = ALERT_INFO_MAP.get(alertKey).getObject() + 1;
                    }
                    ALERT_INFO_MAP.put(alertKey, new CacheObject<Integer>(currentFrequency, System.currentTimeMillis()));
                } else {
                    currentFrequency = 0;
                }
            }
        } else {
            synchronized (ALERT_INFO_MAP) {
                // 之前没有发生过报警情况
                currentFrequency = 1;
                ALERT_INFO_MAP.put(alertKey, new CacheObject<Integer>(currentFrequency, System.currentTimeMillis()));
            }
        }
        // 判断是否超过阈值，超过则报警，同时重置数据
        if (currentFrequency >= monitorTask.getAlertFrequency()) {
            synchronized (ALERT_INFO_MAP) {
                ALERT_INFO_MAP.remove(alertKey);
                return true;
            }
        }
        return false;
    }

    /**
     * 获取唯一识别报警信息的key
     *
     * @param taskId   监控任务id
     * @param assistId 实例id/机器id
     * @return
     */
    private String getAlertKey(int taskId, Integer assistId) {
        if (assistId != null) {
            return taskId + "-" + assistId;
        } else {
            return String.valueOf(taskId);
        }
    }

    /**
     * 根据报警信息key，返回监控任务id
     *
     * @param alertKey 报警信息key
     * @return
     */
    private int getTaskIdFromAlertKey(String alertKey) {
        if (alertKey == null) {
            return 0;
        } else {
            return Integer.parseInt(alertKey.split("-")[0]);
        }
    }

    /**
     * 集群报警信息
     *
     * @param clusterId       集群id
     * @param instanceId      实例id（null表示该报警为集群级报警）
     * @param currentValue    当前值
     * @param alertValue      报警阈值
     * @param alertForm       报警形式
     * @param alertInfo       报警信息
     * @param alertUnit       报警值单位
     * @param time            发生报警时间
     * @param isValueThresold 该项是否为超过阈值情况的监控
     *                        true：进行阈值判断
     *                        false：进行相等判断，如果值不等于给定值，则报警
     *                        null：特殊判断，与自己之前的值进行对比，如果有变化则报警
     */
    private void addAlarmInfo(int clusterId, Integer instanceId, String currentValue, String alertValue, Integer alertForm,
                              String alertInfo, String alertUnit, String time, Integer machineId, Boolean isValueThresold) {
        ZkAlarmInfo zkAlarmInfo = new ZkAlarmInfo();
        String unit = alertUnit == null ? StringUtils.EMPTY : alertUnit;
        String alarmInfoContent;
        if (isValueThresold == null) {
            alarmInfoContent = alertInfo + ":当前值[ " + currentValue + unit + "],原值[" + alertValue + unit + "]";
        } else if (isValueThresold) {
            alarmInfoContent = alertInfo + ":当前值[" + currentValue + unit + "],报警阈值[" + alertValue + unit + "]";
        } else {
            alarmInfoContent = alertInfo + ":当前值[" + currentValue + unit + "],设定值应为[" + alertValue + unit + "]";
        }
        zkAlarmInfo.setClusterId(clusterId);
        zkAlarmInfo.setInstanceId(instanceId);
        zkAlarmInfo.setInfoContent(alarmInfoContent);
        zkAlarmInfo.setAlarmTime(time);
        zkAlarmInfo.setMachineId(machineId);
        zkAlarmInfoService.insertZkAlarmInfo(zkAlarmInfo);
    }

}