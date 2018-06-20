package com.ucar.zkdoctor.pojo.po;

import com.ucar.zkdoctor.pojo.BaseTimeLineObject;

/**
 * Description: 机器状态
 * Created on 2018/1/9 11:05
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class MachineState extends BaseTimeLineObject {

    private static final long serialVersionUID = 5018385768820029267L;

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
     * cpu使用率,百分制数
     */
    private String cpuUsage;

    /**
     * 机器负载
     */
    private String avgLoad;

    /**
     * io网络流量,单位:KB/s
     */
    private String netTraffic;

    /**
     * 内存使用率,百分制数
     */
    private String memoryUsage;

    /**
     * 空闲内存量,单位:kb
     */
    private String memoryFree;

    /**
     * 总内存量,单位:kb
     */
    private String memoryTotal;

    /**
     * 磁盘使用率,data目录所在磁盘使用率
     */
    private String diskUsage;

    /**
     * dataDir下磁盘使用大小，单位：GB
     */
    private String dataDiskUsed;

    /**
     * dataDir下磁盘总大小，单位：GB
     */
    private String dataDiskTotal;

    /**
     * 预留参数
     */
    private String param1;

    @Override
    public String toString() {
        return "MachineState{" +
                "id=" + id +
                ", machineId=" + machineId +
                ", host='" + host + '\'' +
                ", cpuUsage='" + cpuUsage + '\'' +
                ", avgLoad='" + avgLoad + '\'' +
                ", netTraffic='" + netTraffic + '\'' +
                ", memoryUsage='" + memoryUsage + '\'' +
                ", memoryFree='" + memoryFree + '\'' +
                ", memoryTotal='" + memoryTotal + '\'' +
                ", diskUsage='" + diskUsage + '\'' +
                ", dataDiskUsed='" + dataDiskUsed + '\'' +
                ", dataDiskTotal='" + dataDiskTotal + '\'' +
                ", param1='" + param1 + '\'' +
                "} " + super.toString();
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

    public String getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(String cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public String getAvgLoad() {
        return avgLoad;
    }

    public void setAvgLoad(String avgLoad) {
        this.avgLoad = avgLoad;
    }

    public String getNetTraffic() {
        return netTraffic;
    }

    public void setNetTraffic(String netTraffic) {
        this.netTraffic = netTraffic;
    }

    public String getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(String memoryUsage) {
        this.memoryUsage = memoryUsage;
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

    public String getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(String diskUsage) {
        this.diskUsage = diskUsage;
    }

    public String getDataDiskUsed() {
        return dataDiskUsed;
    }

    public void setDataDiskUsed(String dataDiskUsed) {
        this.dataDiskUsed = dataDiskUsed;
    }

    public String getDataDiskTotal() {
        return dataDiskTotal;
    }

    public void setDataDiskTotal(String dataDiskTotal) {
        this.dataDiskTotal = dataDiskTotal;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }
}
