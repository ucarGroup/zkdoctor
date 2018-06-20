import {action, observable} from "mobx";

import {provideSingleton} from "../../common/utils/IOC";
import Pagination from "../../common/model/Pagination";
import {asyncAction} from "mobx-utils";
import {updateMonitorTask, updateMonitorTaskSwitchOn} from "../service/MonitorAlarmTaskService";
import {getMonitorTaskByClusterId} from "../../cluster/service/cluster";

// 集群监控报警任务查询条件
export class ClusterMonitorAlarmTaskSearchVo {
    indicatorName: string;
    clusterName: string;
    switchOn: boolean;
    modifyUserId: number;
    clusterId: number;
}
// 集群监控报警任务信息
export class ClusterMonitorAlarmTaskVo {
    id: number;
    indicatorId: number;
    clusterId: number;
    indicatorName: string;
    clusterName: string;
    alertValue: string;
    alertInterval: number;
    alertFrequency: number;
    alertValueUnit: string;
    alertForm: number;
    switchOn: boolean;
    modifyUserId: number;
    createTime: Date;
    modifyTime: Date;
    param1: string;
}

@provideSingleton(ClusterMonitorAlarmTaskModel)
export class ClusterMonitorAlarmTaskModel extends Pagination {
    /**
     * 数据加载标志
     * @type {boolean}
     */
    @observable
    loading: boolean = false;

    /**
     * 集群监控报警任务列表
     * @type {Array}
     */
    @observable
    clusterMonitorAlarmTasks: Array<ClusterMonitorAlarmTaskVo> = [];

    /**
     * 某项监控指标信息
     * @type {any}
     */
    @observable
    clusterMonitorAlarmTask: ClusterMonitorAlarmTaskVo = null;

    constructor() {
        super();
        this.path = '/monitor/getZkMonitorTaskDataByClusterId';
    }

    @action
    queryCallBack(success: boolean, list: Array<any>): void {
        if (success) {
            this.clusterMonitorAlarmTasks = list as Array<ClusterMonitorAlarmTaskVo>;
        }
        this.loading = false;
    }

    @action
    query(vo: ClusterMonitorAlarmTaskSearchVo) {
        this.loading = true;
        this.queryData(vo);
    }

    /**
     * 修改集群监控任务开关
     * @param id       监控指标id
     * @param switchOn 开关
     */
    @asyncAction
    * updateClusterTaskSwitchOn(id: number, switchOn: boolean) {
        this.loading = true;
        const result = yield updateMonitorTaskSwitchOn({id: id, switchOn: switchOn});
        if (result.success) {
            const newList = this.clusterMonitorAlarmTasks.map(bean => {
                if (bean.id === id) {
                    return {...bean, switchOn: switchOn};
                }
                return bean;
            });
            this.clusterMonitorAlarmTasks = newList;
        }
        this.loading = false;
    }

    /**
     * 更新集群监控报警任务信息
     * @param item    集群监控报警任务信息
     * @param callback 回调处理数据
     */
    @asyncAction
    * updateClusterMonitorTask(item: ClusterMonitorAlarmTaskVo, callback) {
        this.loading = true;
        const result = yield updateMonitorTask(item);
        if (result.success) {
            const newList = this.clusterMonitorAlarmTasks.map(bean => {
                if (bean.id === item.id) {
                    return item;
                }
                return bean;
            });
            this.clusterMonitorAlarmTasks = newList;
            // 回调显示处理成功结果
            callback && callback(result.message);
        }
        this.loading = false;
    }
    /**
     * 根据集群ID获取该集群下的所有监控任务信息
     * @param clusterId 集群id
     */
    @asyncAction
    * getMonitorTaskByClusterId(clusterId: number) {
        const result = yield getMonitorTaskByClusterId(clusterId);
        if (result.success) {
            this.clusterMonitorAlarmTasks = result.data;
        }
    }
    /**
     * 查询所有监控任务信息
     * @param clusterId 集群id
     */
    @asyncAction
    * queryAllData(clusterId: number) {
        this.loading = true;
        const result = yield getMonitorTaskByClusterId(clusterId);
        if (result.success) {
            this.clusterMonitorAlarmTasks = result.data;
        }
        this.loading = false;
    }

}

