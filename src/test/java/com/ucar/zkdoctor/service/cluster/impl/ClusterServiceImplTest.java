package com.ucar.zkdoctor.service.cluster.impl;

import com.ucar.zkdoctor.BaseTest;
import com.ucar.zkdoctor.pojo.bo.ClusterInfoSearchBO;
import com.ucar.zkdoctor.pojo.po.ClusterAlarmUser;
import com.ucar.zkdoctor.pojo.po.ClusterInfo;
import com.ucar.zkdoctor.pojo.po.User;
import com.ucar.zkdoctor.service.cluster.ClusterService;
import com.ucar.zkdoctor.service.system.ServiceLineService;
import com.ucar.zkdoctor.util.constant.ClusterEnumClass;
import com.ucar.zkdoctor.util.constant.ZKServerEnumClass;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description: 集群服务测试类
 * Created on 2018/1/10 9:56
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ClusterServiceImplTest extends BaseTest {

    @Resource
    private ClusterService clusterService;

    @Resource
    private ServiceLineService serviceLineService;

    @Test
    public void testInsertClusterInfo() throws Exception {
        ClusterInfo clusterInfo = new ClusterInfo();
        clusterInfo.setClusterName("test2");
        clusterInfo.setDeployType(ZKServerEnumClass.DeployTypeEnum.CLUSTER.getDeployType());
        clusterInfo.setInstanceNumber(3);
        clusterInfo.setIntro("测试2");
        clusterInfo.setOfficer("吕小玲");
        clusterInfo.setServiceLine(serviceLineService.getDefaultServiceLine().getId());
        clusterInfo.setStatus(ClusterEnumClass.ClusterStatusEnum.NOT_MONITORED.getStatus());
        clusterInfo.setVersion("3.4.10");
        boolean result = clusterService.insertClusterInfo(clusterInfo);
        System.out.println("result is " + result);
    }

    @Test
    public void testGetClusterInfoById() throws Exception {
        ClusterInfo clusterInfo = clusterService.getClusterInfoById(1);
        if (clusterInfo != null) {
            System.out.println(clusterInfo);
        } else {
            System.out.println("Cluster info is NULL.");
        }
    }

    @Test
    public void testGetClusterInfoByClusterName() throws Exception {
        ClusterInfo clusterInfo = clusterService.getClusterInfoByClusterName("test");
        if (clusterInfo != null) {
            System.out.println(clusterInfo);
        } else {
            System.out.println("Cluster info is NULL.");
        }
    }

    @Test
    public void testGetAllClusterInfos() throws Exception {
        List<ClusterInfo> clusterInfoList = clusterService.getAllClusterInfos();
        if (CollectionUtils.isNotEmpty(clusterInfoList)) {
            for (ClusterInfo clusterInfo : clusterInfoList) {
                System.out.println(clusterInfo);
            }
        } else {
            System.out.println("Result is empty.");
        }
    }

    @Test
    public void testGetAllMonitoringClusterInfos() throws Exception {
        List<ClusterInfo> clusterInfoList = clusterService.getAllMonitoringClusterInfos();
        if (CollectionUtils.isNotEmpty(clusterInfoList)) {
            for (ClusterInfo clusterInfo : clusterInfoList) {
                System.out.println(clusterInfo);
            }
        } else {
            System.out.println("Result is empty.");
        }
    }

    @Test
    public void testGetClusterInfosTotalCount() throws Exception {
        int count = clusterService.getClusterInfosTotalCount();
        System.out.println("count is " + count);
    }

    @Test
    public void testGetAllClusterInfosByParams() throws Exception {
        List<ClusterInfo> clusterInfoList = clusterService.getAllClusterInfosByParams(new ClusterInfoSearchBO());
        if (CollectionUtils.isNotEmpty(clusterInfoList)) {
            for (ClusterInfo clusterInfo : clusterInfoList) {
                System.out.println(clusterInfo);
            }
        } else {
            System.out.println("Result is empty.");
        }
    }

    @Test
    public void testUpdateClusterInfo() throws Exception {
        ClusterInfo clusterInfo = clusterService.getClusterInfoById(1);
        if (clusterInfo != null) {
            System.out.println(clusterInfo);
            clusterInfo.setStatus(ClusterEnumClass.ClusterStatusEnum.NOT_MONITORED.getStatus());
            clusterInfo.setVersion("3.4.6");
            clusterInfo.setServiceLine(serviceLineService.getDefaultServiceLine().getId());
            clusterInfo.setIntro("更新测试");
            clusterService.updateClusterInfo(clusterInfo);
            System.out.println(clusterService.getClusterInfoById(1));
        } else {
            System.out.println("ClusterInfo is NULL.");
        }
    }

    @Test
    public void testUpdateClusterStatus() throws Exception {
        ClusterInfo clusterInfo = clusterService.getClusterInfoById(1);
        if (clusterInfo != null) {
            System.out.println(clusterInfo);
            clusterService.updateClusterStatus(1, ClusterEnumClass.ClusterStatusEnum.RUNNING.getStatus());
            System.out.println(clusterService.getClusterInfoById(1));
        } else {
            System.out.println("ClusterInfo is NULL.");
        }
    }

    @Test
    public void testDeleteClusterInfoByClusterId() throws Exception {
        boolean result = clusterService.deleteClusterInfoByClusterId(3);
        System.out.println("result is " + result);
    }

    @Test
    public void testDeleteClusterInfoByClusterName() throws Exception {
        boolean result = clusterService.deleteClusterInfoByClusterName("test");
        System.out.println("result is " + result);
    }

    @Test
    public void testAddClusterAlarmUser() throws Exception {
        ClusterAlarmUser clusterAlarmUser = new ClusterAlarmUser();
        clusterAlarmUser.setClusterId(3);
        clusterAlarmUser.setUserId(1);
        boolean result = clusterService.addClusterAlarmUser(clusterAlarmUser);
        System.out.println("result is " + result);
    }

    @Test
    public void testGetUserIdsByClusterId() throws Exception {
        List<ClusterAlarmUser> clusterAlarmUserList = clusterService.getUserIdsByClusterId(1);
        if (CollectionUtils.isNotEmpty(clusterAlarmUserList)) {
            for (ClusterAlarmUser clusterAlarmUser : clusterAlarmUserList) {
                System.out.println(clusterAlarmUser);
            }
        } else {
            System.out.println("Result is empty.");
        }
    }

    @Test
    public void testGetClusterIdsByUserId() throws Exception {
        List<ClusterAlarmUser> clusterAlarmUserList = clusterService.getClusterIdsByUserId(1);
        if (CollectionUtils.isNotEmpty(clusterAlarmUserList)) {
            for (ClusterAlarmUser clusterAlarmUser : clusterAlarmUserList) {
                System.out.println(clusterAlarmUser);
            }
        } else {
            System.out.println("Result is empty.");
        }
    }

    @Test
    public void testDeleteAlarmUser() throws Exception {
        boolean result = clusterService.deleteAlarmUser(3, 1);
        System.out.println("result is " + result);
    }

    @Test
    public void testClusterAlarmUsers() throws Exception {
        List<User> userList = clusterService.clusterAlarmUsers(1);
        if (CollectionUtils.isNotEmpty(userList)) {
            for (User user : userList) {
                System.out.println(user);
            }
        } else {
            System.out.println("Result is empty.");
        }
    }
}