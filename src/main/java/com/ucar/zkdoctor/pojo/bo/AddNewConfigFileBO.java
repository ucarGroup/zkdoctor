package com.ucar.zkdoctor.pojo.bo;

import java.io.Serializable;

/**
 * Description: 像目标服务器新增某个配置文件BO
 * Created on 2018/3/1 15:32
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class AddNewConfigFileBO implements Serializable {

    private static final long serialVersionUID = 7856548311176266786L;

    /**
     * 目标服务器ip
     */
    private String host;

    /**
     * 新增配置目录
     */
    private String confDir;

    /**
     * 新增配置名称
     */
    private String confFileName;

    /**
     * 配置内容
     */
    private String confFileContent;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getConfDir() {
        return confDir;
    }

    public void setConfDir(String confDir) {
        this.confDir = confDir;
    }

    public String getConfFileName() {
        return confFileName;
    }

    public void setConfFileName(String confFileName) {
        this.confFileName = confFileName;
    }

    public String getConfFileContent() {
        return confFileContent;
    }

    public void setConfFileContent(String confFileContent) {
        this.confFileContent = confFileContent;
    }
}
