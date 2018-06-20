package com.ucar.zkdoctor.pojo.vo;

import java.io.Serializable;

/**
 * Description: 集群部署参数VO
 * Created on 2018/1/22 21:24
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ClusterDeployVO implements Serializable {

    private static final long serialVersionUID = 7657015818597330835L;

    /**
     * 集群名称
     */
    private String clusterName;

    /**
     * 负责人
     */
    private String officer;

    /**
     * 安装文件名称
     */
    private String installFileName;

    /**
     * 安装文件存放在的服务器目录
     */
    private String installFileDir;

    /**
     * 安装包网址
     */
    private String downloadSite;

    /**
     * 业务线
     */
    private Integer serviceLine;

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getOfficer() {
        return officer;
    }

    public void setOfficer(String officer) {
        this.officer = officer;
    }

    public String getInstallFileName() {
        return installFileName;
    }

    public void setInstallFileName(String installFileName) {
        this.installFileName = installFileName;
    }

    public String getInstallFileDir() {
        return installFileDir;
    }

    public void setInstallFileDir(String installFileDir) {
        this.installFileDir = installFileDir;
    }

    public String getDownloadSite() {
        return downloadSite;
    }

    public void setDownloadSite(String downloadSite) {
        this.downloadSite = downloadSite;
    }

    public Integer getServiceLine() {
        return serviceLine;
    }

    public void setServiceLine(Integer serviceLine) {
        this.serviceLine = serviceLine;
    }
}
