import {observable} from "mobx";
import {provideInstance} from "../../common/utils/IOC";
import {asyncAction} from "mobx-utils";
import {
    getConnectionCollectTime,
    getInstanceConfig,
    getInstanceConnections,
    getInstanceDetailInfo,
    getMachineStateInitTrend,
    queryAvgLoad,
    queryByClusterId,
    queryByInstanceStatus,
    queryCpu,
    queryCpuSingle,
    queryDisk,
    queryMemory,
    queryNetTraffic,
    queryNetTrafficDetail,
    searchConnectionHistory,
    updateInstanceConnCollectMonitor
} from "../service/instance";

// 实例基本信息
export class InstanceInfoVo {
    id: number;
    clusterId: number;
    clusterName: string;
    machineId: number;
    host: string;
    port: number;
    connectionMonitor: boolean;
    deployType: number;
    serverStateLag: number;
    status: number;
    param1: string;
    createTime: Date;
    createTimeStr: string;
    modifyTime: Date;
    modifyTimestr: string;
}

// 实例状态信息
export class InstanceStateVo {
    id: number;
    instanceId: number;
    clusterId: number;
    version: string;
    leaderId: number;
    avgLatency: number;
    maxLatency: number;
    minLatency: number;
    received: number;
    sent: number;
    currConnections: number;
    currOutstandings: number;
    serverStateLag: number;
    currZnodeCount: number;
    currWatchCount: number;
    currEphemeralsCount: number;
    approximateDataSize: number;
    openFileDescriptorCount: number;
    maxFileDescriptorCount: number;
    followers: number;
    syncedFollowers: number;
    pendingSyncs: number;
    zxid: number;
    runOk: boolean;
    param1: string;
    createTime: Date;
    createTimeStr: string;
    modifyTime: Date;
    modifyTimeStr: string;
}

// 实例详细信息，包含基本信息以及状态信息
export class InstanceVo {
    instanceInfo: InstanceInfoVo;
    instanceState: InstanceStateVo;
}

// 实例列表展示信息
export class InstanceListVo {
    instanceId: number;
    host: string;
    port: number;
    clusterName: string;
    currConnections: number;
    received: number;
    serverStateLag: number;
    status: number;
    clusterId:number;
}

// 实例配置信息
export class InstanceConfigVo {
    clusterId: number;
    instanceId: number;
    clientPort: number;
    dataDir: string;
    dataLogDir: string;
    tickTime: number;
    maxClientCnxns: number;
    minSessionTimeout: number;
    maxSessionTimeout: number;
    serverId: number;
    initLimit: number;
    syncLimit: number;
    electionAlg: number;
    electionPort: number;
    quorumPort: number;
    peerType: number;
}

// 实例连接信息
export class InstanceConnectionVo {
    ip: string;
    port: number;
    hostInfo: string;
    recved: number;
    sent: number;
    queued: number;
    lop: string;
    maxlat: number;
    avglat: number;
    lzxid: string;
    to: number;
    infoLine: string;
}

// 机器历史状态搜索条件，包含实例id，起始时间、结束时间
export class MachineStatSearchVo {
    id: number;
    start: string;
    end: string;
}

// 实例连接历史情况信息
export class InstanceConnectionHistoryVo {
    key: string;
    id: number;
    instanceId: number;
    clusterId: number;
    clientIp: string;
    clientPort: number;
    sid: string;
    queued: number;
    recved: number;
    sent: number;
    est: number;
    estDate: string;
    toTime: number;
    lcxid: string;
    lzxid: string;
    lresp: number;
    lrespStr: string;
    llat: number;
    minlat: number;
    avglat: number;
    maxlat: number;
    createTimeStr: string;
    info: string;
    clientInfoList: Array<InstanceConnectionHistoryVo>;
}

// 实例连接信息搜索条件
export class ClientConnectionSearchVo {
    clusterId: number;
    instanceId: number;
    startDate: string;
    endDate: string;
    orderBy: string;
}

