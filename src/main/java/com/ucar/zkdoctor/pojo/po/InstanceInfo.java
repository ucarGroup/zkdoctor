package com.ucar.zkdoctor.pojo.po;

import com.ucar.zkdoctor.pojo.BaseTimeLineObject;

/**
 * Description: 实例基本信息
 * Created on 2018/1/9 11:00
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class InstanceInfo extends BaseTimeLineObject {

    private static final long serialVersionUID = -6227590495923365121L;

    /**
     * 实例id
     */
    private Integer id;

    /**
     * 集群id
     */
    private Integer clusterId;

    /**
     * 集群名称
     */
    private String clusterName;

    /**
     * 机器id
     */
    private Integer machineId;

    /**
     * 实例ip
     */
    private String host;

    /**
     * 实例端口号
     */
    private Integer port;

    /**
     * 实例的连接信息是否进行收集,true：收集，false：不收集
     */
    private Boolean connectionMonitor;

    /**
     * 部署类型，请参考ZKServerEnumClass.DeployTypeEnum
     */
    private Integer deployType;

    /**
     * 实例角色，请参考ZKServerEnumClass.ZKServerStateEnum
     */
    private Integer serverStateLag;

    /**
     * 实例状态，请参考InstanceEnumClass.InstanceStatusEnum
     */
    private Integer status;

    /**
     * 预留参数
     */
    private String param1;

    @Override
    public String toString() {
        return "InstanceInfo{" +
                "id=" + id +
                ", clusterId=" + clusterId +
                ", clusterName='" + clusterName + '\'' +
                ", machineId=" + machineId +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", connectionMonitor=" + connectionMonitor +
                ", deployType=" + deployType +
                ", serverStateLag=" + serverStateLag +
                ", status=" + status +
                ", param1='" + param1 + '\'' +
                "} " + super.toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClusterId() {
        return clusterId;
    }

    public void setClusterId(Integer clusterId) {
        this.clusterId = clusterId;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public Integer getMachineId() {
        return machineId;
    }

    public void setMachineId(Integer machineId) {
        this.machineId = machineId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Boolean getConnectionMonitor() {
        return connectionMonitor;
    }

    public void setConnectionMonitor(Boolean connectionMonitor) {
        this.connectionMonitor = connectionMonitor;
    }

    public Integer getDeployType() {
        return deployType;
    }

    public void setDeployType(Integer deployType) {
        this.deployType = deployType;
    }

    public Integer getServerStateLag() {
        return serverStateLag;
    }

    public void setServerStateLag(Integer serverStateLag) {
        this.serverStateLag = serverStateLag;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }
}
