package com.ucar.zkdoctor.pojo.po;

import com.ucar.zkdoctor.pojo.BaseTimeLineObject;

/**
 * Description: 实例状态
 * Created on 2018/1/9 11:02
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class InstanceState extends BaseTimeLineObject {

    private static final long serialVersionUID = -7826789523123116268L;

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
     * 收包数
     */
    private Long received;

    /**
     * 发包数
     */
    private Long sent;

    /**
     * 当前连接数
     */
    private Integer currConnections;

    /**
     * 当前堆积请求数
     */
    private Long currOutstandings;

    /**
     * 实例角色，请参考ZKServerEnumClass.ZKServerStateEnum
     */
    private Integer serverStateLag;

    /**
     * 当前节点数，包含临时节点
     */
    private Integer currZnodeCount;

    /**
     * 当前watch数
     */
    private Integer currWatchCount;

    /**
     * 当前临时节点数
     */
    private Integer currEphemeralsCount;

    /**
     * 数据大小,单位:byte。数据大小,单位:byte。zookeeper内存中存储的所有数据节点path+data的大小
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
     * 预留参数
     */
    private String param1;

    @Override
    public String toString() {
        return "InstanceState{" +
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
                ", currConnections=" + currConnections +
                ", currOutstandings=" + currOutstandings +
                ", serverStateLag=" + serverStateLag +
                ", currZnodeCount=" + currZnodeCount +
                ", currWatchCount=" + currWatchCount +
                ", currEphemeralsCount=" + currEphemeralsCount +
                ", approximateDataSize=" + approximateDataSize +
                ", openFileDescriptorCount=" + openFileDescriptorCount +
                ", maxFileDescriptorCount=" + maxFileDescriptorCount +
                ", followers=" + followers +
                ", syncedFollowers=" + syncedFollowers +
                ", pendingSyncs=" + pendingSyncs +
                ", zxid=" + zxid +
                ", runOk=" + runOk +
                ", param1='" + param1 + '\'' +
                "} " + super.toString();
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

    public Integer getCurrConnections() {
        return currConnections;
    }

    public void setCurrConnections(Integer currConnections) {
        this.currConnections = currConnections;
    }

    public Long getCurrOutstandings() {
        return currOutstandings;
    }

    public void setCurrOutstandings(Long currOutstandings) {
        this.currOutstandings = currOutstandings;
    }

    public Integer getServerStateLag() {
        return serverStateLag;
    }

    public void setServerStateLag(Integer serverStateLag) {
        this.serverStateLag = serverStateLag;
    }

    public Integer getCurrZnodeCount() {
        return currZnodeCount;
    }

    public void setCurrZnodeCount(Integer currZnodeCount) {
        this.currZnodeCount = currZnodeCount;
    }

    public Integer getCurrWatchCount() {
        return currWatchCount;
    }

    public void setCurrWatchCount(Integer currWatchCount) {
        this.currWatchCount = currWatchCount;
    }

    public Integer getCurrEphemeralsCount() {
        return currEphemeralsCount;
    }

    public void setCurrEphemeralsCount(Integer currEphemeralsCount) {
        this.currEphemeralsCount = currEphemeralsCount;
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

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }
}
