import {action, observable} from "mobx";
import {provideSingleton} from "../../common/utils/IOC";
import Pagination from "../../common/model/Pagination";
import {asyncAction} from "mobx-utils";

import {
    addClusterAlarmUser,
    addClusterSubmit,
    checkClusterNameExist,
    createZnode,
    deleteAlarmUser,
    deleteZnode,
    getClusterDetail,
    getClusterInfo,
    getClusterRootZnodes,
    getClusterUsers,
    getClusterZnodesChildren,
    getMonitorTaskByClusterId,
    modifyCluster,
    searchZnodeData,
    updateMonitorStatus,
    updateZnode
} from "../service/cluster";

// 集群信息查询条件
export class ClusterSearchVo {
    clusterName?: string;
    officer?: string;
    status?: number;
    deployType?: number;
    serviceLine?: number;
    version?: string;
}

// 页面显示的集群信息
export class ClusterVo {
    id: number;
    clusterId: number;
    clusterName: string;
    intro: string;
    znodeCount: number;
    requestCount: number;
    connections: number;
    collectTime: string;
    version: string;
    status: number;
    serviceLine: number;
    clusterInfo: ClusterInfoVo;
}

// 集群状态信息
export class ClusterStateVo {
    clusterId: number;
    instanceNumber: number;
    avgLatencyMax: number;
    maxLatencyMax: number;
    minLatencyMax: number;
    receivedTotal: number;
    sentTotal: number;
    connectionTotal: number;
    znodeCount: number;
    watcherTotal: number;
    ephemerals: number;
    outstandingTotal: number;
    approximateDataSize: number;
    openFileDescriptorCountTotal: number;
    modifyTime: Date;
    modifyTimeStr: string;
}

// 集群基本信息
export class ClusterInfoVo {
    id: number;
    clusterName: string;
    officer: string;
    instanceNumber: number;
    status: number;
    deployType: number;
    serviceLine: number;
    version: string;
    intro: string;
    createTime: Date;
    createTimeStr: string;
    modifyTime: Date;
    modifyTimestr: string;
}

// 集群基本信息+集群状态信息
export class ClusterDetailVo {
    clusterInfo: ClusterInfoVo;
    clusterState: ClusterStateVo;
}

// 集群报警用户对应关系
export class ClusterAlarmUserVo {
    id: number;
    clusterId: number;
    userId: number;
    userName: string;
    mobile: string;
    email: string;
}

// 集群统计信息查询条件，包含集群id、起始时间以及结束时间
export class ClusterStatSearchVo {
    id: number;
    start: string;
    end: string;
}

// 新建集群信息，除基本信息外，包含集群实例基本配置
export class NewClusterVo extends ClusterInfoVo {
    constructor() {
        super();
    }

    newClusterServers: string;
}

// 节点信息
export class ClusterZnode {
    title: string;
    key: string;
    isLeaf: boolean;
}

// 节点详细信息
export class ZnodeDetailInfoVo {
    data: string;
    czxid: string;
    mzxid: string;
    ctime: string;
    mtime: string;
    version: number;
    cversion: number;
    aversion: number;
    ephemeralOwner: string;
    dataLength: number;
    numChildren: number;
    pzxid: string;
}

@provideSingleton(ClusterModel)
export class ClusterModel extends Pagination {
    /**
     * 数据加载标志
     * @type {boolean}
     */
    @observable loading: boolean = false;

    /**
     * 集群列表信息
     * @type {Array}
     */
    @observable clusters: Array<ClusterVo> = [];

    /**
     * 集群基本信息
     * @type {any}
     */
    @observable clusterInfo: ClusterVo = null;

    /**
     * 集群详细信息，包含集群的状态信息
     * @type {any}
     */
    @observable clusterDetailVo: ClusterDetailVo = null;

    /**
     * 集群下对应的报警用户列表信息
     * @type {Array}
     */
    @observable alarmUsers: Array<ClusterAlarmUserVo> = [];

    /**
     * 集群名称是否已经存在的标志位 TODO 后续优化
     * @type {boolean}
     */
    @observable clusterNameExist: boolean = false;

    /**
     * 新建集群，提交结果
     * @type {boolean}
     */
    @observable addClusterSubmitResult: boolean = false;

    /**
     * 根节点列表
     * @type {Array}
     */
    @observable znodeRoots: Array<ClusterZnode> = [];

    /**
     * 节点children列表
     * @type {Array}
     */
    @observable znodeChildren: Array<ClusterZnode> = [];

    /**
     * 节点详细信息
     * @type {any}
     */
    @observable znodeDataDetail: ZnodeDetailInfoVo = null;

    constructor() {
        super();
        this.path = '/cluster/query';
    }

    @action
    queryCallBack(success: boolean, list: Array<any>): void {
        if (success) {
            this.clusters = list as Array<ClusterVo>;
        }
        this.loading = false;
    }

    @action
    query(vo: ClusterSearchVo) {
        this.loading = true;
        this.queryData(vo);
    }

    /**
     * 重置新建集群结果信息
     */
    @action
    resetAddClusterSubmitResult() {
        this.addClusterSubmitResult = false;
    }

    /**
     * 获取集群基本信息
     * @param clusterId 集群id
     */
    @asyncAction
    * getClusterInfo(clusterId: number) {
        const result = yield getClusterInfo(clusterId);
        if (result.success) {
            this.clusterInfo = result.data;
        }
    }

    /**
     * 获取集群详细信息，包含集群基本信息以及集群的状态信息
     * @param clusterId 集群id
     */
    @asyncAction
    * getClusterDetail(clusterId: number) {
        const result = yield getClusterDetail(clusterId);
        if (result.success) {
            this.clusterDetailVo = result.data;
        }
    }

