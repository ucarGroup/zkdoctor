package com.ucar.zkdoctor.web.controller.manager;

import com.ucar.zkdoctor.pojo.bo.HostAndPort;
import com.ucar.zkdoctor.pojo.po.ClusterInfo;
import com.ucar.zkdoctor.pojo.vo.ClusterDeployVO;
import com.ucar.zkdoctor.pojo.vo.CreateNewZnodeVO;
import com.ucar.zkdoctor.pojo.vo.UpdateZnodeDataVO;
import com.ucar.zkdoctor.pojo.vo.ZKConfigInfoVO;
import com.ucar.zkdoctor.pojo.vo.ZKServerInfoVO;
import com.ucar.zkdoctor.service.cluster.ClusterService;
import com.ucar.zkdoctor.service.zk.ZKService;
import com.ucar.zkdoctor.util.constant.ClusterEnumClass.ClusterStatusEnum;
import com.ucar.zkdoctor.web.ConResult;
import com.ucar.zkdoctor.web.controller.BaseController;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 集群管理相关Controller，管理员权限功能
 * Created on 2018/1/9 9:46
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Controller
@RequestMapping("/manage/cluster")
public class ClusterManageController extends BaseController {

    @Resource
    private ClusterService clusterService;

    @Resource
    private ZKService zkService;

    /**
     * 增加新集群信息
     *
     * @param clusterInfo       集群基本信息
     * @param newClusterServers server实例信息
     * @return
     */
    @RequestMapping(value = "/addCluster", method = RequestMethod.POST)
    @ResponseBody
    public ConResult doAddCluster(ClusterInfo clusterInfo, String newClusterServers) {
        if (clusterInfo == null || StringUtils.isBlank(newClusterServers)) {
            return ConResult.fail("所填信息为NULL，请检查后重试。");
        }
        try {
            boolean addResult = clusterService.addNewCluster(clusterInfo, newClusterServers);
            if (addResult) {
                return ConResult.success();
            } else {
                return ConResult.fail("添加新集群信息失败，请查看相关日志。");
            }
        } catch (RuntimeException e) {
            return ConResult.fail("添加新集群信息失败：" + e.getMessage());
        }
    }

    /**
     * 修改集群信息
     *
     * @param clusterInfo 集群信息
     * @return
     */
    @RequestMapping(value = "/modifyCluster")
    @ResponseBody
    public ConResult doModifyCluster(ClusterInfo clusterInfo) {
        if (clusterInfo == null || clusterInfo.getId() == null) {
            return ConResult.fail("所需修改的信息为NULL，请重新尝试。");
        }
        boolean result = clusterService.updateClusterInfo(clusterInfo);
        if (result) {
            return new ConResult(true, "修改集群信息成功", null);
        } else {
            return ConResult.fail("修改失败，请重试！");
        }
    }

    /**
     * 集群自动化部署
     *
     * @param clusterDeployVO 集群部署
     * @return
     */
    @RequestMapping(value = "/clusterDeploy", method = RequestMethod.POST)
    @ResponseBody
    public ConResult doClusterDeploy(ClusterDeployVO clusterDeployVO, ZKConfigInfoVO zkConfigInfoVO) {
        try {
            isLegalClusterDeployParams(clusterDeployVO, zkConfigInfoVO);
        } catch (RuntimeException e) {
            return ConResult.fail("失败：" + e.getMessage());
        }
        try {
            List<HostAndPort> hostAndPortList = HostAndPort.parseHostAndPortList(zkConfigInfoVO.getServerConfig(), zkConfigInfoVO.getClientPort());
            boolean result = false;
            if (CollectionUtils.isNotEmpty(hostAndPortList)) {
                // 安装服务
                result = zkService.autoInstallServers(hostAndPortList, zkConfigInfoVO.toString(), clusterDeployVO.getInstallFileName(),
                        clusterDeployVO.getInstallFileDir(), zkConfigInfoVO.getDataDir(), clusterDeployVO.getDownloadSite());
            }
            if (result) {
                return ConResult.success();
            } else {
                return ConResult.fail("集群部署失败，请查看相关日志。");
            }
        } catch (RuntimeException e) {
            return ConResult.fail("集群部署失败：" + e.getMessage());
        }
    }

