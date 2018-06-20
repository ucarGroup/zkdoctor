package com.ucar.zkdoctor.service.instance.impl;

import com.ucar.zkdoctor.dao.mysql.ClientInfoDao;
import com.ucar.zkdoctor.dao.mysql.InstanceStateDao;
import com.ucar.zkdoctor.dao.mysql.InstanceStateLogDao;
import com.ucar.zkdoctor.pojo.bo.ClientConnectionSearchBO;
import com.ucar.zkdoctor.pojo.bo.InstanceStateLogSearchBO;
import com.ucar.zkdoctor.pojo.po.ClientInfo;
import com.ucar.zkdoctor.pojo.po.InstanceState;
import com.ucar.zkdoctor.pojo.po.InstanceStateLog;
import com.ucar.zkdoctor.service.instance.InstanceStateService;
import com.ucar.zkdoctor.util.tool.ChartConvertUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Description: 实例状态服务接口实现类
 * Created on 2018/1/11 15:15
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Service
public class InstanceStateServiceImpl implements InstanceStateService {

    @Resource
    private InstanceStateDao instanceStateDao;

    @Resource
    private InstanceStateLogDao instanceStateLogDao;

    @Resource
    private ClientInfoDao clientInfoDao;

    @Override
    public boolean mergeInstanceState(InstanceState instanceState) {
        if (instanceState == null) {
            return false;
        }
        return instanceStateDao.insertInstanceState(instanceState);
    }

    @Override
    public InstanceState getInstanceStateByInstanceId(int instanceId) {
        return instanceStateDao.getInstanceStateByInstanceId(instanceId);
    }

    @Override
    public List<InstanceState> getInstanceStateByClusterId(int clusterId) {
        return instanceStateDao.getInstanceStateByClusterId(clusterId);
    }

    @Override
    public boolean deleteInstanceStateByInstanceId(int instanceId) {
        return instanceStateDao.deleteInstanceStateByInstanceId(instanceId);
    }

    @Override
    public boolean batchInsertInstanceStateLogs(List<InstanceStateLog> instanceStateLogList) {
        if (CollectionUtils.isEmpty(instanceStateLogList)) {
            return false;
        }
        return instanceStateLogDao.batchInsertInstanceStateLogs(instanceStateLogList);
    }

    @Override
    public boolean insertInstanceStateLogs(InstanceStateLog instanceStateLog) {
        if (instanceStateLog == null) {
            return false;
        }
        return instanceStateLogDao.insertInstanceStateLogs(instanceStateLog);
    }

    @Override
    public List<InstanceStateLog> getInstanceStateLogByClusterParams(InstanceStateLogSearchBO instanceStateLogSearchBO) {
        if (instanceStateLogSearchBO == null) {
            return null;
        }
        return instanceStateLogDao.getInstanceStateLogByClusterParams(instanceStateLogSearchBO);
    }

    @Override
    public List<InstanceStateLog> getInstanceStateLogByInstance(int instanceId, Date startDate, Date endDate) {
        return instanceStateLogDao.getInstanceStateLogByInstance(instanceId, startDate, endDate);
    }

    @Override
    public List<InstanceStateLog> getInstanceStateLogByCluster(int clusterId, Date startDate, Date endDate) {
        return instanceStateLogDao.getInstanceStateLogByCluster(clusterId, startDate, endDate);
    }

    @Override
    public boolean cleanInstanceStateLogData(Date endDate) {
        return instanceStateLogDao.cleanInstanceStateLogData(endDate);
    }

    @Override
    public Long cleanInstanceStateLogCount(Date endDate) {
        return instanceStateLogDao.cleanInstanceStateLogCount(endDate);
    }

    @Override
    public Long cleanClientConnectionsCount(Date endDate) {
        return clientInfoDao.cleanClientConnectionsCount(endDate);
    }

    @Override
    public boolean cleanClientConnectionsData(Date endDate) {
        return clientInfoDao.cleanClientConnectionsData(endDate);
    }

    @Override
    public boolean batchInsertClientConnections(List<ClientInfo> clientInfoList) {
        if (CollectionUtils.isEmpty(clientInfoList)) {
            return false;
        }
        return clientInfoDao.batchInsertClientConnections(clientInfoList);
    }

    @Override
    public List<ClientInfo> getClientConnectionsByParams(ClientConnectionSearchBO clientConnectionSearchBO) {
        return clientInfoDao.getClientConnectionsByParams(clientConnectionSearchBO);
    }

