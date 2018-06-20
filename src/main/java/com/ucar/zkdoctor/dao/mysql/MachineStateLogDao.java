package com.ucar.zkdoctor.dao.mysql;

import com.ucar.zkdoctor.pojo.po.MachineStateLog;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Description: 机器状态历史信息Dao
 * Created on 2018/1/9 17:05
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public interface MachineStateLogDao {

    /**
     * 批量写入机器运行状态记录
     *
     * @param machineStateLogList 机器状态信息
     * @return
     */
    boolean batchInsertMachineStateLogs(List<MachineStateLog> machineStateLogList);

    /**
     * 插入某条机器运行状态记录
     *
     * @param machineStateLog 机器状态信息
     * @return
     */
    boolean insertMachineStateLogs(MachineStateLog machineStateLog);

    /**
     * 获取某机器的历史状态信息
     *
     * @param machineId 机器id
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return
     */
    List<MachineStateLog> getMachineStateLogByMachine(@Param("machineId") int machineId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 删除此时间之前的所有机器状态历史数据
     *
     * @param endDate 此时间之前的数据将删除
     * @return
     */
    boolean cleanMachineStateLogData(Date endDate);

    /**
     * 需要删除的机器状态历史数据记录的数量
     *
     * @param endDate 获取此时间之前的所有数据条数
     * @return
     */
    Long cleanMachineStateLogCount(Date endDate);

    /**
     * 获取某一时间点机器状态信息
     *
     * @param host     机器ip
     * @param dateTime 时间点
     * @return
     */
    MachineStateLog getMachineStateLogByTime(@Param("host") String host, @Param("dateTime") Date dateTime);
}
