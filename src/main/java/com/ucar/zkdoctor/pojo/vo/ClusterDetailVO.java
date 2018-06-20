package com.ucar.zkdoctor.pojo.vo;

import com.ucar.zkdoctor.pojo.po.ClusterInfo;
import com.ucar.zkdoctor.pojo.po.ClusterState;

import java.io.Serializable;

/**
 * Description: 集群详情VO
 * Created on 2018/1/11 9:51
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ClusterDetailVO implements Serializable {

    private static final long serialVersionUID = 7589258238846331400L;

    /**
     * 集群基本信息
     */
    private ClusterInfo clusterInfo;

    /**
     * 集群状态信息
     */
    private ClusterState clusterState;

    public ClusterInfo getClusterInfo() {
        return clusterInfo;
    }

    public void setClusterInfo(ClusterInfo clusterInfo) {
        this.clusterInfo = clusterInfo;
    }

    public ClusterState getClusterState() {
        return clusterState;
    }

    public void setClusterState(ClusterState clusterState) {
        this.clusterState = clusterState;
    }
}
