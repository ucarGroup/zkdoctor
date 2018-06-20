import {action, observable} from "mobx";
import {provideSingleton} from "../../common/utils/IOC";
import Pagination from "../../common/model/Pagination";
import {asyncAction} from "mobx-utils";
import {deleteServiceLine, insertServiceLine, updateServiceLine} from "../service/ServiceLineOpsService";

// 业务线信息
export class ServiceLineVo {
    id: number;
    serviceLineName: string;
    serviceLineDesc: string;
    createTime: Date;
    createTimeStr: string;
    modifyTime: Date;
    modifyTimeStr: string;
}

@provideSingleton(ServiceLineOpsModel)
export class ServiceLineOpsModel extends Pagination {
    /**
     * 数据加载标志
     * @type {boolean}
     */
    @observable loading: boolean = false;

    /**
     * 业务线列表
     * @type {Array}
     */
    @observable serviceLines: Array<ServiceLineVo> = [];

    constructor() {
        super();
        this.path = '/cluster/serviceLine/query';
    }

    @action
    getServiceLineName = (index: number) => {
        let list = this.serviceLines.filter(item => item.id == index);
        if (list[0]) {
            return list[0].serviceLineDesc;
        }
        return '';
    };

    @action
    queryCallBack(success: boolean, list: Array<any>): void {
        if (success) {
            this.serviceLines = list as Array<ServiceLineVo>;
        }
        this.loading = false;
    }

    @action
    query(vo) {
        this.loading = true;
        this.queryData(vo);
    }

    /**
     * 新增业务线信息
     * @param item     新业务线信息
     * @param callback 回调处理数据
     */
    @asyncAction
    * insertServiceLine(item, callback: any) {
        this.loading = true;
        const result = yield insertServiceLine(item);
        if (result.success) {
            this.query({});
            // 回调显示处理成功结果
            callback && callback(result.message);
        }
        this.loading = false;
    }

    /**
     * 更新业务线信息
     * @param item     业务线信息
     * @param callback 回调处理数据
     */
    @asyncAction
    * updateServiceLine(item, callback: any) {
        this.loading = true;
        const result = yield updateServiceLine(item);
        if (result.success) {
            this.query({});
            // 回调显示处理成功结果
            callback && callback(result.message);
        }
        this.loading = false;
    }

    /**
     * 删除业务线信息
     * @param serviceLineName 业务系名称
     * @param callback        回调处理数据
     */
    @asyncAction
    * deleteServiceLine(serviceLineName, callback: any) {
        this.loading = true;
        const result = yield deleteServiceLine({serviceLineName: serviceLineName});
        if (result.success) {
            this.serviceLines = this.serviceLines.filter(bean => {
                return bean.serviceLineName != serviceLineName;
            });
            // 回调显示处理成功结果
            callback && callback(result.message);
        }
        this.loading = false;
    }

}