    @Override
    public List<ClientInfo> dealWithClientConnectionsViews(List<ClientInfo> clientInfoList, String orderBy) {
        if (ChartConvertUtil.CONNECTION_CLIENT_IP.equals(orderBy)) {
            if (CollectionUtils.isEmpty(clientInfoList)) {
                return new ArrayList<ClientInfo>();
            }
            List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
            Map<String, Object> client = new HashMap<String, Object>();
            for (ClientInfo clientInfo : clientInfoList) {
                if (client.get(ChartConvertUtil.CONNECTION_CLIENT_IP) != null &&
                        client.get(ChartConvertUtil.CONNECTION_CLIENT_IP).equals(clientInfo.getClientIp())) {
                    ((LinkedList<ClientInfo>) client.get(ChartConvertUtil.CONNECTION_CLIENT_LIST)).add(clientInfo);
                    client.put(ChartConvertUtil.CONNECTION_RECEIVED_NAME,
                            (Long) client.get(ChartConvertUtil.CONNECTION_RECEIVED_NAME) + clientInfo.getRecved());
                    client.put(ChartConvertUtil.CONNECTION_SENT_NAME,
                            (Long) client.get(ChartConvertUtil.CONNECTION_SENT_NAME) + clientInfo.getSent());
                    client.put(ChartConvertUtil.CONNECTION_QUEUED_NAME,
                            (Long) client.get(ChartConvertUtil.CONNECTION_QUEUED_NAME) + clientInfo.getQueued());
                    Integer maxlat = (Integer) client.get(ChartConvertUtil.CONNECTION_MAXLAT_NAME);
                    maxlat = (maxlat != null && maxlat > clientInfo.getMaxlat() ? maxlat : clientInfo.getMaxlat());
                    client.put(ChartConvertUtil.CONNECTION_MAXLAT_NAME, maxlat);
                } else {
                    client = new HashMap<String, Object>();
                    client.put(ChartConvertUtil.CONNECTION_CLIENT_IP, clientInfo.getClientIp());
                    client.put(ChartConvertUtil.CONNECTION_RECEIVED_NAME, clientInfo.getRecved());
                    client.put(ChartConvertUtil.CONNECTION_SENT_NAME, clientInfo.getSent());
                    client.put(ChartConvertUtil.CONNECTION_QUEUED_NAME, clientInfo.getQueued());
                    client.put(ChartConvertUtil.CONNECTION_MAXLAT_NAME, clientInfo.getMaxlat());
                    LinkedList<ClientInfo> clients = new LinkedList<ClientInfo>();
                    clients.add(clientInfo);
                    client.put(ChartConvertUtil.CONNECTION_CLIENT_LIST, clients);
                    list.add(client);
                }
            }
            // 按照连接数个数递减排序
            Collections.sort(list, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    List<Object> list1 = (List<Object>) (o1.get(ChartConvertUtil.CONNECTION_CLIENT_LIST));
                    List<Object> list2 = (List<Object>) (o2.get(ChartConvertUtil.CONNECTION_CLIENT_LIST));
                    if (list1.size() > list2.size()) {
                        return -1;
                    } else if (list1.size() == list2.size()) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            });
            List<ClientInfo> sortedClientList = new LinkedList<ClientInfo>();
            for (Map<String, Object> entry : list) {
                ClientInfo clientInfo = new ClientInfo();
                clientInfo.setClientIp((String) entry.get(ChartConvertUtil.CONNECTION_CLIENT_IP));
                clientInfo.setRecved((Long) entry.get(ChartConvertUtil.CONNECTION_RECEIVED_NAME));
                clientInfo.setSent((Long) entry.get(ChartConvertUtil.CONNECTION_SENT_NAME));
                clientInfo.setQueued((Long) entry.get(ChartConvertUtil.CONNECTION_QUEUED_NAME));
                clientInfo.setMaxlat((Integer) entry.get(ChartConvertUtil.CONNECTION_MAXLAT_NAME));
                List<ClientInfo> currentList = (List<ClientInfo>) entry.get(ChartConvertUtil.CONNECTION_CLIENT_LIST);
                clientInfo.setClientInfoList(currentList);
                clientInfo.setNumber(currentList.size());
                sortedClientList.add(clientInfo);
            }
            return sortedClientList;
        } else {
            return clientInfoList;
        }
    }

    @Override
    public ClientInfo getLatestClientInfo(int instanceId) {
        return clientInfoDao.getLatestClientInfo(instanceId);
    }
}
