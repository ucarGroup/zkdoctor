import {action, observable} from "mobx";
import {provideSingleton} from "../../common/utils/IOC";
import Pagination from "../../common/model/Pagination";
import {asyncAction} from "mobx-utils";
import {deleteConfig, updateConfig} from "../service/SystemOpsService";


export class SystemConfigVo {
    id: number;
    configName: string;
    configValue: string;
    defaultConfigValue: string;
    configDesc: string;
}


@provideSingleton(SystemOpsModel)
export class SystemOpsModel extends Pagination {
    /**
     * 数据加载标志
     * @type {boolean}
     */
    @observable loading: boolean = false;

    /**
     * 系统配置列表
     * @type {Array}
     */
    @observable sysConfigs: Array<SystemConfigVo> = [];

    constructor() {
        super();
        this.path = '/manage/system/config/query';
    }

    @action
    queryCallBack(success: boolean, list: Array<any>): void {
        if (success) {
            this.sysConfigs = list as Array<SystemConfigVo>;
        }
        this.loading = false;
    }

    @action
    query(vo) {
        this.loading = true;
        this.queryData(vo);
    }

    /**
     * 更新配置
     * @param item     配置信息
     * @param callback 回调处理数据
     */
    @asyncAction
    * updateConfig(item, callback: any) {
        this.loading = true;
        const result = yield updateConfig(item);
        if (result.success) {
            this.sysConfigs = this.sysConfigs.map(bean => {
                if (bean.configName === item.configName) {
                    bean = item;
                }
                return bean;
            });
            // 回调显示处理成功结果
            callback && callback(result.message);
        }
        this.loading = false;
    }

    /**
     * 删除系统配置，走默认配置
     * @param configName 配置名称
     * @param callback   回调处理数据
     */
    @asyncAction
    * deleteConfig(configName, callback: any) {
        this.loading = true;
        const result = yield deleteConfig({configName: configName});
        if (result.success) {
            this.sysConfigs = this.sysConfigs.map(bean => {
                if (bean.configName === configName) {
                    bean.configValue = "";
                }
                return bean;
            });
            // 回调显示处理成功结果
            callback && callback(result.message);
        }
        this.loading = false;
    }

}