    /**
     * 校验服务部署参数
     *
     * @param clusterDeployVO 服务部署参数
     * @return
     */
    private boolean isLegalClusterDeployParams(ClusterDeployVO clusterDeployVO, ZKConfigInfoVO zkConfigInfoVO) {
        if (clusterDeployVO == null || zkConfigInfoVO == null) {
            throw new RuntimeException("部署信息为NULL，请检查后重试。");
        } else if (StringUtils.isBlank(clusterDeployVO.getClusterName()) || clusterService.getClusterInfoByClusterName(clusterDeployVO.getClusterName()) != null) {
            throw new RuntimeException("集群名称不能为空且不能重复，请检查后重试。");
        } else if (StringUtils.isBlank(clusterDeployVO.getInstallFileName())) {
            throw new RuntimeException("安装文件名称不能为空，请检查后重试。");
        } else if (StringUtils.isBlank(clusterDeployVO.getInstallFileDir())) {
            throw new RuntimeException("安装文件所存在的服务器目录不能为空，请检查后重试。");
        } else if (StringUtils.isBlank(clusterDeployVO.getDownloadSite())) {
            throw new RuntimeException("安装文件网址不能为空，请检查后重试。");
        } else if (StringUtils.isBlank(zkConfigInfoVO.getServerConfig())) {
            throw new RuntimeException("Server配置信息不能为空，请检查后重试。");
        } else if (StringUtils.isBlank(zkConfigInfoVO.getDataDir())) {
            throw new RuntimeException("dataDir配置信息不能为空，请检查后重试。");
        } else if (zkConfigInfoVO.getClientPort() == null) {
            throw new RuntimeException("客户端端口号不能为空，请检查后重试。");
        } else if (zkConfigInfoVO.getTickTime() == null) {
            throw new RuntimeException("tickTime不能为空，请检查后重试。");
        } else if (zkConfigInfoVO.getInitLimit() == null) {
            throw new RuntimeException("initLimit不能为空，请检查后重试。");
        } else if (zkConfigInfoVO.getSyncLimit() == null) {
            throw new RuntimeException("syncLimit不能为空，请检查后重试。");
        }
        return true;
    }

    /**
     * 动态扩容
     *
     * @param clusterId      集群id
     * @param zkServerInfoVO 动态扩容参数信息
     * @return
     */
    @RequestMapping(value = "/dynamicExpansion", method = RequestMethod.POST)
    @ResponseBody
    public ConResult doDynamicExpansion(Integer clusterId, ZKServerInfoVO zkServerInfoVO) {
        if (clusterId == null) {
            return ConResult.fail("集群id为NULL，请检查后重试。");
        }
        try {
            isLegalDynamicExpansionParams(zkServerInfoVO);
        } catch (RuntimeException e) {
            return ConResult.fail("失败：" + e.getMessage());
        }
        try {
            boolean result = zkService.dynamicExpansion(clusterId, zkServerInfoVO.toString(), zkServerInfoVO.getHost(), zkServerInfoVO.getClientPort());
            if (result) {
                return new ConResult(true, "动态扩容成功！", null);
            } else {
                return ConResult.fail("动态扩容失败，请查看相关日志。");
            }
        } catch (RuntimeException e) {
            return ConResult.fail("动态扩容失败：" + e.getMessage());
        }
    }

    /**
     * 获取集群动态扩容的结果信息
     *
     * @param clusterId 集群id
     * @return
     */
    @RequestMapping(value = "/getClusterDynamicExpansionResult", method = RequestMethod.POST)
    @ResponseBody
    public ConResult doGetClusterDynamicExpansionResult(Integer clusterId) {
        if (clusterId == null) {
            return ConResult.fail("集群id为NULL，请检查后重试。");
        }
        List<String> result = zkService.queryDynamicExpansionProcess(clusterId);
        // 扩容是否结束，如果结束则前端不再定时获取结果
        boolean isClear = false;
        for (String line : result) {
            if (!line.contains("正在")) {
                isClear = true;
                break;
            }
        }
        Map<String, Object> objectMap = new HashMap<String, Object>();
        objectMap.put("data", result);
        objectMap.put("isClear", isClear);
        return ConResult.success(objectMap);
    }

    /**
     * 获取集群重启的结果信息
     *
     * @param clusterId 集群id
     * @return
     */
    @RequestMapping(value = "/getClusterRestartResult", method = RequestMethod.POST)
    @ResponseBody
    public ConResult doGetClusterRestartResult(Integer clusterId) {
        if (clusterId == null) {
            return ConResult.fail("集群id为NULL，请检查后重试。");
        }
        List<String> result = zkService.queryRestartProcess(clusterId);
        // 重启是否结束，如果结束则前端不再定时获取结果
        boolean isClear = false;
        for (String line : result) {
            if (!line.contains("正在")) {
                isClear = true;
                break;
            }
        }
        Map<String, Object> objectMap = new HashMap<String, Object>();
        objectMap.put("data", result);
        objectMap.put("isClear", isClear);
        return ConResult.success(objectMap);
    }

    /**
     * 获取集群部署的结果信息
     *
     * @return
     */
    @RequestMapping("/getClusterDeployResult")
    @ResponseBody
    public ConResult doGetClusterDeployResult() {
        List<String> result = zkService.queryDeployProcess();
        // 部署过程是否结束，如果结束则前端不再定时获取结果
        boolean isClear = false;
        for (String line : result) {
            if (!line.contains("正在")) {
                isClear = true;
                break;
            }
        }
        Map<String, Object> objectMap = new HashMap<String, Object>();
        objectMap.put("data", result);
        objectMap.put("isClear", isClear);
        return ConResult.success(objectMap);
    }


