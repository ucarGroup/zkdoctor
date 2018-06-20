import {provideSingleton} from "../../common/utils/IOC";
import {ClusterStatSearchVo} from "./ClusterModel";
import {observable} from "mobx";
import {
    getApproximateDataSizeAllInsTrend,
    getAvgLatencyAllInsTrend,
    getAvgLatencyMax,
    getConnectionsAllInsTrend,
    getConnectionTotal,
    getEphemerals,
    getEphemeralsAllInsTrend,
    getFollowersAllInsTrend,
    getInitTrend,
    getMaxFileDescriptorCountAllInsTrend,
    getMaxLatencyAllInsTrend,
    getMaxLatencyMax,
    getMinLatencyAllInsTrend,
    getOpenFileDescriptorCountAllInsTrend,
    getOutstandingsAllInsTrend,
    getOutstandingsTrend,
    getPendingSyncsAllInsTrend,
    getReceivedAllInsTrend,
    getReceivedTrend,
    getSendAllInsTrend,
    getServerStateLagAllInsTrend,
    getSyncedFollowersAllInsTrend,
    getWatcherCountAllInsTrend,
    getWatcherTotal,
    getZnodeCount,
    getZnodeCountAllInsTrend
} from "../service/clusterStat";
import {asyncAction} from "mobx-utils";

@provideSingleton(ClusterStatsTrendModel)
export class ClusterStatsTrendModel {
    /**
     * 数据加载标志
     * @type {boolean}
     */
    @observable loading: boolean = false;

    /**
     * zk集群历史统计数据信息
     * @type {{}}
     */
    @observable receivedTrendData: any = {};
    @observable outstandingsTrendData: any = {};
    @observable maxLatencyMaxTrendData: any = {};
    @observable avgLatencyMaxTrendData: any = {};
    @observable znodeCountTrendData: any = {};
    @observable ephemeralsTrendData: any = {};
    @observable watcherTotalTrendData: any = {};
    @observable connectionTotalTrendData: any = {};

    /**
     * 历史统计信息详情，包含各个实例的数据
     * @type {{}}
     */
    @observable allInsTrendData: any = {};

    /**
     * 初始化加载所有指标历史数据
     * @param searchVo 搜索条件，包含集群id，起始时间以及结束时间
     */
    @asyncAction
    * initData(searchVo: ClusterStatSearchVo) {
        this.loading = true;
        const result = yield getInitTrend(searchVo);
        if (result.success) {
            this.receivedTrendData = result.data.received;
            this.outstandingsTrendData = result.data.outstandings;
            this.maxLatencyMaxTrendData = result.data.maxLatency;
            this.avgLatencyMaxTrendData = result.data.avgLatency;
            this.znodeCountTrendData = result.data.znodeCount;
            this.ephemeralsTrendData = result.data.ephemerals;
            this.watcherTotalTrendData = result.data.watcherCount;
            this.connectionTotalTrendData = result.data.connections;
        }
        this.loading = false;
    }

    /**
     * 查询收包数历史统计信息
     * @param searchVo 搜索条件，包含集群id，起始时间以及结束时间
     */
    @asyncAction
    * queryReceived(searchVo: ClusterStatSearchVo) {
        this.loading = true;
        const result = yield getReceivedTrend(searchVo);
        if (result.success) {
            this.receivedTrendData = result.data;
        }
        this.loading = false;
    }

    /**
     * 查询发包数历史统计信息
     * @param searchVo 搜索条件，包含集群id，起始时间以及结束时间
     */
    @asyncAction
    * queryOutstandings(searchVo: ClusterStatSearchVo) {
        this.loading = true;
        const result = yield getOutstandingsTrend(searchVo);
        if (result.success) {
            this.outstandingsTrendData = result.data;
        }
        this.loading = false;
    }

    /**
     * 查询最大延时历史统计信息
     * @param searchVo 搜索条件，包含集群id，起始时间以及结束时间
     */
    @asyncAction
    * queryMaxLatencyMax(searchVo: ClusterStatSearchVo) {
        this.loading = true;
        const result = yield getMaxLatencyMax(searchVo);
        if (result.success) {
            this.maxLatencyMaxTrendData = result.data;
        }
        this.loading = false;
    }

    /**
     * 查询平均延时历史统计信息
     * @param searchVo 搜索条件，包含集群id，起始时间以及结束时间
     */
    @asyncAction
    * queryAvgLatencyMax(searchVo: ClusterStatSearchVo) {
        this.loading = true;
        const result = yield getAvgLatencyMax(searchVo);
        if (result.success) {
            this.avgLatencyMaxTrendData = result.data;
        }
        this.loading = false;
    }