// 实例配置运维操作VO，更新实例配置使用
export class InstanceConfigOpsVo {
    instanceId: number;
    confDir: string;
    zooConfFileContent: string;
}

// 实例配置文件信息VO，用于新增myid配置使用
export class ConfigFileVo {
    host: string;
    confDir: string;
    confFileName: string;
    confFileContent: string;
}

@provideInstance(InstanceModel)
export class InstanceModel {
    /**
     * 数据加载标志
     * @type {boolean}
     */
    @observable loading: boolean = false;

    /**
     * 实例列表
     * @type {Array}
     */
    @observable instances: Array<InstanceVo> = [];

    /**
     * 实例详情信息，包含实例基本信息以及实例状态信息
     * @type {any}
     */
    @observable instance: InstanceVo = null;

    /**
     * 实例配置信息
     * @type {any}
     */
    @observable instanceConfigVo: InstanceConfigVo = null;

    /**
     * 实例连接信息
     * @type {Array}
     */
    @observable instanceConnections: Array<InstanceConnectionVo> = [];

    /**
     * 最近一次实例连接信息收集时间点
     * @type {any}
     */
    @observable connectionCollectTime = null;

    /**
     * 实例历史连接信息情况
     * @type {Array}
     */
    @observable connectionsHistory: Array<InstanceConnectionHistoryVo> = [];

    /**
     * 实例基本信息
     * @type {any}
     */
    @observable instanceInfo: InstanceInfoVo = null;

    /**
     * 实例状态信息
     * @type {any}
     */
    @observable instanceState: InstanceStateVo = null;

    /**
     * 机器维度监控数据
     * @type {{}}
     */
    @observable netTrafficTrendData: any = {};
    @observable avgLoadTrendData: any = {};
    @observable cpuTrendData: any = {};
    @observable cpuSingleTrendData: any = {};
    @observable memoryTrendData: any = {};
    @observable diskTrendData: any = {};

    /**
     * 查询所有实例详情信息
     * @param clusterId 集群id
     */
    @asyncAction
    * queryAllData(clusterId: number) {
        this.loading = true;
        const result = yield queryByClusterId(clusterId);
        if (result.success) {
            this.instances = result.data;
        }
        this.loading = false;
    }
    /**
     * 查询实例的所有异常信息
     * @param status 实例status
     */
    @asyncAction
    * queryAllExceptionData(status: number) {
        this.loading = true;
        const result = yield queryByInstanceStatus(status);
        if (result.success) {
            this.instances = result.data;
        }
        this.loading = false;
    }
    /**
     * 获取某个实例详情信息
     * @param instanceId 实例id
     */
    @asyncAction
    * getInstanceDetailInfo(instanceId: number) {
        const result = yield getInstanceDetailInfo(instanceId);
        if (result.success) {
            this.instance = result.data;
        }
    }

    /**
     * 获取实例配置信息
     * @param instanceId 实例id
     */
    @asyncAction
    * getInstanceConfig(instanceId: number) {
        const result = yield getInstanceConfig(instanceId);
        if (result.success) {
            this.instanceConfigVo = result.data;
        }
    }

    /**
     * 获取实例连接信息，同时获取最后一次该实例信息重置时间点
     * @param instanceId 实例id
     */
    @asyncAction
    * getInstanceConnections(instanceId: number) {
        this.loading = true;
        const result = yield getInstanceConnections(instanceId);
        if (result.success) {
            this.instanceConnections = result.data;
            const timeResult = yield getConnectionCollectTime(instanceId);
            if (timeResult.success) {
                this.connectionCollectTime = timeResult.data;
            }
        }
        this.loading = false;
    }

