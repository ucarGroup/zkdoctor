package com.ucar.zkdoctor.pojo.dto;

import java.io.Serializable;

/**
 * Description: 连接信息，通过cons命令获取
 * Created on 2018/1/31 13:54
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ConnectionInfoDTO implements Serializable {

    private static final long serialVersionUID = 183473282024953445L;

    /**
     * ip
     */
    private String ip;

    /**
     * 端口
     */
    private Integer port;

    /**
     * host:port，唯一识别一个连接信息
     */
    private String hostInfo;

    /**
     * 索引
     */
    private Integer index;

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
     * session id
     */
    private String sid;

    /**
     * 最后操作命令
     */
    private String lop;

    /**
     * 连接时间戳
     */
    private Long est;

    /**
     * 超时时间
     */
    private Integer to;

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
     * 原信息
     */
    private String infoLine;

    @Override
    public String toString() {
        return "ConnectionInfoDTO{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", hostInfo='" + hostInfo + '\'' +
                ", index=" + index +
                ", queued=" + queued +
                ", recved=" + recved +
                ", sent=" + sent +
                ", sid='" + sid + '\'' +
                ", lop='" + lop + '\'' +
                ", est=" + est +
                ", to=" + to +
                ", lcxid='" + lcxid + '\'' +
                ", lzxid='" + lzxid + '\'' +
                ", lresp=" + lresp +
                ", llat=" + llat +
                ", minlat=" + minlat +
                ", avglat=" + avglat +
                ", maxlat=" + maxlat +
                ", infoLine='" + infoLine + '\'' +
                '}';
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getHostInfo() {
        return hostInfo;
    }

    public void setHostInfo(String hostInfo) {
        this.hostInfo = hostInfo;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
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

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getLop() {
        return lop;
    }

    public void setLop(String lop) {
        this.lop = lop;
    }

    public Long getEst() {
        return est;
    }

    public void setEst(Long est) {
        this.est = est;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
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

    public String getInfoLine() {
        return infoLine;
    }

    public void setInfoLine(String infoLine) {
        this.infoLine = infoLine;
    }
}
