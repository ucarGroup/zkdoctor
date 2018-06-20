package com.ucar.zkdoctor.service.instance.impl;

import com.ucar.zkdoctor.BaseTest;
import com.ucar.zkdoctor.pojo.bo.ClientConnectionSearchBO;
import com.ucar.zkdoctor.pojo.bo.InstanceStateLogSearchBO;
import com.ucar.zkdoctor.pojo.po.ClientInfo;
import com.ucar.zkdoctor.pojo.po.InstanceState;
import com.ucar.zkdoctor.pojo.po.InstanceStateLog;
import com.ucar.zkdoctor.service.instance.InstanceStateService;
import com.ucar.zkdoctor.util.constant.ZKServerEnumClass;
import com.ucar.zkdoctor.util.tool.DateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description: 实例状态服务测试类
 * Created on 2018/1/11 15:23
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class InstanceStateServiceImplTest extends BaseTest {

    @Resource
    private InstanceStateService instanceStateService;

    @Test
    public void testMergeInstanceState() throws Exception {
        InstanceState instanceState = new InstanceState();
        instanceState.setClusterId(1);
        instanceState.setInstanceId(1);
        instanceState.setHostInfo("127.0.0.1:2181");
        instanceState.setApproximateDataSize(123L);
        instanceState.setAvgLatency(12L);
        instanceState.setCurrConnections(12);
        instanceState.setLeaderId(1);
        instanceState.setCurrEphemeralsCount(12);
        instanceState.setCurrOutstandings(1L);
        instanceState.setCurrWatchCount(124);
        instanceState.setCurrZnodeCount(2134);
        instanceState.setFollowers(2);
        instanceState.setMinLatency(23L);
        instanceState.setMaxFileDescriptorCount(12443L);
        instanceState.setMaxLatency(2335L);
        instanceState.setAvgLatency(1L);
        instanceState.setSyncedFollowers(1);
        instanceState.setSent(2335L);
        instanceState.setReceived(1244L);
        instanceState.setRunOk(true);
        instanceState.setOpenFileDescriptorCount(124L);
        instanceState.setServerStateLag(ZKServerEnumClass.ZKServerStateEnum.LEADER.getServerState());
        instanceState.setVersion("3.4.10");
        instanceState.setPendingSyncs(1);
        instanceState.setZxid(1223435346L);
        instanceState.setCreateTime(new Date());
        instanceState.setModifyTime(new Date());
        boolean result = instanceStateService.mergeInstanceState(instanceState);
        System.out.println("result is " + result);
    }

    @Test
    public void testGetInstanceStateByInstanceId() throws Exception {
        InstanceState instanceState = instanceStateService.getInstanceStateByInstanceId(1);
        if (instanceState != null) {
            System.out.println(instanceState);
        } else {
            System.out.println("Instance State is NULL.");
        }
    }

    @Test
    public void testGetInstanceStateByClusterId() throws Exception {
        List<InstanceState> instanceStateList = instanceStateService.getInstanceStateByClusterId(1);
        if (CollectionUtils.isNotEmpty(instanceStateList)) {
            for (InstanceState state : instanceStateList) {
                System.out.println(state);
            }
        } else {
            System.out.println("Result is NULL.");
        }
    }

    @Test
    public void testDeleteInstanceStateByInstanceId() throws Exception {
        boolean result = instanceStateService.deleteInstanceStateByInstanceId(1);
        System.out.println("result is " + result);
    }

    @Test
    public void testBatchInsertInstanceStateLogs() throws Exception {
        List<InstanceStateLog> instanceStateLogs = new ArrayList<InstanceStateLog>();
        for (int i = 0; i < 10; i++) {
            InstanceStateLog instanceStateLog = new InstanceStateLog();
            instanceStateLog.setClusterId(i);
            instanceStateLog.setHostInfo("127.0.0.1:2181");
            instanceStateLog.setSent(2134L);
            instanceStateLog.setWatchCount(124);
            instanceStateLog.setServerStateLag(ZKServerEnumClass.ZKServerStateEnum.LEADER.getServerState());
            instanceStateLog.setSyncedFollowers(12);
            instanceStateLog.setApproximateDataSize(14L);
            instanceStateLog.setAvgLatency(2L);
            instanceStateLog.setConnections(3);
            instanceStateLog.setEphemeralsCount(2);
            instanceStateLog.setFollowers(2);
            instanceStateLog.setInstanceId(1);
            instanceStateLog.setLeaderId(1);
            instanceStateLog.setMaxFileDescriptorCount(234L);
            instanceStateLog.setMaxLatency(11245L);
            instanceStateLog.setMinLatency(233L);
            instanceStateLog.setOpenFileDescriptorCount(1233L);
            instanceStateLog.setOutstandings(4435L);
            instanceStateLog.setPendingSyncs(1);
            instanceStateLog.setReceived(23435L);
            instanceStateLog.setRunOk(true);
            instanceStateLog.setVersion("3.4.10");
            instanceStateLog.setZnodeCount(2342);
            instanceStateLog.setZxid(23841358L);
            instanceStateLog.setCreateTime(new Date());
            instanceStateLogs.add(instanceStateLog);
        }
        boolean result = instanceStateService.batchInsertInstanceStateLogs(instanceStateLogs);
        System.out.println("result is " + result);
    }

    @Test
    public void testInsertInstanceStateLogs() throws Exception {
        InstanceStateLog instanceStateLog = new InstanceStateLog();
        instanceStateLog.setClusterId(1);
        instanceStateLog.setHostInfo("127.0.0.1:2181");
        instanceStateLog.setSent(2134L);
        instanceStateLog.setWatchCount(124);
        instanceStateLog.setServerStateLag(ZKServerEnumClass.ZKServerStateEnum.LEADER.getServerState());
        instanceStateLog.setSyncedFollowers(12);
        instanceStateLog.setApproximateDataSize(14L);
        instanceStateLog.setAvgLatency(2L);
        instanceStateLog.setConnections(3);
        instanceStateLog.setEphemeralsCount(2);
        instanceStateLog.setFollowers(2);
        instanceStateLog.setInstanceId(1);
        instanceStateLog.setLeaderId(1);
        instanceStateLog.setMaxFileDescriptorCount(234L);
        instanceStateLog.setMaxLatency(11245L);
        instanceStateLog.setMinLatency(233L);
        instanceStateLog.setOpenFileDescriptorCount(1233L);
        instanceStateLog.setOutstandings(4435L);
        instanceStateLog.setPendingSyncs(1);
        instanceStateLog.setReceived(23435L);
        instanceStateLog.setRunOk(true);
        instanceStateLog.setVersion("3.4.10");
        instanceStateLog.setZnodeCount(2342);
        instanceStateLog.setZxid(23841358L);
        instanceStateLog.setCreateTime(new Date());
        boolean result = instanceStateService.insertInstanceStateLogs(instanceStateLog);
        System.out.println("result is " + result);
    }

    @Test
    public void testGetInstanceStateLogByClusterParams() throws Exception {
        InstanceStateLogSearchBO instanceStateLogSearchBO = new InstanceStateLogSearchBO();
        instanceStateLogSearchBO.setClusterId(1);
        instanceStateLogSearchBO.setOnlyLeader(false);
        instanceStateLogSearchBO.setStartDate(new Date(System.currentTimeMillis() - 3 * 60 * 60 * 1000));
        instanceStateLogSearchBO.setEndDate(new Date());
        List<InstanceStateLog> instanceStateLogs = instanceStateService.getInstanceStateLogByClusterParams(instanceStateLogSearchBO);
        if (CollectionUtils.isNotEmpty(instanceStateLogs)) {
            for (InstanceStateLog instanceStateLog : instanceStateLogs) {
                System.out.println(instanceStateLog);
            }
        } else {
            System.out.println("Result is empty.");
        }
    }

    @Test
    public void testGetInstanceStateLogByInstance() throws Exception {
        List<InstanceStateLog> instanceStateLogs = instanceStateService.getInstanceStateLogByInstance(1,
                new Date(System.currentTimeMillis() - 60 * 60 * 1000), new Date());
        if (CollectionUtils.isNotEmpty(instanceStateLogs)) {
            for (InstanceStateLog instanceStateLog : instanceStateLogs) {
                System.out.println(instanceStateLog);
            }
        } else {
            System.out.println("Result is empty.");
        }
    }

    @Test
    public void testGetInstanceStateLogByCluster() throws Exception {
        List<InstanceStateLog> instanceStateLogs = instanceStateService.getInstanceStateLogByCluster(1,
                new Date(System.currentTimeMillis() - 60 * 60 * 1000), new Date());
        if (CollectionUtils.isNotEmpty(instanceStateLogs)) {
            for (InstanceStateLog instanceStateLog : instanceStateLogs) {
                System.out.println(instanceStateLog);
            }
        } else {
            System.out.println("Result is empty.");
        }
    }

    @Test
    public void testCleanInstanceStateLogCount() throws Exception {
        Long count = instanceStateService.cleanInstanceStateLogCount(new Date(System.currentTimeMillis() - 60 * 60 * 1000));
        System.out.println("count is " + count);
    }

    @Test
    public void testCleanInstanceStateLogData() throws Exception {
        boolean result = instanceStateService.cleanInstanceStateLogData(new Date(System.currentTimeMillis() - 60 * 60 * 1000));
        System.out.println("result is " + result);
    }

    @Test
    public void testBatchInsertClientConnections() throws Exception {
        List<ClientInfo> clientInfoList = new ArrayList<ClientInfo>();
        for (int i = 0; i < 10; i++) {
            ClientInfo clientInfo = new ClientInfo();
            clientInfo.setClusterId(1);
            clientInfo.setInstanceId(1);
            clientInfo.setClientIp("127.0.0.1");
            clientInfo.setClientPort(1234);
            clientInfo.setSid("0x1234");
            clientInfo.setQueued(1L);
            clientInfo.setRecved(2394L);
            clientInfo.setSent(847L);
            clientInfo.setEst(128534764L);
            clientInfo.setToTime(5000);
            clientInfo.setLcxid("0x4857");
            clientInfo.setLzxid("0x475678");
            clientInfo.setLresp(45467786L);
            clientInfo.setLlat(23);
            clientInfo.setMinlat(3);
            clientInfo.setAvglat(4);
            clientInfo.setMaxlat(123);
            clientInfo.setCreateTime(new Date());
            clientInfoList.add(clientInfo);
        }
        boolean result = instanceStateService.batchInsertClientConnections(clientInfoList);
        System.out.println("result is " + result);
    }

    @Test
    public void testQueryClientConnectionsByParams() throws Exception {
        ClientConnectionSearchBO clientConnectionSearchBO = new ClientConnectionSearchBO();
        clientConnectionSearchBO.setClusterId(1);
        clientConnectionSearchBO.setInstanceId(1);
        clientConnectionSearchBO.setStartDate(DateUtil.formatYYYYMMddHHMMss(DateUtil.getIntervalDate(new Date(), -300000)));
        clientConnectionSearchBO.setEndDate(DateUtil.formatYYYYMMddHHMMss(new Date()));
        clientConnectionSearchBO.setOrderBy("recved");
        List<ClientInfo> clientInfoList = instanceStateService.getClientConnectionsByParams(clientConnectionSearchBO);
        if (CollectionUtils.isNotEmpty(clientInfoList)) {
            for (ClientInfo clientInfo : clientInfoList) {
                System.out.println(clientInfo);
            }
        } else {
            System.out.println("Result is empty.");
        }
    }

    @Test
    public void testCleanClientConnectionsCountt() throws Exception {
        Long count = instanceStateService.cleanClientConnectionsCount(new Date(System.currentTimeMillis() - 60 * 60 * 1000));
        System.out.println("count is " + count);
    }

    @Test
    public void testCleanClientConnectionsData() throws Exception {
        boolean result = instanceStateService.cleanClientConnectionsData(new Date(System.currentTimeMillis() - 60 * 60 * 1000));
        System.out.println("result is " + result);
    }

    @Test
    public void testGetLatestClientInfo() throws Exception {
        ClientInfo clientInfo = instanceStateService.getLatestClientInfo(1);
        System.out.println(clientInfo);
    }
}