    /**
     * 初始加载机器状态历史信息
     * @param searchVo 搜索条件
     */
    @asyncAction
    * initMachineStateData(searchVo: MachineStatSearchVo) {
        this.loading = true;
        const result = yield getMachineStateInitTrend(searchVo);
        if (result.success) {
            this.netTrafficTrendData = result.data.netTraffic;
            this.avgLoadTrendData = result.data.avgLoad;
            this.cpuTrendData = result.data.cpu;
            this.cpuSingleTrendData = result.data.cpuSingle;
            this.memoryTrendData = result.data.memory;
            this.diskTrendData = result.data.disk;
        }
        this.loading = false;
    }

    /**
     * 查询机器网络历史信息
     * @param searchVo 查询条件
     */
    @asyncAction
    * queryNetTraffic(searchVo: MachineStatSearchVo) {
        this.loading = true;
        const result = yield queryNetTraffic(searchVo);
        if (result.success) {
            this.netTrafficTrendData = result.data;
        }
        this.loading = false;
    }

    /**
     * 查询机器平均负载历史信息
     * @param searchVo 查询条件
     */
    @asyncAction
    * queryAvgLoad(searchVo: MachineStatSearchVo) {
        this.loading = true;
        const result = yield queryAvgLoad(searchVo);
        if (result.success) {
            this.avgLoadTrendData = result.data;
        }
        this.loading = false;
    }

    /**
     * 查询机器CPU历史信息
     * @param searchVo 查询条件
     */
    @asyncAction
    * queryCpu(searchVo: MachineStatSearchVo) {
        this.loading = true;
        const result = yield queryCpu(searchVo);
        if (result.success) {
            this.cpuTrendData = result.data;
        }
        this.loading = false;
    }

    /**
     * 查询机器单CPU历史信息
     * @param searchVo 查询条件
     */
    @asyncAction
    * queryCpuSingle(searchVo: MachineStatSearchVo) {
        this.loading = true;
        const result = yield queryCpuSingle(searchVo);
        if (result.success) {
            this.cpuSingleTrendData = result.data;
        }
        this.loading = false;
    }

    /**
     * 查询机器内存历史信息
     * @param searchVo 查询条件
     */
    @asyncAction
    * queryMemory(searchVo: MachineStatSearchVo) {
        this.loading = true;
        const result = yield queryMemory(searchVo);
        if (result.success) {
            this.memoryTrendData = result.data;
        }
        this.loading = false;
    }

    /**
     * 查询机器磁盘历史信息，dataDir所在目录
     * @param searchVo 查询条件
     */
    @asyncAction
    * queryDisk(searchVo: MachineStatSearchVo) {
        this.loading = true;
        const result = yield queryDisk(searchVo);
        if (result.success) {
            this.diskTrendData = result.data;
        }
        this.loading = false;
    }

    /**
     * 更新实例连接信息收集开关
     * @param instanceId    实例id
     * @param monitor       监控开关
     * @param callback      回调处理结果
     */
    @asyncAction
    * updateInstanceConnCollectMonitor(instanceId: number, monitor: boolean, callback: any) {
        this.loading = true;
        const result = yield updateInstanceConnCollectMonitor({instanceId: instanceId, monitor: monitor});
        if (result.success) {
            callback && callback();
        }
        this.loading = false;
    }

    /**
     * 搜索实例连接历史信息
     * @param search 搜索条件
     */
    @asyncAction
    * searchConnectionHistory(search: ClientConnectionSearchVo) {
        this.loading = true;
        const result = yield searchConnectionHistory(search);
        if (result.success) {
            this.connectionsHistory = result.data;
        }
        this.loading = false;
    }

    /**
     * 获取流量TOP10信息
     * @param instanceId 实例id
     * @param dateTime   时间点
     * @param callback   回调处理结果
     */
    @asyncAction
    * queryNetTrafficDetail(instanceId, dateTime, callback) {
        this.loading = true;
        const result = yield queryNetTrafficDetail({instanceId: instanceId, dateTime: dateTime});
        if (result.success) {
            callback && callback(result.data)
        }
        this.loading = false;
    }
}
