package com.ucar.zkdoctor.business.clusterMonitorAlarmTask.service.impl;

import com.ucar.zkdoctor.business.clusterMonitorAlarmTask.base.service.impl.BaseServiceImpl;
import com.ucar.zkdoctor.business.clusterMonitorAlarmTask.dao.ZkAlarmInfoMapper;
import com.ucar.zkdoctor.business.clusterMonitorAlarmTask.pojo.ZkAlarmInfo;
import com.ucar.zkdoctor.business.clusterMonitorAlarmTask.service.ZkAlarmInfoService;
import com.ucar.zkdoctor.business.clusterMonitorAlarmTask.valueobject.IndexCollectInfo;
import com.ucar.zkdoctor.business.clusterMonitorAlarmTask.valueobject.IndexCollectInfos;
import com.ucar.zkdoctor.pojo.vo.PageVO;
import com.ucar.zkdoctor.util.constant.ClusterEnumClass.ClusterStatusEnum;
import com.ucar.zkdoctor.util.constant.InstanceEnumClass.InstanceStatusEnum;
import com.ucar.zkdoctor.util.tool.DateUtil;
import com.ucar.zkdoctor.web.ConResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ZkAlarmInfoServiceImpl extends BaseServiceImpl implements ZkAlarmInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZkAlarmInfoServiceImpl.class);
    @Autowired
    private ZkAlarmInfoMapper zkAlarmInfoDao;

    @Override
    public ConResult getAllZkAlarmInfo() {
        String alarmTime = DateUtil.formatYYYYMMdd(new Date());
        List<HashMap<String, Object>> zkAlarmInfoList = zkAlarmInfoDao.selectAllContainName(alarmTime);
        Map<String, Object> result = new HashMap<String, Object>();
        PageVO pageVO = new PageVO();
        if (zkAlarmInfoList == null || zkAlarmInfoList.isEmpty()) {
            result.put("data", new ArrayList<HashMap<String, Object>>());
            pageVO.setTotal(0);
            pageVO.setPageSize(0);
            pageVO.setPageNum(0);
            result.put("page", pageVO);
            return ConResult.success(result, "The database has no data");
        }
        result.put("data", zkAlarmInfoList);
        pageVO.setTotal(zkAlarmInfoList.size());
        result.put("page", pageVO);
        return ConResult.success(result);
    }

    @Override
    public ConResult insertZkAlarmInfo(ZkAlarmInfo record) {
        if (record == null) {
            LOGGER.info("zkAlarmInfo  is null ");
            return ConResult.fail("zkAlarmInfo  is null");
        }
        int result = zkAlarmInfoDao.insertZkAlarmInfo(record);
        if (result <= 0) {
            return ConResult.fail("插入报警信息失败，请重新尝试。");
        }
        return ConResult.success();
    }

    @Override
    public ConResult getClusterInstanceCollectInfo() {
        IndexCollectInfo clusterCollectInfo = getClusterCollectInfo();
        IndexCollectInfo instanceCollectInfo = getInstanceCollectInfo();
        IndexCollectInfos indexCollectInfos = new IndexCollectInfos();
        indexCollectInfos.setClusterCollectInfo(clusterCollectInfo);
        indexCollectInfos.setInstanceCollectInfo(instanceCollectInfo);
        Map<String, Object> result = new HashMap<String, Object>();
        PageVO pageVO = new PageVO();
        result.put("data", indexCollectInfos);
        pageVO.setTotal(1);
        pageVO.setPageSize(1);
        pageVO.setPageNum(1);
        result.put("page", pageVO);
        return ConResult.success(result);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return 0;
    }

    @Override
    public ConResult insert(ZkAlarmInfo record) {
        return null;
    }


    @Override
    public ZkAlarmInfo selectByPrimaryKey(Integer id) {
        return null;
    }

    @Override
    public List<ZkAlarmInfo> selectAll() {
        return null;
    }

    @Override
    public ConResult updateByPrimaryKey(ZkAlarmInfo record) {
        return null;
    }


    @Override
    public Object getById(Serializable id) {
        return null;
    }

    @Override
    public void save(Object entity) {

    }

    @Override
    public void delete(Serializable id) {

    }

    public IndexCollectInfo getClusterCollectInfo() {
        //状态值,1:未监控,2:运行中,3:已下线,4:异常
        IndexCollectInfo clusterCollectInfo = new IndexCollectInfo();
        clusterCollectInfo.setUnmonitoredTotal(selectClusterTotal(ClusterStatusEnum.NOT_MONITORED.getStatus()));
        clusterCollectInfo.setRunningTotal(selectClusterTotal(ClusterStatusEnum.RUNNING.getStatus()));
        clusterCollectInfo.setReferralTotal(selectClusterTotal(ClusterStatusEnum.OFFLINE.getStatus()));
        clusterCollectInfo.setExceptionsTotal(selectClusterTotal(ClusterStatusEnum.EXCEPTION.getStatus()));
        clusterCollectInfo.setSumTotal(selectClusterTotal(null));
        return clusterCollectInfo;
    }

    public IndexCollectInfo getInstanceCollectInfo() {
        //状态值,0:异常,1:正在运行,3:已下线,4:未运行
        IndexCollectInfo clusterCollectInfo = new IndexCollectInfo();
        clusterCollectInfo.setExceptionsTotal(selectInstanceTotal(InstanceStatusEnum.EXCEPTION.getStatus()));
        clusterCollectInfo.setRunningTotal(selectInstanceTotal(InstanceStatusEnum.RUNNING.getStatus()));
        clusterCollectInfo.setReferralTotal(selectInstanceTotal(InstanceStatusEnum.OFFLINE.getStatus()));
        clusterCollectInfo.setNotRunningTotal(selectInstanceTotal(InstanceStatusEnum.NOT_RUNNING.getStatus()));
        clusterCollectInfo.setSumTotal(selectInstanceTotal(null));
        return clusterCollectInfo;
    }

    private int selectClusterTotal(Integer status) {
        return zkAlarmInfoDao.selectClusterTotal(status);
    }

    private int selectInstanceTotal(Integer status) {
        return zkAlarmInfoDao.selectInstanceTotal(status);
    }
}