    /**
     * 查询节点数历史统计信息
     * @param searchVo 搜索条件，包含集群id，起始时间以及结束时间
     */
    @asyncAction
    * queryZnodeCount(searchVo: ClusterStatSearchVo) {
        this.loading = true;
        const result = yield getZnodeCount(searchVo);
        if (result.success) {
            this.znodeCountTrendData = result.data;
        }
        this.loading = false;
    }

    /**
     * 查询临时节点数历史统计信息
     * @param searchVo 搜索条件，包含集群id，起始时间以及结束时间
     */
    @asyncAction
    * queryEphemerals(searchVo: ClusterStatSearchVo) {
        this.loading = true;
        const result = yield getEphemerals(searchVo);
        if (result.success) {
            this.ephemeralsTrendData = result.data;
        }
        this.loading = false;
    }

    /**
     * 查询watcher数历史统计信息
     * @param searchVo 搜索条件，包含集群id，起始时间以及结束时间
     */
    @asyncAction
    * queryWatcherTotal(searchVo: ClusterStatSearchVo) {
        this.loading = true;
        const result = yield getWatcherTotal(searchVo);
        if (result.success) {
            this.watcherTotalTrendData = result.data;
        }
        this.loading = false;
    }

    /**
     * 查询连接数历史统计信息
     * @param searchVo 搜索条件，包含集群id，起始时间以及结束时间
     */
    @asyncAction
    * queryConnectionTotal(searchVo: ClusterStatSearchVo) {
        this.loading = true;
        const result = yield getConnectionTotal(searchVo);
        if (result.success) {
            this.connectionTotalTrendData = result.data;
        }
        this.loading = false;
    }

    /**
     * 查询集群某项指标的所有实例统计信息
     * @param searchVo  搜索条件，包含集群id，起始时间以及结束时间
     * @param indicator 查询指标信息
     */
    @asyncAction
    * queryAllIns(searchVo: ClusterStatSearchVo, indicator) {
        this.loading = true;
        let result;
        let indicatorTmp = indicator ? indicator : 'received';
        if (indicatorTmp == 'outstandings') {
            result = yield getOutstandingsAllInsTrend(searchVo);
        } else if (indicatorTmp == 'received') {
            result = yield getReceivedAllInsTrend(searchVo);
        } else if (indicatorTmp == 'send') {
            result = yield getSendAllInsTrend(searchVo);
        } else if (indicatorTmp == 'maxLatency') {
            result = yield getMaxLatencyAllInsTrend(searchVo);
        } else if (indicatorTmp == 'avgLatency') {
            result = yield getAvgLatencyAllInsTrend(searchVo);
        } else if (indicatorTmp == 'minLatency') {
            result = yield getMinLatencyAllInsTrend(searchVo);
        } else if (indicatorTmp == 'znodeCount') {
            result = yield getZnodeCountAllInsTrend(searchVo);
        } else if (indicatorTmp == 'ephemerals') {
            result = yield getEphemeralsAllInsTrend(searchVo);
        } else if (indicatorTmp == 'watcherCount') {
            result = yield getWatcherCountAllInsTrend(searchVo);
        } else if (indicatorTmp == 'connections') {
            result = yield getConnectionsAllInsTrend(searchVo);
        } else if (indicatorTmp == 'approximateDataSize') {
            result = yield getApproximateDataSizeAllInsTrend(searchVo);
        } else if (indicatorTmp == 'openFileDescriptorCount') {
            result = yield getOpenFileDescriptorCountAllInsTrend(searchVo);
        } else if (indicatorTmp == 'maxFileDescriptorCount') {
            result = yield getMaxFileDescriptorCountAllInsTrend(searchVo);
        } else if (indicatorTmp == 'followers') {
            result = yield getFollowersAllInsTrend(searchVo);
        } else if (indicatorTmp == 'syncedFollowers') {
            result = yield getSyncedFollowersAllInsTrend(searchVo);
        } else if (indicatorTmp == 'pendingSyncs') {
            result = yield getPendingSyncsAllInsTrend(searchVo);
        } else if (indicatorTmp == 'serverStateLag') {
            result = yield getServerStateLagAllInsTrend(searchVo);
        }
        if (result.success) {
            this.allInsTrendData = result.data;
        }
        this.loading = false;
    }

}