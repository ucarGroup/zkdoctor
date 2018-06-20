package com.ucar.zkdoctor.service.view.impl;

import com.ucar.zkdoctor.pojo.po.User;
import com.ucar.zkdoctor.pojo.vo.TreeNodeVO;
import com.ucar.zkdoctor.service.view.MenuService;
import com.ucar.zkdoctor.util.constant.UserEnumClass.UserRoleEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 目录
 * Created on 2017/12/26 14:58
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Service
public class MenuServiceImpl implements MenuService {

    /**
     * 默认菜单信息
     */
    private static List<TreeNodeVO> noneTree = noneTreeNodes();

    /**
     * 普通用户菜单信息
     */
    private static List<TreeNodeVO> generalUserTree = generalUserTreeNodes();

    /**
     * 管理员菜单信息
     */
    private static List<TreeNodeVO> adminUserTree = adminUserTreeNodes();

    /**
     * 超级管理员菜单信息
     */
    private static List<TreeNodeVO> superAdminUserTree = superAdminUserTreeNodes();

    @Override
    public List<TreeNodeVO> getTreeList(User user) {
        if (user == null || user.getUserRole() == null) {
            return noneTreeNodes();
        }
        UserRoleEnum userRoleEnum = UserRoleEnum.getUserRoleEnumByUserRole(user.getUserRole());
        if (userRoleEnum == null) {
            return noneTree;
        }
        switch (userRoleEnum) {
            case GENERAL:
                return generalUserTreeNodes();
            case ADMIN:
                return adminUserTreeNodes();
            case SUPERADMIN:
                return superAdminUserTreeNodes();
            default:
                return noneTreeNodes();
        }
    }

    /**
     * 无法识别用户角色，走默认
     *
     * @return
     */
    private static List<TreeNodeVO> noneTreeNodes() {
        if (CollectionUtils.isEmpty(noneTree)) {
            List<TreeNodeVO> tree = new ArrayList<TreeNodeVO>();
            TreeNodeVO dashboard = new TreeNodeVO();
            dashboard.setKey("026001");
            dashboard.setName("Dashboard");
            dashboard.setUrl("/dashboard");
            dashboard.setExtra("laptop");
            dashboard.setOrder(1);

            tree.add(dashboard);

            noneTree = tree;
        }
        return noneTree;
    }

    /**
     * 普通用户目录列表
     *
     * @return
     */
    private static List<TreeNodeVO> generalUserTreeNodes() {
        if (CollectionUtils.isEmpty(generalUserTree)) {
            List<TreeNodeVO> tree = new ArrayList<TreeNodeVO>();
            TreeNodeVO dashboard = new TreeNodeVO();
            dashboard.setKey("026001");
            dashboard.setName("Dashboard");
            dashboard.setUrl("/dashboard");
            dashboard.setExtra("laptop");
            dashboard.setOrder(1);

            // 集群管理
            TreeNodeVO cluster = new TreeNodeVO();
            cluster.setKey("026002");
            cluster.setName("集群管理");
            cluster.setExtra("cloud-download-o");
            cluster.setOrder(2);

            List<TreeNodeVO> clusterChildren = new ArrayList<TreeNodeVO>();
            cluster.setChildren(clusterChildren);
            TreeNodeVO clusterList = new TreeNodeVO();
            clusterList.setKey("026002001");
            clusterList.setName("集群列表");
            clusterList.setUrl("/cluster/list");
            clusterList.setOrder(1);
            clusterChildren.add(clusterList);

            // 监控报警
            TreeNodeVO monitor = new TreeNodeVO();
            monitor.setKey("026003");
            monitor.setName("监控报警");
            monitor.setExtra("notification");
            monitor.setOrder(3);

            List<TreeNodeVO> monitorChildren = new ArrayList<TreeNodeVO>();
            monitor.setChildren(monitorChildren);
            TreeNodeVO monitorList = new TreeNodeVO();
            monitorList.setKey("026003001");
            monitorList.setName("监控报警");
            monitorList.setUrl("/monitor/list");
            monitorList.setOrder(1);
            TreeNodeVO alarmHistory = new TreeNodeVO();
            alarmHistory.setKey("026003002");
            alarmHistory.setName("报警历史");
            alarmHistory.setUrl("/monitor/alarmHistory");
            alarmHistory.setOrder(2);
            monitorChildren.add(monitorList);
            monitorChildren.add(alarmHistory);

            // 机器管理
            TreeNodeVO machine = new TreeNodeVO();
            machine.setKey("026004");
            machine.setName("机器管理");
            machine.setExtra("desktop");
            machine.setOrder(4);

            List<TreeNodeVO> machineChildren = new ArrayList<TreeNodeVO>();
            machine.setChildren(machineChildren);
            TreeNodeVO machineList = new TreeNodeVO();
            machineList.setKey("026004001");
            machineList.setName("机器列表");
            machineList.setUrl("/machine/list");
            machineList.setOrder(1);
            machineChildren.add(machineList);

            tree.add(dashboard);
            tree.add(cluster);
            tree.add(monitor);
            tree.add(machine);
            generalUserTree = tree;
        }
        return generalUserTree;
    }

