package com.ucar.zkdoctor.dao.mysql;

import com.ucar.zkdoctor.pojo.bo.ClientConnectionSearchBO;
import com.ucar.zkdoctor.pojo.po.ClientInfo;

import java.util.Date;
import java.util.List;

/**
 * Description: 客户端连接信息操作Dao
 * Created on 2018/2/23 15:21
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public interface ClientInfoDao {

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
     * 获取最近一次收集的客户端信息
     *
     * @param instanceId 实例id
     * @return
     */
    ClientInfo getLatestClientInfo(int instanceId);
}
