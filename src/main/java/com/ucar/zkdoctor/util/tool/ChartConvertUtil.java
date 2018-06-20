package com.ucar.zkdoctor.util.tool;

import com.ucar.zkdoctor.pojo.po.ClusterState;
import com.ucar.zkdoctor.pojo.po.InstanceStateLog;
import com.ucar.zkdoctor.pojo.po.MachineStateLog;
import com.ucar.zkdoctor.pojo.vo.AllTrendChartVO;
import com.ucar.zkdoctor.pojo.vo.TrendChartVO;
import com.ucar.zkdoctor.util.config.ConfigUtil;
import com.ucar.zkdoctor.util.constant.SymbolConstant;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Description: 图表数据转化工具类
 * Created on 2018/1/26 11:15
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ChartConvertUtil {

    /**
     * 统计状态指标信息
     */
    public static final String STATE_OUTSTANDINGS = "outstandings";
    public static final String STATE_RECEIVED = "received";
    public static final String STATE_SEND = "send";
    public static final String STATE_MAXLATENCY = "maxLatency";
    public static final String STATE_AVGLATENCY = "avgLatency";
    public static final String STATE_MINLATENCY = "minLatency";
    public static final String STATE_ZNODECOUNT = "znodeCount";
    public static final String STATE_EPHEMERIALS = "ephemerals";
    public static final String STATE_WATCHERCOUNT = "watcherCount";
    public static final String STATE_CONNECTIONS = "connections";

    public static final String STATE_APPROXIMATEDATASIZE = "approximateDataSize";
    public static final String STATE_OPENFILEDESCRIPTORCOUNT = "openFileDescriptorCount";
    public static final String STATE_MAXFILEDESCRIPTORCOUNT = "maxFileDescriptorCount";
    public static final String STATE_FOLLOWERS = "followers";
    public static final String STATE_SYNCEDFOLLOWERS = "syncedFollowers";
    public static final String STATE_PENDINGSYNCS = "pendingSyncs";
    public static final String STATE_SERVERSTATELAG = "serverStateLag";

    /**
     * 连接信息中的指标
     */
    public static final String CONNECTION_CLIENT_IP = "clientIp";
    public static final String CONNECTION_CLIENT_IP_SID = "clientIpSid";
    public static final String CONNECTION_SENT_NAME = "sent";
    public static final String CONNECTION_RECEIVED_NAME = "recved";
    public static final String CONNECTION_MAXLAT_NAME = "maxlat";
    public static final String CONNECTION_QUEUED_NAME = "queued";
    public static final String CONNECTION_CLIENT_LIST = "clientInfoList";

    /**
     * 机器状态指标信息
     */
    public static final String MACHINE_NET_TRAFFIC = "netTraffic";
    public static final String MACHINE_AVG_LOAD = "avgLoad";
    public static final String MACHINE_CPU = "cpu";
    public static final String MACHINE_CPU_SINGLE = "cpuSingle";
    public static final String MACHINE_MEMORY = "memory";
    public static final String MACHINE_DISK = "disk";

    /**
     * 机器数据标识
     */
    public static final String NET_RECEIVE_DATA = "写流量";
    public static final String NET_SEND_DATA = "读流量";
    public static final String CPU_DATA = "cpu ";
    public static final String USED = "used";
    public static final String TOTAL = "total";

    /**
     * 默认数值0
     */
    private static final String DEFAULT_ZERO = "0";

    /**
     * 集群状态数据转化
     *
     * @param clusterStateList 集群状态数据
     * @return
     */
    public static Map<String, Object> convertClusterInitState(List<ClusterState> clusterStateList) {
        Map<String, Object> clusterInitStateMap = new HashMap<String, Object>();
        clusterInitStateMap.put(STATE_RECEIVED, convertClusterTimeValueMap(clusterStateList, STATE_RECEIVED));
        clusterInitStateMap.put(STATE_OUTSTANDINGS, convertClusterTimeValueMap(clusterStateList, STATE_OUTSTANDINGS));
        clusterInitStateMap.put(STATE_MAXLATENCY, convertClusterTimeValueMap(clusterStateList, STATE_MAXLATENCY));
        clusterInitStateMap.put(STATE_AVGLATENCY, convertClusterTimeValueMap(clusterStateList, STATE_AVGLATENCY));
        clusterInitStateMap.put(STATE_ZNODECOUNT, convertClusterTimeValueMap(clusterStateList, STATE_ZNODECOUNT));
        clusterInitStateMap.put(STATE_EPHEMERIALS, convertClusterTimeValueMap(clusterStateList, STATE_EPHEMERIALS));
        clusterInitStateMap.put(STATE_WATCHERCOUNT, convertClusterTimeValueMap(clusterStateList, STATE_WATCHERCOUNT));
        clusterInitStateMap.put(STATE_CONNECTIONS, convertClusterTimeValueMap(clusterStateList, STATE_CONNECTIONS));
        return clusterInitStateMap;
    }

    /**
     * 机器状态数据转化
     *
     * @param machineStateLogList 机器状态数据
     * @return
     */
    public static Map<String, Object> convertMachineInitState(List<MachineStateLog> machineStateLogList) {
        Map<String, Object> clusterInitStateMap = new HashMap<String, Object>();
        clusterInitStateMap.put(MACHINE_NET_TRAFFIC, convertMultiMachineTimeValueMap(machineStateLogList, MACHINE_NET_TRAFFIC));
        clusterInitStateMap.put(MACHINE_AVG_LOAD, convertMachineTimeValueMap(machineStateLogList, MACHINE_AVG_LOAD));
        clusterInitStateMap.put(MACHINE_CPU, convertMachineTimeValueMap(machineStateLogList, MACHINE_CPU));
        clusterInitStateMap.put(MACHINE_CPU_SINGLE, convertMultiMachineTimeValueMap(machineStateLogList, MACHINE_CPU_SINGLE));
        clusterInitStateMap.put(MACHINE_MEMORY, convertMultiMachineTimeValueMap(machineStateLogList, MACHINE_MEMORY));
        clusterInitStateMap.put(MACHINE_DISK, convertMultiMachineTimeValueMap(machineStateLogList, MACHINE_DISK));
        return clusterInitStateMap;
    }

    /**
     * 转化集群状态为时间-值对应关系列表
     *
     * @param clusterStateList 集群状态历史信息列表
     * @param dimension        状态指标维度
     * @return
     */
    public static TrendChartVO convertClusterTimeValueMap(List<ClusterState> clusterStateList, String dimension) {
        List<String> timeList = new ArrayList<String>();
        List<String> valueList = new ArrayList<String>();
        TrendChartVO trendChartVO = new TrendChartVO();
        if (dimension.equals(STATE_RECEIVED)) {
            for (ClusterState clusterState : clusterStateList) {
                timeList.add(DateUtil.formatYYYYMMddHHMMss(clusterState.getCreateTime()));
                valueList.add(String.valueOf(clusterState.getReceivedTotal() == null ? 0 : clusterState.getReceivedTotal()));
            }
        } else if (dimension.equals(STATE_OUTSTANDINGS)) {
            for (ClusterState clusterState : clusterStateList) {
                timeList.add(DateUtil.formatYYYYMMddHHMMss(clusterState.getCreateTime()));
                valueList.add(String.valueOf(clusterState.getOutstandingTotal() == null ? 0 : clusterState.getOutstandingTotal()));
            }
        } else if (dimension.equals(STATE_MAXLATENCY)) {
            for (ClusterState clusterState : clusterStateList) {
                timeList.add(DateUtil.formatYYYYMMddHHMMss(clusterState.getCreateTime()));
                valueList.add(String.valueOf(clusterState.getMaxLatencyMax() == null ? 0 : clusterState.getMaxLatencyMax()));
            }
        } else if (dimension.equals(STATE_AVGLATENCY)) {
            for (ClusterState clusterState : clusterStateList) {
                timeList.add(DateUtil.formatYYYYMMddHHMMss(clusterState.getCreateTime()));
                valueList.add(String.valueOf(clusterState.getAvgLatencyMax() == null ? 0 : clusterState.getAvgLatencyMax()));
            }
        } else if (dimension.equals(STATE_ZNODECOUNT)) {
            for (ClusterState clusterState : clusterStateList) {
                timeList.add(DateUtil.formatYYYYMMddHHMMss(clusterState.getCreateTime()));
                valueList.add(String.valueOf(clusterState.getZnodeCount() == null ? 0 : clusterState.getZnodeCount()));
            }
        } else if (dimension.equals(STATE_EPHEMERIALS)) {
            for (ClusterState clusterState : clusterStateList) {
                timeList.add(DateUtil.formatYYYYMMddHHMMss(clusterState.getCreateTime()));
                valueList.add(String.valueOf(clusterState.getEphemerals() == null ? 0 : clusterState.getEphemerals()));
            }
        } else if (dimension.equals(STATE_WATCHERCOUNT)) {
            for (ClusterState clusterState : clusterStateList) {
                timeList.add(DateUtil.formatYYYYMMddHHMMss(clusterState.getCreateTime()));
                valueList.add(String.valueOf(clusterState.getWatcherTotal() == null ? 0 : clusterState.getWatcherTotal()));
            }
        } else if (dimension.equals(STATE_CONNECTIONS)) {
            for (ClusterState clusterState : clusterStateList) {
                timeList.add(DateUtil.formatYYYYMMddHHMMss(clusterState.getCreateTime()));
                valueList.add(String.valueOf(clusterState.getConnectionTotal() == null ? 0 : clusterState.getConnectionTotal()));
            }
        }
        trendChartVO.setTime(timeList);
        trendChartVO.setValue(valueList);
        return trendChartVO;
    }

    /**
     * 转化机器状态为时间-值对应关系列表
     *
     * @param machineStateLogList 机器状态数据
     * @param dimension           状态指标维度
     * @return
     */
    public static TrendChartVO convertMachineTimeValueMap(List<MachineStateLog> machineStateLogList, String dimension) {
        List<String> timeList = new ArrayList<String>();
        List<String> valueList = new ArrayList<String>();
        TrendChartVO trendChartVO = new TrendChartVO();
        if (dimension.equals(MACHINE_AVG_LOAD)) {
            for (MachineStateLog machineStateLog : machineStateLogList) {
                timeList.add(DateUtil.formatYYYYMMddHHMMss(machineStateLog.getCreateTime()));
                valueList.add(String.valueOf(machineStateLog.getAvgLoad() == null ? 0 : machineStateLog.getAvgLoad()));
            }
        } else if (dimension.equals(MACHINE_CPU)) {
            for (MachineStateLog machineStateLog : machineStateLogList) {
                timeList.add(DateUtil.formatYYYYMMddHHMMss(machineStateLog.getCreateTime()));
                valueList.add(String.valueOf(machineStateLog.getCpuUsage() == null ? 0 : machineStateLog.getCpuUsage()));
            }
        }
        trendChartVO.setTime(timeList);
        trendChartVO.setValue(valueList);
        return trendChartVO;
    }

    /**
     * 获取实例实例数据
     *
     * @param instanceStateLogList 实例状态历史信息列表
     * @param dimension            状态指标维度
     * @return
     */
    public static AllTrendChartVO convertAllInsTimeValueMap(List<InstanceStateLog> instanceStateLogList, String dimension) {
        List<String> timeList = new ArrayList<String>();
        Map<String, List<String>> valueMap = new HashMap<String, List<String>>();
        Map<String, Map<String, InstanceStateLog>> logList = new HashMap<String, Map<String, InstanceStateLog>>();
        AllTrendChartVO trendChartVO = new AllTrendChartVO();
        // 先获取所有时间点信息、所有实例信息以及数据分类
        Set<String> hostSet = new HashSet<String>();
        for (InstanceStateLog instanceStateLog : instanceStateLogList) {
            String time = DateUtil.formatYYYYMMddHHMMss(instanceStateLog.getCreateTime());
            if (!timeList.contains(time)) {
                timeList.add(time);
            }
            hostSet.add(instanceStateLog.getHostInfo());
            if (logList.containsKey(time)) {
                logList.get(time).put(instanceStateLog.getHostInfo(), instanceStateLog);
            } else {
                Map<String, InstanceStateLog> map = new HashMap<String, InstanceStateLog>();
                map.put(instanceStateLog.getHostInfo(), instanceStateLog);
                logList.put(time, map);
            }
        }
        // 按前端表格数据格式返回数据
        if (dimension.equals(STATE_RECEIVED)) {
            for (String host : hostSet) { // 针对一个实例进行数据收集
                for (String time : timeList) {
                    Map<String, InstanceStateLog> data = logList.get(time);
                    // 时间轴上没有该实例数据，用0补齐
                    String currentValue = DEFAULT_ZERO;
                    if (data.containsKey(host)) { // 该时间点该实例有统计数据，如果没有则默认补0
                        Long value = data.get(host).getReceived();
                        currentValue = value == null ? DEFAULT_ZERO : String.valueOf(value);
                    }
                    if (valueMap.containsKey(host)) {
                        valueMap.get(host).add(currentValue);
                    } else {
                        List<String> value = new ArrayList<String>();
                        value.add(currentValue);
                        valueMap.put(host, value);
                    }
                }
            }
        } else if (dimension.equals(STATE_SEND)) {
            for (String host : hostSet) { // 针对一个实例进行数据收集
                for (String time : timeList) {
                    Map<String, InstanceStateLog> data = logList.get(time);
                    // 时间轴上没有该实例数据，用0补齐
                    String currentValue = DEFAULT_ZERO;
                    if (data.containsKey(host)) { // 该时间点该实例有统计数据，如果没有则默认补0
                        Long value = data.get(host).getSent();
                        currentValue = value == null ? DEFAULT_ZERO : String.valueOf(value);
                    }
                    if (valueMap.containsKey(host)) {
                        valueMap.get(host).add(currentValue);
                    } else {
                        List<String> value = new ArrayList<String>();
                        value.add(currentValue);
                        valueMap.put(host, value);
                    }
                }
            }
        } else if (dimension.equals(STATE_OUTSTANDINGS)) {
            for (String host : hostSet) { // 针对一个实例进行数据收集
                for (String time : timeList) {
                    Map<String, InstanceStateLog> data = logList.get(time);
                    // 时间轴上没有该实例数据，用0补齐
                    String currentValue = DEFAULT_ZERO;
                    if (data.containsKey(host)) { // 该时间点该实例有统计数据，如果没有则默认补0
                        Long value = data.get(host).getOutstandings();
                        currentValue = value == null ? DEFAULT_ZERO : String.valueOf(value);
                    }
                    if (valueMap.containsKey(host)) {
                        valueMap.get(host).add(currentValue);
                    } else {
                        List<String> value = new ArrayList<String>();
                        value.add(currentValue);
                        valueMap.put(host, value);
                    }
                }
            }
        } else if (dimension.equals(STATE_MAXLATENCY)) {
            for (String host : hostSet) { // 针对一个实例进行数据收集
                for (String time : timeList) {
                    Map<String, InstanceStateLog> data = logList.get(time);
                    // 时间轴上没有该实例数据，用0补齐
                    String currentValue = DEFAULT_ZERO;
                    if (data.containsKey(host)) { // 该时间点该实例有统计数据，如果没有则默认补0
                        Long value = data.get(host).getMaxLatency();
                        currentValue = value == null ? DEFAULT_ZERO : String.valueOf(value);
                    }
                    if (valueMap.containsKey(host)) {
                        valueMap.get(host).add(currentValue);
                    } else {
                        List<String> value = new ArrayList<String>();
                        value.add(currentValue);
                        valueMap.put(host, value);
                    }
                }
            }
        } else if (dimension.equals(STATE_AVGLATENCY)) {
            for (String host : hostSet) { // 针对一个实例进行数据收集
                for (String time : timeList) {
                    Map<String, InstanceStateLog> data = logList.get(time);
                    // 时间轴上没有该实例数据，用0补齐
                    String currentValue = DEFAULT_ZERO;
                    if (data.containsKey(host)) { // 该时间点该实例有统计数据，如果没有则默认补0
                        Long value = data.get(host).getAvgLatency();
                        currentValue = value == null ? DEFAULT_ZERO : String.valueOf(value);
                    }
                    if (valueMap.containsKey(host)) {
                        valueMap.get(host).add(currentValue);
                    } else {
                        List<String> value = new ArrayList<String>();
                        value.add(currentValue);
                        valueMap.put(host, value);
                    }
                }
            }

        } else if (dimension.equals(STATE_MINLATENCY)) {
            for (String host : hostSet) { // 针对一个实例进行数据收集
                for (String time : timeList) {
                    Map<String, InstanceStateLog> data = logList.get(time);
                    // 时间轴上没有该实例数据，用0补齐
                    String currentValue = DEFAULT_ZERO;
                    if (data.containsKey(host)) { // 该时间点该实例有统计数据，如果没有则默认补0
                        Long value = data.get(host).getMinLatency();
                        currentValue = value == null ? DEFAULT_ZERO : String.valueOf(value);
                    }
                    if (valueMap.containsKey(host)) {
                        valueMap.get(host).add(currentValue);
                    } else {
                        List<String> value = new ArrayList<String>();
                        value.add(currentValue);
                        valueMap.put(host, value);
                    }
                }
            }
        } else if (dimension.equals(STATE_ZNODECOUNT)) {
            for (String host : hostSet) { // 针对一个实例进行数据收集
                for (String time : timeList) {
                    Map<String, InstanceStateLog> data = logList.get(time);
                    // 时间轴上没有该实例数据，用0补齐
                    String currentValue = DEFAULT_ZERO;
                    if (data.containsKey(host)) { // 该时间点该实例有统计数据，如果没有则默认补0
                        Integer value = data.get(host).getZnodeCount();
                        currentValue = value == null ? DEFAULT_ZERO : String.valueOf(value);
                    }
                    if (valueMap.containsKey(host)) {
                        valueMap.get(host).add(currentValue);
                    } else {
                        List<String> value = new ArrayList<String>();
                        value.add(currentValue);
                        valueMap.put(host, value);
                    }
                }
            }
        } else if (dimension.equals(STATE_EPHEMERIALS)) {
            for (String host : hostSet) { // 针对一个实例进行数据收集
                for (String time : timeList) {
                    Map<String, InstanceStateLog> data = logList.get(time);
                    // 时间轴上没有该实例数据，用0补齐
                    String currentValue = DEFAULT_ZERO;
                    if (data.containsKey(host)) { // 该时间点该实例有统计数据，如果没有则默认补0
                        Integer value = data.get(host).getEphemeralsCount();
                        currentValue = value == null ? DEFAULT_ZERO : String.valueOf(value);
                    }
                    if (valueMap.containsKey(host)) {
                        valueMap.get(host).add(currentValue);
                    } else {
                        List<String> value = new ArrayList<String>();
                        value.add(currentValue);
                        valueMap.put(host, value);
                    }
                }
            }
        } else if (dimension.equals(STATE_WATCHERCOUNT)) {
            for (String host : hostSet) { // 针对一个实例进行数据收集
                for (String time : timeList) {
                    Map<String, InstanceStateLog> data = logList.get(time);
                    // 时间轴上没有该实例数据，用0补齐
                    String currentValue = DEFAULT_ZERO;
                    if (data.containsKey(host)) { // 该时间点该实例有统计数据，如果没有则默认补0
                        Integer value = data.get(host).getWatchCount();
                        currentValue = value == null ? DEFAULT_ZERO : String.valueOf(value);
                    }
                    if (valueMap.containsKey(host)) {
                        valueMap.get(host).add(currentValue);
                    } else {
                        List<String> value = new ArrayList<String>();
                        value.add(currentValue);
                        valueMap.put(host, value);
                    }
                }
            }
        } else if (dimension.equals(STATE_CONNECTIONS)) {
            for (String host : hostSet) { // 针对一个实例进行数据收集
                for (String time : timeList) {
                    Map<String, InstanceStateLog> data = logList.get(time);
                    // 时间轴上没有该实例数据，用0补齐
                    String currentValue = DEFAULT_ZERO;
                    if (data.containsKey(host)) { // 该时间点该实例有统计数据，如果没有则默认补0
                        Integer value = data.get(host).getConnections();
                        currentValue = value == null ? DEFAULT_ZERO : String.valueOf(value);
                    }
                    if (valueMap.containsKey(host)) {
                        valueMap.get(host).add(currentValue);
                    } else {
                        List<String> value = new ArrayList<String>();
                        value.add(currentValue);
                        valueMap.put(host, value);
                    }
                }
            }
        } else if (dimension.equals(STATE_APPROXIMATEDATASIZE)) {
            for (String host : hostSet) { // 针对一个实例进行数据收集
                for (String time : timeList) {
                    Map<String, InstanceStateLog> data = logList.get(time);
                    // 时间轴上没有该实例数据，用0补齐
                    String currentValue = DEFAULT_ZERO;
                    if (data.containsKey(host)) { // 该时间点该实例有统计数据，如果没有则默认补0
                        Long value = data.get(host).getApproximateDataSize();
                        currentValue = value == null ? DEFAULT_ZERO : String.valueOf(value);
                    }
                    if (valueMap.containsKey(host)) {
                        valueMap.get(host).add(currentValue);
                    } else {
                        List<String> value = new ArrayList<String>();
                        value.add(currentValue);
                        valueMap.put(host, value);
                    }
                }
            }
        } else if (dimension.equals(STATE_OPENFILEDESCRIPTORCOUNT)) {
            for (String host : hostSet) { // 针对一个实例进行数据收集
                for (String time : timeList) {
                    Map<String, InstanceStateLog> data = logList.get(time);
                    // 时间轴上没有该实例数据，用0补齐
                    String currentValue = DEFAULT_ZERO;
                    if (data.containsKey(host)) { // 该时间点该实例有统计数据，如果没有则默认补0
                        Long value = data.get(host).getOpenFileDescriptorCount();
                        currentValue = value == null ? DEFAULT_ZERO : String.valueOf(value);
                    }
                    if (valueMap.containsKey(host)) {
                        valueMap.get(host).add(currentValue);
                    } else {
                        List<String> value = new ArrayList<String>();
                        value.add(currentValue);
                        valueMap.put(host, value);
                    }
                }
            }
        } else if (dimension.equals(STATE_MAXFILEDESCRIPTORCOUNT)) {
            for (String host : hostSet) { // 针对一个实例进行数据收集
                for (String time : timeList) {
                    Map<String, InstanceStateLog> data = logList.get(time);
                    // 时间轴上没有该实例数据，用0补齐
                    String currentValue = DEFAULT_ZERO;
                    if (data.containsKey(host)) { // 该时间点该实例有统计数据，如果没有则默认补0
                        Long value = data.get(host).getMaxFileDescriptorCount();
                        currentValue = value == null ? DEFAULT_ZERO : String.valueOf(value);
                    }
                    if (valueMap.containsKey(host)) {
                        valueMap.get(host).add(currentValue);
                    } else {
                        List<String> value = new ArrayList<String>();
                        value.add(currentValue);
                        valueMap.put(host, value);
                    }
                }
            }
        } else if (dimension.equals(STATE_FOLLOWERS)) {
            for (String host : hostSet) { // 针对一个实例进行数据收集
                for (String time : timeList) {
                    Map<String, InstanceStateLog> data = logList.get(time);
                    // 时间轴上没有该实例数据，用0补齐
                    String currentValue = DEFAULT_ZERO;
                    if (data.containsKey(host)) { // 该时间点该实例有统计数据，如果没有则默认补0
                        Integer value = data.get(host).getFollowers();
                        currentValue = value == null ? DEFAULT_ZERO : String.valueOf(value);
                    }
                    if (valueMap.containsKey(host)) {
                        valueMap.get(host).add(currentValue);
                    } else {
                        List<String> value = new ArrayList<String>();
                        value.add(currentValue);
                        valueMap.put(host, value);
                    }
                }
            }
        } else if (dimension.equals(STATE_SYNCEDFOLLOWERS)) {
            for (String host : hostSet) { // 针对一个实例进行数据收集
                for (String time : timeList) {
                    Map<String, InstanceStateLog> data = logList.get(time);
                    // 时间轴上没有该实例数据，用0补齐
                    String currentValue = DEFAULT_ZERO;
                    if (data.containsKey(host)) { // 该时间点该实例有统计数据，如果没有则默认补0
                        Integer value = data.get(host).getSyncedFollowers();
                        currentValue = value == null ? DEFAULT_ZERO : String.valueOf(value);
                    }
                    if (valueMap.containsKey(host)) {
                        valueMap.get(host).add(currentValue);
                    } else {
                        List<String> value = new ArrayList<String>();
                        value.add(currentValue);
                        valueMap.put(host, value);
                    }
                }
            }
        } else if (dimension.equals(STATE_PENDINGSYNCS)) {
            for (String host : hostSet) { // 针对一个实例进行数据收集
                for (String time : timeList) {
                    Map<String, InstanceStateLog> data = logList.get(time);
                    // 时间轴上没有该实例数据，用0补齐
                    String currentValue = DEFAULT_ZERO;
                    if (data.containsKey(host)) { // 该时间点该实例有统计数据，如果没有则默认补0
                        Integer value = data.get(host).getPendingSyncs();
                        currentValue = value == null ? DEFAULT_ZERO : String.valueOf(value);
                    }
                    if (valueMap.containsKey(host)) {
                        valueMap.get(host).add(currentValue);
                    } else {
                        List<String> value = new ArrayList<String>();
                        value.add(currentValue);
                        valueMap.put(host, value);
                    }
                }
            }
        } else if (dimension.equals(STATE_SERVERSTATELAG)) {
            for (String host : hostSet) { // 针对一个实例进行数据收集
                for (String time : timeList) {
                    Map<String, InstanceStateLog> data = logList.get(time);
                    // 时间轴上没有该实例数据，用0补齐
                    String currentValue = DEFAULT_ZERO;
                    if (data.containsKey(host)) { // 该时间点该实例有统计数据，如果没有则默认补0
                        Integer value = data.get(host).getServerStateLag();
                        currentValue = value == null ? DEFAULT_ZERO : String.valueOf(value);
                    }
                    if (valueMap.containsKey(host)) {
                        valueMap.get(host).add(currentValue);
                    } else {
                        List<String> value = new ArrayList<String>();
                        value.add(currentValue);
                        valueMap.put(host, value);
                    }
                }
            }
        }
        trendChartVO.setTime(timeList);
        trendChartVO.setValue(valueMap);
        return trendChartVO;
    }

    /**
     * 机器流量以及单cpu数据
     *
     * @param machineStateLogList 机器状态数据
     * @param dimension           状态指标维度
     * @return
     */
    public static AllTrendChartVO convertMultiMachineTimeValueMap(List<MachineStateLog> machineStateLogList, String dimension) {
        List<String> timeList = new ArrayList<String>();
        Map<String, List<String>> valueMap = new HashMap<String, List<String>>();
        AllTrendChartVO trendChartVO = new AllTrendChartVO();
        if (dimension.equals(MACHINE_NET_TRAFFIC)) {
            for (MachineStateLog machineStateLog : machineStateLogList) {
                String time = DateUtil.formatYYYYMMddHHMMss(machineStateLog.getCreateTime());
                if (!timeList.contains(time)) {
                    timeList.add(time);
                }
                // 单位：KB/s
                String netTraffic = machineStateLog.getNetTraffic();
                String netReceive = DEFAULT_ZERO;
                String netSend = DEFAULT_ZERO;
                if (netTraffic != null) {
                    String[] net = netTraffic.split(SymbolConstant.BLANK);
                    if (net.length == 2) {
                        netReceive = net[0];
                        netSend = net[1];
                    }
                }
                if (valueMap.containsKey(NET_RECEIVE_DATA)) {
                    valueMap.get(NET_RECEIVE_DATA).add(netReceive);
                } else {
                    List<String> value = new ArrayList<String>();
                    value.add(netReceive);
                    valueMap.put(NET_RECEIVE_DATA, value);
                }

                if (valueMap.containsKey(NET_SEND_DATA)) {
                    valueMap.get(NET_SEND_DATA).add(netSend);
                } else {
                    List<String> value = new ArrayList<String>();
                    value.add(netSend);
                    valueMap.put(NET_SEND_DATA, value);
                }
            }
        } else if (dimension.equals(MACHINE_CPU_SINGLE)) {
            for (MachineStateLog machineStateLog : machineStateLogList) {
                String time = DateUtil.formatYYYYMMddHHMMss(machineStateLog.getCreateTime());
                if (!timeList.contains(time)) {
                    timeList.add(time);
                }
                String cpuSingle = machineStateLog.getCpuSingleUsage();
                if (cpuSingle != null) {
                    String[] cpuArray = cpuSingle.split(SymbolConstant.BLANK);
                    for (int i = 0; i < cpuArray.length; i++) {
                        String key = CPU_DATA + i;
                        if (valueMap.containsKey(key)) {
                            valueMap.get(key).add(cpuArray[i]);
                        } else {
                            List<String> value = new ArrayList<String>();
                            value.add(cpuArray[i]);
                            valueMap.put(key, value);
                        }
                    }
                }
            }
        } else if (dimension.equals(MACHINE_DISK)) {
            for (MachineStateLog machineStateLog : machineStateLogList) {
                String time = DateUtil.formatYYYYMMddHHMMss(machineStateLog.getCreateTime());
                if (!timeList.contains(time)) {
                    timeList.add(time);
                }
                String diskSituation = machineStateLog.getDiskSituation();
                if (diskSituation != null) {
                    String[] diskArray = diskSituation.split(SymbolConstant.DASH);
                    for (int i = 0; i < diskArray.length; i++) {
                        if (diskArray[i].contains(ConfigUtil.getDataDirConfig())) { // dataDir目录下的磁盘使用情况
                            String[] disk = diskArray[i].split(SymbolConstant.BLANK);
                            if (valueMap.containsKey(TOTAL)) {
                                valueMap.get(TOTAL).add(disk[1]);
                            } else {
                                List<String> value = new ArrayList<String>();
                                value.add(disk[1]);
                                valueMap.put(TOTAL, value);
                            }
                            if (valueMap.containsKey(USED)) {
                                valueMap.get(USED).add(disk[2]);
                            } else {
                                List<String> value = new ArrayList<String>();
                                value.add(disk[2]);
                                valueMap.put(USED, value);
                            }
                        }
                    }
                }
            }
        } else if (dimension.equals(MACHINE_MEMORY)) {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            for (MachineStateLog machineStateLog : machineStateLogList) {
                String time = DateUtil.formatYYYYMMddHHMMss(machineStateLog.getCreateTime());
                if (!timeList.contains(time)) {
                    timeList.add(time);
                }
                String memoryBuffers = machineStateLog.getBuffers();
                String memoryCached = machineStateLog.getCached();
                String memoryFree = machineStateLog.getMemoryFree();
                String memoryTotal = machineStateLog.getMemoryTotal();
                String memoryUsed = DEFAULT_ZERO;
                if (memoryFree != null && memoryTotal != null && memoryBuffers != null && memoryCached != null) {
                    // 单位：GB
                    memoryUsed = decimalFormat.format((Double.parseDouble(memoryTotal) - Double.parseDouble(memoryFree) -
                            Double.parseDouble(memoryBuffers) - Double.parseDouble(memoryCached)) /
                            SymbolConstant.BASE_UNIT / SymbolConstant.BASE_UNIT);
                }
                if (valueMap.containsKey(USED)) {
                    valueMap.get(USED).add(memoryUsed);
                } else {
                    List<String> value = new ArrayList<String>();
                    value.add(memoryUsed);
                    valueMap.put(USED, value);
                }
                String memoryTotalStr = DEFAULT_ZERO;
                if (memoryTotal != null) { // 单位：GB
                    memoryTotalStr = decimalFormat.format(Double.parseDouble(memoryTotal) / SymbolConstant.BASE_UNIT / SymbolConstant.BASE_UNIT);
                }
                if (valueMap.containsKey(TOTAL)) {
                    valueMap.get(TOTAL).add(memoryTotalStr);
                } else {
                    List<String> value = new ArrayList<String>();
                    value.add(memoryTotalStr);
                    valueMap.put(TOTAL, value);
                }
            }
        }
        trendChartVO.setTime(timeList);
        trendChartVO.setValue(valueMap);
        return trendChartVO;
    }
}
