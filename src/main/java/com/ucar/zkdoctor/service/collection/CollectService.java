package com.ucar.zkdoctor.service.collection;

import com.ucar.zkdoctor.pojo.dto.ConnectionInfoDTO;
import com.ucar.zkdoctor.pojo.dto.ServerStateInfoDTO;
import com.ucar.zkdoctor.pojo.dto.ZKServerConfigDTO;
import com.ucar.zkdoctor.pojo.po.MachineState;
import com.ucar.zkdoctor.pojo.po.MachineStateLog;
import org.quartz.JobKey;

import java.util.Date;
import java.util.List;

/**
 * Description: 信息收集服务接口
 * Created on 2018/1/23 11:36
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public interface CollectService {

    /**
     * 部署zk收集定时任务
     *
     * @param clusterId 集群id
     * @return
     */
    boolean deployZKCollection(int clusterId);

    /**
     * 取消zk收集定时任务
     *
     * @param clusterId 集群id
     * @return
     */
    boolean unDeployZKCollection(int clusterId);

    /**
     * 部署zk收集任务
     *
     * @param clusterId 集群id
     * @param jobKey    job key
     * @return
     */
    boolean deployZKCollection(int clusterId, JobKey jobKey);

    /**
     * 收集集群基本信息
     *
     * @param clusterId 集群id
     */
    void collectZKInfo(int clusterId);

    /**
     * 获取zk状态信息，通过mntr四字命令
     *
     * @param host 实例ip
     * @param port 实例port
     * @return
     */
    ServerStateInfoDTO collectServerStateInfo(String host, int port);

    /**
     * 收集zk配置信息，通过conf四字命令
     *
     * @param host 实例ip
     * @param port 实例port
     * @return
     */
    ZKServerConfigDTO collectConfigInfo(String host, int port);

    /**
     * 收集zk连接信息，通过cons四字命令
     *
     * @param host 实例ip
     * @param port 实例port
     * @return
     */
    List<ConnectionInfoDTO> collectConnectionInfo(String host, int port);

    /**
     * 重置客户端连接统计
     *
     * @param host 实例ip
     * @param port 实例port
     * @return
     */
    boolean resetConnectionInfo(String host, int port);

    /**
     * 重置zk服务状态信息，通过srst四字命令
     *
     * @param host 实例ip
     * @param port 实例port
     */
    boolean resetServerStatistic(String host, int port);

    /**
     * 四字命令ruok，检测服务器是否运行正常，正常返回 imok
     *
     * @param host 服务器ip
     * @param port 服务器port
     * @return
     */
    String checkServerRunningNormal(String host, int port);

    /**
     * 实例是否正常运行，通过ruok四字命令
     *
     * @param host 服务器ip
     * @param port 服务器port
     * @return
     */
    Boolean checkInstanceRunOk(String host, int port);

    /**
     * 清除收集的统计数据信息
     *
     * @return
     */
    boolean cleanCollectData();

    /**
     * 清除集群历史统计数据
     *
     * @param endDate 此日志之前的数据全部清除
     */
    void cleanClusterStateData(Date endDate);

    /**
     * 清除实例历史统计数据
     *
     * @param endDate 此日志之前的数据全部清除
     */
    void cleanInstanceStateData(Date endDate);

    /**
     * 清除机器历史统计数据
     *
     * @param endDate 此日志之前的数据全部清除
     */
    void cleanMachineStateData(Date endDate);

    /**
     * 清除实例连接信息历史统计数据
     *
     * @param endDate 此日志之前的数据全部清除
     */
    void cleanClientConnectionsData(Date endDate);

    /**
     * 收集机器信息
     */
    void collectAllMachineState();

    /**
     * 收集指定机器的状态信息，并保存
     *
     * @param host            机器ip
     * @param machineState    机器状态
     * @param machineStateLog 机器状态历史信息
     * @return
     */
    boolean collectMachineState(String host, MachineState machineState, MachineStateLog machineStateLog);

    /**
     * 收集实例连接信息
     */
    void collectAllInstanceConnections();
}
