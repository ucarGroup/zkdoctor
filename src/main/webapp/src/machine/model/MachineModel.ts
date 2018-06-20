import {action, observable} from "mobx";
import {provideSingleton} from "../../common/utils/IOC";
import Pagination from "../../common/model/Pagination";
import {asyncAction} from "mobx-utils";
import {addMachine, deleteMachine, downloadMachineInitScript, editMachineInfo, queryMachineInstances, updateMonitorStatus} from "../service/machine";
import {InstanceInfoVo} from "../../instance/model/InstanceModel";

// 机器信息搜索条件
export class MachineSearchVo {
    host?: string;
    serviceLine?: number;
}

// 机器基本信息
export class MachineInfoVo {
    id: number;
    host: string;
    memory: number;
    cpu: number;
    virtual: boolean;
    realHost: string;
    room: string;
    monitor: boolean;
    available: boolean;
    serviceLine: number;
    hostName: string;
    hostDomain: string;
    createTimeStr: string;
    modifyTimeStr: string;
}

// 机器状态信息
export class MachineStateVo {
    id: number;
    machineId: number;
    host: string;
    cpuUsage: string;
    avgLoad: string;
    netTraffic: string;
    memoryUsage: string;
    memoryFree: string;
    memoryTotal: string;
    diskUsage: string;
    dataDiskUsed: string;
    dataDiskTotal: string;
    createTimeStr: string;
    modifyTimeStr: string;
}

// 机器详情信息，包含机器基本信息以及机器状态信息
export class MachineDetailVo {
    machineInfo: MachineInfoVo;
    machineState: MachineStateVo;
}

@provideSingleton(MachineModel)
export class MachineModel extends Pagination {
    /**
     * 数据加载标志
     * @type {boolean}
     */
    @observable loading: boolean = false;

    /**
     * 机器详情列表
     * @type {Array}
     */
    @observable machines: Array<MachineDetailVo> = [];

    /**
     * 某个机器详细信息
     * @type {any}
     */
    @observable machine: MachineDetailVo = null;

    /**
     * 机器上的实例列表信息
     * @type {Array}
     */
    @observable machineInstances: Array<InstanceInfoVo> = [];

    constructor() {
        super();
        this.path = '/machine/query';
    }

    @action
    queryCallBack(success: boolean, list: Array<any>): void {
        if (success) {
            this.machines = list as Array<MachineDetailVo>;
        }
        this.loading = false;
    }

    @action
    query(vo: MachineSearchVo) {
        this.loading = true;
        this.queryData(vo);
    }

    /**
     * 删除某机器信息
     * @param machineId  机器id
     * @param callback   回调处理结果
     */
    @asyncAction
    * deleteMachine(machineId, callback) {
        this.loading = true;
        const result = yield deleteMachine(machineId);
        if (result.success) {
            const newList = this.machines.filter(bean => bean.machineInfo.id !== machineId);
            this.machines = newList;
            // 回调显示处理成功结果
            callback && callback(result.message);
        }
        this.loading = false;
    }

    /**
     * 增加机器信息
     * @param item      新增机器信息
     * @param callback  回调处理结果
     */
    @asyncAction
    * addMachine(item: MachineInfoVo, callback) {
        const result = yield addMachine(item);
        if (result.success) {
            this.query(new MachineSearchVo());
            // 回调显示处理成功结果
            callback && callback(result.message);
        }
    }

    /**
     * 更新机器信息
     * @param item     机器信息
     * @param callback 回调处理结果
     */
    @asyncAction
    * editMachineInfo(item: MachineInfoVo, callback) {
        const result = yield editMachineInfo(item);
        if (result.success) {
            this.query(new MachineSearchVo());
            // 回调显示处理成功结果
            callback && callback(result.message);
        }
    }

    /**
     * 更新机器监控开关
     * @param machineId 机器id
     * @param monitor   监控开关
     */
    @asyncAction
    * updateMonitorStatus(machineId: number, monitor: boolean) {
        this.loading = true;
        const result = yield updateMonitorStatus({machineId: machineId, monitor: monitor});
        if (result.success) {
            const newMachines = this.machines.map(bean => {
                if (bean.machineInfo.id === machineId) {
                    bean.machineInfo.monitor = monitor;
                    return bean;
                }
                return bean;
            });
            this.machines = newMachines;
        }
        this.loading = false;
    }

    /**
     * 下载机器初始化脚本
     */
    @action
    downloadMachineInitScript() {
        downloadMachineInitScript({url: "/manage/machine/downloadMachineInitScript", filename: 'machine_init.sh', params: {}});
    }

    /**
     * 查询机器上的所有实例信息
     * @param machineId 机器id
     */
    @asyncAction
    * queryMachineInstances(machineId: number) {
        this.loading = true;
        const result = yield queryMachineInstances({machineId: machineId});
        if (result.success) {
            this.machineInstances = result.data.data;
        }
        this.loading = false;
    }
}