    /**
     * 获取集群下的报警用户列表
     * @param clusterId 集群id
     */
    @asyncAction
    * getClusterUsers(clusterId: number) {
        const result = yield getClusterUsers(clusterId);
        if (result.success) {
            this.alarmUsers = result.data;
        }
    }

    /**
     * 删除某个报警用户信息
     * @param item 集群与用户的对应关系
     */
    @asyncAction
    * deleteAlarmUser(item: ClusterAlarmUserVo) {
        this.loading = true;
        const result = yield deleteAlarmUser({clusterId: item.clusterId, userId: item.id});
        if (result.success) {
            const newList = this.alarmUsers.filter(bean => bean.id !== item.id);
            this.alarmUsers = newList;
        }
        this.loading = false;
    }

    /**
     * 新增报警用户信息
     * @param item 集群与用户的对应关系
     */
    @asyncAction
    * addClusterAlarmUser(item: ClusterAlarmUserVo) {
        const result = yield addClusterAlarmUser(item);
        if (result.success) {
            this.getClusterUsers(item.clusterId)
        }
    }

    /**
     * 修改集群基本信息
     * @param vo 集群基本信息
     * @param callback 回调处理结果
     */
    @asyncAction
    * editClusterInfo(vo: any, callback) {
        this.loading = true;
        const result = yield modifyCluster(vo);
        if (result.success) {
            const newList = this.clusters.map(bean => {
                if (bean.clusterId === vo.id) {
                    bean.clusterInfo = vo;
                    bean.clusterName = vo.clusterName;
                    bean.intro = vo.intro;
                    bean.version = vo.version;
                    bean.serviceLine = vo.serviceLine;
                    return bean;
                }
                return bean;
            });
            this.clusters = newList;
            callback && callback(result.message);
        }
        this.loading = false;
    }

    /**
     * 校验集群名称是否已经存在
     * @param clusterName  集群名称
     * @param callback 回调处理结果
     */
    @asyncAction
    * checkClusterNameExist(clusterName, callback: any) {
        const result = yield checkClusterNameExist(clusterName);
        if (result.success) {
            this.clusterNameExist = result.data;
            if (this.clusterNameExist == true) { // 如果集群名称已经存在，则回调处理显示
                callback && callback();
            }
        }
    }

    /**
     * 新建集群
     * @param vo 新集群基本信息
     */
    @asyncAction
    * addClusterSubmit(vo: NewClusterVo) {
        this.loading = true;
        const result = yield addClusterSubmit(vo);
        if (result.success) {
            this.addClusterSubmitResult = true;
        }
        this.loading = false;
    }

    /**
     * 更新集群监控状态
     * @param clusterId  集群id
     * @param status 集群监控状态
     */
    @asyncAction
    * updateMonitorStatus(clusterId: number, status: boolean) {
        this.loading = true;
        const result = yield updateMonitorStatus({clusterId: clusterId, status: status});
        if (result.success) {
            const newClusters = this.clusters.map(bean => {
                let clusterStatus = status ? 2 : 1;
                if (bean.clusterId === clusterId) {
                    return {...bean, status: clusterStatus};
                }
                return bean;
            });
            this.clusters = newClusters;
        }
        this.loading = false;
    }

    /**
     * 获取zk根节点/下的所有节点信息
     * @param clusterId 集群id
     */
    @asyncAction
    * getClusterRootZnodes(clusterId: number) {
        const result = yield getClusterRootZnodes(clusterId);
        if (result.success) {
            this.znodeRoots = result.data;
        }
    }

    /**
     * 获取某个节点下的所有子节点信息
     * @param id        集群id
     * @param znode     待获取子节点的节点信息
     * @param callback  回调处理结果
     */
    @asyncAction
    * getClusterZnodesChildren(id: number, znode: string, callback: any) {
        const result = yield getClusterZnodesChildren(id, znode);
        if (result.success) {
            this.znodeChildren = result.data;
            // 回调渲染子节点数据
            callback && callback(result.data);
        }
    }

    /**
     * 删除某个节点信息
     * @param id        集群id
     * @param znode     待删除节点信息
     * @param callback  回调处理结果
     */
    @asyncAction
    * deleteZnode(id: number, znode: string, callback: any) {
        const result = yield deleteZnode(id, znode);
        if (result.success) {
            // 回调显示处理成功结果
            callback && callback(result.message);
        }
    }

    /**
     * 查询某个节点的数据信息
     * @param id            集群id
     * @param znode         待查询数据的节点路径
     * @param serializable  序列化方式
     */
    @asyncAction
    * searchZnodeData(id: number, znode: string, serializable: string) {
        const result = yield searchZnodeData(id, znode, serializable);
        if (result.success) {
            this.znodeDataDetail = result.data;
        }
    }

    /**
     * 更新节点数据信息
     * @param item      更新所需数据信息
     * @param callback  回调处理结果
     */
    @asyncAction
    * updateZnode(item, callback: any) {
        const result = yield updateZnode(item);
        if (result.success) {
            // 回调显示处理成功结果
            callback && callback(result.message);
        }
    }

    /**
     * 创建节点
     * @param item     创建节点信息
     * @param callback 回调处理结果
     */
    @asyncAction
    * createZnode(item, callback: any) {
        const result = yield createZnode(item);
        if (result.success) {
            // 回调显示处理成功结果
            callback && callback(result.message);
        }
    }
    /**
     * 根据集群ID获取该集群下的所有监控任务信息
     * @param clusterId 集群id
     */
    @asyncAction
    * getMonitorTaskByClusterId(clusterId: number) {
        const result = yield getMonitorTaskByClusterId(clusterId);
        if (result.success) {
            this.znodeRoots = result.data;
        }
    }
}
