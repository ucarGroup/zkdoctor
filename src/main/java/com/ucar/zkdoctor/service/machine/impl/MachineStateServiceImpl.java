package com.ucar.zkdoctor.service.machine.impl;

import com.ucar.zkdoctor.dao.mysql.MachineStateDao;
import com.ucar.zkdoctor.dao.mysql.MachineStateLogDao;
import com.ucar.zkdoctor.pojo.po.MachineState;
import com.ucar.zkdoctor.pojo.po.MachineStateLog;
import com.ucar.zkdoctor.service.machine.MachineStateService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Description: 机器状态服务接口实现类
 * Created on 2018/1/11 15:15
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Service
public class MachineStateServiceImpl implements MachineStateService {

    @Resource
    private MachineStateDao machineStateDao;

    @Resource
    private MachineStateLogDao machineStateLogDao;

    @Override
    public boolean mergeMachineState(MachineState machineState) {
        if (machineState == null) {
            return false;
        }
        return machineStateDao.insertMachineState(machineState);
    }

    @Override
    public MachineState getMachineStateByMachineId(int machineId) {
        return machineStateDao.getMachineStateByMachineId(machineId);
    }

    @Override
    public boolean deleteMachineStateByMachineId(int machineId) {
        return machineStateDao.deleteMachineStateByMachineId(machineId);
    }

    @Override
    public boolean batchInsertMachineStateLogs(List<MachineStateLog> machineStateLogList) {
        if (CollectionUtils.isEmpty(machineStateLogList)) {
            return false;
        }
        return machineStateLogDao.batchInsertMachineStateLogs(machineStateLogList);
    }

    @Override
    public boolean insertMachineStateLogs(MachineStateLog machineStateLog) {
        if (machineStateLog == null) {
            return false;
        }
        return machineStateLogDao.insertMachineStateLogs(machineStateLog);
    }

    @Override
    public List<MachineStateLog> getMachineStateLogByMachine(int machineId, Date startDate, Date endDate) {
        return machineStateLogDao.getMachineStateLogByMachine(machineId, startDate, endDate);
    }

    @Override
    public boolean cleanMachineStateLogData(Date endDate) {
        return machineStateLogDao.cleanMachineStateLogData(endDate);
    }

    @Override
    public Long cleanMachineStateLogCount(Date endDate) {
        return machineStateLogDao.cleanMachineStateLogCount(endDate);
    }

    @Override
    public MachineStateLog getMachineStateLogByTime(String host, Date dateTime) {
        return machineStateLogDao.getMachineStateLogByTime(host, dateTime);
    }
}
