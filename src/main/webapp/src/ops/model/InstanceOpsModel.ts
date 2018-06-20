import {observable} from "mobx";
import {provideSingleton} from "../../common/utils/IOC";
import Pagination from "../../common/model/Pagination";
import {asyncAction} from "mobx-utils";
import {ConfigFileVo, InstanceConfigOpsVo} from "../../instance/model/InstanceModel";
import {
    addNewConfigFile,
    addNewInstance,
    instanceConfOps,
    instanceUpdateServer,
    offLineInstance,
    queryInstanceConfig,
    queryUploadedJarFile,
    removeInstance,
    restartInstance
} from "../service/InstanceOpsService";

@provideSingleton(InstanceOpsModel)
export class InstanceOpsModel extends Pagination {
    /**
     * 数据加载标志
     * @type {boolean}
     */
    @observable loading: boolean = false;

    constructor() {
        super();
    }

    /**
     * 移除实例
     * @param instanceId 实例id
     * @param callback   回调处理结果
     */
    @asyncAction
    * removeInstance(instanceId, callback) {
        this.loading = true;
        const result = yield removeInstance(instanceId);
        if (result.success) {
            // 回调显示处理成功结果
            callback && callback(result.message);
        }
        this.loading = false;
    }

    /**
     * 下线实例
     * @param instanceId 实例id
     * @param callback   回调处理结果
     */
    @asyncAction
    * offLineInstance(instanceId, callback) {
        this.loading = true;
        const result = yield offLineInstance(instanceId);
        if (result.success) {
            // 回调显示处理成功结果
            callback && callback(result.message);
        }
        this.loading = false;
    }

    /**
     * 重启实例
     * @param instanceId 实例id
     * @param callback   回调处理结果
     */
    @asyncAction
    * restartInstance(instanceId, callback) {
        this.loading = true;
        const result = yield restartInstance(instanceId);
        if (result.success) {
            // 回调显示处理成功结果
            callback && callback(result.message);
        }
        this.loading = false;
    }

    /**
     * 查询实例配置信息
     * @param host       实例ip
     * @param callback   回调处理结果
     */
    @asyncAction
    * queryInstanceConfig(host, callback) {
        this.loading = true;
        const result = yield queryInstanceConfig(host);
        if (result.success) {
            callback && callback(result.data);
        }
        this.loading = false;
    }

    /**
     * 修改实例配置
     * @param instanceConfigOpsVo 实例配置信息
     * @param callback            回调处理数据
     */
    @asyncAction
    * instanceConfOps(instanceConfigOpsVo: InstanceConfigOpsVo, callback) {
        this.loading = true;
        const result = yield instanceConfOps(instanceConfigOpsVo);
        if (result.success) {
            // 回调显示处理成功结果
            callback && callback(result.message);
        }
        this.loading = false;
    }

    /**
     * 新增实例
     * @param params   新实例参数
     * @param callback 回调处理数据
     */
    @asyncAction
    * addNewInstance(params, callback) {
        this.loading = true;
        const result = yield addNewInstance(params);
        if (result.success) {
            // 回调显示处理成功结果
            callback && callback(result.message);
        }
        this.loading = false;
    }

    /**
     * 新增配置文件，主要用于myid文件
     * @param configFileVo 新配置文件信息
     * @param callback     回调处理数据
     */
    @asyncAction
    * addNewConfigFile(configFileVo: ConfigFileVo, callback) {
        this.loading = true;
        const result = yield addNewConfigFile(configFileVo);
        if (result.success) {
            // 回调显示处理成功结果
            callback && callback(result.message);
        }
        this.loading = false;
    }

    /**
     * 查询升级的jar文件是否已经上传
     * @param callback 回调处理结果
     */
    @asyncAction
    * queryUploadedJarFile(callback) {
        const result = yield queryUploadedJarFile();
        if (result.success) {
            callback && callback(result.data);
        }
    }

    /**
     * 升级jar文件
     * @param instanceId 实例id
     * @param callback   回调处理数据
     */
    @asyncAction
    * instanceUpdateServer(instanceId, callback) {
        this.loading = true;
        const result = yield instanceUpdateServer(instanceId);
        if (result.success) {
            // 回调显示处理成功结果
            callback && callback(result.message);
        }
        this.loading = false;
    }

}
