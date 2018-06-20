package com.ucar.zkdoctor.pojo.po;

import com.ucar.zkdoctor.pojo.BaseTimeLineObject;

/**
 * Description: 监控指标信息
 * Created on 2018/2/6 11:30
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class MonitorIndicator extends BaseTimeLineObject {

    private static final long serialVersionUID = -567029039805546043L;

    /**
     * 监控指标id
     */
    private Integer id;

    /**
     * 监控指标名称
     */
    private String indicatorName;

    /**
     * 实现该监控指标的类名
     */
    private String className;

    /**
     * 默认报警阈值
     */
    private String defaultAlertValue;

    /**
     * 默认报警间隔，单位：分钟
     */
    private Integer defaultAlertInterval;

    /**
     * 在报警间隔内，达到报警值的defaultAlertFrequency次数则进行报警
     */
    private Integer defaultAlertFrequency;

    /**
     * 默认报警形式，0：邮件，1：短信，2：邮件+短信
     */
    private Integer defaultAlertForm;

    /**
     * 警报值单位
     */
    private String alertValueUnit;

    /**
     * 监控指标是否开启，0：关闭，1：开启
     */
    private Boolean switchOn;

    /**
     * 修改用户id
     */
    private Integer modifyUserId;

    /**
     * 备注说明
     */
    private String info;

    /**
     * 预留参数1
     */
    private String param1;

    @Override
    public String toString() {
        return "MonitorIndicator{" +
                "id=" + id +
                ", indicatorName='" + indicatorName + '\'' +
                ", className='" + className + '\'' +
                ", defaultAlertValue='" + defaultAlertValue + '\'' +
                ", defaultAlertInterval=" + defaultAlertInterval +
                ", defaultAlertFrequency=" + defaultAlertFrequency +
                ", defaultAlertForm=" + defaultAlertForm +
                ", alertValueUnit='" + alertValueUnit + '\'' +
                ", switchOn=" + switchOn +
                ", modifyUserId=" + modifyUserId +
                ", info='" + info + '\'' +
                ", param1='" + param1 + '\'' +
                "} " + super.toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIndicatorName() {
        return indicatorName;
    }

    public void setIndicatorName(String indicatorName) {
        this.indicatorName = indicatorName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDefaultAlertValue() {
        return defaultAlertValue;
    }

    public void setDefaultAlertValue(String defaultAlertValue) {
        this.defaultAlertValue = defaultAlertValue;
    }

    public Integer getDefaultAlertInterval() {
        return defaultAlertInterval;
    }

    public void setDefaultAlertInterval(Integer defaultAlertInterval) {
        this.defaultAlertInterval = defaultAlertInterval;
    }

    public Integer getDefaultAlertFrequency() {
        return defaultAlertFrequency;
    }

    public void setDefaultAlertFrequency(Integer defaultAlertFrequency) {
        this.defaultAlertFrequency = defaultAlertFrequency;
    }

    public Integer getDefaultAlertForm() {
        return defaultAlertForm;
    }

    public void setDefaultAlertForm(Integer defaultAlertForm) {
        this.defaultAlertForm = defaultAlertForm;
    }

    public String getAlertValueUnit() {
        return alertValueUnit;
    }

    public void setAlertValueUnit(String alertValueUnit) {
        this.alertValueUnit = alertValueUnit;
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }
}
