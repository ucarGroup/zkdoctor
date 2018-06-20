package com.ucar.zkdoctor.service.instance;

import com.ucar.zkdoctor.pojo.bo.ClientConnectionSearchBO;
import com.ucar.zkdoctor.pojo.bo.InstanceStateLogSearchBO;
import com.ucar.zkdoctor.pojo.po.ClientInfo;
import com.ucar.zkdoctor.pojo.po.InstanceState;
import com.ucar.zkdoctor.pojo.po.InstanceStateLog;

import java.util.Date;
import java.util.List;

/**
 * Description: 实例状态服务接口
 * Created on 2018/1/11 15:15
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public interface InstanceStateService {

    /**
     * 插入新的实例状态信息
     *
     * @param instanceState 实例状态
     * @return
     */
    boolean mergeInstanceState(InstanceState instanceState);

    /**
     * 通过实例id获取最新实例状态信息
     *
     * @param instanceId 实例id
     * @return
     */
    InstanceState getInstanceStateByInstanceId(int instanceId);

    /**
     * 获取某集群下所有实例最近状态信息
     *
     * @param clusterId 集群id
     * @return
     */
    List<InstanceState> getInstanceStateByClusterId(int clusterId);

    /**
     * 删除某实例状态信息
     *
     * @param instanceId 实例id
     * @return
     */
    boolean deleteInstanceStateByInstanceId(int instanceId);

    /**
     * 批量写入实例运行状态记录
     *
     * @param instanceStateLogList 实例状态信息
     * @return
     */
    boolean batchInsertInstanceStateLogs(List<InstanceStateLog> instanceStateLogList);

    /**
     * 插入某条实例运行状态记录
     *
     * @param instanceStateLog 实例状态信息
     * @return
     */
    boolean insertInstanceStateLogs(InstanceStateLog instanceStateLog);

    /**
     * 获取符合条件的实例状态记录，集群汇总信息
     *
     * @param instanceStateLogSearchBO 查询条件
     * @return
     */
    List<InstanceStateLog> getInstanceStateLogByClusterParams(InstanceStateLogSearchBO instanceStateLogSearchBO);

    /**
     * 获取某实例的历史状态信息
     *
     * @param instanceId 实例id
     * @param startDate  开始时间
     * @param endDate    结束时间
     * @return
     */
    List<InstanceStateLog> getInstanceStateLogByInstance(int instanceId, Date startDate, Date endDate);

    /**
     * 获取某集群下所有实例的历史状态信息
     *
     * @param clusterId 集群id
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return
     */
    List<InstanceStateLog> getInstanceStateLogByCluster(int clusterId, Date startDate, Date endDate);

    /**
     * 删除此时间之前的所有实例状态历史数据
     *
     * @param endDate 时间
     * @return
     */
    boolean cleanInstanceStateLogData(Date endDate);

    /**
     * 需要删除的实例连接信息状态历史数据记录的数量
     *
     * @param endDate 时间
     * @return
     */
    Long cleanClientConnectionsCount(Date endDate);

    /**
     * 删除此时间之前的所有实例连接信息状态历史数据
     *
     * @param endDate 时间
     * @return
     */
    boolean cleanClientConnectionsData(Date endDate);

    /**
     * 需要删除的实例状态历史数据记录的数量
     *
     * @param endDate 时间
     * @return
     */
    Long cleanInstanceStateLogCount(Date endDate);

    /**
     * 批量保存客户端连接信息
     *
     * @param clientInfoList 客户端连接信息
     * @return
     */
    boolean batchInsertClientConnections(List<ClientInfo> clientInfoList);

    /**
     * 根据查询条件，获取符合条件的客户端连接信息
     *
     * @param clientConnectionSearchBO 查询条件
     * @return
     */
    List<ClientInfo> getClientConnectionsByParams(ClientConnectionSearchBO clientConnectionSearchBO);

    /**
     * 处理连接信息，用于前端展示
     *
     * @param clientInfoList 客户端连接信息
     * @param orderBy        排序条件
     * @return
     */
    List<ClientInfo> dealWithClientConnectionsViews(List<ClientInfo> clientInfoList, String orderBy);

    /**
     * 获取最近一次收集的客户端信息
     *
     * @param instanceId 实例id
     * @return
     */
    ClientInfo getLatestClientInfo(int instanceId);
}
