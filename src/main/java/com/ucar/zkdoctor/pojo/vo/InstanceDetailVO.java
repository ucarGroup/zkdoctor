package com.ucar.zkdoctor.pojo.vo;

import com.ucar.zkdoctor.pojo.po.InstanceInfo;
import com.ucar.zkdoctor.pojo.po.InstanceState;

import java.io.Serializable;

/**
 * Description: 实例详情VO
 * Created on 2018/1/18 17:26
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class InstanceDetailVO implements Serializable {

    private static final long serialVersionUID = 6685428792542140166L;

    /**
     * 实例基本信息
     */
    private InstanceInfo instanceInfo;

    /**
     * 实例状态信息
     */
    private InstanceState instanceState;

    public InstanceInfo getInstanceInfo() {
        return instanceInfo;
    }

    public void setInstanceInfo(InstanceInfo instanceInfo) {
        this.instanceInfo = instanceInfo;
    }

    public InstanceState getInstanceState() {
        return instanceState;
    }

    public void setInstanceState(InstanceState instanceState) {
        this.instanceState = instanceState;
    }
}
