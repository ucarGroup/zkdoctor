package com.ucar.zkdoctor.pojo.vo;

import com.ucar.zkdoctor.pojo.po.ClusterInfo;

import java.io.Serializable;

/**
 * Description: 集群显示VO
 * Created on 2018/1/10 22:03
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ClusterListVO implements Serializable {

    private static final long serialVersionUID = 9139343305100441130L;

    /**
     * 集群id
     */
    private Integer clusterId;

    /**
     * 集群名
     */
    private String clusterName;

    /**
     * 集群描述
     */
    private String intro;

    /**
     * 节点数
     */
    private Integer znodeCount;

    /**
     * 集群总请求数（received）
     */
    private Long requestCount;

    /**
     * 连接数
     */
    private Integer connections;

    /**
     * 状态信息收集时间
     */
    private String collectTime;

    /**
     * 集群状态
     */
    private Integer status;

    /**
     * 集群所属业务线
     */
    private Integer serviceLine;

    /**
     * zk版本号
     */
    private String version;

    /**
     * 集群基本信息
     */
    private ClusterInfo clusterInfo;

    @Override
    public String toString() {
        return "ClusterListVO{" +
                "clusterId=" + clusterId +
                ", clusterName='" + clusterName + '\'' +
                ", intro='" + intro + '\'' +
                ", znodeCount=" + znodeCount +
                ", requestCount=" + requestCount +
                ", connections=" + connections +
                ", collectTime='" + collectTime + '\'' +
                ", status=" + status +
                ", serviceLine=" + serviceLine +
                ", version='" + version + '\'' +
                ", clusterInfo=" + clusterInfo +
                '}';
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

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Integer getZnodeCount() {
        return znodeCount;
    }

    public void setZnodeCount(Integer znodeCount) {
        this.znodeCount = znodeCount;
    }

    public Long getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(Long requestCount) {
        this.requestCount = requestCount;
    }

    public Integer getConnections() {
        return connections;
    }

    public void setConnections(Integer connections) {
        this.connections = connections;
    }

    public String getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(String collectTime) {
        this.collectTime = collectTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getServiceLine() {
        return serviceLine;
    }

    public void setServiceLine(Integer serviceLine) {
        this.serviceLine = serviceLine;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ClusterInfo getClusterInfo() {
        return clusterInfo;
    }

    public void setClusterInfo(ClusterInfo clusterInfo) {
        this.clusterInfo = clusterInfo;
    }
}
