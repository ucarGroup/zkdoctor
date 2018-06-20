import {action, observable} from "mobx";
import {provideSingleton} from "../../common/utils/IOC";
import Pagination from "../../common/model/Pagination";
import {asyncAction} from "mobx-utils";
import {pauseTrigger, removeTrigger, resumeTrigger} from "../service/quartz";

// 定时任务信息
export class TriggerInfoVo {
    id: string;
    triggerName: string;
    triggerGroup: string;
    cron: string;
    nextFireTimeStr: string;
    prevFireTimeStr: string;
    startTimeStr: string;
    triggerState: string;
    description: string;
}

@provideSingleton(QuartzModel)
export class QuartzModel extends Pagination {
    /**
     * 数据加载标志
     * @type {boolean}
     */
    @observable loading: boolean = false;

    /**
     * 定时任务列表
     * @type {Array}
     */
    @observable triggers: Array<TriggerInfoVo> = [];

    /**
     * 某个定时任务信息
     * @type {any}
     */
    @observable trigger: TriggerInfoVo = null;

    constructor() {
        super();
        this.path = '/manage/quartz/query';
    }

    @action
    queryCallBack(success: boolean, list: Array<any>): void {
        if (success) {
            this.triggers = list as Array<TriggerInfoVo>;
        }
        this.loading = false;
    }

    @action
    query(query) {
        this.loading = true;
        this.queryData(query);
    }

    /**
     * 暂停某个定时任务
     * @param triggerName  触发器名称
     * @param triggerGroup 触发器组
     * @param callback     回调处理数据
     */
    @asyncAction
    * pauseTrigger(triggerName, triggerGroup, callback) {
        const result = yield pauseTrigger(triggerName, triggerGroup);
        if (result.success) {
            this.query({});
            callback && callback(result.message);
        }
    }

    /**
     * 恢复暂停的定时任务
     * @param triggerName  触发器名称
     * @param triggerGroup 触发器组
     * @param callback     回调处理数据
     */
    @asyncAction
    * resumeTrigger(triggerName, triggerGroup, callback) {
        const result = yield resumeTrigger(triggerName, triggerGroup);
        if (result.success) {
            this.query({});
            callback && callback(result.message);
        }
    }

    @asyncAction
    * removeTrigger(triggerName, triggerGroup, callback) {
        const result = yield removeTrigger(triggerName, triggerGroup);
        if (result.success) {
            this.query({});
            callback && callback(result.message);
        }
    }

}
