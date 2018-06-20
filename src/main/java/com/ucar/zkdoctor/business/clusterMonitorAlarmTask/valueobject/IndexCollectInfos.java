package com.ucar.zkdoctor.business.clusterMonitorAlarmTask.valueobject;

import java.io.Serializable;

public class IndexCollectInfos implements Serializable {
    private IndexCollectInfo clusterCollectInfo;
    private IndexCollectInfo instanceCollectInfo;

    public IndexCollectInfo getClusterCollectInfo() {
        return clusterCollectInfo;
    }

    public void setClusterCollectInfo(IndexCollectInfo clusterCollectInfo) {
        this.clusterCollectInfo = clusterCollectInfo;
    }

    public IndexCollectInfo getInstanceCollectInfo() {
        return instanceCollectInfo;
    }

    public void setInstanceCollectInfo(IndexCollectInfo instanceCollectInfo) {
        this.instanceCollectInfo = instanceCollectInfo;
    }
}


