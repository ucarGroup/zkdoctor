package com.ucar.zkdoctor.pojo.bo;

import java.io.Serializable;

/**
 * Description: 监控指标搜索BO
 * Created on 2018/2/6 14:19
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class MonitorIndicatorSearchBO implements Serializable {

    private static final long serialVersionUID = -2476686051289665955L;

    /**
     * 监控指标名称
     */
    private String indicatorName;

    /**
     * 实现该监控指标的类名称
     */
    private String className;

    /**
     * 监控指标是否开启，0：关闭，1：开启
     */
    private Boolean switchOn;

    /**
     * 修改用户id
     */
    private Integer modifyUserId;

    @Override
    public String toString() {
        return "MonitorIndicatorSearchBO{" +
                "indicatorName='" + indicatorName + '\'' +
                ", className='" + className + '\'' +
                ", switchOn=" + switchOn +
                ", modifyUserId=" + modifyUserId +
                '}';
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
}
