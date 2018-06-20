package com.ucar.zkdoctor.service.machine.impl;

import com.ucar.zkdoctor.dao.mysql.MachineInfoDao;
import com.ucar.zkdoctor.pojo.bo.HostAndPort;
import com.ucar.zkdoctor.pojo.bo.MachineSearchBO;
import com.ucar.zkdoctor.pojo.po.MachineInfo;
import com.ucar.zkdoctor.pojo.po.MachineState;
import com.ucar.zkdoctor.pojo.vo.MachineDetailVO;
import com.ucar.zkdoctor.service.machine.MachineService;
import com.ucar.zkdoctor.service.machine.MachineStateService;
import com.ucar.zkdoctor.service.system.ServiceLineService;
import com.ucar.zkdoctor.util.constant.SymbolConstant;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description: 机器服务接口实现类
 * Created on 2018/1/9 17:08
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Service
public class MachineServiceImpl implements MachineService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MachineServiceImpl.class);

    @Resource
    private MachineInfoDao machineInfoDao;

    @Resource
    private MachineStateService machineStateService;

    @Resource
    private ServiceLineService serviceLineService;

    @Override
    public boolean insertMachine(MachineInfo machineInfo) {
        if (machineInfo == null) {
            return false;
        }
        try {
            // 新加入机器，默认收集信息开关关闭
            machineInfo.setMonitor(false);
            machineInfo.setCreateTime(new Date());
            return machineInfoDao.insertMachine(machineInfo);
        } catch (Exception e) {
            LOGGER.error("Insert new machine {} failed.", machineInfo, e);
            return false;
        }
    }

    @Override
    public MachineInfo getMachineInfoById(int id) {
        return machineInfoDao.getMachineInfoById(id);
    }

    @Override
    public MachineInfo getMachineInfoByHost(String host) {
        return machineInfoDao.getMachineInfoByHost(host);
    }

    @Override
    public MachineInfo getMachineInfoByHostName(String hostName) {
        return machineInfoDao.getMachineInfoByHostName(hostName);
    }

    @Override
    public MachineInfo getMachineInfoByDomain(String hostDomain) {
        return machineInfoDao.getMachineInfoByDomain(hostDomain);
    }

    @Override
    public List<MachineInfo> getAllAvailableMachineInfos() {
        return machineInfoDao.getAllAvailableMachineInfos();
    }

    @Override
    public List<MachineInfo> getAllMachineInfos() {
        return machineInfoDao.getAllMachineInfos();
    }

    @Override
    public List<MachineInfo> getAllMachinesByParams(MachineSearchBO machineSearchBO) {
        return machineInfoDao.getAllMachinesByParams(machineSearchBO);
    }

    @Override
    public List<MachineInfo> getMachineInfoByClusterId(int clusterId) {
        return machineInfoDao.getMachineInfoByClusterId(clusterId);
    }

    @Override
    public boolean updateMachineInfo(MachineInfo machineInfo) {
        if (machineInfo == null) {
            return false;
        }
        machineInfo.setModifyTime(new Date());
        return machineInfoDao.updateMachineInfo(machineInfo);
    }

    @Override
    public boolean updateMachineAvailable(int machineId, boolean available) {
        return machineInfoDao.updateMachineAvailable(machineId, available);
    }

    @Override
    public boolean deleteMachineInfoById(int machineId) {
        return machineInfoDao.deleteMachineInfoById(machineId);
    }

    @Override
    public boolean batchInsertNotExistsMachine(List<HostAndPort> hostAndPortList) {
        if (CollectionUtils.isEmpty(hostAndPortList)) {
            return false;
        }
        boolean result = true;
        for (HostAndPort hostAndPort : hostAndPortList) {
            if (!insertIfNotExistsMachine(hostAndPort.getHost())) {
                result = false;
            }
        }
        return result;
    }

    @Override
    public boolean insertIfNotExistsMachine(String host) {
        MachineInfo machineInfo = new MachineInfo();
        machineInfo.setHost(host);
        machineInfo.setServiceLine(serviceLineService.getDefaultServiceLine().getId());
        // 新加入机器，默认收集信息开关关闭
        machineInfo.setMonitor(false);
        machineInfo.setAvailable(true);
        machineInfo.setCreateTime(new Date());
        machineInfo.setModifyTime(new Date());
        try {
            return insertIfNotExistsMachine(machineInfo);
        } catch (Exception e) {
            LOGGER.error("Insert machine if not exists failed.", e);
            return false;
        }
    }

    @Override
    public boolean insertIfNotExistsMachine(MachineInfo machineInfo) {
        if (machineInfoDao.getMachineInfoByHost(machineInfo.getHost()) != null) {
            return true;
        }
        return machineInfoDao.insertMachine(machineInfo);
    }

    @Override
    public List<MachineDetailVO> getInstanceDetailVOByParams(MachineSearchBO machineSearchBO) {
        List<MachineInfo> machineInfoList = getAllMachinesByParams(machineSearchBO);
        if (CollectionUtils.isEmpty(machineInfoList)) {
            return null;
        }
        List<MachineDetailVO> machineDetailVOList = new ArrayList<MachineDetailVO>();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        for (MachineInfo machineInfo : machineInfoList) {
            MachineDetailVO machineDetailVO = new MachineDetailVO();
            machineDetailVO.setMachineInfo(machineInfo);
            MachineState machineState = machineStateService.getMachineStateByMachineId(machineInfo.getId());
            machineDetailVO.setMachineState(machineState == null ? new MachineState() : machineState);
            if (machineDetailVO.getMachineState().getMemoryTotal() != null) {
                machineDetailVO.getMachineState().setMemoryTotal(decimalFormat.format(Double.parseDouble(machineDetailVO.getMachineState().getMemoryTotal())
                        / SymbolConstant.BASE_UNIT / SymbolConstant.BASE_UNIT));
            }
            if (machineDetailVO.getMachineState().getMemoryFree() != null) {
                machineDetailVO.getMachineState().setMemoryFree(decimalFormat.format(Double.parseDouble(machineDetailVO.getMachineState().getMemoryFree())
                        / SymbolConstant.BASE_UNIT / SymbolConstant.BASE_UNIT));
            }
            machineDetailVOList.add(machineDetailVO);
        }
        return machineDetailVOList;
    }

    @Override
    public List<MachineInfo> getAllMonitorMachine() {
        return machineInfoDao.getAllMonitorMachine();
    }

    @Override
    public boolean updateMachineMonitor(int machineId, boolean monitor) {
        return machineInfoDao.updateMachineMonitor(machineId, monitor);
    }
}
