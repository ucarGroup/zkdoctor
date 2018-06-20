package com.ucar.zkdoctor.service.monitor;

import com.ucar.zkdoctor.pojo.bo.MonitorIndicatorSearchBO;
import com.ucar.zkdoctor.pojo.bo.MonitorTaskSearchBO;
import com.ucar.zkdoctor.pojo.po.ClusterState;
import com.ucar.zkdoctor.pojo.po.InstanceState;
import com.ucar.zkdoctor.pojo.po.MachineState;
import com.ucar.zkdoctor.pojo.po.MonitorIndicator;
import com.ucar.zkdoctor.pojo.po.MonitorTask;

import java.util.List;

/**
 * Description: 监控服务接口
 * Created on 2018/2/5 16:13
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public interface MonitorService {
    /**
     * 初始化所有监控信息
     */
    void initMonitor();

    /**
     * 保存监控指标
     *
     * @param monitorIndicator 监控指标
     * @return
     */
    boolean insertIndicator(MonitorIndicator monitorIndicator);

    /**
     * 更新监控指标
     *
     * @param monitorIndicator 监控指标
     * @return
     */
    boolean updateIndicator(MonitorIndicator monitorIndicator);

    /**
     * 修改监控指标开关
     *
     * @param indicatorId 监控指标id
     * @param switchOn    开关状态
     * @return
     */
    boolean updateIndicatorSwitchOn(int indicatorId, boolean switchOn);

    /**
     * 根据监控指标id查询监控指标信息
     *
     * @param id 监控指标id
     * @return
     */
    MonitorIndicator getIndicatorByIndicatorId(int id);

    /**
     * 根据监控指标类名查询监控指标信息
     *
     * @param className 监控类名
     * @return
     */
    MonitorIndicator getIndicatorByClassName(String className);

    /**
     * 根据查询条件查询监控指标列表
     *
     * @param monitorIndicatorSearchBO 监控指标查询条件
     * @return
     */
    List<MonitorIndicator> getIndicatorsByParams(MonitorIndicatorSearchBO monitorIndicatorSearchBO);

    /**
     * 删除某一监控指标
     *
     * @param id 监控指标id
     * @return
     */
    boolean deleteIndicatorByIndicatorId(int id);

    /**
     * 创建监控任务
     *
     * @param monitorTask 监控任务
     * @return
     */
    boolean insertTask(MonitorTask monitorTask);

    /**
     * 批量添加监控任务信息
     *
     * @param clusterId 需要添加监控任务的集群id
     */
    boolean batchSaveMonitorTask(int clusterId);

    /**
     * 更新某一监控任务
     *
     * @param monitorTask 监控任务
     * @return
     */
    boolean updateTask(MonitorTask monitorTask);

    /**
     * 修改监控任务开关
     *
     * @param taskId   监控任务id
     * @param switchOn 开关状态
     * @return
     */
    boolean updateTaskSwitchOn(int taskId, boolean switchOn);

    /**
     * 更新该集群的所有监控任务
     *
     * @param clusterId 集群id
     */
    void updateClusterTaskSwitchOn(int clusterId, boolean switchOn);

    /**
     * 修改监控任务开关
     *
     * @param indicatorId 监控指标id
     * @param taskId      监控任务id
     * @param switchOn    开关状态
     * @return
     */
    boolean updateTaskSwitchOn(int indicatorId, int taskId, boolean switchOn);

    /**
     * 获取某一监控任务
     *
     * @param id 监控任务id
     * @return
     */
    MonitorTask getTaskByTaskId(int id);

    /**
     * 根据查询条件，获取相关的监控任务列表
     *
     * @param indicatorId 监控指标id
     * @param clusterId   集群id
     * @param switchOn    监控任务开关状态
     * @return
     */
    List<MonitorTask> getTasksByParams(Integer indicatorId, Integer clusterId, Boolean switchOn);

    /**
     * 根据查询条件，获取相关的监控任务列表
     *
     * @param monitorTaskSearchBO 监控任务查询条件
     * @return
     */
    List<MonitorTask> getTasksByParams(MonitorTaskSearchBO monitorTaskSearchBO);

    /**
     * 删除某一监控任务
     *
     * @param id 监控任务id
     * @return
     */
    boolean deleteTaskByTaskId(int id);

    /**
     * 删除某一监控指标下的所有监控任务
     *
     * @param indicatorId 监控指标id
     * @return
     */
    boolean deleteTaskByIndicatorId(int indicatorId);

    /**
     * 监控集群状态信息
     *
     * @param clusterState   集群状态信息
     * @param currentValue   当前监控指标值
     * @param monitorTask    监控任务信息
     * @param alertInfo      报警信息
     * @param alertValueUnit 报警值单位
     */
    void monitorClusterState(ClusterState clusterState, Object currentValue, MonitorTask monitorTask, String alertInfo, String alertValueUnit);

    /**
     * 监控实例状态信息
     *
     * @param instanceState   实例状态信息
     * @param currentValue    当前监控指标值
     * @param monitorTask     监控任务信息
     * @param alertInfo       报警信息
     * @param alertValueUnit  报警值单位
     * @param isValueThresold 该项是否为超过阈值情况的监控
     *                        true：进行阈值判断
     *                        false：进行相等判断，如果值不等于给定值，则报警
     *                        Boolean：特殊判断，与自己之前的值进行对比，如果有变化则报警
     */
    void monitorInstanceState(InstanceState instanceState, Object currentValue, MonitorTask monitorTask, String alertInfo,
                              String alertValueUnit, Boolean isValueThresold);

    /**
     * 监控机器状态信息
     *
     * @param machineState   机器状态信息
     * @param currentValue   当前监控指标值
     * @param monitorTask    监控任务信息
     * @param alertInfo      报警信息
     * @param alertValueUnit 报警值单位
     */
    void monitorMachineState(MachineState machineState, Object currentValue, MonitorTask monitorTask, String alertInfo,
                             String alertValueUnit);

}

