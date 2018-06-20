package com.ucar.zkdoctor.pojo.po;

import java.io.Serializable;
import java.util.Date;

/**
 * Description: 机器状态历史信息
 * Created on 2018/1/9 16:33
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class MachineStateLog implements Serializable {

    private static final long serialVersionUID = -5684731853579033151L;

    /**
     * id
     */
    private Integer id;

    /**
     * 机器id
     */
    private Integer machineId;

    /**
     * 机器ip
     */
    private String host;

    /**
     * 用户级别下消耗的CPU时间的比例
     */
    private String cpuUserPercent;

    /**
     * nice优先级用户级集群程序消耗的CPU时间比例
     */
    private String cpuNicePercent;

    /**
     * 系统核心级别下消耗的CPU时间的比例
     */
    private String cpuSysPercent;

    /**
     * 单cpu使用率情况,以空格分隔
     */
    private String cpuSingleUsage;

    /**
     * cpu使用率,百分制数
     */
    private String cpuUsage;

    /**
     * 等待I/O操作占用CPU总时间的百分比
     */
    private String ioWaitPercent;

    /**
     * CPU空闲时间占用CPU总时间的百分比
     */
    private String idlePercent;

    /**
     * 空闲内存总量，单位：kb
     */
    private String memoryFree;

    /**
     * 已用内存总量，单位：kb
     */
    private String memoryTotal;

    /**
     * buffers内存总量，单位：kb
     */
    private String buffers;

    /**
     * cached内存总量，单位：kb
     */
    private String cached;

    /**
     * 内存使用率,百分制数
     */
    private String memoryUsage;

    /**
     * 机器负载
     */
    private String avgLoad;

    /**
     * 磁盘使用情况，每个挂载点以-分隔，挂载点数据之间以空格分隔，记录已用空间大小、空闲空间大小，单位：byte
     */
    private String diskSituation;

    /**
     * 磁盘空闲率,每个挂载点以-分隔，挂载点数据之间以空格分隔，记录已用空间大小、空闲空间大小，单位：byte
     */
    private String diskFreePercent;

    /**
     * 磁盘使用率,data目录所在磁盘使用率
     */
    private String diskUsage;

    /**
     * io网络流量,单位:KB/s
     */
    private String netTraffic;

    /**
     * 入流量信息，取top10客户端ip以及其对应的入流量，单位：KB/s
     */
    private String netFlowIn;

    /**
     * 出流量信息，取top10客户端ip以及其对应的出流量，单位：KB/s
     */
    private String netFlowOut;

    /**
     * 收集时间
     */
    private Date createTime;

    /**
     * 预留参数
     */
    private String param1;

    @Override
    public String toString() {
        return "MachineStateLog{" +
                "id=" + id +
                ", machineId=" + machineId +
                ", host='" + host + '\'' +
                ", cpuUserPercent='" + cpuUserPercent + '\'' +
                ", cpuNicePercent='" + cpuNicePercent + '\'' +
                ", cpuSysPercent='" + cpuSysPercent + '\'' +
                ", cpuSingleUsage='" + cpuSingleUsage + '\'' +
                ", cpuUsage='" + cpuUsage + '\'' +
                ", ioWaitPercent='" + ioWaitPercent + '\'' +
                ", idlePercent='" + idlePercent + '\'' +
                ", memoryFree='" + memoryFree + '\'' +
                ", memoryTotal='" + memoryTotal + '\'' +
                ", buffers='" + buffers + '\'' +
                ", cached='" + cached + '\'' +
                ", memoryUsage='" + memoryUsage + '\'' +
                ", avgLoad='" + avgLoad + '\'' +
                ", diskSituation='" + diskSituation + '\'' +
                ", diskFreePercent='" + diskFreePercent + '\'' +
                ", diskUsage='" + diskUsage + '\'' +
                ", netTraffic='" + netTraffic + '\'' +
                ", netFlowIn='" + netFlowIn + '\'' +
                ", netFlowOut='" + netFlowOut + '\'' +
                ", createTime=" + createTime +
                ", param1='" + param1 + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getCpuUserPercent() {
        return cpuUserPercent;
    }

    public void setCpuUserPercent(String cpuUserPercent) {
        this.cpuUserPercent = cpuUserPercent;
    }

    public String getCpuNicePercent() {
        return cpuNicePercent;
    }

    public void setCpuNicePercent(String cpuNicePercent) {
        this.cpuNicePercent = cpuNicePercent;
    }

    public String getCpuSysPercent() {
        return cpuSysPercent;
    }

    public void setCpuSysPercent(String cpuSysPercent) {
        this.cpuSysPercent = cpuSysPercent;
    }

    public String getCpuSingleUsage() {
        return cpuSingleUsage;
    }

    public void setCpuSingleUsage(String cpuSingleUsage) {
        this.cpuSingleUsage = cpuSingleUsage;
    }

    public String getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(String cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public String getIoWaitPercent() {
        return ioWaitPercent;
    }

    public void setIoWaitPercent(String ioWaitPercent) {
        this.ioWaitPercent = ioWaitPercent;
    }

    public String getIdlePercent() {
        return idlePercent;
    }

    public void setIdlePercent(String idlePercent) {
        this.idlePercent = idlePercent;
    }

    public String getMemoryFree() {
        return memoryFree;
    }

    public void setMemoryFree(String memoryFree) {
        this.memoryFree = memoryFree;
    }

    public String getMemoryTotal() {
        return memoryTotal;
    }

    public void setMemoryTotal(String memoryTotal) {
        this.memoryTotal = memoryTotal;
    }

    public String getBuffers() {
        return buffers;
    }

    public void setBuffers(String buffers) {
        this.buffers = buffers;
    }

    public String getCached() {
        return cached;
    }

    public void setCached(String cached) {
        this.cached = cached;
    }

    public String getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(String memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public String getAvgLoad() {
        return avgLoad;
    }

    public void setAvgLoad(String avgLoad) {
        this.avgLoad = avgLoad;
    }

    public String getDiskSituation() {
        return diskSituation;
    }

    public void setDiskSituation(String diskSituation) {
        this.diskSituation = diskSituation;
    }

    public String getDiskFreePercent() {
        return diskFreePercent;
    }

    public void setDiskFreePercent(String diskFreePercent) {
        this.diskFreePercent = diskFreePercent;
    }

    public String getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(String diskUsage) {
        this.diskUsage = diskUsage;
    }

    public String getNetTraffic() {
        return netTraffic;
    }

    public void setNetTraffic(String netTraffic) {
        this.netTraffic = netTraffic;
    }

    public String getNetFlowIn() {
        return netFlowIn;
    }

    public void setNetFlowIn(String netFlowIn) {
        this.netFlowIn = netFlowIn;
    }

    public String getNetFlowOut() {
        return netFlowOut;
    }

    public void setNetFlowOut(String netFlowOut) {
        this.netFlowOut = netFlowOut;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }
}
