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
 * Description: 堆积请求数监控
 * Created on 2018/2/7 15:43
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class OutstandingsMonitor extends MonitorBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(OutstandingsMonitor.class);

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
                            monitorService.monitorClusterState(clusterState, clusterState.getOutstandingTotal(), monitorTask,
                                    "总堆积请求数过多", monitorIndicator.getAlertValueUnit());
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("Thread monitor outstandings total error! clusterId is {}.", monitorTask.getClusterId(), e);
                } finally {
                    LOGGER.debug("Thread monitor outstandings total over. clusterId is {}.", monitorTask.getClusterId());
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
        monitorIndicator.setIndicatorName("堆积请求数监控");
        monitorIndicator.setClassName(this.getClass().getSimpleName());
        monitorIndicator.setDefaultAlertValue("50");
        monitorIndicator.setDefaultAlertInterval(30);
        monitorIndicator.setDefaultAlertFrequency(3);
        monitorIndicator.setDefaultAlertForm(CommonEnumClass.AlertForm.MAIL.getAlert());
        monitorIndicator.setSwitchOn(true);
        monitorIndicator.setAlertValueUnit("个");
        synMonitorInfo(monitorIndicator);
    }
}

