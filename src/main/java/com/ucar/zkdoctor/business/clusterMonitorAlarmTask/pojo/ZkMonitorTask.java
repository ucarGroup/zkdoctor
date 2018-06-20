package com.ucar.zkdoctor.business.clusterMonitorAlarmTask.pojo;

import java.util.Date;

public class ZkMonitorTask {
    private Integer id;

    private Integer indicatorId;

    private Integer clusterId;

    private String alertValue;

    private Integer alertInterval;

    private Integer alertFrequency;

    private Integer alertForm;

    private Boolean switchOn;

    private Integer modifyUserId;

    private Date createTime;

    private Date modifyTime;

    private String param1;

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

    public String getAlertValue() {
        return alertValue;
    }

    public void setAlertValue(String alertValue) {
        this.alertValue = alertValue == null ? null : alertValue.trim();
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1 == null ? null : param1.trim();
    }
}