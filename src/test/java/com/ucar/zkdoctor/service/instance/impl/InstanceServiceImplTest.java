package com.ucar.zkdoctor.service.instance.impl;

import com.ucar.zkdoctor.BaseTest;
import com.ucar.zkdoctor.pojo.bo.HostAndPort;
import com.ucar.zkdoctor.pojo.bo.InstanceInfoSearchBO;
import com.ucar.zkdoctor.pojo.po.InstanceInfo;
import com.ucar.zkdoctor.service.instance.InstanceService;
import com.ucar.zkdoctor.util.constant.InstanceEnumClass;
import com.ucar.zkdoctor.util.constant.ZKServerEnumClass;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 实例服务测试类
 * Created on 2018/1/9 19:38
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class InstanceServiceImplTest extends BaseTest {

    @Resource
    private InstanceService instanceService;

    @Test
    public void testGetInstanceInfoById() throws Exception {
        InstanceInfo instanceInfo = instanceService.getInstanceInfoById(1);
        if (instanceInfo != null) {
            System.out.println(instanceInfo);
        } else {
            System.out.println("Instance is NULL.");
        }
    }

    @Test
    public void testInstanceByHostAndPort() throws Exception {
        InstanceInfo instanceInfo = instanceService.getInstanceByHostAndPort("127.0.0.1", 2181);
        if (instanceInfo != null) {
            System.out.println(instanceInfo);
        } else {
            System.out.println("Instance is NULL.");
        }
    }

    @Test
    public void testGetInstancesByClusterId() throws Exception {
        List<InstanceInfo> instanceInfoList = instanceService.getInstancesByClusterId(1);
        if (CollectionUtils.isNotEmpty(instanceInfoList)) {
            for (InstanceInfo instanceInfo : instanceInfoList) {
                System.out.println(instanceInfo);
            }
        } else {
            System.out.println("Result is empty.");
        }
    }

    @Test
    public void testGetAllOnLineInstancesByClusterId() throws Exception {
        List<InstanceInfo> instanceInfoList = instanceService.getAllOnLineInstancesByClusterId(1);
        if (CollectionUtils.isNotEmpty(instanceInfoList)) {
            for (InstanceInfo instanceInfo : instanceInfoList) {
                System.out.println(instanceInfo);
            }
        } else {
            System.out.println("Result is empty.");
        }
    }

    @Test
    public void testAllInstances() throws Exception {
        List<InstanceInfo> instanceInfoList = instanceService.getAllInstances();
        if (CollectionUtils.isNotEmpty(instanceInfoList)) {
            for (InstanceInfo instanceInfo : instanceInfoList) {
                System.out.println(instanceInfo);
            }
        } else {
            System.out.println("Result is empty.");
        }
    }

    @Test
    public void testGetByMachineId() throws Exception {
        List<InstanceInfo> instanceInfoList = instanceService.getByMachineId(1);
        if (CollectionUtils.isNotEmpty(instanceInfoList)) {
            for (InstanceInfo instanceInfo : instanceInfoList) {
                System.out.println(instanceInfo);
            }
        } else {
            System.out.println("Result is empty.");
        }
    }

    @Test
    public void testGetAllInstancesByParams() throws Exception {
        List<InstanceInfo> instanceInfoList = instanceService.getAllInstancesByParams(new InstanceInfoSearchBO());
        if (CollectionUtils.isNotEmpty(instanceInfoList)) {
            for (InstanceInfo instanceInfo : instanceInfoList) {
                System.out.println(instanceInfo);
            }
        } else {
            System.out.println("Result is empty.");
        }
    }

    @Test
    public void testInsertInstance() throws Exception {
        InstanceInfo instanceInfo = new InstanceInfo();
        instanceInfo.setClusterId(1);
        instanceInfo.setStatus(InstanceEnumClass.InstanceStatusEnum.RUNNING.getStatus());
        instanceInfo.setHost("127.0.0.1");
        instanceInfo.setPort(2181);
        instanceInfo.setDeployType(ZKServerEnumClass.DeployTypeEnum.STANDALONE.getDeployType());
        instanceInfo.setMachineId(2);
        boolean result = instanceService.insertInstance(instanceInfo);
        System.out.println("result is : " + result);
    }

    @Test
    public void testUdateInstance() throws Exception {
        InstanceInfo instanceInfo = instanceService.getInstanceInfoById(1);
        if (instanceInfo != null) {
            System.out.println(instanceInfo);
            instanceInfo.setDeployType(ZKServerEnumClass.DeployTypeEnum.CLUSTER.getDeployType());
            instanceInfo.setStatus(InstanceEnumClass.InstanceStatusEnum.EXCEPTION.getStatus());
            instanceService.updateInstance(instanceInfo);
            System.out.println(instanceService.getInstanceInfoById(1));
        } else {
            System.out.println("Instance is NULL.");
        }
    }

    @Test
    public void testUpdateInstanceStatus() throws Exception {
        InstanceInfo instanceInfo = instanceService.getInstanceInfoById(1);
        if (instanceInfo != null) {
            System.out.println(instanceInfo);
            instanceInfo.setStatus(InstanceEnumClass.InstanceStatusEnum.RUNNING.getStatus());
            instanceService.updateInstance(instanceInfo);
            System.out.println(instanceService.getInstanceInfoById(1));
        } else {
            System.out.println("Instance is NULL.");
        }
    }

    @Test
    public void testUpdateInstanceServerStateLag() throws Exception {
        boolean result = instanceService.updateInstanceServerStateLag(1, ZKServerEnumClass.ZKServerStateEnum.FOLLOWER.getServerState());
        System.out.println("result is " + result);
    }

    @Test
    public void testDeleteInstanceById() throws Exception {
        boolean result = instanceService.deleteInstanceById(3);
        System.out.println("result is " + result);
    }

    @Test
    public void testDeleteInstancesByClusterId() throws Exception {
        boolean result = instanceService.deleteInstancesByClusterId(1);
        System.out.println("result is " + result);
    }

    @Test
    public void testGetInstanceCountByClusterId() throws Exception {
        int count = instanceService.getInstanceCountByClusterId(1);
        System.out.println("count is " + count);
    }

    @Test
    public void testGetInstanceTotalCount() throws Exception {
        int count = instanceService.getInstanceTotalCount();
        System.out.println("count is " + count);
    }

    @Test
    public void testGetNormalInstancesByClusterId() throws Exception {
        List<InstanceInfo> instanceInfoList = instanceService.getNormalInstancesByClusterId(1);
        if (CollectionUtils.isNotEmpty(instanceInfoList)) {
            for (InstanceInfo instanceInfo : instanceInfoList) {
                System.out.println(instanceInfo);
            }
        } else {
            System.out.println("Result is empty.");
        }
    }

    @Test
    public void testGetMonitoringCount() throws Exception {
        int count = instanceService.getMonitoringCount();
        System.out.println("count is " + count);
    }

    @Test
    public void testExistsInstances() throws Exception {
        List<HostAndPort> hostAndPortList = new ArrayList<HostAndPort>();
        HostAndPort hostAndPort = new HostAndPort("127.0.0.1", 3181);
        HostAndPort hostAndPort1 = new HostAndPort("127.0.0.2", 2181);
        HostAndPort hostAndPort2 = new HostAndPort("127.0.0.4", 3181);
        hostAndPortList.add(hostAndPort);
        hostAndPortList.add(hostAndPort1);
        hostAndPortList.add(hostAndPort2);
        boolean result = instanceService.existsInstances(hostAndPortList);
        System.out.println("result is " + result);
    }

    @Test
    public void testGetLeaderOrStandaloneInstance() throws Exception {
        InstanceInfo instanceInfo = instanceService.getLeaderOrStandaloneInstance(1);
        if (instanceInfo != null) {
            System.out.println(instanceInfo);
        } else {
            System.out.println("Instance is NULL.");
        }
    }

    @Test
    public void testUpdateInstanceConnMonitor() throws Exception {
        boolean update = instanceService.updateInstanceConnMonitor(3, true);
        System.out.println("result is " + update);
    }

    @Test
    public void testGetAllConnMonitorInstance() throws Exception {
        List<InstanceInfo> instanceInfoList = instanceService.getAllConnMonitorInstance();
        if (CollectionUtils.isNotEmpty(instanceInfoList)) {
            for (InstanceInfo instanceInfo : instanceInfoList) {
                System.out.println(instanceInfo);
            }
        } else {
            System.out.println("Result is empty.");
        }
    }
}