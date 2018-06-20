package com.ucar.zkdoctor.pojo.bo;

import java.io.Serializable;

/**
 * Description: 集群查询信息
 * Created on 2018/1/10 10:31
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ClusterInfoSearchBO implements Serializable {

    private static final long serialVersionUID = 7235621084735424474L;

    /**
     * 集群名称
     */
    private String clusterName;

    /**
     * 负责人用户名
     */
    private String officer;

    /**
     * 集群状态
     */
    private Integer status;

    /**
     * 部署类型
     */
    private Integer deployType;

    /**
     * 集群所属业务线
     */
    private Integer serviceLine;

    /**
     * zk版本号
     */
    private String version;

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getOfficer() {
        return officer;
    }

    public void setOfficer(String officer) {
        this.officer = officer;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDeployType() {
        return deployType;
    }

    public void setDeployType(Integer deployType) {
        this.deployType = deployType;
    }

    public Integer getServiceLine() {
        return serviceLine;
    }

    public void setServiceLine(Integer serviceLine) {
        this.serviceLine = serviceLine;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