    /**
     * 管理员目录列表
     *
     * @return
     */
    private static List<TreeNodeVO> adminUserTreeNodes() {
        if (CollectionUtils.isEmpty(adminUserTree)) {
            List<TreeNodeVO> tree = new ArrayList<TreeNodeVO>();
            TreeNodeVO dashboard = new TreeNodeVO();
            dashboard.setKey("026001");
            dashboard.setName("Dashboard");
            dashboard.setUrl("/dashboard");
            dashboard.setExtra("laptop");
            dashboard.setOrder(1);

            // 集群管理
            TreeNodeVO cluster = new TreeNodeVO();
            cluster.setKey("026002");
            cluster.setName("集群管理");
            cluster.setExtra("cloud-download-o");
            cluster.setOrder(2);

            List<TreeNodeVO> clusterChildren = new ArrayList<TreeNodeVO>();
            cluster.setChildren(clusterChildren);
            TreeNodeVO clusterList = new TreeNodeVO();
            clusterList.setKey("026002001");
            clusterList.setName("集群列表");
            clusterList.setUrl("/cluster/list");
            clusterList.setOrder(1);
            TreeNodeVO addCluster = new TreeNodeVO();
            addCluster.setKey("026002002");
            addCluster.setName("添加集群");
            addCluster.setUrl("/cluster/add");
            addCluster.setOrder(2);
            clusterChildren.add(clusterList);
            clusterChildren.add(addCluster);

            // 运维管理
            TreeNodeVO ops = new TreeNodeVO();
            ops.setKey("026003");
            ops.setName("运维管理");
            ops.setExtra("tool");
            ops.setOrder(3);

            List<TreeNodeVO> opsChildren = new ArrayList<TreeNodeVO>();
            ops.setChildren(opsChildren);
            TreeNodeVO clusterOps = new TreeNodeVO();
            clusterOps.setKey("026003001");
            clusterOps.setName("集群运维");
            clusterOps.setUrl("/manage/cluster/opsManage");
            clusterOps.setOrder(1);
            TreeNodeVO serviceDeploy = new TreeNodeVO();
            serviceDeploy.setKey("026003002");
            serviceDeploy.setName("服务部署");
            serviceDeploy.setUrl("/manage/cluster/opsDeploy");
            serviceDeploy.setOrder(2);
            TreeNodeVO systemConfig = new TreeNodeVO();
            systemConfig.setKey("026003003");
            systemConfig.setName("系统配置");
            systemConfig.setUrl("/manage/system/configlist");
            systemConfig.setOrder(3);
            TreeNodeVO serviceLine = new TreeNodeVO();
            serviceLine.setKey("026003004");
            serviceLine.setName("业务线配置");
            serviceLine.setUrl("/manage/system/serviceLineList");
            serviceLine.setOrder(4);
            opsChildren.add(clusterOps);
            opsChildren.add(serviceDeploy);
            opsChildren.add(systemConfig);
            opsChildren.add(serviceLine);

            // 监控报警
            TreeNodeVO monitor = new TreeNodeVO();
            monitor.setKey("026004");
            monitor.setName("监控报警");
            monitor.setExtra("notification");
            monitor.setOrder(4);

            List<TreeNodeVO> monitorChildren = new ArrayList<TreeNodeVO>();
            monitor.setChildren(monitorChildren);
            TreeNodeVO monitorList = new TreeNodeVO();
            monitorList.setKey("026004001");
            monitorList.setName("监控报警");
            monitorList.setUrl("/monitor/list");
            monitorList.setOrder(1);
            TreeNodeVO alarmHistory = new TreeNodeVO();
            alarmHistory.setKey("026004002");
            alarmHistory.setName("报警历史");
            alarmHistory.setUrl("/monitor/alarmHistory");
            alarmHistory.setOrder(2);
            monitorChildren.add(monitorList);
            monitorChildren.add(alarmHistory);

            // 机器管理
            TreeNodeVO machine = new TreeNodeVO();
            machine.setKey("026005");
            machine.setName("机器管理");
            machine.setExtra("desktop");
            machine.setOrder(5);

            List<TreeNodeVO> machineChildren = new ArrayList<TreeNodeVO>();
            machine.setChildren(machineChildren);
            TreeNodeVO machineList = new TreeNodeVO();
            machineList.setKey("026005001");
            machineList.setName("机器列表");
            machineList.setUrl("/machine/list");
            machineList.setOrder(1);
            machineChildren.add(machineList);

            // Quartz管理
            TreeNodeVO quartz = new TreeNodeVO();
            quartz.setKey("026006");
            quartz.setName("Quartz管理");
            quartz.setExtra("schedule");
            quartz.setOrder(6);

            List<TreeNodeVO> quartzChildren = new ArrayList<TreeNodeVO>();
            quartz.setChildren(quartzChildren);
            TreeNodeVO quartzList = new TreeNodeVO();
            quartzList.setKey("026006001");
            quartzList.setName("定时任务列表");
            quartzList.setUrl("/quartz/list");
            quartzList.setOrder(1);
            quartzChildren.add(quartzList);

            // 用户管理
            TreeNodeVO user = new TreeNodeVO();
            user.setKey("026007");
            user.setName("用户管理");
            user.setExtra("contacts");
            user.setOrder(7);

            List<TreeNodeVO> userChildren = new ArrayList<TreeNodeVO>();
            user.setChildren(userChildren);
            TreeNodeVO userList = new TreeNodeVO();
            userList.setKey("026007001");
            userList.setName("用户列表");
            userList.setUrl("/user");
            userList.setOrder(1);
            userChildren.add(userList);

            tree.add(dashboard);
            tree.add(cluster);
            tree.add(ops);
            tree.add(monitor);
            tree.add(machine);
            tree.add(quartz);
            tree.add(user);
            adminUserTree = tree;
        }
        return adminUserTree;
    }

