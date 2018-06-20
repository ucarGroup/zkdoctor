import {action, observable} from "mobx";
import {provideSingleton} from "../../common/utils/IOC";
import Pagination from "../../common/model/Pagination";
import {asyncAction} from "mobx-utils";
import {getCollectInfo} from "../service/ClusterMonitorAlarmInfoService";
// 集群监控报警信息
export class ClusterMonitorAlarmInfoVo {
    id: number;
    clusterId: number;
    clusterName: string;
    machineId: number;
    machineIp: string;
    instanceId: number;
    instanceIp: string;
    port: number;
    infoContent: string;
    alarmTime: Date;
    remark: string;
    param1: String;
}

export class IndexCollectInfoVo {
    sumTotal: number;
    runningTotal: number;
    notRunningTotal: number;
    exceptionsTotal: number;
    referralTotal: number;
    unmonitoredTotal: number;

}
export class IndexCollectInfosVo {
    clusterCollectInfo: IndexCollectInfoVo;
    instanceCollectInfo: IndexCollectInfoVo;
}
@provideSingleton(ClusterMonitorAlarmInfoModel)
export class ClusterMonitorAlarmInfoModel extends Pagination {
    /**
     * 数据加载标志
     * @type {boolean}
     */
    @observable
    loading: boolean = false;
    /**
     * 集群监控报警信息列表
     * @type {Array}
     */
    @observable
    clusterMonitorAlarmInfos: Array<ClusterMonitorAlarmInfoVo> = [];
    /**
     * 某个集群监控报警信息
     * @type {any}
     */
    @observable
    clusterMonitorAlarmInfo: ClusterMonitorAlarmInfoVo = null;


    /**
     * 集群监控报指标汇总信息列表
     * @type {Array}
     */
    @observable
    indexCollects: IndexCollectInfosVo = null;
    /**
     * 某个指标汇总信息
     * @type {any}
     */
    @observable
    indexCollect: IndexCollectInfoVo = null;

    constructor() {
        super();
        this.path = '/alarmInfo/getAllZkAlarmInfo';
    }

    @action
    queryCallBack(success: boolean, list: Array<any>): void {
        if (success) {
            this.clusterMonitorAlarmInfos = list as Array<ClusterMonitorAlarmInfoVo>;
        }
        this.loading = false;
    }

    @action
    query(vo: ClusterMonitorAlarmInfoVo) {
        this.loading = true;
        this.queryData(vo);
    }

    /**
     * 获取集群指标汇总信息
     */
    @asyncAction
    * queryAllCollectInfo() {
        this.loading = true;
        const result = yield getCollectInfo();
        if (result.success) {
            this.indexCollects = result.data.data;

        }
        this.loading = false;
    }

    /**
     * 更新报警信息
     * @param item     监控指标信息
     * @param callback 回调处理数据
     */
    @asyncAction
    * updateAlarmInfo(item: ClusterMonitorAlarmInfoVo, callback) {
        this.loading = true;
        // const result = yield updateIndicator(item);
        const result = null;
        if (result.success) {
            const newList = this.clusterMonitorAlarmInfos.map(bean => {
                if (bean.id === item.id) {
                    return item;
                }
                return bean;
            });
            this.clusterMonitorAlarmInfos = newList;
            // 回调显示处理成功结果
            callback && callback(result.message);
        }
        this.loading = false;
    }

}
