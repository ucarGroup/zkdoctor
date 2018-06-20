package com.ucar.zkdoctor.dao.mysql;

import com.ucar.zkdoctor.pojo.po.MachineState;

/**
 * Description: 机器状态Dao
 * Created on 2018/1/9 11:07
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public interface MachineStateDao {

    /**
     * 插入新的机器状态信息
     *
     * @param machineState 机器状态
     * @return
     */
    boolean insertMachineState(MachineState machineState);

    /**
     * 通过机器id获取最新机器状态信息
     *
     * @param machineId 机器id
     * @return
     */
    MachineState getMachineStateByMachineId(int machineId);

    /**
     * 删除某机器状态信息
     *
     * @param machineId 机器id
     * @return
     */
    boolean deleteMachineStateByMachineId(int machineId);

}
