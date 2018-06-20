package com.ucar.zkdoctor.service.cluster.impl;

import com.ucar.zkdoctor.BaseTest;
import com.ucar.zkdoctor.pojo.po.ClusterState;
import com.ucar.zkdoctor.service.cluster.ClusterStateService;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description: 集群状态服务测试类
 * Created on 2018/1/11 15:18
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ClusterStateServiceImplTest extends BaseTest {

    @Resource
    private ClusterStateService clusterStateService;

    @Test
    public void testMergeClusterState() throws Exception {
        ClusterState clusterState = new ClusterState();
        clusterState.setClusterId(1);
        clusterState.setInstanceNumber(3);
        clusterState.setApproximateDataSize(1234L);
        clusterState.setAvgLatencyMax(1L);
        clusterState.setConnectionTotal(11);
        clusterState.setEphemerals(35);
        clusterState.setMaxLatencyMax(124L);
        clusterState.setOpenFileDescriptorCountTotal(281L);
        clusterState.setMinLatencyMax(0L);
        clusterState.setOutstandingTotal(10L);
        clusterState.setReceivedTotal(12435L);
        clusterState.setSentTotal(12355L);
        clusterState.setWatcherTotal(12);
        clusterState.setWatcherTotal(129);
        clusterState.setZnodeCount(1234);
        clusterState.setCreateTime(new Date());
        clusterState.setModifyTime(new Date());
        boolean result = clusterStateService.mergeClusterState(clusterState);
        System.out.println("result is " + result);
    }

    @Test
    public void testGetClusterStateByClusterId() throws Exception {
        ClusterState clusterState = clusterStateService.getClusterStateByClusterId(1);
        if (clusterState != null) {
            System.out.println(clusterState);
        } else {
            System.out.println("Cluster state is NULL.");
        }
    }

    @Test
    public void testDeleteClusterStateByClusterId() throws Exception {
        boolean result = clusterStateService.deleteClusterStateByClusterId(3);
        System.out.println("result is " + result);
    }

    @Test
    public void testBatchInsertClusterStateLogs() throws Exception {
        List<ClusterState> clusterStateList = new ArrayList<ClusterState>();
        for (int i = 0; i < 10; i++) {
            ClusterState clusterState = new ClusterState();
            clusterState.setClusterId(1);
            clusterState.setInstanceNumber(3);
            clusterState.setApproximateDataSize(1234L);
            clusterState.setAvgLatencyMax(1L);
            clusterState.setConnectionTotal(11);
            clusterState.setEphemerals(35);
            clusterState.setMaxLatencyMax(124L);
            clusterState.setOpenFileDescriptorCountTotal(281L);
            clusterState.setMinLatencyMax(0L);
            clusterState.setOutstandingTotal(10L);
            clusterState.setReceivedTotal(12435L);
            clusterState.setSentTotal(12355L);
            clusterState.setWatcherTotal(12);
            clusterState.setWatcherTotal(129);
            clusterState.setZnodeCount(1234);
            clusterState.setCreateTime(new Date());
            clusterStateList.add(clusterState);
        }
        boolean result = clusterStateService.batchInsertClusterStateLogs(clusterStateList);
        System.out.println("result is " + result);
    }

    @Test
    public void testInsertClusterStateLogs() throws Exception {
        ClusterState clusterState = new ClusterState();
        clusterState.setClusterId(1);
        clusterState.setInstanceNumber(3);
        clusterState.setApproximateDataSize(1234L);
        clusterState.setAvgLatencyMax(1L);
        clusterState.setConnectionTotal(11);
        clusterState.setEphemerals(35);
        clusterState.setMaxLatencyMax(124L);
        clusterState.setOpenFileDescriptorCountTotal(281L);
        clusterState.setMinLatencyMax(0L);
        clusterState.setOutstandingTotal(10L);
        clusterState.setReceivedTotal(12435L);
        clusterState.setSentTotal(12355L);
        clusterState.setWatcherTotal(12);
        clusterState.setWatcherTotal(129);
        clusterState.setZnodeCount(1234);
        clusterState.setCreateTime(new Date());
        boolean result = clusterStateService.insertClusterStateLogs(clusterState);
        System.out.println("result is " + result);
    }

    @Test
    public void testGetClusterStateLogByClusterId() throws Exception {
        List<ClusterState> clusterStateList = clusterStateService.getClusterStateLogByClusterId(1,
                new Date(System.currentTimeMillis() - 60 * 60 * 1000), new Date());
        if (CollectionUtils.isNotEmpty(clusterStateList)) {
            for (ClusterState clusterState : clusterStateList) {
                System.out.println(clusterState);
            }
        } else {
            System.out.println("Result is empty.");
        }
    }

    @Test
    public void testCleanClusterStateLogData() throws Exception {
        boolean result = clusterStateService.cleanClusterStateLogData(new Date(System.currentTimeMillis() - 60 * 60 * 1000));
        System.out.println("result is " + result);
    }

    @Test
    public void testCleanClusterStateLogCount() throws Exception {
        Long count = clusterStateService.cleanClusterStateLogCount(new Date(System.currentTimeMillis() - 60 * 60 * 1000));
        System.out.println("count is " + count);
    }
}