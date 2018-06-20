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
 * Description: 实例平均延时监控
 * Created on 2018/2/7 14:49
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class InstanceAvgLatencyMonitor extends MonitorBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(InstanceAvgLatencyMonitor.class);

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
                        if (instanceState.getAvgLatency() != null && monitorTimeIsLegal) {
                            monitorService.monitorInstanceState(instanceState, instanceState.getAvgLatency(), monitorTask,
                                    "平均延时过长", monitorIndicator.getAlertValueUnit(), true);
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("Thread monitor avgLatency error! clusterId is {}.", monitorTask.getClusterId(), e);
                } finally {
                    LOGGER.debug("Thread monitor avgLatency over. clusterId is {}.", monitorTask.getClusterId());
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
        monitorIndicator.setIndicatorName("实例平均延时监控");
        monitorIndicator.setClassName(this.getClass().getSimpleName());
        monitorIndicator.setDefaultAlertValue("10");
        monitorIndicator.setDefaultAlertInterval(10);
        monitorIndicator.setDefaultAlertFrequency(2);
        monitorIndicator.setDefaultAlertForm(CommonEnumClass.AlertForm.MAIL.getAlert());
        monitorIndicator.setSwitchOn(true);
        monitorIndicator.setInfo("平均延时单位：ms");
        monitorIndicator.setAlertValueUnit("毫秒");
        synMonitorInfo(monitorIndicator);
    }
}
