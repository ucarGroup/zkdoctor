package com.ucar.zkdoctor.pojo.po;

import com.ucar.zkdoctor.pojo.BaseTimeLineObject;

/**
 * Description: 集群基本信息
 * Created on 2018/1/9 11:00
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ClusterInfo extends BaseTimeLineObject {

    private static final long serialVersionUID = -4829767121215284440L;

    /**
     * 集群id
     */
    private Integer id;

    /**
     * 集群名称
     */
    private String clusterName;

    /**
     * 负责人用户名
     */
    private String officer;

    /**
     * 实例个数
     */
    private Integer instanceNumber;

    /**
     * 集群状态，请参考ClusterEnumClass.ClusterStatusEnum
     */
    private Integer status;

    /**
     * 部署类型，请参考ZKServerEnumClass.DeployTypeEnum
     */
    private Integer deployType;

    /**
     * 集群所属业务线，请参考CommonEnumClass.ServiceLineEnum。默认为default：0
     */
    private Integer serviceLine = 0;

    /**
     * zk版本号
     */
    private String version;

    /**
     * 集群描述
     */
    private String intro;

    /**
     * 预留参数
     */
    private String param1;

    @Override
    public String toString() {
        return "ClusterInfo{" +
                "id=" + id +
                ", clusterName='" + clusterName + '\'' +
                ", officer='" + officer + '\'' +
                ", instanceNumber=" + instanceNumber +
                ", status=" + status +
                ", deployType=" + deployType +
                ", serviceLine=" + serviceLine +
                ", version='" + version + '\'' +
                ", intro='" + intro + '\'' +
                ", param1='" + param1 + '\'' +
                "} " + super.toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Integer getInstanceNumber() {
        return instanceNumber;
    }

    public void setInstanceNumber(Integer instanceNumber) {
        this.instanceNumber = instanceNumber;
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

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }
}
