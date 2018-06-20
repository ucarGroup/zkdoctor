package com.ucar.zkdoctor.service.machine.impl;

import com.ucar.zkdoctor.BaseTest;
import com.ucar.zkdoctor.pojo.po.MachineState;
import com.ucar.zkdoctor.pojo.po.MachineStateLog;
import com.ucar.zkdoctor.service.machine.MachineStateService;
import com.ucar.zkdoctor.util.tool.DateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description: 机器状态服务测试类
 * Created on 2018/1/11 15:27
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class MachineStateServiceImplTest extends BaseTest {

    @Resource
    private MachineStateService machineStateService;

    @Test
    public void testMergeMachineState() throws Exception {
        MachineState machineState = new MachineState();
        machineState.setMachineId(1);
        machineState.setAvgLoad("20");
        machineState.setCpuUsage("0.22");
        machineState.setDiskUsage("2.33");
        machineState.setHost("127.0.0.2");
        machineState.setMemoryFree("124");
        machineState.setMemoryTotal("123234");
        machineState.setMemoryUsage("23.32");
        machineState.setNetTraffic("324.4");
        machineState.setCreateTime(new Date());
        machineState.setModifyTime(new Date());
        boolean result = machineStateService.mergeMachineState(machineState);
        System.out.println("result is " + result);
    }

    @Test
    public void testGetMachineStateByMachineId() throws Exception {
        MachineState machineState = machineStateService.getMachineStateByMachineId(1);
        if (machineState != null) {
            System.out.println(machineState);
        } else {
            System.out.println("Machine State is NULL.");
        }
    }

    @Test
    public void testDeleteMachineStateByMachineId() throws Exception {
        boolean result = machineStateService.deleteMachineStateByMachineId(4);
        System.out.println("result is " + result);
    }

    @Test
    public void testBatchInsertMachineStateLogs() throws Exception {
        List<MachineStateLog> machineStateLogList = new ArrayList<MachineStateLog>();
        for (int i = 0; i < 10; i++) {
            MachineStateLog machineStateLog = new MachineStateLog();
            machineStateLog.setMachineId(i);
            machineStateLog.setAvgLoad("1");
            machineStateLog.setBuffers("124");
            machineStateLog.setCached("234");
            machineStateLog.setCpuUserPercent("123");
            machineStateLog.setCpuNicePercent("12.123");
            machineStateLog.setCpuSingleUsage("12 232 343 4");
            machineStateLog.setCpuSysPercent("12.34");
            machineStateLog.setCpuUsage("12.34");
            machineStateLog.setDiskFreePercent("233");
            machineStateLog.setIdlePercent("334");
            machineStateLog.setIoWaitPercent("34");
            machineStateLog.setDiskSituation("test");
            machineStateLog.setNetFlowIn("124");
            machineStateLog.setNetFlowOut("35");
            machineStateLog.setNetTraffic("12443");
            machineStateLog.setMemoryFree("12");
            machineStateLog.setMemoryTotal("124");
            machineStateLog.setMemoryUsage("124");
            machineStateLog.setDiskUsage("124");
            machineStateLog.setCreateTime(new Date());
            machineStateLogList.add(machineStateLog);
        }
        boolean result = machineStateService.batchInsertMachineStateLogs(machineStateLogList);
        System.out.println("result is " + result);
    }

    @Test
    public void testInsertMachineStateLogs() throws Exception {
        MachineStateLog machineStateLog = new MachineStateLog();
        machineStateLog.setMachineId(1);
        machineStateLog.setAvgLoad("1");
        machineStateLog.setBuffers("124");
        machineStateLog.setCached("234");
        machineStateLog.setCpuUserPercent("123");
        machineStateLog.setCpuNicePercent("12.123");
        machineStateLog.setCpuSingleUsage("12 232 343 4");
        machineStateLog.setCpuSysPercent("12.34");
        machineStateLog.setCpuUsage("12.34");
        machineStateLog.setDiskFreePercent("233");
        machineStateLog.setIdlePercent("334");
        machineStateLog.setIoWaitPercent("34");
        machineStateLog.setDiskSituation("test");
        machineStateLog.setNetFlowIn("124");
        machineStateLog.setNetFlowOut("35");
        machineStateLog.setNetTraffic("12443");
        machineStateLog.setMemoryFree("12");
        machineStateLog.setMemoryTotal("124");
        machineStateLog.setMemoryUsage("124");
        machineStateLog.setDiskUsage("124");
        machineStateLog.setCreateTime(new Date());
        boolean result = machineStateService.insertMachineStateLogs(machineStateLog);
        System.out.println("result is " + result);
    }

    @Test
    public void testCleanMachineStateLogData() throws Exception {
        boolean result = machineStateService.cleanMachineStateLogData(new Date(System.currentTimeMillis() - 60 * 60 * 1000));
        System.out.println("result is " + result);
    }

    @Test
    public void testCleanMachineStateLogCount() throws Exception {
        Long count = machineStateService.cleanMachineStateLogCount(new Date(System.currentTimeMillis() - 60 * 60 * 1000));
        System.out.println("count is " + count);
    }

    @Test
    public void testGetMachineStateLogByMachine() throws Exception {
        List<MachineStateLog> machineStateLogs = machineStateService.getMachineStateLogByMachine(1,
                new Date(System.currentTimeMillis() - 60 * 60 * 1000), new Date());
        if (CollectionUtils.isNotEmpty(machineStateLogs)) {
            for (MachineStateLog machineStateLog : machineStateLogs) {
                System.out.println(machineStateLog);
            }
        } else {
            System.out.println("Result is empty.");
        }
    }

    @Test
    public void testGetMachineStateLogByTime() throws Exception {
        MachineStateLog machineStateLog = machineStateService.getMachineStateLogByTime("127.0.0.1",
                DateUtil.parseYYYYMMddHHmmss("2018-04-11 17:31:29"));
        System.out.println(machineStateLog);
    }
}