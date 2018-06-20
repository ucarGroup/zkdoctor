package com.ucar.zkdoctor.pojo.vo;

import java.io.Serializable;

/**
 * Description: 更新节点数据VO
 * Created on 2018/2/5 10:19
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class UpdateZnodeDataVO implements Serializable {

    private static final long serialVersionUID = 3983724855440812081L;

    /**
     * 修改数据的节点路径
     */
    private String path;

    /**
     * 新数据内容，目前只支持string类型
     */
    private String data;

    /**
     * 指定节点的版本号
     */
    private Integer version;

    @Override
    public String toString() {
        return "UpdateZnodeDataVO{" +
                ", path='" + path + '\'' +
                ", data='" + data + '\'' +
                ", version=" + version +
                '}';
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
