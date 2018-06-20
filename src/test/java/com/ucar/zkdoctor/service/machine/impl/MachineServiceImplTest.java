package com.ucar.zkdoctor.service.machine.impl;

import com.ucar.zkdoctor.BaseTest;
import com.ucar.zkdoctor.pojo.bo.HostAndPort;
import com.ucar.zkdoctor.pojo.bo.MachineSearchBO;
import com.ucar.zkdoctor.pojo.po.MachineInfo;
import com.ucar.zkdoctor.pojo.vo.MachineDetailVO;
import com.ucar.zkdoctor.service.machine.MachineService;
import com.ucar.zkdoctor.service.system.ServiceLineService;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description: 机器服务测试类
 * Created on 2018/1/10 14:38
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class MachineServiceImplTest extends BaseTest {

    @Resource
    private MachineService machineService;

    @Resource
    private ServiceLineService serviceLineService;

    @Test
    public void insertMachine() throws Exception {
        MachineInfo machineInfo = new MachineInfo();
        machineInfo.setHost("127.0.0.1");
        machineInfo.setAvailable(true);
        machineInfo.setCpu(4);
        machineInfo.setHostDomain("test.com");
        machineInfo.setHostName("test.machine");
        machineInfo.setRealHost("127.0.0.1");
        machineInfo.setMonitor(false);
        machineInfo.setVirtual(true);
        machineInfo.setMemory(4);
        machineInfo.setRoom("test");
        machineInfo.setServiceLine(serviceLineService.getDefaultServiceLine().getId());
        machineInfo.setCreateTime(new Date());
        boolean result = machineService.insertMachine(machineInfo);
        System.out.println("result is " + result);
    }

    @Test
    public void getMachineInfoById() throws Exception {
        MachineInfo machineInfo = machineService.getMachineInfoById(1);
        if (machineInfo != null) {
            System.out.println(machineInfo);
        } else {
            System.out.println("Machine info is NULL.");
        }
    }

    @Test
    public void testGetMachineInfoByHost() throws Exception {
        MachineInfo machineInfo = machineService.getMachineInfoByHost("127.0.0.1");
        if (machineInfo != null) {
            System.out.println(machineInfo);
        } else {
            System.out.println("Machine info is NULL.");
        }
    }

    @Test
    public void testGetMachineInfoByHostName() throws Exception {
        MachineInfo machineInfo = machineService.getMachineInfoByHostName("test.machine");
        if (machineInfo != null) {
            System.out.println(machineInfo);
        } else {
            System.out.println("Machine info is NULL.");
        }
    }

    @Test
    public void testGetMachineInfoByDomain() throws Exception {
        MachineInfo machineInfo = machineService.getMachineInfoByDomain("test.com");
        if (machineInfo != null) {
            System.out.println(machineInfo);
        } else {
            System.out.println("Machine info is NULL.");
        }
    }

    @Test
    public void testGetAllAvailableMachineInfos() throws Exception {
        List<MachineInfo> machineInfoList = machineService.getAllAvailableMachineInfos();
        if (CollectionUtils.isNotEmpty(machineInfoList)) {
            for (MachineInfo machineInfo : machineInfoList) {
                System.out.println(machineInfo);
            }
        } else {
            System.out.println("Result is empty.");
        }
    }

    @Test
    public void testGetAllMachineInfos() throws Exception {
        List<MachineInfo> machineInfoList = machineService.getAllMachineInfos();
        if (CollectionUtils.isNotEmpty(machineInfoList)) {
            for (MachineInfo machineInfo : machineInfoList) {
                System.out.println(machineInfo);
            }
        } else {
            System.out.println("Result is empty.");
        }
    }

    @Test
    public void testGetAllMachinesByParams() throws Exception {
        MachineSearchBO machineSearchBO = new MachineSearchBO();
        machineSearchBO.setAvailable(true);
        List<MachineInfo> machineInfoList = machineService.getAllMachinesByParams(machineSearchBO);
        if (CollectionUtils.isNotEmpty(machineInfoList)) {
            for (MachineInfo machineInfo : machineInfoList) {
                System.out.println(machineInfo);
            }
        } else {
            System.out.println("Result is empty.");
        }
    }

    @Test
    public void testUpdateMachineInfo() throws Exception {
        MachineInfo machineInfo = machineService.getMachineInfoById(1);
        if (machineInfo != null) {
            System.out.println(machineInfo);
            machineInfo.setAvailable(false);
            machineInfo.setRoom("T");
            machineService.updateMachineInfo(machineInfo);
            System.out.println(machineService.getMachineInfoById(1));
        } else {
            System.out.println("Machine info is NULL.");
        }
    }

    @Test
    public void testUpdateMachineAvailable() throws Exception {
        MachineInfo machineInfo = machineService.getMachineInfoById(1);
        if (machineInfo != null) {
            System.out.println(machineInfo);
            machineService.updateMachineAvailable(1, true);
            System.out.println(machineService.getMachineInfoById(1));
        } else {
            System.out.println("Machine info is NULL.");
        }
    }

    @Test
    public void testDeleteMachineInfoById() throws Exception {
        boolean result = machineService.deleteMachineInfoById(1);
        System.out.println("result is " + result);
    }

    @Test
    public void testGetMachineInfoByClusterId() throws Exception {
        List<MachineInfo> machineInfoList = machineService.getMachineInfoByClusterId(1);
        if (CollectionUtils.isNotEmpty(machineInfoList)) {
            for (MachineInfo machineInfo : machineInfoList) {
                System.out.println(machineInfo);
            }
        } else {
            System.out.println("Result is empty.");
        }
    }

    @Test
    public void testBatchInsertNotExistsMachine() throws Exception {
        List<HostAndPort> hostAndPortList = new ArrayList<HostAndPort>();
        HostAndPort hostAndPort = new HostAndPort("127.0.0.1", 2181);
        HostAndPort hostAndPort1 = new HostAndPort("127.0.0.2", 2181);
        HostAndPort hostAndPort2 = new HostAndPort("127.0.0.4", 2181);
        hostAndPortList.add(hostAndPort);
        hostAndPortList.add(hostAndPort1);
        hostAndPortList.add(hostAndPort2);
        boolean result = machineService.batchInsertNotExistsMachine(hostAndPortList);
        System.out.println("result is " + result);
    }

    @Test
    public void testGetInstanceDetailVOByParams() throws Exception {
        List<MachineDetailVO> machineDetailVOList = machineService.getInstanceDetailVOByParams(new MachineSearchBO());
        if (CollectionUtils.isNotEmpty(machineDetailVOList)) {
            for (MachineDetailVO machineDetailVO : machineDetailVOList) {
                System.out.println(machineDetailVO);
            }
        } else {
            System.out.println("Result is empty.");
        }
    }

    @Test
    public void testGetAllMonitorMachine() throws Exception {
        List<MachineInfo> machineInfoList = machineService.getAllMonitorMachine();
        if (CollectionUtils.isNotEmpty(machineInfoList)) {
            for (MachineInfo machineInfo : machineInfoList) {
                System.out.println(machineInfo);
            }
        } else {
            System.out.println("Result is empty.");
        }
    }

    @Test
    public void testUpdateMachineMonitor() throws Exception {
        boolean update = machineService.updateMachineMonitor(3, false);
        System.out.println("result is " + update);
    }
}