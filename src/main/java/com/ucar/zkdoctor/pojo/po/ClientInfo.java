package com.ucar.zkdoctor.pojo.po;

import com.ucar.zkdoctor.pojo.BaseTimeLineObject;
import com.ucar.zkdoctor.util.tool.DateUtil;

import java.util.Date;
import java.util.List;

/**
 * Description: 客户端连接信息PO
 * Created on 2018/2/23 14:47
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ClientInfo extends BaseTimeLineObject {

    private static final long serialVersionUID = 5393639171980849471L;

    /**
     * 唯一识别某条客户端连接信息的key
     */
    private String key;

    /**
     * id
     */
    private Long id;

    /**
     * 实例id
     */
    private Integer instanceId;

    /**
     * 集群id
     */
    private Integer clusterId;

    /**
     * 客户端ip
     */
    private String clientIp;

    /**
     * 客户端端口
     */
    private Integer clientPort;

    /**
     * session id
     */
    private String sid;

    /**
     * 堆积请求数
     */
    private Long queued;

    /**
     * 收包数
     */
    private Long recved;

    /**
     * 发包数
     */
    private Long sent;

    /**
     * 连接时间戳
     */
    private Long est;

    /**
     * 连接时间字符表示
     */
    private String estDate;

    /**
     * 超时时间
     */
    private Integer toTime;

    /**
     * 最后客户端请求id
     */
    private String lcxid;

    /**
     * 最后事务id（状态变更id）
     */
    private String lzxid;

    /**
     * 最后响应时间戳
     */
    private Long lresp;

    /**
     * 最后响应时间字符表示
     */
    private String lrespStr;

    /**
     * 最新延时
     */
    private Integer llat;

    /**
     * 最小延时
     */
    private Integer minlat;

    /**
     * 平均延时
     */
    private Integer avglat;

    /**
     * 最大延时
     */
    private Integer maxlat;

    /**
     * 如果连接信息按照ip排序，此字段表示连接个数
     */
    private Integer number;

    /**
     * 如果连接信息按照ip排序，此字段表示该ip下的连接详细信息
     */
    private List<ClientInfo> clientInfoList;

    /**
     * 连接信息的字符表示
     */
    private String info;

    @Override
    public String toString() {
        return "ClientInfo{" +
                "key='" + key + '\'' +
                ", id=" + id +
                ", instanceId=" + instanceId +
                ", clusterId=" + clusterId +
                ", clientIp='" + clientIp + '\'' +
                ", clientPort=" + clientPort +
                ", sid='" + sid + '\'' +
                ", queued=" + queued +
                ", recved=" + recved +
                ", sent=" + sent +
                ", est=" + est +
                ", estDate='" + estDate + '\'' +
                ", toTime=" + toTime +
                ", lcxid='" + lcxid + '\'' +
                ", lzxid='" + lzxid + '\'' +
                ", lresp=" + lresp +
                ", lrespStr='" + lrespStr + '\'' +
                ", llat=" + llat +
                ", minlat=" + minlat +
                ", avglat=" + avglat +
                ", maxlat=" + maxlat +
                ", info='" + info + '\'' +
                "} " + super.toString();
    }

    public String getKey() {
        if (sid != null) {
            return sid + getCreateTime();
        } else {
            return clientIp + getCreateTime();
        }
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Integer instanceId) {
        this.instanceId = instanceId;
    }

    public Integer getClusterId() {
        return clusterId;
    }

    public void setClusterId(Integer clusterId) {
        this.clusterId = clusterId;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public Integer getClientPort() {
        return clientPort;
    }

    public void setClientPort(Integer clientPort) {
        this.clientPort = clientPort;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public Long getQueued() {
        return queued;
    }

    public void setQueued(Long queued) {
        this.queued = queued;
    }

    public Long getRecved() {
        return recved;
    }

    public void setRecved(Long recved) {
        this.recved = recved;
    }

    public Long getSent() {
        return sent;
    }

    public void setSent(Long sent) {
        this.sent = sent;
    }

    public Long getEst() {
        return est;
    }

    public void setEst(Long est) {
        this.est = est;
    }

    public String getEstDate() {
        if (est != null) {
            return DateUtil.formatYYYYMMddHHMMss(new Date(est));
        } else {
            return "";
        }
    }

    public void setEstDate(String estDate) {
        this.estDate = estDate;
    }

    public Integer getToTime() {
        return toTime;
    }

    public void setToTime(Integer toTime) {
        this.toTime = toTime;
    }

    public String getLcxid() {
        return lcxid;
    }

    public void setLcxid(String lcxid) {
        this.lcxid = lcxid;
    }

    public String getLzxid() {
        return lzxid;
    }

    public void setLzxid(String lzxid) {
        this.lzxid = lzxid;
    }

    public Long getLresp() {
        return lresp;
    }

    public void setLresp(Long lresp) {
        this.lresp = lresp;
    }

    public String getLrespStr() {
        if (lresp != null) {
            return DateUtil.formatYYYYMMddHHMMss(new Date(lresp));
        } else {
            return "";
        }
    }

    public void setLrespStr(String lrespStr) {
        this.lrespStr = lrespStr;
    }

    public Integer getLlat() {
        return llat;
    }

    public void setLlat(Integer llat) {
        this.llat = llat;
    }

    public Integer getMinlat() {
        return minlat;
    }

    public void setMinlat(Integer minlat) {
        this.minlat = minlat;
    }

    public Integer getAvglat() {
        return avglat;
    }

    public void setAvglat(Integer avglat) {
        this.avglat = avglat;
    }

    public Integer getMaxlat() {
        return maxlat;
    }

    public void setMaxlat(Integer maxlat) {
        this.maxlat = maxlat;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public List<ClientInfo> getClientInfoList() {
        return clientInfoList;
    }

    public void setClientInfoList(List<ClientInfo> clientInfoList) {
        this.clientInfoList = clientInfoList;
    }

    public String getInfo() {
        return this.toString();
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
