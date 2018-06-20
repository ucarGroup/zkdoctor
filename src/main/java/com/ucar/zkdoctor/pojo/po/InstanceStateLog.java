package com.ucar.zkdoctor.pojo.po;

import java.io.Serializable;
import java.util.Date;

/**
 * Description: 实例状态历史信息
 * Created on 2018/1/9 16:45
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class InstanceStateLog implements Serializable {

    private static final long serialVersionUID = -4974358555989097013L;

    /**
     * id
     */
    private Integer id;

    /**
     * 实例id
     */
    private Integer instanceId;

    /**
     * 实例host:port
     */
    private String hostInfo;

    /**
     * 集群id
     */
    private Integer clusterId;

    /**
     * 版本信息
     */
    private String version;

    /**
     * leader实例id
     */
    private Integer leaderId;

    /**
     * 平均延时,单位:ms
     */
    private Long avgLatency;

    /**
     * 最大延时,单位:ms
     */
    private Long maxLatency;

    /**
     * 最小延时,单位:ms
     */
    private Long minLatency;

    /**
     * 接收请求数
     */
    private Long received;

    /**
     * 响应请求数
     */
    private Long sent;

    /**
     * 当前连接数
     */
    private Integer connections;

    /**
     * 当前节点数，包含临时节点
     */
    private Integer znodeCount;

    /**
     * 当前watch数
     */
    private Integer watchCount;

    /**
     * 当前临时节点数
     */
    private Integer ephemeralsCount;

    /**
     * 当前堆积请求数
     */
    private Long outstandings;

    /**
     * 数据大小,单位:byte。zookeeper内存中存储的所有数据节点path+data的大小
     */
    private Long approximateDataSize;

    /**
     * 打开的文件描述符个数
     */
    private Long openFileDescriptorCount;

    /**
     * 最大文件描述符个数
     */
    private Long maxFileDescriptorCount;

    /**
     * follower数量
     */
    private Integer followers;

    /**
     * 同步的follower数量
     */
    private Integer syncedFollowers;

    /**
     * 待同步的follow数量
     */
    private Integer pendingSyncs;

    /**
     * 最后的事务id
     */
    private Long zxid;

    /**
     * 实例是否运行正常
     */
    private Boolean runOk;

    /**
     * 实例角色，请参考ZKServerEnumClass.ZKServerStateEnum
     */
    private Integer serverStateLag;

    /**
     * 集群状态收集时间
     */
    private Date createTime;

    @Override
    public String toString() {
        return "InstanceStateLog{" +
                "id=" + id +
                ", instanceId=" + instanceId +
                ", hostInfo='" + hostInfo + '\'' +
                ", clusterId=" + clusterId +
                ", version='" + version + '\'' +
                ", leaderId=" + leaderId +
                ", avgLatency=" + avgLatency +
                ", maxLatency=" + maxLatency +
                ", minLatency=" + minLatency +
                ", received=" + received +
                ", sent=" + sent +
                ", connections=" + connections +
                ", znodeCount=" + znodeCount +
                ", watchCount=" + watchCount +
                ", ephemeralsCount=" + ephemeralsCount +
                ", outstandings=" + outstandings +
                ", approximateDataSize=" + approximateDataSize +
                ", openFileDescriptorCount=" + openFileDescriptorCount +
                ", maxFileDescriptorCount=" + maxFileDescriptorCount +
                ", followers=" + followers +
                ", syncedFollowers=" + syncedFollowers +
                ", pendingSyncs=" + pendingSyncs +
                ", zxid=" + zxid +
                ", runOk=" + runOk +
                ", serverStateLag=" + serverStateLag +
                ", createTime=" + createTime +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Integer instanceId) {
        this.instanceId = instanceId;
    }

    public String getHostInfo() {
        return hostInfo;
    }

    public void setHostInfo(String hostInfo) {
        this.hostInfo = hostInfo;
    }

    public Integer getClusterId() {
        return clusterId;
    }

    public void setClusterId(Integer clusterId) {
        this.clusterId = clusterId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Integer leaderId) {
        this.leaderId = leaderId;
    }

    public Long getAvgLatency() {
        return avgLatency;
    }

    public void setAvgLatency(Long avgLatency) {
        this.avgLatency = avgLatency;
    }

    public Long getMaxLatency() {
        return maxLatency;
    }

    public void setMaxLatency(Long maxLatency) {
        this.maxLatency = maxLatency;
    }

    public Long getMinLatency() {
        return minLatency;
    }

    public void setMinLatency(Long minLatency) {
        this.minLatency = minLatency;
    }

    public Long getReceived() {
        return received;
    }

    public void setReceived(Long received) {
        this.received = received;
    }

    public Long getSent() {
        return sent;
    }

    public void setSent(Long sent) {
        this.sent = sent;
    }

    public Integer getConnections() {
        return connections;
    }

    public void setConnections(Integer connections) {
        this.connections = connections;
    }

    public Integer getZnodeCount() {
        return znodeCount;
    }

    public void setZnodeCount(Integer znodeCount) {
        this.znodeCount = znodeCount;
    }

    public Integer getWatchCount() {
        return watchCount;
    }

    public void setWatchCount(Integer watchCount) {
        this.watchCount = watchCount;
    }

    public Integer getEphemeralsCount() {
        return ephemeralsCount;
    }

    public void setEphemeralsCount(Integer ephemeralsCount) {
        this.ephemeralsCount = ephemeralsCount;
    }

    public Long getOutstandings() {
        return outstandings;
    }

    public void setOutstandings(Long outstandings) {
        this.outstandings = outstandings;
    }

    public Long getApproximateDataSize() {
        return approximateDataSize;
    }

    public void setApproximateDataSize(Long approximateDataSize) {
        this.approximateDataSize = approximateDataSize;
    }

    public Long getOpenFileDescriptorCount() {
        return openFileDescriptorCount;
    }

    public void setOpenFileDescriptorCount(Long openFileDescriptorCount) {
        this.openFileDescriptorCount = openFileDescriptorCount;
    }

    public Long getMaxFileDescriptorCount() {
        return maxFileDescriptorCount;
    }

    public void setMaxFileDescriptorCount(Long maxFileDescriptorCount) {
        this.maxFileDescriptorCount = maxFileDescriptorCount;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getSyncedFollowers() {
        return syncedFollowers;
    }

    public void setSyncedFollowers(Integer syncedFollowers) {
        this.syncedFollowers = syncedFollowers;
    }

    public Integer getPendingSyncs() {
        return pendingSyncs;
    }

    public void setPendingSyncs(Integer pendingSyncs) {
        this.pendingSyncs = pendingSyncs;
    }

    public Long getZxid() {
        return zxid;
    }

    public void setZxid(Long zxid) {
        this.zxid = zxid;
    }

    public Boolean getRunOk() {
        return runOk;
    }

    public void setRunOk(Boolean runOk) {
        this.runOk = runOk;
    }

    public Integer getServerStateLag() {
        return serverStateLag;
    }

    public void setServerStateLag(Integer serverStateLag) {
        this.serverStateLag = serverStateLag;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
