import {action, observable} from "mobx";
import {provideSingleton} from "../../common/utils/IOC";
import Pagination from "../../common/model/Pagination";
import {asyncAction} from "mobx-utils";
import {updateTask, updateTaskSwitchOn} from "../service/monitor";

// 监控任务搜索条件
export class MonitorTaskSearchVo {
    indicatorId: number;
    clusterId: number;
    switchOn: boolean;
}

// 监控任务信息
export class MonitorTaskVo {
    id: number;
    indicatorId: number;
    clusterName: string;
    clusterId: number;
    alertValue: string;
    alertInterval: number;
    alertFrequency: number;
    alertForm: number;
    switchOn: boolean;
    modifyUserId: number;
    createTime: Date;
    createTimeStr: string;
    modifyTime: Date;
    modifyTimestr: string;
}

@provideSingleton(MonitorTaskModel)
export class MonitorTaskModel extends Pagination {
    /**
     * 数据加载标志
     * @type {boolean}
     */
    @observable loading: boolean = false;

    /**
     * 监控任务列表
     * @type {Array}
     */
    @observable tasks: Array<MonitorTaskVo> = [];

    /**
     * 总共打开的tab页大小
     * @type {number}
     */
    @observable totalIndex: number = 0;

    /**
     * 监控任务数组信息，用于tab页展示
     * @type {Array}
     */
    @observable tasksArray: Array<any> = [];

    constructor() {
        super();
        this.path = '/monitor/tasks/query';
    }

    @action
    queryCallBack(success: boolean, list: Array<any>): void {
        if (success) {
            this.tasks = list as Array<MonitorTaskVo>;
            this.tasksArray.push(this.tasks);
            this.totalIndex = this.totalIndex + 1;
        }
        this.loading = false;
    }

    @action
    query(vo: MonitorTaskSearchVo) {
        this.loading = true;
        this.queryData(vo);
    }

    /**
     * 更新任务监控开关
     * @param activeKey  当前监控任务所在tab页信息
     * @param id         监控任务id
     * @param switchOn   开关
     */
    @asyncAction
    * updateTaskSwitchOn(activeKey: number, id: number, switchOn: boolean) {
        this.loading = true;
        const result = yield updateTaskSwitchOn({id: id, switchOn: switchOn});
        if (result.success) {
            if (activeKey >= 2 && this.tasksArray.length >= activeKey - 2 &&
                this.tasksArray[activeKey - 2]) {
                let taskSize = this.tasksArray[activeKey - 2].length;
                for (var i = 0; i < taskSize; i++){
                    if (this.tasksArray[activeKey - 2][i].id === id) {
                        this.tasksArray[activeKey - 2][i].switchOn = switchOn;
                    }
                }
            }
        }
        this.loading = false;
    }

    /**
     * 更新监控任务信息
     * @param editTaskIndex 监控任务所处位置信息
     * @param item          监控任务信息
     * @param callback      回调处理数据
     */
    @asyncAction
    * updateTask(editTaskIndex: number, item: MonitorTaskVo, callback) {
        this.loading = true;
        const result = yield updateTask(item);
        if (result.success) {
            if (editTaskIndex >= 2 && this.tasksArray.length >= editTaskIndex - 2 &&
                this.tasksArray[editTaskIndex - 2]) {
                let taskSize = this.tasksArray[editTaskIndex - 2].length;
                for (var i = 0; i < taskSize; i++){
                    if (this.tasksArray[editTaskIndex - 2][i].id === item.id) {
                        this.tasksArray[editTaskIndex - 2][i] = item;
                    }
                }
            }
            // 回调显示处理成功结果
            callback && callback(result.message);
        }
        this.loading = false;
    }

}






