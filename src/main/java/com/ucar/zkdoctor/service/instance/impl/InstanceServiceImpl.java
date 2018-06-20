package com.ucar.zkdoctor.service.instance.impl;

import com.ucar.zkdoctor.dao.mysql.InstanceInfoDao;
import com.ucar.zkdoctor.pojo.bo.AddNewConfigFileBO;
import com.ucar.zkdoctor.pojo.bo.HostAndPort;
import com.ucar.zkdoctor.pojo.bo.InstanceInfoSearchBO;
import com.ucar.zkdoctor.pojo.po.InstanceInfo;
import com.ucar.zkdoctor.pojo.po.MachineInfo;
import com.ucar.zkdoctor.pojo.vo.InstanceDetailVO;
import com.ucar.zkdoctor.pojo.vo.UploadedJarFileVO;
import com.ucar.zkdoctor.service.instance.InstanceService;
import com.ucar.zkdoctor.service.instance.InstanceStateService;
import com.ucar.zkdoctor.service.machine.MachineService;
import com.ucar.zkdoctor.service.zk.ZKService;
import com.ucar.zkdoctor.util.config.ModifiableConfig;
import com.ucar.zkdoctor.util.constant.ZKServerEnumClass.DeployTypeEnum;
import com.ucar.zkdoctor.util.exception.SSHException;
import com.ucar.zkdoctor.util.ssh.SSHUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description: 实例服务接口实现类
 * Created on 2018/1/9 17:08
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Service
public class InstanceServiceImpl implements InstanceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InstanceServiceImpl.class);

    @Resource
    private InstanceInfoDao instanceInfoDao;

    @Resource
    private MachineService machineService;

    @Resource
    private ZKService zkService;

    @Resource
    private InstanceStateService instanceStateService;

    @Override
    public boolean insertInstance(InstanceInfo instanceInfo) {
        if (instanceInfo == null) {
            return false;
        }
        try {
            instanceInfo.setConnectionMonitor(false);
            instanceInfo.setCreateTime(new Date());
            return instanceInfoDao.insertInstance(instanceInfo);
        } catch (Exception e) {
            LOGGER.error("Insert new instance {} failed.", instanceInfo, e);
            return false;
        }
    }

    @Override
    public InstanceInfo getInstanceInfoById(int id) {
        return instanceInfoDao.getInstanceInfoById(id);
    }

    @Override
    public InstanceInfo getInstanceByHostAndPort(String host, int port) {
        if (StringUtils.isBlank(host)) {
            return null;
        }
        return instanceInfoDao.getInstanceByHostAndPort(host, port);
    }

    @Override
    public List<InstanceInfo> getInstancesByClusterId(int clusterId) {
        return instanceInfoDao.getInstancesByClusterId(clusterId);
    }

    @Override
    public List<InstanceInfo> getInstancesByStatus(int status) {
        return instanceInfoDao.getInstanceInfoByStatus(status);
    }

    @Override
    public List<InstanceInfo> getAllOnLineInstancesByClusterId(int clusterId) {
        return instanceInfoDao.getAllOnLineInstancesByClusterId(clusterId);
    }

    @Override
    public List<InstanceInfo> getAllInstances() {
        return instanceInfoDao.getAllInstances();
    }

    @Override
    public List<InstanceInfo> getByMachineId(int machineId) {
        return instanceInfoDao.getInstancesByMachineId(machineId);
    }

    @Override
    public List<InstanceInfo> getAllInstancesByParams(InstanceInfoSearchBO instanceInfoSearchBO) {
        return instanceInfoDao.getAllInstancesByParams(instanceInfoSearchBO);
    }

    @Override
    public boolean updateInstance(InstanceInfo instanceInfo) {
        if (instanceInfo == null) {
            return false;
        }
        instanceInfo.setModifyTime(new Date());
        instanceInfoDao.updateInstance(instanceInfo);
        return true;
    }

    @Override
    public boolean updateInstanceStatus(int id, int status) {
        return instanceInfoDao.updateInstanceStatus(id, status);
    }

    @Override
    public boolean updateInstanceServerStateLag(int id, int serverStateLag) {
        return instanceInfoDao.updateInstanceServerStateLag(id, serverStateLag);
    }

    @Override
    public boolean deleteInstanceById(int id) {
        return instanceInfoDao.deleteInstanceById(id);
    }

    @Override
    public boolean deleteInstancesByClusterId(int clusterId) {
        return instanceInfoDao.deleteInstancesByClusterId(clusterId);
    }

    @Override
    public int getInstanceCountByClusterId(int clusterId) {
        return instanceInfoDao.getInstanceCountByClusterId(clusterId);
    }

    @Override
    public int getInstanceTotalCount() {
        return instanceInfoDao.getInstanceTotalCount();
    }

    @Override
    public List<InstanceInfo> getNormalInstancesByClusterId(int clusterId) {
        return instanceInfoDao.getNormalInstancesByClusterId(clusterId);
    }

    @Override
    public int getMonitoringCount() {
        return instanceInfoDao.getMonitoringCount();
    }

    @Override
    public boolean existsInstances(List<HostAndPort> hostAndPortList) {
        if (CollectionUtils.isEmpty(hostAndPortList)) {
            return false;
        }
        for (HostAndPort hostAndPort : hostAndPortList) {
            if (existsInstances(hostAndPort)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean existsInstances(HostAndPort hostAndPort) {
        InstanceInfo existInfo = getInstanceByHostAndPort(hostAndPort.getHost(), hostAndPort.getPort());
        return existInfo != null;
    }

    @Override
    public boolean batchinsertInstanceForCluster(int clusterId, List<HostAndPort> hostAndPortList) {
        if (CollectionUtils.isEmpty(hostAndPortList)) {
            return false;
        }
        for (HostAndPort hostAndPort : hostAndPortList) {
            try {
                insertInstanceForCluster(clusterId, hostAndPort.getHost(), hostAndPort.getPort());
            } catch (RuntimeException e) {
                LOGGER.error("Add instance {}:{} for cluster {} error.", hostAndPort.getHost(), hostAndPort.getPort(), clusterId, e);
                throw e;
            }
        }
        return true;
    }

    @Override
    public boolean insertInstanceForCluster(int clusterId, String host, int port) {
        InstanceInfo instanceInfo = generateNewInstanceInfo(host, port);
        instanceInfo.setClusterId(clusterId);
        insertInstanceForCluster(clusterId, instanceInfo);
        return true;
    }

    @Override
    public boolean insertInstanceForCluster(int clusterId, InstanceInfo instanceInfo) {
        // 检验当前实例状态
        int status = zkService.checkZKStatus(instanceInfo.getHost(), instanceInfo.getPort());
        instanceInfo.setStatus(status);
        return insertInstance(instanceInfo);
    }

    @Override
    public InstanceInfo generateNewInstanceInfo(String host, int port) {
        MachineInfo machineInfo = machineService.getMachineInfoByHost(host);
        if (machineInfo != null) {
            InstanceInfo instanceInfo = new InstanceInfo();
            instanceInfo.setHost(host);
            instanceInfo.setPort(port);
            instanceInfo.setMachineId(machineInfo.getId());
            instanceInfo.setDeployType(DeployTypeEnum.CLUSTER.getDeployType());
            instanceInfo.setCreateTime(new Date());
            return instanceInfo;
        } else {
            throw new RuntimeException("系统中不存在该机器信息ip：" + host);
        }
    }

    @Override
    public InstanceDetailVO getInstanceDetailVOByInstanceId(int instanceId) {
        InstanceDetailVO instanceDetailVO = new InstanceDetailVO();
        instanceDetailVO.setInstanceInfo(instanceInfoDao.getInstanceInfoById(instanceId));
        instanceDetailVO.setInstanceState(instanceStateService.getInstanceStateByInstanceId(instanceId));
        return instanceDetailVO;
    }

    @Override
    public List<InstanceDetailVO> getInstanceDetailVOByClusterId(int clusterId) {
        List<InstanceInfo> instanceInfoList = getInstancesByClusterId(clusterId);
        if (CollectionUtils.isEmpty(instanceInfoList)) {
            return null;
        }
        List<InstanceDetailVO> instanceDetailVOList = new ArrayList<InstanceDetailVO>();
        for (InstanceInfo instanceInfo : instanceInfoList) {
            InstanceDetailVO instanceDetailVO = new InstanceDetailVO();
            instanceDetailVO.setInstanceInfo(instanceInfo);
            instanceDetailVO.setInstanceState(instanceStateService.getInstanceStateByInstanceId(instanceInfo.getId()));
            instanceDetailVOList.add(instanceDetailVO);
        }
        return instanceDetailVOList;
    }

    @Override
    public List<InstanceDetailVO> getInstanceDetailVOByStatus(int status) {
        List<InstanceInfo> instanceInfoList = getInstancesByStatus(status);
        if (CollectionUtils.isEmpty(instanceInfoList)) {
            return new ArrayList<InstanceDetailVO>();
        }
        List<InstanceDetailVO> instanceDetailVOList = new ArrayList<InstanceDetailVO>();
        for (InstanceInfo instanceInfo : instanceInfoList) {
            InstanceDetailVO instanceDetailVO = new InstanceDetailVO();
            instanceDetailVO.setInstanceInfo(instanceInfo);
            instanceDetailVO.setInstanceState(instanceStateService.getInstanceStateByInstanceId(instanceInfo.getId()));
            instanceDetailVOList.add(instanceDetailVO);
        }
        return instanceDetailVOList;
    }

    @Override
    public boolean removeInstanceForCluster(int instanceId) {
        boolean result = deleteInstanceById(instanceId);
        // 实例状态信息有无，不影响删除实例结果
        instanceStateService.deleteInstanceStateByInstanceId(instanceId);
        return result;
    }

    @Override
    public InstanceInfo getLeaderOrStandaloneInstance(int clusterId) {
        return instanceInfoDao.getLeaderOrStandaloneInstance(clusterId);
    }

    @Override
    public boolean updateInstanceConnMonitor(int instanceId, boolean connectionMonitor) {
        return instanceInfoDao.updateInstanceConnMonitor(instanceId, connectionMonitor);
    }

    @Override
    public List<InstanceInfo> getAllConnMonitorInstance() {
        return instanceInfoDao.getAllConnMonitorInstance();
    }

    @Override
    public String queryInstanceConfig(String host) {
        try {
            return SSHUtil.getZKConfig(host);
        } catch (SSHException e) {
            LOGGER.error("Query instance {} zoo.cfg config file content failed.", host, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean instanceConfOps(String host, String zooConfFileContent) {
        try {
            return SSHUtil.editZKConfig(host, zooConfFileContent);
        } catch (SSHException e) {
            LOGGER.error("Edit instance {} zoo.cfg config file content failed, new conf file is {}.",
                    host, zooConfFileContent, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean addNewConfigFile(AddNewConfigFileBO addNewConfigFileBO) {
        try {
            return SSHUtil.addNewConfigFile(addNewConfigFileBO.getHost(), addNewConfigFileBO.getConfDir(),
                    addNewConfigFileBO.getConfFileName(), addNewConfigFileBO.getConfFileContent());
        } catch (SSHException e) {
            LOGGER.error("Add instance {} file:{}/{} content failed, new file content is {}.", addNewConfigFileBO.getHost(),
                    addNewConfigFileBO.getConfDir(), addNewConfigFileBO.getConfFileName(), addNewConfigFileBO.getConfFileContent(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UploadedJarFileVO> queryUploadedJarFile() {
        File file = new File(ModifiableConfig.uploadFileDir + ModifiableConfig.uploadFileName);
        if (file.exists()) {
            List<UploadedJarFileVO> uploadedJarFileVOList = new ArrayList<UploadedJarFileVO>();
            UploadedJarFileVO uploadedJarFileVO = new UploadedJarFileVO();
            uploadedJarFileVO.setUid(1);
            uploadedJarFileVO.setName(ModifiableConfig.uploadFileName);
            uploadedJarFileVO.setStatus("done");
            uploadedJarFileVOList.add(uploadedJarFileVO);
            return uploadedJarFileVOList;
        }
        return null;
    }

    @Override
    public boolean updateServer(int instanceId) {
        InstanceInfo instanceInfo = getInstanceInfoById(instanceId);
        if (instanceInfo == null) {
            throw new RuntimeException("指定id:" + instanceId + "的实例信息为null，请检查后重试。");
        }
        return zkService.updateServer(instanceInfo.getHost());
    }
}
