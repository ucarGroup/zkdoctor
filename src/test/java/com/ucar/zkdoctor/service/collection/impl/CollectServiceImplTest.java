package com.ucar.zkdoctor.service.collection.impl;

import com.ucar.zkdoctor.BaseTest;
import com.ucar.zkdoctor.pojo.dto.ConnectionInfoDTO;
import com.ucar.zkdoctor.pojo.dto.ServerStateInfoDTO;
import com.ucar.zkdoctor.pojo.dto.ZKServerConfigDTO;
import com.ucar.zkdoctor.pojo.po.MachineState;
import com.ucar.zkdoctor.pojo.po.MachineStateLog;
import com.ucar.zkdoctor.service.collection.CollectService;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description: zk信息收集服务测试类
 * Created on 2018/1/23 11:41
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class CollectServiceImplTest extends BaseTest {

    @Resource
    private CollectService collectService;

    @Test
    public void checkServerRunningNormal() throws Exception {
        String result = collectService.checkServerRunningNormal("127.0.0.1", 2181);
        System.out.println(result);
    }

    @Test
    public void testCollectServerStateInfo() throws Exception {
        ServerStateInfoDTO serverStateInfoDTO = collectService.collectServerStateInfo("127.0.0.1", 2181);
        if (serverStateInfoDTO != null) {
            System.out.println(serverStateInfoDTO);
        } else {
            System.out.println("serverStateInfoDTO is NULL.");
        }
    }

    @Test
    public void testResetServerStatistic() throws Exception {
        boolean result = collectService.resetServerStatistic("127.0.0.1", 2181);
        System.out.println("result is " + result);
    }

    @Test
    public void testDeployZKCollection() throws Exception {
        boolean result = collectService.deployZKCollection(1);
        System.out.println("result is " + result);
    }

    @Test
    public void testCollectConfigInfo() throws Exception {
        ZKServerConfigDTO zkServerConfigDTO = collectService.collectConfigInfo("127.0.0.1", 2181);
        if (zkServerConfigDTO != null) {
            System.out.println(zkServerConfigDTO);
        } else {
            System.out.println("zkServerConfigDTO is NULL.");
        }
    }

    @Test
    public void testVollectConnectionInfo() throws Exception {
        List<ConnectionInfoDTO> connectionInfoDTOList = collectService.collectConnectionInfo("127.0.0.1", 2181);
        if (CollectionUtils.isNotEmpty(connectionInfoDTOList)) {
            for (ConnectionInfoDTO connectionInfoDTO : connectionInfoDTOList) {
                System.out.println(connectionInfoDTO);
            }
        } else {
            System.out.println("connectionInfoDTOList is NULL.");
        }
    }

    @Test
    public void testCollectMachineState() throws Exception {
        MachineState machineState = new MachineState();
        MachineStateLog machineStateLog = new MachineStateLog();
        boolean result = collectService.collectMachineState("127.0.0.1", machineState, machineStateLog);
        if (result) {
            System.out.println(machineState);
            System.out.println(machineStateLog);
        } else {
            System.out.println("Result is false.");
        }
    }

    @Test
    public void testResetConnectionInfo() throws Exception {
        boolean result = collectService.resetConnectionInfo("127.0.0.1", 2181);
        System.out.println("result is " + result);
    }
}