package com.ucar.zkdoctor.pojo.bo;

import java.io.Serializable;

/**
 * Description: 机器查询信息
 * Created on 2018/1/10 14:36
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class MachineSearchBO implements Serializable {

    private static final long serialVersionUID = -7390578957623112712L;

    /**
     * 机器ip
     */
    private String host;

    /**
     * 是否为虚机
     */
    private Boolean virtual;

    /**
     * 机器所在机房名称
     */
    private String room;

    /**
     * 机器是否可用
     */
    private Boolean available;

    /**
     * 机器所属业务线
     */
    private Integer serviceLine;

    /**
     * 域名
     */
    private String hostDomain;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Boolean getVirtual() {
        return virtual;
    }

    public void setVirtual(Boolean virtual) {
        this.virtual = virtual;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Integer getServiceLine() {
        return serviceLine;
    }

    public void setServiceLine(Integer serviceLine) {
        this.serviceLine = serviceLine;
    }

    public String getHostDomain() {
        return hostDomain;
    }

    public void setHostDomain(String hostDomain) {
        this.hostDomain = hostDomain;
    }
}