    /**
     * 校验自动扩容参数
     *
     * @param zkServerInfoVO 自动扩容参数
     * @return
     */
    private boolean isLegalDynamicExpansionParams(ZKServerInfoVO zkServerInfoVO) {
        if (zkServerInfoVO == null) {
            throw new RuntimeException("动态扩容信息为NULL，请检查后重试。");
        } else if (zkServerInfoVO.getServerId() == null) {
            throw new RuntimeException("server id不能为空，请检查后重试。");
        } else if (StringUtils.isBlank(zkServerInfoVO.getHost())) {
            throw new RuntimeException("新机器ip不能为空，请检查后重试。");
        } else if (zkServerInfoVO.getClientPort() == null) {
            throw new RuntimeException("客户端端口不能为空，请检查后重试。");
        } else if (zkServerInfoVO.getQuorumPort() == null) {
            throw new RuntimeException("法人端口不能为空，请检查后重试。");
        } else if (zkServerInfoVO.getElectionPort() == null) {
            throw new RuntimeException("选举端口不能为空，请检查后重试。");
        } else if (StringUtils.isBlank(zkServerInfoVO.getPeerType())) {
            throw new RuntimeException("新节点角色类型不能为空，请检查后重试。");
        }
        return true;
    }

    /**
     * 下线集群
     *
     * @param clusterId 集群id
     * @return
     */
    @RequestMapping(value = "/offLine", method = RequestMethod.POST)
    @ResponseBody
    public ConResult doOffLine(Integer clusterId) {
        if (clusterId == null) {
            return ConResult.fail("集群id为NULL，请检查后重试。");
        }
        try {
            boolean result = zkService.offLineCluster(clusterId);
            if (result) {
                return new ConResult(true, "下线集群成功！", null);
            } else {
                return ConResult.fail("下线集群失败，请查看相关日志。");
            }
        } catch (RuntimeException e) {
            return ConResult.fail("下线集群失败：" + e.getMessage());
        }
    }

    /**
     * 重启集群
     *
     * @param clusterId   集群id
     * @param sleepTimeMs 验证数据同步时间间隔，单位：毫秒
     * @return
     */
    @RequestMapping(value = "/restartQuorum", method = RequestMethod.POST)
    @ResponseBody
    public ConResult doRestartQuorum(Integer clusterId, Integer sleepTimeMs) {
        if (clusterId == null || sleepTimeMs == null) {
            return ConResult.fail("集群id或所填为验证数据同步时间间隔为NULL，请检查后重试。");
        }
        try {
            boolean result = zkService.restartQuorumServer(clusterId, sleepTimeMs);
            if (result) {
                return new ConResult(true, "集群成功！", null);
            } else {
                return ConResult.fail("重启集群失败，请查看相关日志。");
            }
        } catch (RuntimeException e) {
            return ConResult.fail("重启集群失败：" + e.getMessage());
        }
    }

    /**
     * 启动/停止集群监控
     *
     * @param clusterId 集群id
     * @param status    true：开启集群监控，false：关闭集群监控
     * @return
     */
    @RequestMapping(value = "/updateMonitorStatus")
    @ResponseBody
    public ConResult doUpdateMonitorStatus(Integer clusterId, Boolean status) {
        if (clusterId == null || status == null) {
            return ConResult.fail("集群id为NULL，请检查后重试。");
        }
        try {
            int clusterStatus = status ? ClusterStatusEnum.RUNNING.getStatus() : ClusterStatusEnum.NOT_MONITORED.getStatus();
            boolean result = clusterService.updateClusterStatus(clusterId, clusterStatus);
            if (result) {
                return ConResult.success();
            } else {
                return ConResult.fail("修改集群监控状态失败，请查看相关日志。");
            }
        } catch (RuntimeException e) {
            return ConResult.fail("修改集群监控状态失败：" + e.getMessage());
        }
    }

    /**
     * 删除该节点以及其子节点
     *
     * @param clusterId 集群id
     * @param znode     需要删除的节点路径
     * @return
     */
    @RequestMapping(value = "/deleteZnode")
    @ResponseBody
    public ConResult doDeleteZnode(Integer clusterId, String znode) {
        if (clusterId == null || StringUtils.isBlank(znode)) {
            return ConResult.fail("集群id为NULL或所选节点为空，请检查后重试。");
        }
        boolean deleteResult = zkService.deleteZnodeAndChildren(clusterId, znode);
        if (deleteResult) {
            return new ConResult(true, "删除节点以及其子节点成功！" + znode, null);
        } else {
            return ConResult.fail("删除节点以及其子节点失败！" + znode);
        }
    }

