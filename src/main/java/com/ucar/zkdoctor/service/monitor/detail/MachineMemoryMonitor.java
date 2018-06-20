package com.ucar.zkdoctor.service.monitor.detail;

import com.ucar.zkdoctor.pojo.po.InstanceInfo;
import com.ucar.zkdoctor.pojo.po.MachineState;
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
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Description: 机器内存使用率监控
 * Created on 2018/2/26 10:35
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class MachineMemoryMonitor extends MonitorBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(MachineMemoryMonitor.class);

    private final String monitorServiceName = this.getClass().getSimpleName();

    @Override
    public void monitor() {
        specifyMonitor(monitorServiceName, new Monitor() {
            @Override
            public void monitor(MonitorTask monitorTask, MonitorIndicator monitorIndicator) {
                try {
                    List<InstanceInfo> instanceInfoList = instanceService.getInstancesByClusterId(monitorTask.getClusterId());
                    if (CollectionUtils.isEmpty(instanceInfoList)) {
                        return;
                    }
                    for (InstanceInfo instanceInfo : instanceInfoList) {
                        MachineState machineState = currMachineStates.get(instanceInfo.getHost());
                        if (machineState != null && machineState.getMemoryUsage() != null) {
                            monitorService.monitorMachineState(machineState, machineState.getMemoryUsage(), monitorTask,
                                    "机器内存使用率过高", monitorIndicator.getAlertValueUnit());
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("Thread monitor machine memory error! clusterId is {}.", monitorTask.getClusterId(), e);
                } finally {
                    LOGGER.debug("Thread monitor machine memory over. clusterId is {}.", monitorTask.getClusterId());
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
        monitorIndicator.setIndicatorName("机器内存使用率监控");
        monitorIndicator.setClassName(this.getClass().getSimpleName());
        monitorIndicator.setDefaultAlertValue("70");
        monitorIndicator.setDefaultAlertInterval(10);
        monitorIndicator.setDefaultAlertFrequency(2);
        monitorIndicator.setDefaultAlertForm(CommonEnumClass.AlertForm.MAIL.getAlert());
        monitorIndicator.setSwitchOn(true);
        monitorIndicator.setAlertValueUnit("%");
        synMonitorInfo(monitorIndicator);
    }
}

