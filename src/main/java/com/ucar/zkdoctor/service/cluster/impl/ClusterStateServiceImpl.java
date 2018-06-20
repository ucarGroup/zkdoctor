package com.ucar.zkdoctor.service.cluster.impl;

import com.ucar.zkdoctor.dao.mysql.ClusterStateDao;
import com.ucar.zkdoctor.pojo.po.ClusterState;
import com.ucar.zkdoctor.service.cluster.ClusterStateService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Description: 集群状态服务接口实现类
 * Created on 2018/1/11 15:14
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Service
public class ClusterStateServiceImpl implements ClusterStateService {

    @Resource
    private ClusterStateDao clusterStateDao;

    @Override
    public boolean mergeClusterState(ClusterState clusterState) {
        if (clusterState == null) {
            return false;
        }
        return clusterStateDao.insertClusterState(clusterState);
    }

    @Override
    public ClusterState getClusterStateByClusterId(int clusterId) {
        return clusterStateDao.getClusterStateByClusterId(clusterId);
    }

    @Override
    public boolean deleteClusterStateByClusterId(int clusterId) {
        return clusterStateDao.deleteClusterStateByClusterId(clusterId);
    }

    @Override
    public boolean batchInsertClusterStateLogs(List<ClusterState> clusterStateList) {
        if (CollectionUtils.isEmpty(clusterStateList)) {
            return false;
        }
        return clusterStateDao.batchInsertClusterStateLogs(clusterStateList);
    }

    @Override
    public boolean insertClusterStateLogs(ClusterState clusterState) {
        if (clusterState == null) {
            return false;
        }
        return clusterStateDao.insertClusterStateLogs(clusterState);
    }

    @Override
    public List<ClusterState> getClusterStateLogByClusterId(int clusterId, Date startDate, Date endDate) {
        return clusterStateDao.getClusterStateLogByClusterId(clusterId, startDate, endDate);
    }

    @Override
    public boolean cleanClusterStateLogData(Date endDate) {
        return clusterStateDao.cleanClusterStateLogData(endDate);
    }

    @Override
    public Long cleanClusterStateLogCount(Date endDate) {
        return clusterStateDao.cleanClusterStateLogCount(endDate);
    }

}