    /**
     * 更新节点数据，目前只支持string类型数据
     *
     * @param clusterId         集群id
     * @param updateZnodeDataVO 更新数据参数
     * @return
     */
    @RequestMapping(value = "/updateZnode")
    @ResponseBody
    public ConResult doUpdateZnode(Integer clusterId, UpdateZnodeDataVO updateZnodeDataVO) {
        if (clusterId == null) {
            return ConResult.fail("集群id为NULL或所选节点为空，请检查后重试。");
        }
        try {
            isLegalUpdateZnodeParams(updateZnodeDataVO);
        } catch (RuntimeException e) {
            return ConResult.fail(e.getMessage());
        }
        try {
            boolean updateResult = zkService.updataZnodeData(clusterId, updateZnodeDataVO.getPath(),
                    updateZnodeDataVO.getData(), updateZnodeDataVO.getVersion());
            if (updateResult) {
                return new ConResult(true, "更新节点成功！" + updateZnodeDataVO.getPath(), null);
            } else {
                return ConResult.fail("更新节点失败！" + updateZnodeDataVO.getPath());
            }
        } catch (Exception e) {
            return ConResult.fail("更新节点失败！" + updateZnodeDataVO.getPath() + ":" + e.getMessage());
        }
    }

    /**
     * 检验更新节点数据参数
     *
     * @param updateZnodeDataVO 更新节点数据参数信息
     * @return
     */
    private boolean isLegalUpdateZnodeParams(UpdateZnodeDataVO updateZnodeDataVO) {
        if (updateZnodeDataVO == null) {
            throw new RuntimeException("更新节点信息为NULL，请检查后重试。");
        } else if (StringUtils.isBlank(updateZnodeDataVO.getPath())) {
            throw new RuntimeException("节点路径为空，请检查后重试。");
        } else if (updateZnodeDataVO.getData() == null) {
            throw new RuntimeException("数据为NULL，请检查后重试。");
        } else if (updateZnodeDataVO.getVersion() == null) {
            throw new RuntimeException("版本号不能为空，请检查后重试。");
        }
        return true;
    }

    /**
     * 创建新节点
     *
     * @param clusterId        集群id
     * @param createNewZnodeVO 新节点参数信息
     * @return
     */
    @RequestMapping(value = "/createZnode")
    @ResponseBody
    public ConResult doCreateZnodee(Integer clusterId, CreateNewZnodeVO createNewZnodeVO) {
        if (clusterId == null) {
            return ConResult.fail("集群id为NULL或所选节点为空，请检查后重试。");
        }
        try {
            isLegalCreateZnodeParams(createNewZnodeVO);
        } catch (RuntimeException e) {
            return ConResult.fail(e.getMessage());
        }
        try {
            boolean createResult = zkService.createNewZnode(clusterId,
                    createNewZnodeVO.getParentPath() + createNewZnodeVO.getChildPath(), createNewZnodeVO.getData(),
                    createNewZnodeVO.getCreateParentNeeded(), createNewZnodeVO.getAcl());
            if (createResult) {
                return new ConResult(true, "创建新节点成功！" + createNewZnodeVO.getParentPath() + createNewZnodeVO.getChildPath(), null);
            } else {
                return ConResult.fail("创建新节点失败！" + createNewZnodeVO.getParentPath() + createNewZnodeVO.getChildPath());
            }
        } catch (Exception e) {
            return ConResult.fail("创建新节点失败！" + createNewZnodeVO.getParentPath() + createNewZnodeVO.getChildPath() + ":" + e.getMessage());
        }
    }

    /**
     * 检验创建节点数据参数
     *
     * @param createNewZnodeVO 创建节点数据参数信息
     * @return
     */
    private boolean isLegalCreateZnodeParams(CreateNewZnodeVO createNewZnodeVO) {
        if (createNewZnodeVO == null) {
            throw new RuntimeException("创建节点信息为NULL，请检查后重试。");
        } else if (StringUtils.isBlank(createNewZnodeVO.getParentPath())) {
            throw new RuntimeException("父节点路径为空，请检查后重试。");
        } else if (StringUtils.isBlank(createNewZnodeVO.getChildPath())) {
            throw new RuntimeException("子节点路径为空，请检查后重试。");
        } else if (createNewZnodeVO.getData() == null) {
            throw new RuntimeException("数据为NULL，请检查后重试。");
        } else if (createNewZnodeVO.getCreateParentNeeded() == null) {
            throw new RuntimeException("是否创建不存在的父节点条件为NULL，请检查后重试。");
        } else if (StringUtils.isBlank(createNewZnodeVO.getAcl())) {
            throw new RuntimeException("ACL条件为NULL，请检查后重试。");
        }
        return true;
    }

}
