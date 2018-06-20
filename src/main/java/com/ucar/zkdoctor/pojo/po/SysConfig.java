package com.ucar.zkdoctor.pojo.po;

import java.io.Serializable;

/**
 * Description: zkdoctor系统一些指定配置，可通过后台进行配置
 * Created on 2018/1/23 13:59
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class SysConfig implements Serializable {

    private static final long serialVersionUID = 2513762099919859497L;

    /**
     * id
     */
    private Integer id;

    /**
     * 配置名称
     */
    private String configName;

    /**
     * 配置值
     */
    private String configValue;

    /**
     * 默认配置值
     */
    private String defaultConfigValue;

    /**
     * 配置描述
     */
    private String configDesc;

    @Override
    public String toString() {
        return "SysConfig{" +
                "id=" + id +
                ", configName='" + configName + '\'' +
                ", configValue='" + configValue + '\'' +
                ", defaultConfigValue='" + defaultConfigValue + '\'' +
                ", configDesc='" + configDesc + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getDefaultConfigValue() {
        return defaultConfigValue;
    }

    public void setDefaultConfigValue(String defaultConfigValue) {
        this.defaultConfigValue = defaultConfigValue;
    }

    public String getConfigDesc() {
        return configDesc;
    }

    public void setConfigDesc(String configDesc) {
        this.configDesc = configDesc;
    }
}
