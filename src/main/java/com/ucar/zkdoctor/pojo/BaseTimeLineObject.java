package com.ucar.zkdoctor.pojo;

import com.ucar.zkdoctor.util.tool.DateUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Description: 所有拥有创建时间与修改时间的对象基类
 * Created on 2018/1/5 16:21
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class BaseTimeLineObject implements Serializable {

    private static final long serialVersionUID = 1846619688484980693L;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建时间字符串表示，yyyy-mm-dd hh:mm:ss
     */
    private String createTimeStr;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 修改时间字符串表示，yyyy-mm-dd hh:mm:ss
     */
    private String modifyTimeStr;

    @Override
    public String toString() {
        return "BaseTimeLineObject{" +
                "createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                '}';
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeStr() {
        if (createTime != null) {
            return DateUtil.formatYYYYMMddHHMMss(createTime);
        } else {
            return "";
        }
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public String getModifyTimeStr() {
        if (modifyTime != null) {
            return DateUtil.formatYYYYMMddHHMMss(modifyTime);
        } else {
            return "";
        }
    }

    public void setModifyTimeStr(String modifyTimeStr) {
        this.modifyTimeStr = modifyTimeStr;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
