package com.ucar.zkdoctor.pojo.po;

/**
 * Description: 集群下配置的用户信息，用于报警使用
 * Created on 2018/1/11 20:41
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ClusterAlarmUser {

    /**
     * id
     */
    private Long id;

    /**
     * 集群id
     */
    private Integer clusterId;

    /**
     * 用户id
     */
    private Integer userId;

    @Override
    public String toString() {
        return "ClusterAlarmUser{" +
                "id=" + id +
                ", clusterId=" + clusterId +
                ", userId=" + userId +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getClusterId() {
        return clusterId;
    }

    public void setClusterId(Integer clusterId) {
        this.clusterId = clusterId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
