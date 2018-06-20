package com.ucar.zkdoctor.service.machine;

import com.ucar.zkdoctor.pojo.bo.HostAndPort;
import com.ucar.zkdoctor.pojo.bo.MachineSearchBO;
import com.ucar.zkdoctor.pojo.po.MachineInfo;
import com.ucar.zkdoctor.pojo.vo.MachineDetailVO;

import java.util.List;

/**
 * Description: 机器服务接口
 * Created on 2018/1/9 17:07
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public interface MachineService {

    /**
     * 保存新机器信息
     *
     * @param machineInfo 机器信息
     * @return
     */
    boolean insertMachine(MachineInfo machineInfo);

    /**
     * 通过机器id获取机器信息
     *
     * @param id 机器id
     * @return
     */
    MachineInfo getMachineInfoById(int id);

    /**
     * 通过机器ip获取机器信息
     *
     * @param host 机器ip
     * @return
     */
    MachineInfo getMachineInfoByHost(String host);

    /**
     * 通过机器名称获取机器信息
     *
     * @param hostName 机器名称
     * @return
     */
    MachineInfo getMachineInfoByHostName(String hostName);

    /**
     * 通过机器域名获取机器信息
     *
     * @param hostDomain 机器域名
     * @return
     */
    MachineInfo getMachineInfoByDomain(String hostDomain);

    /**
     * 获取所有可用状态的机器列表
     *
     * @return
     */
    List<MachineInfo> getAllAvailableMachineInfos();

    /**
     * 获取所有机器信息
     *
     * @return
     */
    List<MachineInfo> getAllMachineInfos();

    /**
     * 根据查询条件获取机器信息
     *
     * @param machineSearchBO 查询条件
     * @return
     */
    List<MachineInfo> getAllMachinesByParams(MachineSearchBO machineSearchBO);

    /**
     * 获取某集群下的所有机器信息
     *
     * @param clusterId 集群id
     * @return
     */
    List<MachineInfo> getMachineInfoByClusterId(int clusterId);

    /**
     * 更新机器信息
     *
     * @param machineInfo
     * @return
     */
    boolean updateMachineInfo(MachineInfo machineInfo);

    /**
     * 更新机器是否可用
     *
     * @param machineId 机器id
     * @param available 机器是否可用
     * @return
     */
    boolean updateMachineAvailable(int machineId, boolean available);

    /**
     * 删除某机器信息
     *
     * @param machineId 机器id
     * @return
     */
    boolean deleteMachineInfoById(int machineId);

    /**
     * 将所有机器加入到数据库中，如果存在则忽略
     *
     * @param hostAndPortList 机器列表
     * @return
     */
    boolean batchInsertNotExistsMachine(List<HostAndPort> hostAndPortList);

    /**
     * 增加一台机器，如果存在则忽略
     *
     * @param host 机器ip
     * @return
     */
    boolean insertIfNotExistsMachine(String host);

    /**
     * 增加一台机器，如果存在则忽略
     *
     * @param machineInfo 机器信息
     * @return
     */
    boolean insertIfNotExistsMachine(MachineInfo machineInfo);

    /**
     * 获取机器详情信息，包括机器基本信息以及机器状态信息
     *
     * @param machineSearchBO 机器查询条件
     * @return
     */
    List<MachineDetailVO> getInstanceDetailVOByParams(MachineSearchBO machineSearchBO);

    /**
     * 获取所有监控中的机器列表信息
     *
     * @return
     */
    List<MachineInfo> getAllMonitorMachine();

    /**
     * 修改机器监控开关
     *
     * @param machineId 机器id
     * @param monitor   是否监控
     * @return
     */
    boolean updateMachineMonitor(int machineId, boolean monitor);

}
