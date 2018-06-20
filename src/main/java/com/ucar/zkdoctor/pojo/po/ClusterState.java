package com.ucar.zkdoctor.pojo.po;

import com.ucar.zkdoctor.pojo.BaseTimeLineObject;

/**
 * Description: 集群状态
 * Created on 2018/1/9 11:02
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ClusterState extends BaseTimeLineObject {

    private static final long serialVersionUID = 6192323561260280678L;

    /**
     * id
     */
    private Integer id;

    /**
     * 集群id
     */
    private Integer clusterId;

    /**
     * 实例数
     */
    private Integer instanceNumber;

    /**
     * 同一时间集群中实例的平均延时的最大值,单位:ms
     */
    private Long avgLatencyMax;

    /**
     * 同一时间集群中实例的最大延时的最大值,单位:ms
     */
    private Long maxLatencyMax;

    /**
     * 同一时间集群中实例的最小延时的最大值,单位:ms
     */
    private Long minLatencyMax;

    /**
     * 集群总收包数
     */
    private Long receivedTotal;

    /**
     * 集群总发包数
     */
    private Long sentTotal;

    /**
     * 集群总连接数
     */
    private Integer connectionTotal;

    /**
     * 节点数
     */
    private Integer znodeCount;

    /**
     * 集群总watcher数
     */
    private Integer watcherTotal;

    /**
     * 临时节点数
     */
    private Integer ephemerals;

    /**
     * 集群总堆积请求数
     */
    private Long outstandingTotal;

    /**
     * 数据大小,单位:byte。数据大小,单位:byte。zookeeper内存中存储的所有数据节点path+data的大小
     */
    private Long approximateDataSize;

    /**
     * 集群总打开的文件描述符数量
     */
    private Long openFileDescriptorCountTotal;

    /**
     * 预留参数
     */
    private String param1;

    @Override
    public String toString() {
        return "ClusterState{" +
                "id=" + id +
                ", clusterId=" + clusterId +
                ", instanceNumber=" + instanceNumber +
                ", avgLatencyMax=" + avgLatencyMax +
                ", maxLatencyMax=" + maxLatencyMax +
                ", minLatencyMax=" + minLatencyMax +
                ", receivedTotal=" + receivedTotal +
                ", sentTotal=" + sentTotal +
                ", connectionTotal=" + connectionTotal +
                ", znodeCount=" + znodeCount +
                ", watcherTotal=" + watcherTotal +
                ", ephemerals=" + ephemerals +
                ", outstandingTotal=" + outstandingTotal +
                ", approximateDataSize=" + approximateDataSize +
                ", openFileDescriptorCountTotal=" + openFileDescriptorCountTotal +
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

    public Integer getInstanceNumber() {
        return instanceNumber;
    }

    public void setInstanceNumber(Integer instanceNumber) {
        this.instanceNumber = instanceNumber;
    }

    public Long getAvgLatencyMax() {
        return avgLatencyMax;
    }

    public void setAvgLatencyMax(Long avgLatencyMax) {
        this.avgLatencyMax = avgLatencyMax;
    }

    public Long getMaxLatencyMax() {
        return maxLatencyMax;
    }

    public void setMaxLatencyMax(Long maxLatencyMax) {
        this.maxLatencyMax = maxLatencyMax;
    }

    public Long getMinLatencyMax() {
        return minLatencyMax;
    }

    public void setMinLatencyMax(Long minLatencyMax) {
        this.minLatencyMax = minLatencyMax;
    }

    public Long getReceivedTotal() {
        return receivedTotal;
    }

    public void setReceivedTotal(Long receivedTotal) {
        this.receivedTotal = receivedTotal;
    }

    public Long getSentTotal() {
        return sentTotal;
    }

    public void setSentTotal(Long sentTotal) {
        this.sentTotal = sentTotal;
    }

    public Integer getConnectionTotal() {
        return connectionTotal;
    }

    public void setConnectionTotal(Integer connectionTotal) {
        this.connectionTotal = connectionTotal;
    }

    public Integer getZnodeCount() {
        return znodeCount;
    }

    public void setZnodeCount(Integer znodeCount) {
        this.znodeCount = znodeCount;
    }

    public Integer getWatcherTotal() {
        return watcherTotal;
    }

    public void setWatcherTotal(Integer watcherTotal) {
        this.watcherTotal = watcherTotal;
    }

    public Integer getEphemerals() {
        return ephemerals;
    }

    public void setEphemerals(Integer ephemerals) {
        this.ephemerals = ephemerals;
    }

    public Long getOutstandingTotal() {
        return outstandingTotal;
    }

    public void setOutstandingTotal(Long outstandingTotal) {
        this.outstandingTotal = outstandingTotal;
    }

    public Long getApproximateDataSize() {
        return approximateDataSize;
    }

    public void setApproximateDataSize(Long approximateDataSize) {
        this.approximateDataSize = approximateDataSize;
    }

    public Long getOpenFileDescriptorCountTotal() {
        return openFileDescriptorCountTotal;
    }

    public void setOpenFileDescriptorCountTotal(Long openFileDescriptorCountTotal) {
        this.openFileDescriptorCountTotal = openFileDescriptorCountTotal;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }
}
