package com.ucar.zkdoctor.service.cluster.impl;

import com.ucar.zkdoctor.dao.mysql.ClusterAlarmUserDao;
import com.ucar.zkdoctor.dao.mysql.ClusterInfoDao;
import com.ucar.zkdoctor.pojo.bo.ClusterInfoSearchBO;
import com.ucar.zkdoctor.pojo.bo.HostAndPort;
import com.ucar.zkdoctor.pojo.po.ClusterAlarmUser;
import com.ucar.zkdoctor.pojo.po.ClusterInfo;
import com.ucar.zkdoctor.pojo.po.ClusterState;
import com.ucar.zkdoctor.pojo.po.User;
import com.ucar.zkdoctor.pojo.vo.ClusterDetailVO;
import com.ucar.zkdoctor.pojo.vo.ClusterListVO;
import com.ucar.zkdoctor.service.cluster.ClusterService;
import com.ucar.zkdoctor.service.cluster.ClusterStateService;
import com.ucar.zkdoctor.service.collection.CollectService;
import com.ucar.zkdoctor.service.instance.InstanceService;
import com.ucar.zkdoctor.service.instance.impl.InstanceServiceImpl;
import com.ucar.zkdoctor.service.machine.MachineService;
import com.ucar.zkdoctor.service.monitor.MonitorService;
import com.ucar.zkdoctor.service.user.UserService;
import com.ucar.zkdoctor.util.constant.ClusterEnumClass.ClusterStatusEnum;
import com.ucar.zkdoctor.util.tool.DateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description: 集群服务接口实现类
 * Created on 2018/1/9 17:07
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Service
public class ClusterServiceImpl implements ClusterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InstanceServiceImpl.class);

    @Resource
    private ClusterInfoDao clusterInfoDao;

    @Resource
    private ClusterStateService clusterStateService;

    @Resource
    private UserService userService;

    @Resource
    private ClusterAlarmUserDao clusterAlarmUserDao;

    @Resource
    private MachineService machineService;

    @Resource
    private InstanceService instanceService;

    @Resource
    private CollectService collectService;

    @Resource
    private MonitorService monitorService;

    @Override
    public boolean insertClusterInfo(ClusterInfo clusterInfo) {
        if (clusterInfo == null) {
            return false;
        }
        try {
            clusterInfo.setStatus(ClusterStatusEnum.NOT_MONITORED.getStatus());
            clusterInfo.setCreateTime(new Date());
            return clusterInfoDao.insertClusterInfo(clusterInfo);
        } catch (Exception e) {
            LOGGER.error("Insert new clusterInfo {} failed.", clusterInfo, e);
            return false;
        }
    }

    @Override
    public ClusterInfo getClusterInfoById(int id) {
        return clusterInfoDao.getClusterInfoById(id);
    }

    @Override
    public ClusterInfo getClusterInfoByClusterName(String clusterName) {
        return clusterInfoDao.getClusterInfoByClusterName(clusterName);
    }

    @Override
    public List<ClusterInfo> getAllClusterInfos() {
        return clusterInfoDao.getAllClusterInfos();
    }

    @Override
    public List<ClusterInfo> getAllMonitoringClusterInfos() {
        return clusterInfoDao.getAllMonitoringClusterInfos();
    }

    @Override
    public int getClusterInfosTotalCount() {
        return clusterInfoDao.getClusterInfosTotalCount();
    }

    @Override
    public List<ClusterInfo> getAllClusterInfosByParams(ClusterInfoSearchBO clusterInfoSearchBO) {
        return clusterInfoDao.getAllClusterInfosByParams(clusterInfoSearchBO);
    }

    @Override
    public boolean updateClusterInfo(ClusterInfo clusterInfo) {
        if (clusterInfo == null) {
            return false;
        }
        clusterInfo.setModifyTime(new Date());
        clusterInfoDao.updateClusterInfo(clusterInfo);
        return true;
    }

    @Override
    public boolean updateClusterStatus(int id, int status) {
        boolean result = false;
        if (status == ClusterStatusEnum.RUNNING.getStatus()) { // 启用监控
            result = collectService.deployZKCollection(id);
            if (!result) {
                LOGGER.warn("Update cluster {} status to {}, deploy ZKCollection failed!", id, status);
            }
            // 开启所有监控任务
            monitorService.updateClusterTaskSwitchOn(id, true);
        } else if (status == ClusterStatusEnum.NOT_MONITORED.getStatus() ||
                status == ClusterStatusEnum.OFFLINE.getStatus()) { // 关闭监控或者下线集群
            result = collectService.unDeployZKCollection(id);
            if (!result) {
                LOGGER.warn("Update cluster {} status to {}, unDeploy ZKCollection failed!", id, status);
            }
            // 关闭所有监控任务
            monitorService.updateClusterTaskSwitchOn(id, false);
        }
        if (result) {
            return clusterInfoDao.updateClusterStatus(id, status);
        } else {
            return result;
        }
    }

    @Override
    public boolean deleteClusterInfoByClusterId(int id) {
        return clusterInfoDao.deleteClusterInfoByClusterId(id);
    }

    @Override
    public boolean deleteClusterInfoByClusterName(String clusterName) {
        return clusterInfoDao.deleteClusterInfoByClusterName(clusterName);
    }

    @Override
    public List<ClusterListVO> searchForClusterListVO(ClusterInfoSearchBO clusterInfoSearchBO) {
        List<ClusterInfo> clusterInfoList = getAllClusterInfosByParams(clusterInfoSearchBO);
        if (CollectionUtils.isEmpty(clusterInfoList)) {
            return null;
        }
        List<ClusterListVO> clusterListVOList = new ArrayList<ClusterListVO>();
        for (ClusterInfo clusterInfo : clusterInfoList) {
            ClusterListVO clusterListVO = new ClusterListVO();
            clusterListVO.setClusterId(clusterInfo.getId());
            clusterListVO.setClusterName(clusterInfo.getClusterName());
            clusterListVO.setIntro(clusterInfo.getIntro());
            clusterListVO.setStatus(clusterInfo.getStatus());
            clusterListVO.setServiceLine(clusterInfo.getServiceLine());
            clusterListVO.setVersion(clusterInfo.getVersion());
            clusterListVO.setClusterInfo(clusterInfo);

            clusterListVOList.add(clusterListVO);

            ClusterState clusterState = clusterStateService.getClusterStateByClusterId(clusterInfo.getId());
            if (clusterState == null) {
                continue;
            }
            clusterListVO.setZnodeCount(clusterState.getZnodeCount());
            clusterListVO.setRequestCount(clusterState.getReceivedTotal());
            clusterListVO.setConnections(clusterState.getConnectionTotal());
            Date time = clusterState.getModifyTime() == null ? clusterState.getCreateTime() : clusterState.getModifyTime();
            if (time != null) {
                clusterListVO.setCollectTime(DateUtil.formatYYYYMMddHHMMss(time));
            }
        }
        return clusterListVOList;
    }

    @Override
    public ClusterDetailVO getClusterDetailVOByClusterId(int clusterId) {
        ClusterDetailVO clusterDetailVO = new ClusterDetailVO();
        clusterDetailVO.setClusterInfo(getClusterInfoById(clusterId));
        clusterDetailVO.setClusterState(clusterStateService.getClusterStateByClusterId(clusterId));
        return clusterDetailVO;
    }

    @Override
    public List<User> clusterAlarmUsers(int clusterId) {
        List<ClusterAlarmUser> clusterAlarmUserList = getUserIdsByClusterId(clusterId);
        if (CollectionUtils.isEmpty(clusterAlarmUserList)) {
            return null;
        }
        List<User> userList = new ArrayList<User>();
        for (ClusterAlarmUser clusterAlarmUser : clusterAlarmUserList) {
            User user = userService.getUserById(clusterAlarmUser.getUserId());
            if (user == null) {
                continue;
            }
            userList.add(user);
        }
        return userList;
    }

    @Override
    public boolean addClusterAlarmUser(ClusterAlarmUser clusterAlarmUser) {
        if (clusterAlarmUser == null) {
            return false;
        }
        try {
            return clusterAlarmUserDao.insertClusterAlarmUser(clusterAlarmUser);
        } catch (Exception e) {
            LOGGER.error("Insert new cluster to user {} failed.", clusterAlarmUser, e);
            return false;
        }
    }

    @Override
    public List<ClusterAlarmUser> getUserIdsByClusterId(int clusterId) {
        return clusterAlarmUserDao.getUserIdsByClusterId(clusterId);
    }

    @Override
    public List<ClusterAlarmUser> getClusterIdsByUserId(int userId) {
        return clusterAlarmUserDao.getClusterIdsByUserId(userId);
    }

    @Override
    public boolean deleteAlarmUser(int clusterId, int userId) {
        return clusterAlarmUserDao.deleteAlarmUser(clusterId, userId);
    }

    @Override
    public boolean addNewCluster(ClusterInfo clusterInfo, String newClusterServers) {
        List<HostAndPort> hostAndPortList = generateClusterServersList(newClusterServers);
        if (CollectionUtils.isEmpty(hostAndPortList)) {
            throw new RuntimeException("所填服务器host列表信息格式有误，请检查。");
        }
        // 校验机器以及端口信息
        boolean saveMachine = machineService.batchInsertNotExistsMachine(hostAndPortList);
        if (!saveMachine) {
            throw new RuntimeException("保存服务器host列表信息失败，请检查。");
        }
        boolean checkExistsInstance = instanceService.existsInstances(hostAndPortList);
        if (checkExistsInstance) {
            throw new RuntimeException("所填写的服务器host列表信息中存在已经被其他集群占用的服务器，请检查。");
        }
        // 保存新的集群信息
        boolean insertClusterInfo = insertClusterInfo(clusterInfo);
        if (!insertClusterInfo) {
            throw new RuntimeException("保存新的集群信息失败，请检查。");
        }
        // 保存对应的实例信息
        boolean saveInstanceInfo = instanceService.batchinsertInstanceForCluster(clusterInfo.getId(), hostAndPortList);
        if (!saveInstanceInfo) {
            // 回退已经保存的应用信息
            boolean rollback = deleteClusterInfoByClusterId(clusterInfo.getId());
            LOGGER.warn("Add new cluster failed, and roll back for save new cluster, result:{}", rollback);
            throw new RuntimeException("保存实例信息失败，请检查。");
        }
        // 部署收集信息任务
        boolean deployZKCollect = collectService.deployZKCollection(clusterInfo.getId());
        if (!deployZKCollect) {
            // 回退已经保存的应用以及实例信息
            boolean rollbackCluster = deleteClusterInfoByClusterId(clusterInfo.getId());
            LOGGER.warn("Add new cluster failed, and roll back for save new cluster, result:{}", rollbackCluster);
            boolean rollbackInstance = instanceService.deleteInstancesByClusterId(clusterInfo.getId());
            LOGGER.warn("Add new cluster failed, and roll back for save new instances, result:{}", rollbackInstance);
            throw new RuntimeException("部署信息收集任务失败，请检查。");
        }
        updateClusterStatus(clusterInfo.getId(), ClusterStatusEnum.RUNNING.getStatus());
        // 部署监控报警任务
        return monitorService.batchSaveMonitorTask(clusterInfo.getId());
    }

    @Override
    public boolean deleteAllAlarmUser(int userId) {
        return clusterAlarmUserDao.deleteAllAlarmUser(userId);
    }

    /**
     * 将输入的服务器host列表转化为HostAndPort列表
     *
     * @param newClusterServers server实例信息字符串
     * @return
     */
    private List<HostAndPort> generateClusterServersList(String newClusterServers) {
        return HostAndPort.transformToHostAndPortList(newClusterServers);
    }
}
