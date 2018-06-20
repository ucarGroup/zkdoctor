package com.ucar.zkdoctor.pojo.vo;

import java.io.Serializable;

/**
 * Description: znode详细信息，包含data以及stat
 * Created on 2018/2/2 13:52
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ZnodeDetailInfoVO implements Serializable {

    private static final long serialVersionUID = -3996124269660139759L;

    /**
     * 节点数据
     */
    private String data;

    /**
     * 创建该节点的事务id
     */
    private String czxid;

    /**
     * 最后一次修改该节点的事务id,如无修改，则为czxid
     */
    private String mzxid;

    /**
     * 创建该节点的时间，格式‘YYYY-MM-dd HH-MM-ss’
     */
    private String ctime;

    /**
     * 最后一次修改该节点的时间
     */
    private String mtime;

    /**
     * 节点版本号
     */
    private Integer version;

    /**
     * 最新子节点的版本号
     */
    private Integer cversion;

    /**
     * 设置节点ACL的版本号
     */
    private Integer aversion;

    /**
     * 临时节点所属的sessionId，若未持久化节点，该值为0
     */
    private String ephemeralOwner;

    /**
     * 数据长度
     */
    private Integer dataLength;

    /**
     * 子节点数量
     */
    private Integer numChildren;

    /**
     * 最后一次修改该节点或最后一次修改该节点子节点的事务id（如无修改，则为czxid）
     */
    private String pzxid;

    @Override
    public String toString() {
        return "ZnodeDetailInfoVO{" +
                "data='" + data + '\'' +
                ", czxid='" + czxid + '\'' +
                ", mzxid='" + mzxid + '\'' +
                ", ctime='" + ctime + '\'' +
                ", mtime='" + mtime + '\'' +
                ", version=" + version +
                ", cversion=" + cversion +
                ", aversion=" + aversion +
                ", ephemeralOwner='" + ephemeralOwner + '\'' +
                ", dataLength=" + dataLength +
                ", numChildren=" + numChildren +
                ", pzxid='" + pzxid + '\'' +
                '}';
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCzxid() {
        return czxid;
    }

    public void setCzxid(String czxid) {
        this.czxid = czxid;
    }

    public String getMzxid() {
        return mzxid;
    }

    public void setMzxid(String mzxid) {
        this.mzxid = mzxid;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getMtime() {
        return mtime;
    }

    public void setMtime(String mtime) {
        this.mtime = mtime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getCversion() {
        return cversion;
    }

    public void setCversion(Integer cversion) {
        this.cversion = cversion;
    }

    public Integer getAversion() {
        return aversion;
    }

    public void setAversion(Integer aversion) {
        this.aversion = aversion;
    }

    public String getEphemeralOwner() {
        return ephemeralOwner;
    }

    public void setEphemeralOwner(String ephemeralOwner) {
        this.ephemeralOwner = ephemeralOwner;
    }

    public Integer getDataLength() {
        return dataLength;
    }

    public void setDataLength(Integer dataLength) {
        this.dataLength = dataLength;
    }

    public Integer getNumChildren() {
        return numChildren;
    }

    public void setNumChildren(Integer numChildren) {
        this.numChildren = numChildren;
    }

    public String getPzxid() {
        return pzxid;
    }

    public void setPzxid(String pzxid) {
        this.pzxid = pzxid;
    }
}
