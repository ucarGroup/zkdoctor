package com.ucar.zkdoctor.web.controller.ordinary;

import com.alibaba.fastjson.JSONObject;
import com.ucar.zkdoctor.pojo.bo.CacheObject;
import com.ucar.zkdoctor.pojo.bo.ClientConnectionSearchBO;
import com.ucar.zkdoctor.pojo.bo.StatSearchBO;
import com.ucar.zkdoctor.pojo.dto.ConnectionInfoDTO;
import com.ucar.zkdoctor.pojo.dto.ZKServerConfigDTO;
import com.ucar.zkdoctor.pojo.po.ClientInfo;
import com.ucar.zkdoctor.pojo.po.InstanceInfo;
import com.ucar.zkdoctor.pojo.po.InstanceStateLog;
import com.ucar.zkdoctor.pojo.po.MachineStateLog;
import com.ucar.zkdoctor.pojo.vo.AllTrendChartVO;
import com.ucar.zkdoctor.pojo.vo.InstanceDetailVO;
import com.ucar.zkdoctor.pojo.vo.TrendChartVO;
import com.ucar.zkdoctor.service.collection.CollectService;
import com.ucar.zkdoctor.service.instance.InstanceService;
import com.ucar.zkdoctor.service.instance.InstanceStateService;
import com.ucar.zkdoctor.service.machine.MachineStateService;
import com.ucar.zkdoctor.util.tool.ChartConvertUtil;
import com.ucar.zkdoctor.util.tool.DateUtil;
import com.ucar.zkdoctor.web.ConResult;
import com.ucar.zkdoctor.web.controller.BaseController;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 实例Controller，普通用户权限相关操作
 * Created on 2018/1/9 10:56
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Controller
@RequestMapping("/instance")
public class InstanceController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(InstanceController.class);

    @Resource
    private InstanceService instanceService;

    @Resource
    private InstanceStateService instanceStateService;

    @Resource
    private CollectService collectService;

    @Resource
    private MachineStateService machineStateService;

    /**
     * 实例配置缓存时长，单位：s，默认保存10分钟，超过10分钟则再次获取该值
     */
    private static final int ZK_CONFIG_EXPIRED = 600;

    /**
     * 实例连接信息缓存时长，单位：s，默认保存30s，超过30s则再次获取该值
     */
    private static final int ZK_CONNECTION_EXPIRED = 30;

    /**
     * 获取实例配置，缓存
     */
    private Map<Integer, CacheObject<ZKServerConfigDTO>> zkServerConfigCache = new HashMap<Integer, CacheObject<ZKServerConfigDTO>>();

    /**
     * 获取实例连接信息，缓存
     */
    private Map<Integer, CacheObject<List<ConnectionInfoDTO>>> zkServerConnectionCache = new HashMap<Integer, CacheObject<List<ConnectionInfoDTO>>>();

    /**
     * 根据集群id查询实例详情信息
     *
     * @param clusterId
     * @return
     */
    @RequestMapping("/queryByClusterId")
    @ResponseBody
    public ConResult doQuery(Integer clusterId) {
        if (clusterId == null) {
            return ConResult.fail("集群id为NULL，请重试。");
        }
        List<InstanceDetailVO> instanceDetailVOList = instanceService.getInstanceDetailVOByClusterId(clusterId);
        return ConResult.success(instanceDetailVOList);
    }


    /**
     * 根据实例status查询实例详情信息
     *
     * @param status
     * @return
     */
    @RequestMapping("/queryByInstanceStatus")
    @ResponseBody
    public ConResult doQueryByStatus(Integer status) {
        if (status == null) {
            return ConResult.fail("实例status为NULL，请重试。");
        }
        List<InstanceDetailVO> instanceDetailVOList = instanceService.getInstanceDetailVOByStatus(status);
        return ConResult.success(instanceDetailVOList);
    }

    /**
     * 根据实例id获取实例详情信息
     *
     * @param instanceId 实例id
     * @return
     */
    @RequestMapping("/instanceDetailInfo")
    @ResponseBody
    public ConResult doGetInstanceDetailInfo(Integer instanceId) {
        if (instanceId == null) {
            return ConResult.fail("实例id为NULL，请重试。");
        }
        return ConResult.success(instanceService.getInstanceDetailVOByInstanceId(instanceId));
    }

    /**
     * 获取集群下所有实例的收包数历史信息
     *
     * @param statSearchBO 历史信息查询条件
     * @return
     */
    @RequestMapping("/stat/receivedAllIns/trend")
    @ResponseBody
    public ConResult doReceivedAllIns(StatSearchBO statSearchBO) {
        return getClusterStateTrend(statSearchBO, ChartConvertUtil.STATE_RECEIVED);
    }

    /**
     * 获取集群下所有实例的发包数历史信息
     *
     * @param statSearchBO 历史信息查询条件
     * @return
     */
    @RequestMapping("/stat/sendAllIns/trend")
    @ResponseBody
    public ConResult doSendAllIns(StatSearchBO statSearchBO) {
        return getClusterStateTrend(statSearchBO, ChartConvertUtil.STATE_SEND);
    }

    /**
     * 获取集群下所有实例的堆积请求数历史信息
     *
     * @param statSearchBO 历史信息查询条件
     * @return
     */
    @RequestMapping("/stat/outstandingsAllIns/trend")
    @ResponseBody
    public ConResult doOutstandingsAllIns(StatSearchBO statSearchBO) {
        return getClusterStateTrend(statSearchBO, ChartConvertUtil.STATE_OUTSTANDINGS);
    }

    /**
     * 获取集群下所有实例的最大延时历史信息
     *
     * @param statSearchBO 历史信息查询条件
     * @return
     */
    @RequestMapping("/stat/maxLatencyAllIns/trend")
    @ResponseBody
    public ConResult doMaxLatencyAllIns(StatSearchBO statSearchBO) {
        return getClusterStateTrend(statSearchBO, ChartConvertUtil.STATE_MAXLATENCY);
    }

    /**
     * 获取集群下所有实例的平均延时历史信息
     *
     * @param statSearchBO 历史信息查询条件
     * @return
     */
    @RequestMapping("/stat/avgLatencyAllIns/trend")
    @ResponseBody
    public ConResult doAvgLatencyAllIns(StatSearchBO statSearchBO) {
        return getClusterStateTrend(statSearchBO, ChartConvertUtil.STATE_AVGLATENCY);
    }

    /**
     * 获取集群下所有实例的最小延时历史信息
     *
     * @param statSearchBO 历史信息查询条件
     * @return
     */
    @RequestMapping("/stat/minLatencyAllIns/trend")
    @ResponseBody
    public ConResult doMinLatencyAllIns(StatSearchBO statSearchBO) {
        return getClusterStateTrend(statSearchBO, ChartConvertUtil.STATE_MINLATENCY);
    }

    /**
     * 获取集群下所有实例的节点数历史信息
     *
     * @param statSearchBO 历史信息查询条件
     * @return
     */
    @RequestMapping("/stat/znodeCountAllIns/trend")
    @ResponseBody
    public ConResult doZnodeCountAllIns(StatSearchBO statSearchBO) {
        return getClusterStateTrend(statSearchBO, ChartConvertUtil.STATE_ZNODECOUNT);
    }

    /**
     * 获取集群下所有实例的临时节点数历史信息
     *
     * @param statSearchBO 历史信息查询条件
     * @return
     */
    @RequestMapping("/stat/ephemeralsdAllIns/trend")
    @ResponseBody
    public ConResult doEphemeralsdAllIns(StatSearchBO statSearchBO) {
        return getClusterStateTrend(statSearchBO, ChartConvertUtil.STATE_EPHEMERIALS);
    }

    /**
     * 获取集群下所有实例的watcher数历史信息
     *
     * @param statSearchBO 历史信息查询条件
     * @return
     */
    @RequestMapping("/stat/watcherCountAllIns/trend")
    @ResponseBody
    public ConResult doWatcherCountAllIns(StatSearchBO statSearchBO) {
        return getClusterStateTrend(statSearchBO, ChartConvertUtil.STATE_WATCHERCOUNT);
    }

    /**
     * 获取集群下所有实例的连接数历史信息
     *
     * @param statSearchBO 历史信息查询条件
     * @return
     */
    @RequestMapping("/stat/connectionsAllIns/trend")
    @ResponseBody
    public ConResult doConnectionsAllIns(StatSearchBO statSearchBO) {
        return getClusterStateTrend(statSearchBO, ChartConvertUtil.STATE_CONNECTIONS);
    }

    /**
     * 获取集群下所有实例的数据大小历史信息
     *
     * @param statSearchBO 历史信息查询条件
     * @return
     */
    @RequestMapping("/stat/approximateDataSizeAllIns/trend")
    @ResponseBody
    public ConResult doApproximateDataSizeAllIns(StatSearchBO statSearchBO) {
        return getClusterStateTrend(statSearchBO, ChartConvertUtil.STATE_APPROXIMATEDATASIZE);
    }

    /**
     * 获取集群下所有实例的打开文件描述符历史信息
     *
     * @param statSearchBO 历史信息查询条件
     * @return
     */
    @RequestMapping("/stat/openFileDescriptorCountAllIns/trend")
    @ResponseBody
    public ConResult doOpenFileDescriptorCountAllIns(StatSearchBO statSearchBO) {
        return getClusterStateTrend(statSearchBO, ChartConvertUtil.STATE_OPENFILEDESCRIPTORCOUNT);
    }

    /**
     * 获取集群下所有实例的最大文件描述符历史信息
     *
     * @param statSearchBO 历史信息查询条件
     * @return
     */
    @RequestMapping("/stat/maxFileDescriptorCountAllIns/trend")
    @ResponseBody
    public ConResult doMaxFileDescriptorCountAllInss(StatSearchBO statSearchBO) {
        return getClusterStateTrend(statSearchBO, ChartConvertUtil.STATE_MAXFILEDESCRIPTORCOUNT);
    }

    /**
     * 获取集群下所有实例的follower数历史信息
     *
     * @param statSearchBO 历史信息查询条件
     * @return
     */
    @RequestMapping("/stat/followersAllIns/trend")
    @ResponseBody
    public ConResult doFollowersAllIns(StatSearchBO statSearchBO) {
        return getClusterStateTrend(statSearchBO, ChartConvertUtil.STATE_FOLLOWERS);
    }

    /**
     * 获取集群下所有实例的同步follower数历史信息
     *
     * @param statSearchBO 历史信息查询条件
     * @return
     */
    @RequestMapping("/stat/syncedFollowersAllIns/trend")
    @ResponseBody
    public ConResult doSyncedFollowersAllIns(StatSearchBO statSearchBO) {
        return getClusterStateTrend(statSearchBO, ChartConvertUtil.STATE_SYNCEDFOLLOWERS);
    }

    /**
     * 获取集群下所有实例的待同步follower数历史信息
     *
     * @param statSearchBO 历史信息查询条件
     * @return
     */
    @RequestMapping("/stat/pendingSyncsAllIns/trend")
    @ResponseBody
    public ConResult doPendingSyncsAllIns(StatSearchBO statSearchBO) {
        return getClusterStateTrend(statSearchBO, ChartConvertUtil.STATE_PENDINGSYNCS);
    }

    /**
     * 获取集群下所有实例的角色历史信息
     *
     * @param statSearchBO 历史信息查询条件
     * @return
     */
    @RequestMapping("/stat/serverStateLagAllIns/trend")
    @ResponseBody
    public ConResult doServerStateLagAllIns(StatSearchBO statSearchBO) {
        return getClusterStateTrend(statSearchBO, ChartConvertUtil.STATE_SERVERSTATELAG);
    }

    /**
     * 获取zk服务配置信息
     *
     * @param instanceId 实例id
     * @return
     */
    @RequestMapping("/instanceConfig")
    @ResponseBody
    public ConResult doInstanceConfig(Integer instanceId) {
        if (instanceId == null) {
            return ConResult.fail("实例id为NULL，请重试。");
        }
        ZKServerConfigDTO zkServerConfigDTO = getZkConfig(instanceId);
        return ConResult.success(zkServerConfigDTO != null ? zkServerConfigDTO : new ZKServerConfigDTO());
    }

    /**
     * 获取zk服务连接信息
     *
     * @param instanceId 实例id
     * @return
     */
    @RequestMapping("/instanceConnections")
    @ResponseBody
    public ConResult doInstanceConnections(Integer instanceId) {
        if (instanceId == null) {
            return ConResult.fail("实例id为NULL，请重试。");
        }
        List<ConnectionInfoDTO> connectionInfoDTOList = getZkConnection(instanceId);
        return ConResult.success(connectionInfoDTOList != null ? connectionInfoDTOList : new ArrayList<ConnectionInfoDTO>());
    }

    /**
     * 初始化获取所有机器历史数据
     *
     * @param statSearchBO 查询条件
     * @return
     */
    @RequestMapping(value = "/machine/init/trend")
    @ResponseBody
    public ConResult doMachineStatInit(StatSearchBO statSearchBO) {
        return getMachineStateTrend(statSearchBO, null, true);
    }

    /**
     * 获取机器网络流量历史数据
     *
     * @param statSearchBO 查询条件
     * @return
     */
    @RequestMapping(value = "/machine/netTraffic/trend")
    @ResponseBody
    public ConResult doMachineNetTraffic(StatSearchBO statSearchBO) {
        return getMachineStateTrend(statSearchBO, ChartConvertUtil.MACHINE_NET_TRAFFIC, false);
    }

    /**
     * 获取机器平均负载历史数据
     *
     * @param statSearchBO 查询条件
     * @return
     */
    @RequestMapping(value = "/machine/avgLoad/trend")
    @ResponseBody
    public ConResult doMachineAvgLoad(StatSearchBO statSearchBO) {
        return getMachineStateTrend(statSearchBO, ChartConvertUtil.MACHINE_AVG_LOAD, false);
    }

    /**
     * 获取机器cpu使用率历史数据
     *
     * @param statSearchBO 查询条件
     * @return
     */
    @RequestMapping(value = "/machine/cpu/trend")
    @ResponseBody
    public ConResult doMachineCpu(StatSearchBO statSearchBO) {
        return getMachineStateTrend(statSearchBO, ChartConvertUtil.MACHINE_CPU, false);
    }

    /**
     * 获取机器单cpu使用率历史数据
     *
     * @param statSearchBO 查询条件
     * @return
     */
    @RequestMapping(value = "/machine/cpuSingle/trend")
    @ResponseBody
    public ConResult doMachineCpuSingle(StatSearchBO statSearchBO) {
        return getMachineStateTrend(statSearchBO, ChartConvertUtil.MACHINE_CPU_SINGLE, false);
    }

    /**
     * 获取机器内存使用率历史数据
     *
     * @param statSearchBO 查询条件
     * @return
     */
    @RequestMapping(value = "/machine/memory/trend")
    @ResponseBody
    public ConResult doMachineMemory(StatSearchBO statSearchBO) {
        return getMachineStateTrend(statSearchBO, ChartConvertUtil.MACHINE_MEMORY, false);
    }

    /**
     * 获取机器磁盘使用率历史数据
     *
     * @param statSearchBO 查询条件
     * @return
     */
    @RequestMapping(value = "/machine/disk/trend")
    @ResponseBody
    public ConResult doMachineDisk(StatSearchBO statSearchBO) {
        return getMachineStateTrend(statSearchBO, ChartConvertUtil.MACHINE_DISK, false);
    }

    /**
     * 查询连接历史信息
     *
     * @param clientConnectionSearchBO 查询条件
     * @return
     */
    @RequestMapping(value = "/searchConnectionHistory")
    @ResponseBody
    public ConResult doSearchConnectionHistory(ClientConnectionSearchBO clientConnectionSearchBO) {
        try {
            isLegalClientConnHistorySearchParams(clientConnectionSearchBO);
        } catch (RuntimeException e) {
            return ConResult.fail("失败：" + e.getMessage());
        }
        List<ClientInfo> clientInfoList = instanceStateService.dealWithClientConnectionsViews(instanceStateService.getClientConnectionsByParams(clientConnectionSearchBO),
                clientConnectionSearchBO.getOrderBy());
        return ConResult.success(clientInfoList != null ? clientInfoList : new ArrayList<ClientInfo>());
    }

    /**
     * 获取最近一次实例连接信息收集的时间点
     *
     * @param instanceId 实例id
     * @return
     */
    @RequestMapping(value = "/getConnectionCollectTime")
    @ResponseBody
    public ConResult doGetConnectionCollectTime(Integer instanceId) {
        if (instanceId == null) {
            return ConResult.fail("实例id为NULL，请重试。");
        }
        ClientInfo clientInfo = instanceStateService.getLatestClientInfo(instanceId);
        return ConResult.success(clientInfo == null ? "" : clientInfo.getCreateTimeStr());
    }

    /**
     * 查询某实例某个时间点机器流量Top10数据信息
     *
     * @param instanceId 实例id
     * @param dateTime   时间点
     * @return
     */
    @RequestMapping(value = "/queryNetTrafficDetail")
    @ResponseBody
    public ConResult doQueryNetTrafficDetail(Integer instanceId, String dateTime) {
        if (instanceId == null || StringUtils.isBlank(dateTime)) {
            return ConResult.fail("实例id或时间为NULL，请重试。");
        }
        Map<String, Object> data = new HashMap<String, Object>();
        Object trafficInDetail = "";
        Object trafficOutDetail = "";
        try {
            InstanceInfo instanceInfo = instanceService.getInstanceInfoById(instanceId);
            if (instanceInfo != null) {
                MachineStateLog machineStateLog = machineStateService.getMachineStateLogByTime(instanceInfo.getHost(),
                        DateUtil.parseYYYYMMddHHmmss(dateTime));
                if (machineStateLog != null) {
                    if (machineStateLog.getNetFlowIn() != null) {
                        trafficInDetail = JSONObject.parseArray(machineStateLog.getNetFlowIn());
                    }
                    if (machineStateLog.getNetFlowOut() != null) {
                        trafficOutDetail = JSONObject.parseArray(machineStateLog.getNetFlowOut());
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Get Traffic detail info failed.", e);
        }
        data.put("trafficInDetail", trafficInDetail);
        data.put("trafficOutDetail", trafficOutDetail);
        return ConResult.success(data);
    }

    /**
     * 获取对应的机器状态历史运行趋势
     *
     * @param statSearchBO 机器历史信息查询条件
     * @param dimension    机器状态指标维度
     * @param isAll        是否获取所有历史状态信息
     * @return
     */
    private ConResult getMachineStateTrend(StatSearchBO statSearchBO, String dimension, boolean isAll) {
        try {
            isLegalStatSearchParams(statSearchBO);
        } catch (RuntimeException e) {
            return ConResult.fail("失败：" + e.getMessage());
        }
        try {
            Date startDate = DateUtil.parseYYYYMMddHHmm(statSearchBO.getStart());
            Date endDate = DateUtil.parseYYYYMMddHHmm(statSearchBO.getEnd());
            InstanceInfo instanceInfo = instanceService.getInstanceInfoById(statSearchBO.getId());
            if (instanceInfo == null) {
                return ConResult.fail("实例信息为NULL");
            }
            List<MachineStateLog> machineStateLogList = machineStateService.getMachineStateLogByMachine(instanceInfo.getMachineId(),
                    startDate, endDate);
            if (isAll) {
                Map<String, Object> machineChartData = ChartConvertUtil.convertMachineInitState(machineStateLogList);
                return ConResult.success(machineChartData);
            } else {
                if (ChartConvertUtil.MACHINE_AVG_LOAD.equals(dimension) || ChartConvertUtil.MACHINE_CPU.equals(dimension)) {
                    TrendChartVO machineStateChartData = ChartConvertUtil.convertMachineTimeValueMap(machineStateLogList, dimension);
                    return ConResult.success(machineStateChartData);
                } else {
                    AllTrendChartVO machineStateChartData = ChartConvertUtil.convertMultiMachineTimeValueMap(machineStateLogList, dimension);
                    return ConResult.success(machineStateChartData);
                }
            }
        } catch (ParseException e) {
            return ConResult.fail("时间转化失败：" + e.getMessage());
        }
    }

    /**
     * 获取对应的集群状态历史运行趋势
     *
     * @param statSearchBO 集群历史信息查询条件
     * @param dimension    集群状态指标维度
     * @return
     */
    private ConResult getClusterStateTrend(StatSearchBO statSearchBO, String dimension) {
        try {
            isLegalStatSearchParams(statSearchBO);
        } catch (RuntimeException e) {
            return ConResult.fail("失败：" + e.getMessage());
        }
        try {
            Date startDate = DateUtil.parseYYYYMMddHHmm(statSearchBO.getStart());
            Date endDate = DateUtil.parseYYYYMMddHHmm(statSearchBO.getEnd());
            List<InstanceStateLog> instanceStateLogList = instanceStateService.getInstanceStateLogByCluster(statSearchBO.getId(), startDate, endDate);
            AllTrendChartVO allTrendChartVO = ChartConvertUtil.convertAllInsTimeValueMap(instanceStateLogList, dimension);
            return ConResult.success(allTrendChartVO);
        } catch (ParseException e) {
            return ConResult.fail("时间转化失败：" + e.getMessage());
        }
    }

    /**
     * 检验历史信息查询参数
     *
     * @param statSearchBO 历史信息查询条件
     * @return
     */
    private boolean isLegalStatSearchParams(StatSearchBO statSearchBO) {
        if (statSearchBO == null) {
            throw new RuntimeException("搜索条件信息为空，请检查后重试。");
        } else if (statSearchBO.getId() == null) {
            throw new RuntimeException("集群/实例id不能为空，请检查后重试。");
        } else if (StringUtils.isBlank(statSearchBO.getStart())) {
            throw new RuntimeException("开始时间不能为空，请检查后重试。");
        } else if (StringUtils.isBlank(statSearchBO.getEnd())) {
            throw new RuntimeException("结束时间不能为空，请检查后重试。");
        }
        return true;
    }

    /**
     * 获取实例配置信息，conf四字命令
     *
     * @param instanceId 实例id
     * @return
     */
    private ZKServerConfigDTO getZkConfig(int instanceId) {
        CacheObject<ZKServerConfigDTO> cacheObject = zkServerConfigCache.get(instanceId);
        if (cacheObject == null || cacheObject.expired(ZK_CONFIG_EXPIRED)) {
            synchronized (zkServerConfigCache) {
                cacheObject = zkServerConfigCache.get(instanceId);
                if (cacheObject == null || cacheObject.expired(ZK_CONFIG_EXPIRED)) {
                    ZKServerConfigDTO config = null;
                    InstanceInfo instanceInfo = instanceService.getInstanceInfoById(instanceId);
                    if (instanceInfo != null) {
                        config = collectService.collectConfigInfo(instanceInfo.getHost(), instanceInfo.getPort());
                    }
                    cacheObject = new CacheObject<ZKServerConfigDTO>(config, System.currentTimeMillis());
                    zkServerConfigCache.put(instanceId, cacheObject);
                }
            }
        }
        return cacheObject.getObject();
    }

    /**
     * 获取实例连接信息，cons四字命令
     *
     * @param instanceId 实例id
     * @return
     */
    private List<ConnectionInfoDTO> getZkConnection(int instanceId) {
        CacheObject<List<ConnectionInfoDTO>> cacheObject = zkServerConnectionCache.get(instanceId);
        if (cacheObject == null || cacheObject.expired(ZK_CONNECTION_EXPIRED)) {
            synchronized (zkServerConnectionCache) {
                cacheObject = zkServerConnectionCache.get(instanceId);
                if (cacheObject == null || cacheObject.expired(ZK_CONNECTION_EXPIRED)) {
                    List<ConnectionInfoDTO> connections = null;
                    InstanceInfo instanceInfo = instanceService.getInstanceInfoById(instanceId);
                    if (instanceInfo != null) {
                        connections = collectService.collectConnectionInfo(instanceInfo.getHost(), instanceInfo.getPort());
                    }
                    cacheObject = new CacheObject<List<ConnectionInfoDTO>>(connections, System.currentTimeMillis());
                    zkServerConnectionCache.put(instanceId, cacheObject);
                }
            }
        }
        return cacheObject.getObject();
    }

    private boolean isLegalClientConnHistorySearchParams(ClientConnectionSearchBO clientConnectionSearchBO) {
        if (clientConnectionSearchBO == null) {
            throw new RuntimeException("搜索条件信息为空，请检查后重试。");
        } else if (clientConnectionSearchBO.getClusterId() == null) {
            throw new RuntimeException("集群id不能为空，请检查后重试。");
        } else if (clientConnectionSearchBO.getInstanceId() == null) {
            throw new RuntimeException("实例id不能为空，请检查后重试。");
        } else if (clientConnectionSearchBO.getStartDate() == null) {
            throw new RuntimeException("开始时间不能为空，请检查后重试。");
        } else if (clientConnectionSearchBO.getEndDate() == null) {
            throw new RuntimeException("结束时间不能为空，请检查后重试。");
        }
        return true;
    }
}
