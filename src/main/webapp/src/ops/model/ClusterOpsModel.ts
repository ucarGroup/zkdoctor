import {action, observable} from "mobx";
import {provideSingleton} from "../../common/utils/IOC";
import Pagination from "../../common/model/Pagination";
import {asyncAction} from "mobx-utils";

import {
    clusterDeploySubmit,
    clusterDynamicExpansion,
    getClusterDeployResult,
    getClusterDynamicExpansionResult,
    getClusterRestartResult,
    offLine,
    restartQuorum
} from "../service/ClusterOpsService";

// 集群服务部署信息
export class ClusterDeployVo {
    clusterName: string;
    officer: string;
    installFileName: string;
    downloadSite: string;
    serviceLine: number;
    zkConfigInfoVO: ZKConfigInfoVo;
}

// zk配置信息
export class ZKConfigInfoVo {
    serverConfig: string;
    dataDir: string;
    clientPort: number;
    tickTime: number;
    initLimit: number;
    syncLimit: number;
    extraConfig: string;
}

// zk动态扩容配置信息
export class ZKDynamicExpansionConfig {
    clusterId: number;
    clusterName: string;
    serverId: number;
    host: string;
    clientPort: number;
    quorumPort: number;
    electionPort: number;
    peerType: string;
    domain: string;
}


@provideSingleton(ClusterOpsModel)
export class ClusterOpsModel extends Pagination {
    /**
     * 数据加载标志
     * @type {boolean}
     */
    @observable loading: boolean = false;

    /**
     * 集群名称是否存在
     * @type {boolean}
     */
    @observable clusterNameExist: boolean = false;

    /**
     * 集群部署结果
     * @type {boolean}
     */
    @observable clusterDeploySubmitResult: boolean = false;

    /**
     * 动态扩容结果列表信息
     * @type {Array}
     */
    @observable dynamicExpansionResultList: Array<any> = [];

    /**
     * 最后一次扩容结果
     * @type {boolean}
     */
    @observable lastDynamicExpansionResult: boolean = false;

    /**
     * 动态扩容结果
     * @type {boolean}
     */
    @observable dynamicExpansionResult: boolean = true;

    /**
     * 集群重启结果列表信息
     * @type {Array}
     */
    @observable restartResultList: Array<any> = [];

    /**
     * 最后一次重启结果
     * @type {boolean}
     */
    @observable lastRestartResult: boolean = false;

    /**
     * 重启结果
     * @type {boolean}
     */
    @observable restartResult: boolean = true;

    /**
     * 集群部署结果列表信息
     * @type {Array}
     */
    @observable deployResultList: Array<any> = [];

    /**
     * 最后一次集群部署结果
     * @type {boolean}
     */
    @observable lastDeployResult: boolean = false;

    /**
     * 集群部署结果
     * @type {boolean}
     */
    @observable deployResult: boolean = true;

    constructor() {
        super();
        this.path = '/cluster/query';
    }

    /**
     * 重置集群部署结果
     */
    @action
    resetClusterDeploySubmitResult() {
        this.clusterDeploySubmitResult = false;
    }

    /**
     * 集群部署
     * @param vo       集群部署信息
     * @param callback 回调处理数据
     */
    @asyncAction
    * clusterDeploySubmit(vo: ClusterDeployVo, callback) {
        this.loading = true;
        const result = yield clusterDeploySubmit(vo);
        if (result.success) {
            this.deployResult = true;
        } else {
            this.deployResult = false;
            callback && callback(result.message);
        }
        this.lastDeployResult = true;
        this.loading = false;
    }

    /**
     * 集群动态扩容
     * @param vo       动态扩容参数
     * @param callback 回调处理数据
     */
    @asyncAction
    * clusterDynamicExpansion(vo: ZKDynamicExpansionConfig, callback) {
        this.loading = true;
        const result = yield clusterDynamicExpansion(vo);
        if (result.success) {
            this.dynamicExpansionResult = true;
        } else {
            this.dynamicExpansionResult = false;
            callback && callback(result.message);
        }
        this.lastDynamicExpansionResult = true;
        this.loading = false;
    }

    /**
     * 集群下线
     * @param clusterId    集群id
     * @param callback 回调处理数据
     */
    @asyncAction
    * offLine(clusterId, callback) {
        this.loading = true;
        const result = yield offLine(clusterId);
        if (result.success) {
            // 回调显示处理成功结果
            callback && callback(result.message);
        }
        this.loading = false;
    }

    /**
     * 重启集群
     * @param item     重启集群参数
     * @param callback 回调处理数据
     */
    @asyncAction
    * restartQuorum(item, callback) {
        this.loading = true;
        const result = yield restartQuorum(item);
        if (result.success) {
            this.restartResult = true;
        } else {
            this.restartResult = false;
            callback && callback(result.message);
        }
        this.lastRestartResult = true;
        this.loading = false;
    }

    /**
     * 获取集群动态扩容的结果列表信息
     * @param clusterId     集群id
     * @param callback  回调处理数据
     */
    @asyncAction
    * getClusterDynamicExpansionResult(clusterId: number, callback) {
        const result = yield getClusterDynamicExpansionResult(clusterId);
        if (result.success) {
            this.dynamicExpansionResultList = result.data.data;
            callback && callback(result.data.isClear)
        }
    }

    /**
     * 获取集群重启的结果列表信息
     * @param clusterId     集群id
     * @param callback  回调处理数据
     */
    @asyncAction
    * getClusterRestartResult(clusterId: number, callback) {
        const result = yield getClusterRestartResult(clusterId);
        if (result.success) {
            this.restartResultList = result.data.data;
            callback && callback(result.data.isClear)
        }
    }

    /**
     * 获取集群部署结果
     * @param callback 回调处理数据
     */
    @asyncAction
    * getClusterDeployResult(callback) {
        const result = yield getClusterDeployResult();
        if (result.success) {
            this.deployResultList = result.data.data;
            callback && callback(result.data.isClear)
        }
    }
}
