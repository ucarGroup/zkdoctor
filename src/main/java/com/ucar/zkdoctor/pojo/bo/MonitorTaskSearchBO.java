package com.ucar.zkdoctor.pojo.bo;

import java.io.Serializable;

/**
 * Description: 监控任务搜索BO
 * Created on 2018/2/6 14:24
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class MonitorTaskSearchBO implements Serializable {

    private static final long serialVersionUID = 1209876326729280870L;

    /**
     * 监控指标id
     */
    private Integer indicatorId;

    /**
     * 集群id
     */
    private Integer clusterId;

    /**
     * 监控任务是否开启
     */
    private Boolean switchOn;

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

    public Boolean getSwitchOn() {
        return switchOn;
    }

    public void setSwitchOn(Boolean switchOn) {
        this.switchOn = switchOn;
    }
}