    /**
     * 超级管理员目录列表
     *
     * @return
     */
    private static List<TreeNodeVO> superAdminUserTreeNodes() {
        if (CollectionUtils.isEmpty(superAdminUserTree)) {
            List<TreeNodeVO> tree = new ArrayList<TreeNodeVO>();
            TreeNodeVO dashboard = new TreeNodeVO();
            dashboard.setKey("026001");
            dashboard.setName("Dashboard");
            dashboard.setUrl("/dashboard");
            dashboard.setExtra("laptop");
            dashboard.setOrder(1);

            // 集群管理
            TreeNodeVO cluster = new TreeNodeVO();
            cluster.setKey("026002");
            cluster.setName("集群管理");
            cluster.setExtra("cloud-download-o");
            cluster.setOrder(2);

            List<TreeNodeVO> clusterChildren = new ArrayList<TreeNodeVO>();
            cluster.setChildren(clusterChildren);
            TreeNodeVO clusterList = new TreeNodeVO();
            clusterList.setKey("026002001");
            clusterList.setName("集群列表");
            clusterList.setUrl("/cluster/list");
            clusterList.setOrder(1);
            TreeNodeVO addCluster = new TreeNodeVO();
            addCluster.setKey("026002002");
            addCluster.setName("添加集群");
            addCluster.setUrl("/cluster/add");
            addCluster.setOrder(2);
            clusterChildren.add(clusterList);
            clusterChildren.add(addCluster);

            // 运维管理
            TreeNodeVO ops = new TreeNodeVO();
            ops.setKey("026003");
            ops.setName("运维管理");
            ops.setExtra("tool");
            ops.setOrder(3);

            List<TreeNodeVO> opsChildren = new ArrayList<TreeNodeVO>();
            ops.setChildren(opsChildren);
            TreeNodeVO clusterOps = new TreeNodeVO();
            clusterOps.setKey("026003001");
            clusterOps.setName("集群运维");
            clusterOps.setUrl("/manage/cluster/opsManage");
            clusterOps.setOrder(1);
            TreeNodeVO serviceDeploy = new TreeNodeVO();
            serviceDeploy.setKey("026003002");
            serviceDeploy.setName("服务部署");
            serviceDeploy.setUrl("/manage/cluster/opsDeploy");
            serviceDeploy.setOrder(2);
            TreeNodeVO systemConfig = new TreeNodeVO();
            systemConfig.setKey("026003003");
            systemConfig.setName("系统配置");
            systemConfig.setUrl("/manage/system/configlist");
            systemConfig.setOrder(3);
            TreeNodeVO serviceLine = new TreeNodeVO();
            serviceLine.setKey("026003004");
            serviceLine.setName("业务线配置");
            serviceLine.setUrl("/manage/system/serviceLineList");
            serviceLine.setOrder(4);
            opsChildren.add(clusterOps);
            opsChildren.add(serviceDeploy);
            opsChildren.add(systemConfig);
            opsChildren.add(serviceLine);

            // 监控报警
            TreeNodeVO monitor = new TreeNodeVO();
            monitor.setKey("026004");
            monitor.setName("监控报警");
            monitor.setExtra("notification");
            monitor.setOrder(4);

            List<TreeNodeVO> monitorChildren = new ArrayList<TreeNodeVO>();
            monitor.setChildren(monitorChildren);
            TreeNodeVO monitorList = new TreeNodeVO();
            monitorList.setKey("026004001");
            monitorList.setName("监控报警");
            monitorList.setUrl("/monitor/list");
            monitorList.setOrder(1);
            TreeNodeVO alarmHistory = new TreeNodeVO();
            alarmHistory.setKey("026004002");
            alarmHistory.setName("报警历史");
            alarmHistory.setUrl("/monitor/alarmHistory");
            alarmHistory.setOrder(2);
            monitorChildren.add(monitorList);
            monitorChildren.add(alarmHistory);


            // 机器管理
            TreeNodeVO machine = new TreeNodeVO();
            machine.setKey("026005");
            machine.setName("机器管理");
            machine.setExtra("desktop");
            machine.setOrder(5);

            List<TreeNodeVO> machineChildren = new ArrayList<TreeNodeVO>();
            machine.setChildren(machineChildren);
            TreeNodeVO machineList = new TreeNodeVO();
            machineList.setKey("026005001");
            machineList.setName("机器列表");
            machineList.setUrl("/machine/list");
            machineList.setOrder(1);
            TreeNodeVO ssh = new TreeNodeVO();
            ssh.setKey("026005002");
            ssh.setName("SSH终端");
            ssh.setUrl("/machine/ssh");
            ssh.setOrder(2);
            machineChildren.add(machineList);
            machineChildren.add(ssh);

            // Quartz管理
            TreeNodeVO quartz = new TreeNodeVO();
            quartz.setKey("026006");
            quartz.setName("Quartz管理");
            quartz.setExtra("schedule");
            quartz.setOrder(6);

            List<TreeNodeVO> quartzChildren = new ArrayList<TreeNodeVO>();
            quartz.setChildren(quartzChildren);
            TreeNodeVO quartzList = new TreeNodeVO();
            quartzList.setKey("026006001");
            quartzList.setName("定时任务列表");
            quartzList.setUrl("/quartz/list");
            quartzList.setOrder(1);
            quartzChildren.add(quartzList);

            // 用户管理
            TreeNodeVO user = new TreeNodeVO();
            user.setKey("026007");
            user.setName("用户管理");
            user.setExtra("contacts");
            user.setOrder(7);

            List<TreeNodeVO> userChildren = new ArrayList<TreeNodeVO>();
            user.setChildren(userChildren);
            TreeNodeVO userList = new TreeNodeVO();
            userList.setKey("026007001");
            userList.setName("用户列表");
            userList.setUrl("/user");
            userList.setOrder(1);
            userChildren.add(userList);

            tree.add(dashboard);
            tree.add(cluster);
            tree.add(ops);
            tree.add(monitor);
            tree.add(machine);
            tree.add(quartz);
            tree.add(user);
            superAdminUserTree = tree;
        }
        return superAdminUserTree;
    }
}
