package com.ucar.zkdoctor.pojo.vo;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * Description: zk server基本配置信息，配置项：server
 * Created on 2018/1/23 9:14
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ZKServerInfoVO implements Serializable {

    private static final long serialVersionUID = -8024617614674239902L;

    /**
     * server id
     */
    private Integer serverId;

    /**
     * server host
     */
    private String host;

    /**
     * 客户端port
     */
    private Integer clientPort;

    /**
     * 法人端口
     */
    private Integer quorumPort;

    /**
     * 选举端口
     */
    private Integer electionPort;

    /**
     * 类型
     */
    private String peerType;

    /**
     * 域名
     */
    private String domain;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("server.").append(serverId).append("=");
        // 支持域名配置
        if (StringUtils.isBlank(domain) || "undefined".equals(domain)) {
            sb.append(host).append(":");
        } else {
            sb.append(domain).append(":");
        }
        sb.append(quorumPort).append(":");
        sb.append(electionPort).append(":");
        sb.append(peerType);
        return sb.toString();
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getClientPort() {
        return clientPort;
    }

    public void setClientPort(Integer clientPort) {
        this.clientPort = clientPort;
    }

    public Integer getQuorumPort() {
        return quorumPort;
    }

    public void setQuorumPort(Integer quorumPort) {
        this.quorumPort = quorumPort;
    }

    public Integer getElectionPort() {
        return electionPort;
    }

    public void setElectionPort(Integer electionPort) {
        this.electionPort = electionPort;
    }

    public String getPeerType() {
        return peerType;
    }

    public void setPeerType(String peerType) {
        this.peerType = peerType;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
