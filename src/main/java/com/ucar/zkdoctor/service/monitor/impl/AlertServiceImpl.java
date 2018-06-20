package com.ucar.zkdoctor.service.monitor.impl;

import com.ucar.zkdoctor.pojo.bo.HostAndPort;
import com.ucar.zkdoctor.pojo.bo.UserSearchBO;
import com.ucar.zkdoctor.pojo.po.ClusterAlarmUser;
import com.ucar.zkdoctor.pojo.po.ClusterInfo;
import com.ucar.zkdoctor.pojo.po.InstanceInfo;
import com.ucar.zkdoctor.pojo.po.MachineInfo;
import com.ucar.zkdoctor.pojo.po.User;
import com.ucar.zkdoctor.service.cluster.ClusterService;
import com.ucar.zkdoctor.service.instance.InstanceService;
import com.ucar.zkdoctor.service.machine.MachineService;
import com.ucar.zkdoctor.service.monitor.AlertService;
import com.ucar.zkdoctor.service.user.UserService;
import com.ucar.zkdoctor.util.config.ConfigUtil;
import com.ucar.zkdoctor.util.constant.UserEnumClass;
import com.ucar.zkdoctor.util.tool.MessageUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Description: 报警接口实现类
 * Created on 2018/2/5 16:15
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Service
public class AlertServiceImpl implements AlertService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlertServiceImpl.class);

    @Resource
    private ClusterService clusterService;

    @Resource
    private InstanceService instanceService;

    @Resource
    private MachineService machineService;

    @Resource
    private UserService userService;

    /**
     * 报警标题信息
     */
    private static final String BASE_ALERT_TITLE = "[zkdoctor]%s%s报警[集群:%s]";

    /**
     * 集群基本页面的链接
     */
    private static final String BASE_URL = ConfigUtil.getEnvURL() + "/#/cluster/detail/%s";

    /**
     * 集群超过阈值的邮件报警内容
     */
    private static final String CLUSTER_THRESHOLD_MAIL_ALERT =
            "[报警]：%s<br />\n" +
                    "[当前值]：%s%s<br />\n" +
                    "[报警阈值]：%s%s<br />\n" +
                    "[时间]：%s<br />\n" +
                    "[环境]：%s<br />\n" +
                    "[集群]：<a href=%s>%s，id：%s</a><br />\n" +
                    "请及时关注！";

    /**
     * 实例超过阈值的邮件报警内容
     */
    private static final String INSTANCE_THRESHOLD_MAIL_ALERT =
            "[报警]：%s<br />\n" +
                    "[host]：%s<br />\n" +
                    "[当前值]：%s%s<br />\n" +
                    "[报警阈值]：%s%s<br />\n" +
                    "[时间]：%s<br />\n" +
                    "[环境]：%s<br />\n" +
                    "[集群]：<a href=%s>%s，id：%s</a><br />\n" +
                    "请及时关注！";

    /**
     * 实例状态变化的邮件报警内容
     */
    private static final String STATUS_MAIL_ALERT =
            "[报警]：%s<br />\n" +
                    "[host]：%s<br />\n" +
                    "[当前值]：%s%s<br />\n" +
                    "[原值]：%s%s<br />\n" +
                    "[时间]：%s<br />\n" +
                    "[环境]：%s<br />\n" +
                    "[集群]：<a href=%s>%s，id：%s</a><br />\n" +
                    "请及时关注！";

    /**
     * 集群超过阈值的短信报警内容
     */
    private static final String CLUSTER_THRESHOLD_MOBILE_ALERT =
            "%s - [当前值]:%s%s - [报警阈值]:%s%s - [集群]:%s,id:%s - [时间]:%s - [集群链接]:%s";

    /**
     * 实例超过阈值的短信报警内容
     */
    private static final String INSTANCE_THRESHOLD_MOBILE_ALERT =
            "%s - [host]:%s - [当前值]:%s%s - [报警阈值]:%s%s - [集群]:%s,id:%s - [时间]:%s - [集群链接]:%s";

    /**
     * 实例状态变化的短信报警内容
     */
    private static final String STATUS_MOBILE_ALERT =
            "%s - [实例]:%s - [当前值]:%s%s - [原值]:%s%s - [集群]:%s,id:%s - [时间]:%s - [集群链接]:%s";

    @Override
    public void alert(int clusterId, Integer instanceId, String currentValue, String alertValue,
                      int alertForm, String alertInfo, String alertUnit, String time, Boolean isValueThresold) {

        ClusterInfo clusterInfo = clusterService.getClusterInfoById(clusterId);
        if (clusterInfo == null) {
            LOGGER.warn("Alert:{} for cluster: {} interrupted because cluster info is null.",
                    alertInfo, clusterId);
            return;
        }
        String hostInfo = "";
        if (instanceId != null) {
            InstanceInfo instanceInfo = instanceService.getInstanceInfoById(instanceId);
            if (instanceInfo != null) {
                hostInfo = instanceInfo.getHost() + HostAndPort.HOST_PORT_SEPARATOR + instanceInfo.getPort();
            }
        }

        alertUnit = alertUnit == null ? "" : alertUnit;
        String title = String.format(BASE_ALERT_TITLE, ConfigUtil.getEnvDesc(), alertInfo, clusterInfo.getClusterName());
        String emailContent;
        String mobileContent;
        String url = String.format(BASE_URL, clusterId);
        if (isValueThresold == null) { // 与自己之前值比，有变化，包括角色变化报警
            emailContent = String.format(STATUS_MAIL_ALERT, alertInfo, hostInfo, currentValue, alertUnit, alertValue, alertUnit,
                    time, ConfigUtil.getEnvDesc(), url, clusterInfo.getClusterName(), clusterId);
            mobileContent = String.format(STATUS_MOBILE_ALERT, title, hostInfo, currentValue, alertUnit, alertValue, alertUnit,
                    clusterInfo.getClusterName(), clusterId, time, url);

        } else { // 超过阈值或不为某项值情况报警，包括集群指标检测报警、实例指标检测报警
            if (instanceId == null) { // 集群级阈值或不为某项值报警
                emailContent = String.format(CLUSTER_THRESHOLD_MAIL_ALERT, alertInfo, currentValue, alertUnit, alertValue, alertUnit,
                        time, ConfigUtil.getEnvDesc(), url, clusterInfo.getClusterName(), clusterId);
                mobileContent = String.format(CLUSTER_THRESHOLD_MOBILE_ALERT, title, currentValue, alertUnit, alertValue, alertUnit,
                        clusterInfo.getClusterName(), clusterId, time, url);
            } else { // 实例级阈值或不为某项值报警
                mobileContent = String.format(INSTANCE_THRESHOLD_MOBILE_ALERT, title, hostInfo, currentValue, alertUnit,
                        alertValue, alertUnit, clusterInfo.getClusterName(), clusterId, time, url);
                emailContent = String.format(INSTANCE_THRESHOLD_MAIL_ALERT, alertInfo, hostInfo, currentValue, alertUnit, alertValue,
                        alertUnit, time, ConfigUtil.getEnvDesc(), url, clusterInfo.getClusterName(), clusterId);
            }
        }
        alert(clusterId, alertForm, title, emailContent, mobileContent);
    }

    @Override
    public void alert(int clusterId, Integer machineId, String currentValue, String alertValue, int alertForm, String
            alertInfo, String alertUnit, String time) {

        ClusterInfo clusterInfo = clusterService.getClusterInfoById(clusterId);
        if (clusterInfo == null) {
            LOGGER.warn("Alert:{} for cluster: {} interrupted because cluster info is null.",
                    alertInfo, clusterId);
            return;
        }
        String title = String.format(BASE_ALERT_TITLE, ConfigUtil.getEnvDesc(), alertInfo, clusterInfo.getClusterName());
        MachineInfo machineInfo = machineService.getMachineInfoById(machineId);
        if (machineInfo == null) {
            LOGGER.warn("Alert:{} for cluster: {} interrupted because machine:{} info is null.",
                    alertInfo, clusterId, machineId);
            return;
        }
        String url = String.format(BASE_URL, clusterId);
        alertUnit = alertUnit == null ? "" : alertUnit;
        String mobileContent = String.format(INSTANCE_THRESHOLD_MOBILE_ALERT, title, machineInfo.getHost(),
                currentValue, alertUnit, alertValue, alertUnit, clusterInfo.getClusterName(), clusterId, time, url);
        String emailContent = String.format(INSTANCE_THRESHOLD_MAIL_ALERT, alertInfo, machineInfo.getHost(), currentValue,
                alertUnit, alertValue, alertUnit, time, ConfigUtil.getEnvDesc(), url, clusterInfo.getClusterName(), clusterId);
        alert(clusterId, alertForm, title, emailContent, mobileContent);
    }

    /**
     * 报警
     *
     * @param clusterId     集群id
     * @param alertForm     报警形式
     * @param title         标题
     * @param emailContent  报警邮件内容
     * @param mobileContent 报警短信内容
     */
    private void alert(int clusterId, int alertForm, String title, String emailContent, String mobileContent) {
        // 报警
        switch (alertForm) {
            // 邮件报警
            case 0:
            case 2:
                LOGGER.info("Alert {} by email, content is {}.", title, emailContent);
                List<String> alertEmailList = getEmailOrMobileList(clusterId, true);
                MessageUtil.sendMail(title, emailContent, alertEmailList);
                if (alertForm != 2) {
                    break;
                }
                // 短信报警
            case 1:
                LOGGER.info("Alert {} by mobile, content is {}.", title, mobileContent);
                List<String> alertMobileList = getEmailOrMobileList(clusterId, false);
                MessageUtil.sendSMS(mobileContent, alertMobileList);
                break;
            default:
                break;
        }
    }

    /**
     * 获取需要进行报警的用户邮箱或者手机号列表
     *
     * @param clusterId 集群id
     * @param isEmail   是否获取邮箱，true：获取邮箱，false：获取手机号
     * @return
     */
    private List<String> getEmailOrMobileList(int clusterId, boolean isEmail) {
        // 配置的报警用户信息
        List<ClusterAlarmUser> clusterAlarmUserList = clusterService.getUserIdsByClusterId(clusterId);
        List<User> userList = new ArrayList<User>();
        if (CollectionUtils.isNotEmpty(clusterAlarmUserList)) {
            for (ClusterAlarmUser clusterAlarmUser : clusterAlarmUserList) {
                User user = userService.getUserById(clusterAlarmUser.getUserId());
                if (user != null) {
                    userList.add(user);
                }
            }
        }
        // 超级管理员，默认接收所有报警信息
        UserSearchBO userSearchBO = new UserSearchBO();
        userSearchBO.setUserRole(UserEnumClass.UserRoleEnum.SUPERADMIN.getUserRole());
        List<User> superAdminUserList = userService.getAllUsersByParams(userSearchBO);
        // 最终报警用户列表
        List<User> alertUserList = new ArrayList<User>();
        alertUserList.addAll(userList);
        if (CollectionUtils.isNotEmpty(superAdminUserList)) {
            alertUserList.addAll(superAdminUserList);
        }
        if (CollectionUtils.isEmpty(alertUserList)) {
            return Collections.emptyList();
        }
        // 根据标志位，确定返回用户邮箱或手机号
        List<String> alertList = new ArrayList<String>();
        if (isEmail) {
            for (User user : alertUserList) {
                if (StringUtils.isNotBlank(user.getEmail())) {
                    alertList.add(user.getEmail());
                }
            }
        } else {
            for (User user : alertUserList) {
                if (StringUtils.isNotBlank(user.getMobile())) {
                    alertList.add(user.getMobile());
                }
            }
        }
        // 去重
        return new ArrayList<String>(new HashSet<String>(alertList));
    }
}
