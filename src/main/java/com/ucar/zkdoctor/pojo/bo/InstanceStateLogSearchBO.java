package com.ucar.zkdoctor.pojo.bo;

import java.io.Serializable;
import java.util.Date;

/**
 * Description: 实例状态历史记录查询条件
 * Created on 2018/1/10 16:02
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class InstanceStateLogSearchBO implements Serializable {

    private static final long serialVersionUID = -7181815544517795324L;

    /**
     * 集群id
     */
    private Integer clusterId;

    /**
     * 查询开始时间
     */
    private Date startDate;

    /**
     * 查询结束时间
     */
    private Date endDate;

    /**
     * 所查询数据是否只选择leader的数据
     */
    private Boolean onlyLeader;

    public Integer getClusterId() {
        return clusterId;
    }

    public void setClusterId(Integer clusterId) {
        this.clusterId = clusterId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean getOnlyLeader() {
        return onlyLeader;
    }

    public void setOnlyLeader(Boolean onlyLeader) {
        this.onlyLeader = onlyLeader;
    }
}
