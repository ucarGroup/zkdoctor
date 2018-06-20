package com.ucar.zkdoctor.pojo.po;

import com.ucar.zkdoctor.pojo.BaseTimeLineObject;

/**
 * Description: 监控任务信息
 * Created on 2018/2/6 11:30
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class MonitorTask extends BaseTimeLineObject {

    private static final long serialVersionUID = -227863684512418429L;

    /**
     * 监控任务id
     */
    private Integer id;

    /**
     * 监控指标id
     */
    private Integer indicatorId;

    /**
     * 集群名称
     */
    private String clusterName;

    /**
     * 集群id
     */
    private Integer clusterId;

    /**
     * 报警阀值
     */
    private String alertValue;

    /**
     * 报警间隔，单位：分钟
     */
    private Integer alertInterval;

    /**
     * 在报警间隔内，达到报警值的alertFrequency次数则进行报警
     */
    private Integer alertFrequency;

    /**
     * 报警形式，0：邮件，1：短信，2：邮件+短信
     */
    private Integer alertForm;

    /**
     * 监控任务是否开启，0：关闭，1：开启
     */
    private Boolean switchOn;

    /**
     * 修改用户id
     */
    private Integer modifyUserId;

    /**
     * 预留参数1
     */
    private String param1;

    @Override
    public String toString() {
        return "MonitorTask{" +
                "id=" + id +
                ", indicatorId=" + indicatorId +
                ", clusterName='" + clusterName + '\'' +
                ", clusterId=" + clusterId +
                ", alertValue='" + alertValue + '\'' +
                ", alertInterval=" + alertInterval +
                ", alertFrequency=" + alertFrequency +
                ", alertForm=" + alertForm +
                ", switchOn=" + switchOn +
                ", modifyUserId=" + modifyUserId +
                ", param1='" + param1 + '\'' +
                "} " + super.toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIndicatorId() {
        return indicatorId;
    }

    public void setIndicatorId(Integer indicatorId) {
        this.indicatorId = indicatorId;
    }

    public Integer getClusterId() {
        return clusterId;
    }

    public void setClusterId(Integer clusterId) {
        this.clusterId = clusterId;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getAlertValue() {
        return alertValue;
    }

    public void setAlertValue(String alertValue) {
        this.alertValue = alertValue;
    }

    public Integer getAlertInterval() {
        return alertInterval;
    }

    public void setAlertInterval(Integer alertInterval) {
        this.alertInterval = alertInterval;
    }

    public Integer getAlertFrequency() {
        return alertFrequency;
    }

    public void setAlertFrequency(Integer alertFrequency) {
        this.alertFrequency = alertFrequency;
    }

    public Integer getAlertForm() {
        return alertForm;
    }

    public void setAlertForm(Integer alertForm) {
        this.alertForm = alertForm;
    }

    public Boolean getSwitchOn() {
        return switchOn;
    }

    public void setSwitchOn(Boolean switchOn) {
        this.switchOn = switchOn;
    }

    public Integer getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(Integer modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }
}
