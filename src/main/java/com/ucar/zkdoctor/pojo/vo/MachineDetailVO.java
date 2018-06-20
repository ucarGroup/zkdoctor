package com.ucar.zkdoctor.pojo.vo;

import com.ucar.zkdoctor.pojo.po.MachineInfo;
import com.ucar.zkdoctor.pojo.po.MachineState;

import java.io.Serializable;

/**
 * Description: 机器详情VO
 * Created on 2018/1/19 16:55
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class MachineDetailVO implements Serializable {

    private static final long serialVersionUID = 1502401795667199341L;

    /**
     * 机器基本信息
     */
    private MachineInfo machineInfo;

    /**
     * 机器状态信息
     */
    private MachineState machineState;

    public MachineInfo getMachineInfo() {
        return machineInfo;
    }

    public void setMachineInfo(MachineInfo machineInfo) {
        this.machineInfo = machineInfo;
    }

    public MachineState getMachineState() {
        return machineState;
    }

    public void setMachineState(MachineState machineState) {
        this.machineState = machineState;
    }
}
