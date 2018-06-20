package com.ucar.zkdoctor.service.monitor.detail;

import com.ucar.zkdoctor.pojo.po.InstanceState;
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
import com.ucar.zkdoctor.util.tool.DateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * Description: 实例角色状态变更监控
 * Created on 2018/2/7 19:38
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class InstanceStateChangeMonitor extends MonitorBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(InstanceStateChangeMonitor.class);

    private final String monitorServiceName = this.getClass().getSimpleName();

    @Override
    public void monitor() {
        specifyMonitor(monitorServiceName, new Monitor() {
            @Override
            public void monitor(MonitorTask monitorTask, MonitorIndicator monitorIndicator) {
                try {
                    List<InstanceState> instanceStateList = instanceStateService.getInstanceStateByClusterId(monitorTask.getClusterId());
                    if (CollectionUtils.isEmpty(instanceStateList)) {
                        return;
                    }
                    for (InstanceState instanceState : instanceStateList) {
                        // 保证状态信息为最近2分钟的新数据
                        boolean monitorTimeIsLegal = DateUtil.isMonitorTime(new Date(), instanceState.getCreateTime(),
                                instanceState.getModifyTime(), -120000);
                        if (monitorTimeIsLegal) {
                            monitorService.monitorInstanceState(instanceState, instanceState.getServerStateLag(), monitorTask,
                                    "实例角色变更", monitorIndicator.getAlertValueUnit(), null);
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("Thread monitor InstanceOpenFilesDescCountMonitor error! clusterId is {}.", monitorTask.getClusterId(), e);
                } finally {
                    LOGGER.debug("Thread monitor InstanceOpenFilesDescCountMonitor over. clusterId is {}.", monitorTask.getClusterId());
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
        monitorIndicator.setIndicatorName("实例角色变更监控");
        monitorIndicator.setClassName(this.getClass().getSimpleName());
        monitorIndicator.setDefaultAlertInterval(10);
        monitorIndicator.setDefaultAlertFrequency(1);
        monitorIndicator.setDefaultAlertForm(CommonEnumClass.AlertForm.MAIL.getAlert());
        monitorIndicator.setSwitchOn(true);
        synMonitorInfo(monitorIndicator);
    }
}
