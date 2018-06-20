package com.ucar.zkdoctor.service.monitor.detail;

import com.ucar.zkdoctor.pojo.po.ClusterState;
import com.ucar.zkdoctor.pojo.po.MonitorIndicator;
import com.ucar.zkdoctor.pojo.po.MonitorTask;
import com.ucar.zkdoctor.service.cluster.ClusterService;
import com.ucar.zkdoctor.service.cluster.ClusterStateService;
import com.ucar.zkdoctor.service.instance.InstanceService;
import com.ucar.zkdoctor.service.instance.InstanceStateService;
import com.ucar.zkdoctor.service.machine.MachineService;
import com.ucar.zkdoctor.service.machine.MachineStateService;
import com.ucar.zkdoctor.service.monitor.MonitorBase;
import com.ucar.zkdoctor.service.monitor.MonitorService;
import com.ucar.zkdoctor.util.constant.CommonEnumClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description: 最大延时监控
 * Created on 2018/2/7 15:40
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class MaxLatencyMonitor extends MonitorBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(MaxLatencyMonitor.class);

    private final String monitorServiceName = this.getClass().getSimpleName();

    @Override
    public void monitor() {
        specifyMonitor(monitorServiceName, new Monitor() {
            @Override
            public void monitor(MonitorTask monitorTask, MonitorIndicator monitorIndicator) {
                try {
                    if (currClusterStates.containsKey(monitorTask.getClusterId())) {
                        ClusterState clusterState = currClusterStates.get(monitorTask.getClusterId());
                        if (clusterState != null) {
                            monitorService.monitorClusterState(clusterState, clusterState.getMaxLatencyMax(), monitorTask,
                                    "最大延时过大", monitorIndicator.getAlertValueUnit());
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("Thread monitor max latency total error! clusterId is {}.", monitorTask.getClusterId(), e);
                } finally {
                    LOGGER.debug("Thread monitor max latency total over. clusterId is {}.", monitorTask.getClusterId());
                }
            }
        });
    }

    @Override
    public void init(MonitorService monitorService, ClusterService clusterService,
                     ClusterStateService clusterStateService, InstanceService instanceService, InstanceStateService instanceStateService,
                     MachineService machineService, MachineStateService machineStateService) {
        super.init(monitorService, clusterService, clusterStateService, instanceService, instanceStateService,
                machineService, machineStateService);
        MonitorIndicator monitorIndicator = new MonitorIndicator();
        monitorIndicator.setIndicatorName("最大延时监控");
        monitorIndicator.setClassName(this.getClass().getSimpleName());
        monitorIndicator.setDefaultAlertValue("500");
        monitorIndicator.setDefaultAlertInterval(30);
        monitorIndicator.setDefaultAlertFrequency(3);
        monitorIndicator.setDefaultAlertForm(CommonEnumClass.AlertForm.MAIL.getAlert());
        monitorIndicator.setSwitchOn(true);
        monitorIndicator.setAlertValueUnit("毫秒");
        monitorIndicator.setInfo("所有实例最大延时中的最大值作为集群最大延时");
        synMonitorInfo(monitorIndicator);
    }
}

