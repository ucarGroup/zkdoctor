package com.ucar.zkdoctor.pojo.vo;

import com.ucar.zkdoctor.util.constant.SymbolConstant;

import java.io.Serializable;

/**
 * Description: 用于接收自动化部署输入的参数
 * Created on 2018/1/22 21:24
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ZKConfigInfoVO implements Serializable {

    private static final long serialVersionUID = 5377733397038357168L;

    /**
     * server配置，server.id=host:port:port
     */
    private String serverConfig;

    /**
     * dataDir
     */
    private String dataDir;

    /**
     * clientPort
     */
    private Integer clientPort;

    /**
     * tickTime
     */
    private Integer tickTime;

    /**
     * initLimit
     */
    private Integer initLimit;

    /**
     * syncLimit
     */
    private Integer syncLimit;

    /**
     * 其余自定义配置
     */
    private String extraConfig;

    @Override
    public String toString() {
        if (serverConfig == null) {
            return null;
        }
        if (dataDir == null) {
            return null;
        }
        if (clientPort == null) {
            return null;
        }
        if (tickTime == null) {
            return null;
        }
        if (initLimit == null) {
            return null;
        }
        if (syncLimit == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("dataDir=").append(dataDir).append(SymbolConstant.NEXT_LINE)
                .append("clientPort=").append(clientPort).append(SymbolConstant.NEXT_LINE)
                .append("tickTime=").append(tickTime).append(SymbolConstant.NEXT_LINE)
                .append("initLimit=").append(initLimit).append(SymbolConstant.NEXT_LINE)
                .append("syncLimit=").append(syncLimit).append(SymbolConstant.NEXT_LINE)
                .append(extraConfig).append(SymbolConstant.NEXT_LINE)
                .append(serverConfig).append(SymbolConstant.NEXT_LINE);
        return stringBuilder.toString();
    }

    public String getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(String serverConfig) {
        this.serverConfig = serverConfig;
    }

    public String getDataDir() {
        return dataDir;
    }

    public void setDataDir(String dataDir) {
        this.dataDir = dataDir;
    }

    public Integer getClientPort() {
        return clientPort;
    }

    public void setClientPort(Integer clientPort) {
        this.clientPort = clientPort;
    }

    public Integer getTickTime() {
        return tickTime;
    }

    public void setTickTime(Integer tickTime) {
        this.tickTime = tickTime;
    }

    public Integer getInitLimit() {
        return initLimit;
    }

    public void setInitLimit(Integer initLimit) {
        this.initLimit = initLimit;
    }

    public Integer getSyncLimit() {
        return syncLimit;
    }

    public void setSyncLimit(Integer syncLimit) {
        this.syncLimit = syncLimit;
    }

    public String getExtraConfig() {
        return extraConfig;
    }

    public void setExtraConfig(String extraConfig) {
        this.extraConfig = extraConfig;
    }
}
