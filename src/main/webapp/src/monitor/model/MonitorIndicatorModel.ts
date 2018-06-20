import {action, observable} from "mobx";
import {provideSingleton} from "../../common/utils/IOC";
import Pagination from "../../common/model/Pagination";
import {asyncAction} from "mobx-utils";
import {updateIndicator, updateIndicatorSwitchOn} from "../service/monitor";

// 监控指标查询条件
export class MonitorIndicatorSearchVo {
    indicatorName: string;
    className: string;
    switchOn: boolean;
    modifyUserId: number;
}

// 监控指标信息
export class MonitorIndicatorVo {
    id: number;
    indicatorName: string;
    className: string;
    defaultAlertValue: string;
    defaultAlertInterval: number;
    defaultAlertFrequency: number;
    defaultAlertForm: number;
    alertValueUnit: string;
    switchOn: boolean;
    modifyUserId: number;
    info: string;
    createTime: Date;
    createTimeStr: string;
    modifyTime: Date;
    modifyTimestr: string;
}

@provideSingleton(MonitorIndicatorModel)
export class MonitorIndicatorModel extends Pagination {
    /**
     * 数据加载标志
     * @type {boolean}
     */
    @observable loading: boolean = false;

    /**
     * 监控指标列表
     * @type {Array}
     */
    @observable indicators: Array<MonitorIndicatorVo> = [];

    /**
     * 某项监控指标信息
     * @type {any}
     */
    @observable indicator: MonitorIndicatorVo = null;

    constructor() {
        super();
        this.path = '/monitor/indicator/query';
    }

    @action
    queryCallBack(success: boolean, list: Array<any>): void {
        if (success) {
            this.indicators = list as Array<MonitorIndicatorVo>;
        }
        this.loading = false;
    }

    @action
    query(vo: MonitorIndicatorSearchVo) {
        this.loading = true;
        this.queryData(vo);
    }

    /**
     * 更新监控指标开关
     * @param id       监控指标id
     * @param switchOn 开关
     */
    @asyncAction
    *updateIndicatorSwitchOn(id: number, switchOn: boolean) {
        this.loading = true;
        const result = yield updateIndicatorSwitchOn({id: id, switchOn: switchOn});
        if (result.success) {
            const newList = this.indicators.map(bean => {
                if (bean.id === id) {
                    return {...bean, switchOn: switchOn};
                }
                return bean;
            });
            this.indicators = newList;
        }
        this.loading = false;
    }

    /**
     * 更新监控指标信息
     * @param item     监控指标信息
     * @param callback 回调处理数据
     */
    @asyncAction
    * updateIndicator(item: MonitorIndicatorVo, callback) {
        this.loading = true;
        const result = yield updateIndicator(item);
        if (result.success) {
            const newList = this.indicators.map(bean => {
                if (bean.id === item.id) {
                    return item;
                }
                return bean;
            });
            this.indicators = newList;
            // 回调显示处理成功结果
            callback && callback(result.message);
        }
        this.loading = false;
    }


}

