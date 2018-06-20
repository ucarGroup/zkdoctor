package com.ucar.zkdoctor.pojo.bo;

import java.io.Serializable;

/**
 * Description: 实例查询信息
 * Created on 2018/1/9 17:21
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class InstanceInfoSearchBO implements Serializable {

    private static final long serialVersionUID = -7448713796459220199L;

    /**
     * 集群id
     */
    private Integer clusterId;

    /**
     * 机器id
     */
    private Integer machineId;

    /**
     * 实例ip
     */
    private String host;

    /**
     * 部署类型
     */
    private Integer deployType;

    /**
     * 实例状态
     */
    private Integer status;

    public Integer getClusterId() {
        return clusterId;
    }

    public void setClusterId(Integer clusterId) {
        this.clusterId = clusterId;
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

    public Integer getDeployType() {
        return deployType;
    }

    public void setDeployType(Integer deployType) {
        this.deployType = deployType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
