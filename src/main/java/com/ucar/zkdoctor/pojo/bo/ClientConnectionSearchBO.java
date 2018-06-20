package com.ucar.zkdoctor.pojo.bo;

import java.io.Serializable;

/**
 * Description: 客户端连接信息查询BO
 * Created on 2018/2/23 16:59
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ClientConnectionSearchBO implements Serializable {

    private static final long serialVersionUID = 5679522468625763190L;

    /**
     * 集群id
     */
    private Integer clusterId;

    /**
     * 实例id
     */
    private Integer instanceId;

    /**
     * 查询开始时间
     */
    private String startDate;

    /**
     * 查询结束时间
     */
    private String endDate;

    /**
     * 排序字段
     */
    private String orderBy;

    public Integer getClusterId() {
        return clusterId;
    }

    public void setClusterId(Integer clusterId) {
        this.clusterId = clusterId;
    }

    public Integer getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Integer instanceId) {
        this.instanceId = instanceId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
