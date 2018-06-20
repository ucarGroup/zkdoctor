package com.ucar.zkdoctor.pojo.po;

import com.ucar.zkdoctor.pojo.BaseTimeLineObject;

/**
 * Description: 业务线
 * Created on 2018/4/10 10:28
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ServiceLine extends BaseTimeLineObject {

    private static final long serialVersionUID = -3718012297900500354L;

    /**
     * id
     */
    private Integer id;

    /**
     * 业务线名称
     */
    private String serviceLineName;

    /**
     * 业务线描述
     */
    private String serviceLineDesc;

    @Override
    public String toString() {
        return "ServiceLine{" +
                "id=" + id +
                ", serviceLineName='" + serviceLineName + '\'' +
                ", serviceLineDesc='" + serviceLineDesc + '\'' +
                "} " + super.toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServiceLineName() {
        return serviceLineName;
    }

    public void setServiceLineName(String serviceLineName) {
        this.serviceLineName = serviceLineName;
    }

    public String getServiceLineDesc() {
        return serviceLineDesc;
    }

    public void setServiceLineDesc(String serviceLineDesc) {
        this.serviceLineDesc = serviceLineDesc;
    }
}
