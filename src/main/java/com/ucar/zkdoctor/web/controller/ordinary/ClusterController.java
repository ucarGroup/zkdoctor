package com.ucar.zkdoctor.web.controller.ordinary;

import com.ucar.zkdoctor.pojo.bo.ClusterInfoSearchBO;
import com.ucar.zkdoctor.pojo.bo.StatSearchBO;
import com.ucar.zkdoctor.pojo.po.ClusterAlarmUser;
import com.ucar.zkdoctor.pojo.po.ClusterInfo;
import com.ucar.zkdoctor.pojo.po.ClusterState;
import com.ucar.zkdoctor.pojo.po.ServiceLine;
import com.ucar.zkdoctor.pojo.po.User;
import com.ucar.zkdoctor.pojo.vo.ClusterDetailVO;
import com.ucar.zkdoctor.pojo.vo.ClusterListVO;
import com.ucar.zkdoctor.pojo.vo.PageVO;
import com.ucar.zkdoctor.pojo.vo.TrendChartVO;
import com.ucar.zkdoctor.pojo.vo.ZnodeDetailInfoVO;
import com.ucar.zkdoctor.pojo.vo.ZnodeTreeNodeVO;
import com.ucar.zkdoctor.service.cluster.ClusterService;
import com.ucar.zkdoctor.service.cluster.ClusterStateService;
import com.ucar.zkdoctor.service.system.ServiceLineService;
import com.ucar.zkdoctor.service.zk.ZKService;
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
 * Description: 集群Controller，普通用户权限相关操作
 * Created on 2018/1/9 10:55
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Controller
@RequestMapping("/cluster")
public class ClusterController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterController.class);

    @Resource
    private ClusterService clusterService;

    @Resource
    private ClusterStateService clusterStateService;

    @Resource
    private ZKService zkService;

    @Resource
    private ServiceLineService serviceLineService;

    /**
     * 查询符合条件的集群信息
     *
     * @param clusterInfoSearchBO 查询条件
     * @return
     */
    @RequestMapping("/query")
    @ResponseBody
    public ConResult doQuery(ClusterInfoSearchBO clusterInfoSearchBO) {
        List<ClusterListVO> clusterListVOList = clusterService.searchForClusterListVO(clusterInfoSearchBO);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", clusterListVOList == null ? new ArrayList<ClusterListVO>() : clusterListVOList);
        PageVO pageVO = new PageVO();
        if (clusterListVOList != null) {
            pageVO.setTotal(clusterListVOList.size());
        }
        result.put("page", pageVO);
        return ConResult.success(result);
    }

    /**
     * 查询某个集群信息
     *
     * @param clusterId 集群id
     * @return
     */
    @RequestMapping("/info")
    @ResponseBody
    public ConResult doQueryClusterInfo(Integer clusterId) {
        if (clusterId == null) {
            return ConResult.fail("集群id为NULL，请重新尝试。");
        }
        ClusterInfo clusterInfo = clusterService.getClusterInfoById(clusterId);
        return ConResult.success(clusterInfo);
    }

    /**
     * 查询集群详细信息
     *
     * @param clusterId 集群id
     * @return
     */
    @RequestMapping("/detail")
    @ResponseBody
    public ConResult doQueryClusterDetail(Integer clusterId) {
        if (clusterId == null) {
            return ConResult.fail("集群id为NULL，请重新尝试。");
        }
        ClusterDetailVO clusterDetailVO = clusterService.getClusterDetailVOByClusterId(clusterId);
        return ConResult.success(clusterDetailVO);
    }

    /**
     * 获取集群下的所有配置的用户信息
     *
     * @param clusterId 集群id
     * @return
     */
    @RequestMapping(value = "/clusterAlarmUsers")
    @ResponseBody
    public ConResult getClusterAlarmUsers(Integer clusterId) {
        if (clusterId == null) {
            return ConResult.fail("集群id为NULL，请重新尝试。");
        }
        try {
            List<User> list = clusterService.clusterAlarmUsers(clusterId);
            if (list != null) {
                return ConResult.success(list);
            }
            return ConResult.success(new ArrayList<User>());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ConResult.fail("查询集群下的用户信息失败，请查看相关日志。");
        }
    }

    /**
     * 新增报警用户信息
     *
     * @param clusterAlarmUser 集群id与用户id
     * @return
     */
    @RequestMapping(value = "/addClusterAlarmUser")
    @ResponseBody
    public ConResult doAddClusterAlarmUser(ClusterAlarmUser clusterAlarmUser) {
        if (clusterAlarmUser == null || clusterAlarmUser.getClusterId() == null || clusterAlarmUser.getUserId() == null) {
            return ConResult.fail("所选信息为NULL，请重新尝试。");
        }
        boolean result = clusterService.addClusterAlarmUser(clusterAlarmUser);
        if (result) {
            return ConResult.success("添加成功！");
        } else {
            return ConResult.fail("添加失败，请重试！");
        }
    }

    /**
     * 删除报警用户信息
     *
     * @param clusterAlarmUser 集群id与用户id
     * @return
     */
    @RequestMapping(value = "/deleteAlarmUser")
    @ResponseBody
    public ConResult doDeleteAlarmUser(ClusterAlarmUser clusterAlarmUser) {
        if (clusterAlarmUser == null || clusterAlarmUser.getClusterId() == null || clusterAlarmUser.getUserId() == null) {
            return ConResult.fail("所需删除的信息为NULL，请重新尝试。");
        }
        boolean result = clusterService.deleteAlarmUser(clusterAlarmUser.getClusterId(), clusterAlarmUser.getUserId());
        if (result) {
            return ConResult.success("删除成功！");
        } else {
            return ConResult.fail("删除失败，请重试！");
        }
    }

    /**
     * 检查该集群名称是否存在
     *
     * @param clusterName 集群名称
     * @return
     */
    @RequestMapping(value = "/checkClusterNameExist")
    @ResponseBody
    public ConResult doCheckClusterNameExist(String clusterName) {
        if (StringUtils.isBlank(clusterName)) {
            return ConResult.fail("集群名称不能为空");
        }
        ClusterInfo clusterInfo = clusterService.getClusterInfoByClusterName(clusterName);
        if (clusterInfo == null) {
            return ConResult.success(false);
        } else {
            return ConResult.success(true);
        }
    }

    /**
     * 初始化拉取集群所有状态信息
     *
     * @param statSearchBO 集群历史信息查询条件
     * @return
     */
    @RequestMapping(value = "/stat/init/trend")
    @ResponseBody
    public ConResult doClusterpStatInit(StatSearchBO statSearchBO) {
        return getClusterStateTrend(statSearchBO, null, true);
    }

    /**
     * 获取收包数历史信息
     *
     * @param statSearchBO 集群历史信息查询条件
     * @return
     */
    @RequestMapping(value = "/stat/received/trend")
    @ResponseBody
    public ConResult doClusterStatReceived(StatSearchBO statSearchBO) {
        return getClusterStateTrend(statSearchBO, ChartConvertUtil.STATE_RECEIVED, false);
    }

    /**
     * 获取堆积请求数历史信息
     *
     * @param statSearchBO 集群历史信息查询条件
     * @return
     */
    @RequestMapping(value = "/stat/outstandings/trend")
    @ResponseBody
    public ConResult doClusterStatOutstandings(StatSearchBO statSearchBO) {
        return getClusterStateTrend(statSearchBO, ChartConvertUtil.STATE_OUTSTANDINGS, false);
    }

    /**
     * 获取集群最大延时历史信息
     *
     * @param statSearchBO 集群历史信息查询条件
     * @return
     */
    @RequestMapping(value = "/stat/maxLatencyMax/trend")
    @ResponseBody
    public ConResult doClusterStaMaxLatencyMax(StatSearchBO statSearchBO) {
        return getClusterStateTrend(statSearchBO, ChartConvertUtil.STATE_MAXLATENCY, false);
    }

    /**
     * 获取实例平均延时最大值历史信息
     *
     * @param statSearchBO 集群历史信息查询条件
     * @return
     */
    @RequestMapping(value = "/stat/avgLatencyMax/trend")
    @ResponseBody
    public ConResult doClusterStatAvgLatencyMax(StatSearchBO statSearchBO) {
        return getClusterStateTrend(statSearchBO, ChartConvertUtil.STATE_AVGLATENCY, false);
    }

    /**
     * 获取节点数历史信息
     *
     * @param statSearchBO 集群历史信息查询条件
     * @return
     */
    @RequestMapping(value = "/stat/znodeCount/trend")
    @ResponseBody
    public ConResult doClusterStatZnodeCount(StatSearchBO statSearchBO) {
        return getClusterStateTrend(statSearchBO, ChartConvertUtil.STATE_ZNODECOUNT, false);
    }

    /**
     * 获取临时节点数历史信息
     *
     * @param statSearchBO 集群历史信息查询条件
     * @return
     */
    @RequestMapping(value = "/stat/ephemerals/trend")
    @ResponseBody
    public ConResult doClusterStatEphemerals(StatSearchBO statSearchBO) {
        return getClusterStateTrend(statSearchBO, ChartConvertUtil.STATE_EPHEMERIALS, false);
    }

    /**
     * 获取watcher数历史信息
     *
     * @param statSearchBO 集群历史信息查询条件
     * @return
     */
    @RequestMapping(value = "/stat/watcherTotal/trend")
    @ResponseBody
    public ConResult doClusterStatWatcherTotal(StatSearchBO statSearchBO) {
        return getClusterStateTrend(statSearchBO, ChartConvertUtil.STATE_WATCHERCOUNT, false);
    }

    /**
     * 获取总连接数历史信息
     *
     * @param statSearchBO 集群历史信息查询条件
     * @return
     */
    @RequestMapping(value = "/stat/connectionTotal/trend")
    @ResponseBody
    public ConResult doClusterStatConnectionTotal(StatSearchBO statSearchBO) {
        return getClusterStateTrend(statSearchBO, ChartConvertUtil.STATE_CONNECTIONS, false);
    }

    /**
     * 初始化获取所有根节点/下的节点信息
     *
     * @param clusterId 集群id
     * @return
     */
    @RequestMapping(value = "/clusterRootZnodes")
    @ResponseBody
    public ConResult doInitRootZnodes(Integer clusterId) {
        if (clusterId == null) {
            return ConResult.fail("集群id为NULL，请重新尝试。");
        }
        try {
            List<ZnodeTreeNodeVO> znodeTreeNodeVOList = zkService.initZnodesTree(clusterId);
            return ConResult.success(znodeTreeNodeVOList);
        } catch (RuntimeException e) {
            return ConResult.fail(e.getMessage());
        }
    }

    /**
     * 展示给定节点的子节点信息
     *
     * @param clusterId 集群id
     * @param znode     节点路径
     * @return
     */
    @RequestMapping(value = "/clusterZnodesChildren")
    @ResponseBody
    public ConResult doGetZnodesChildren(Integer clusterId, String znode) {
        if (clusterId == null) {
            return ConResult.fail("集群id为NULL，请重新尝试。");
        }
        try {
            List<ZnodeTreeNodeVO> znodeTreeNodeVOList = zkService.initNodesChildren(clusterId, znode);
            return ConResult.success(znodeTreeNodeVOList);
        } catch (RuntimeException e) {
            return ConResult.fail(e.getMessage());
        }
    }

    /**
     * 查询节点详细信息，包括数据，默认走string序列化方式
     *
     * @param clusterId 集群id
     * @param znode     节点路径
     * @return
     */
    @RequestMapping(value = "/searchZnodeData")
    @ResponseBody
    public ConResult doSearchZnodeData(Integer clusterId, String znode, String serializable) {
        if (clusterId == null) {
            return ConResult.fail("集群id为NULL，请重新尝试。");
        }
        // 目前只支持string以及hessian序列化方式
        boolean isStringSerializable = true;
        if ("hessian".equals(serializable)) {
            isStringSerializable = false;
        }
        try {
            ZnodeDetailInfoVO znodeDetailInfoVO = zkService.searchDataForZnode(clusterId, znode, isStringSerializable);
            return ConResult.success(znodeDetailInfoVO);
        } catch (RuntimeException e) {
            return ConResult.fail("获取数据失败：" + e.getMessage());
        }
    }

    /**
     * 获取业务线信息
     *
     * @return
     */
    @RequestMapping(value = "/serviceLine/query")
    @ResponseBody
    public ConResult doServiceLineQuery() {
        try {
            List<ServiceLine> serviceLineList = serviceLineService.getAllServiceLine();
            serviceLineList = serviceLineList == null ? new ArrayList<ServiceLine>() : serviceLineList;
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("data", serviceLineList);
            PageVO pageVO = new PageVO();
            pageVO.setTotal(serviceLineList.size());
            result.put("page", pageVO);
            return ConResult.success(result);
        } catch (RuntimeException e) {
            return ConResult.fail("获取业务线信息失败：" + e.getMessage());
        }
    }

    /**
     * 获取对应的集群状态历史运行趋势
     *
     * @param statSearchBO 集群历史信息查询条件
     * @param dimension    集群状态指标维度
     * @param isAll        是否获取所有历史状态信息
     * @return
     */
    private ConResult getClusterStateTrend(StatSearchBO statSearchBO, String dimension, boolean isAll) {
        try {
            isLegalClusterStatSearchParams(statSearchBO);
        } catch (RuntimeException e) {
            return ConResult.fail("失败：" + e.getMessage());
        }
        try {
            Date startDate = DateUtil.parseYYYYMMddHHmm(statSearchBO.getStart());
            Date endDate = DateUtil.parseYYYYMMddHHmm(statSearchBO.getEnd());
            List<ClusterState> clusterStateList = clusterStateService.getClusterStateLogByClusterId(statSearchBO.getId(), startDate, endDate);
            if (isAll) {
                Map<String, Object> clusterStateChartData = ChartConvertUtil.convertClusterInitState(clusterStateList);
                return ConResult.success(clusterStateChartData);
            } else {
                TrendChartVO clusterStateChartData = ChartConvertUtil.convertClusterTimeValueMap(clusterStateList, dimension);
                return ConResult.success(clusterStateChartData);
            }
        } catch (ParseException e) {
            return ConResult.fail("时间转化失败：" + e.getMessage());
        }
    }

    /**
     * 检验集群历史信息查询参数
     *
     * @param statSearchBO 集群历史信息查询条件
     * @return
     */
    private boolean isLegalClusterStatSearchParams(StatSearchBO statSearchBO) {
        if (statSearchBO == null) {
            throw new RuntimeException("搜索条件信息为空，请检查后重试。");
        } else if (statSearchBO.getId() == null) {
            throw new RuntimeException("集群id不能为空，请检查后重试。");
        } else if (StringUtils.isBlank(statSearchBO.getStart())) {
            throw new RuntimeException("开始时间不能为空，请检查后重试。");
        } else if (StringUtils.isBlank(statSearchBO.getEnd())) {
            throw new RuntimeException("结束时间不能为空，请检查后重试。");
        }
        return true;
    }

}
