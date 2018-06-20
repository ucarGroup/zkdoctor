package com.ucar.zkdoctor.pojo.po;

import com.ucar.zkdoctor.pojo.BaseTimeLineObject;

/**
 * Description: 机器信息
 * Created on 2018/1/9 11:04
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class MachineInfo extends BaseTimeLineObject {

    private static final long serialVersionUID = -4970501783715339590L;

    /**
     * 机器id
     */
    private Integer id;

    /**
     * 机器ip
     */
    private String host;

    /**
     * 内存大小,单位:GB
     */
    private Integer memory;

    /**
     * cpu个数
     */
    private Integer cpu;

    /**
     * 是否为虚机
     */
    private Boolean virtual;

    /**
     * 如果是虚机,对应物理机ip
     */
    private String realHost;

    /**
     * 机器所在机房名称
     */
    private String room;

    /**
     * 机器是否处于监控状态，true：监控中，false：未监控
     */
    private Boolean monitor;

    /**
     * 机器是否可用
     */
    private Boolean available;

    /**
     * 机器所属业务线，请参考CommonEnumClass.ServiceLineEnum。默认为default：0
     */
    private Integer serviceLine;

    /**
     * 机器名
     */
    private String hostName;

    /**
     * 域名
     */
    private String hostDomain;

    /**
     * 预留参数
     */
    private String param1;

    @Override
    public String toString() {
        return "MachineInfo{" +
                "id=" + id +
                ", host='" + host + '\'' +
                ", memory=" + memory +
                ", cpu=" + cpu +
                ", virtual=" + virtual +
                ", realHost='" + realHost + '\'' +
                ", room='" + room + '\'' +
                ", monitor=" + monitor +
                ", available=" + available +
                ", serviceLine=" + serviceLine +
                ", hostName='" + hostName + '\'' +
                ", hostDomain='" + hostDomain + '\'' +
                ", param1='" + param1 + '\'' +
                "} " + super.toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public Integer getCpu() {
        return cpu;
    }

    public void setCpu(Integer cpu) {
        this.cpu = cpu;
    }

    public Boolean getVirtual() {
        return virtual;
    }

    public void setVirtual(Boolean virtual) {
        this.virtual = virtual;
    }

    public String getRealHost() {
        return realHost;
    }

    public void setRealHost(String realHost) {
        this.realHost = realHost;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Boolean getMonitor() {
        return monitor;
    }

    public void setMonitor(Boolean monitor) {
        this.monitor = monitor;
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

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostDomain() {
        return hostDomain;
    }

    public void setHostDomain(String hostDomain) {
        this.hostDomain = hostDomain;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }
}
