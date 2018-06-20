package com.ucar.zkdoctor.business.clusterMonitorAlarmTask.service.impl;

import com.ucar.zkdoctor.business.clusterMonitorAlarmTask.base.service.impl.BaseServiceImpl;
import com.ucar.zkdoctor.business.clusterMonitorAlarmTask.dao.ZkMonitorTaskMapper;
import com.ucar.zkdoctor.business.clusterMonitorAlarmTask.pojo.ZkMonitorTask;
import com.ucar.zkdoctor.business.clusterMonitorAlarmTask.service.ZkMonitorTaskService;
import com.ucar.zkdoctor.pojo.vo.PageVO;
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
public class ZkMonitorTaskServiceImpl extends BaseServiceImpl implements ZkMonitorTaskService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZkMonitorTaskServiceImpl.class);
    @Autowired
    private ZkMonitorTaskMapper zkMonitorTaskDao;

    @Override
    public ConResult getAllZkMonitorTask() {
        List<HashMap<String, Object>> zkMonitorTaskList = zkMonitorTaskDao.selectAllContainName();
        Map<String, Object> result = new HashMap<String, Object>();
        PageVO pageVO = new PageVO();
        if (zkMonitorTaskList == null || zkMonitorTaskList.isEmpty()) {
            LOGGER.info("The database has no data");
            result.put("data", new ArrayList<HashMap<String, Object>>());
            pageVO.setTotal(0);
            pageVO.setPageSize(0);
            pageVO.setPageNum(0);
            result.put("page", pageVO);
            return ConResult.success(result, "The database has no data");
        }
        result.put("data", zkMonitorTaskList);
        pageVO.setTotal(zkMonitorTaskList.size());
        result.put("page", pageVO);
        return ConResult.success(result);
    }

    @Override
    public ConResult selectByClusterId(Integer clusterId) {
        if (clusterId == 0) {
            LOGGER.info("clusterId  is null or clusterId error");
            return ConResult.fail("clusterId  is null or clusterId error");
        }
        List<HashMap<String, Object>> zkMonitorTaskList = zkMonitorTaskDao.selectByClusterId(clusterId);
        Map<String, Object> result = new HashMap<String, Object>();
        PageVO pageVO = new PageVO();
        if (zkMonitorTaskList == null || zkMonitorTaskList.isEmpty()) {
            LOGGER.info("The database has no qualified  data");
            result.put("data", new ArrayList<HashMap<String, Object>>());
            pageVO.setTotal(0);
            pageVO.setPageSize(0);
            pageVO.setPageNum(0);
            result.put("page", pageVO);
            return ConResult.success(result, "The database has no  qualified data");
        }
        result.put("data", zkMonitorTaskList);
        pageVO.setTotal(zkMonitorTaskList.size());
        result.put("page", pageVO);
        return ConResult.success(result);
    }

    @Override
    public ConResult getZkMonitorTaskList() {
        return null;
    }


    @Override
    public ConResult updateMonitorTaskSwitchOn(ZkMonitorTask zkMonitorTask) {
        if ((zkMonitorTask == null || zkMonitorTask.getId() == 0) || zkMonitorTask.getSwitchOn() == null) {
            LOGGER.info("monitorTaskId  is null or monitorTaskId error");
            return ConResult.fail("monitorTaskId  is null or monitorTaskId error");
        }
        int result = zkMonitorTaskDao.updateMonitorTaskSwitchOn(zkMonitorTask);
        if (result <= 0) {
            return ConResult.fail("修改监控任务默认开关信息失败，请重新尝试。");
        }
        return ConResult.success();
    }

    @Override
    public ConResult updateByPrimaryKey(ZkMonitorTask zkMonitorTask) {
        if ((zkMonitorTask == null || zkMonitorTask.getId() == 0)) {
            LOGGER.info("monitorTaskId  is null or monitorTaskId error");
            return ConResult.fail("monitorTaskId  is null or monitorTaskId error");
        }
        zkMonitorTask.setModifyTime(new Date());
        int result = zkMonitorTaskDao.updateByPrimaryKey(zkMonitorTask);
        if (result <= 0) {
            return ConResult.fail("修改监控任务信息失败，请重新尝试。");
        }
        return ConResult.success();
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return 0;
    }

    @Override
    public int insert(ZkMonitorTask record) {
        return 0;
    }

    @Override
    public ZkMonitorTask selectByPrimaryKey(Integer id) {
        return zkMonitorTaskDao.selectByPrimaryKey(id);
    }

    @Override
    public List<ZkMonitorTask> selectAll() {
        return zkMonitorTaskDao.selectAll();
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
}
