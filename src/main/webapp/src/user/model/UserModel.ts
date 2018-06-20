import {action, observable} from "mobx";
import {provideSingleton} from "../../common/utils/IOC";
import {create, listAll, remove, update} from "../service/user";
import {asyncAction} from "mobx-utils";
import Pagination from "../../common/model/Pagination";

// 用户搜索条件
export class UserSearchVo {
    userName?: String;
    chName?: string;
    email?: string;
}

// 用户信息
export class UserVo {
    id: number;
    userName: string;
    chName: string;
    password: string;
    email: string;
    mobile: string;
    userRole: number;
    param1: string;
}

@provideSingleton(UserModel)
export class UserModel extends Pagination {
    /**
     * 数据加载标志
     * @type {boolean}
     */
    @observable loading: boolean = false;

    /**
     * 用户列表
     * @type {Array}
     */
    @observable users: Array<UserVo> = [];

    constructor() {
        super();
        this.path = '/users/query';
    }

    @action
    queryCallBack(success: boolean, list: Array<any>): void {
        if (success) {
            this.users = list as Array<UserVo>;
        }
        this.loading = false;
    }

    @action
    query(vo: UserSearchVo) {
        this.loading = true;
        this.queryData(vo);
    }

    /**
     * 查询所有用户信息
     * @param callback 回调处理数据
     */
    @asyncAction
    * listAll(callback) {
        const result = yield listAll();
        if (result.success) {
            this.users=result.data;
            callback && callback();
        }
    }

    /**
     * 增加新用户信息
     * @param vo         用户信息
     * @param callback   回调处理数据
     */
    @asyncAction
    * add(vo: UserVo, callback) {
        this.loading = true;
        const result = yield create(vo);
        if (result.success) {
            this.listAll();
            // 回调显示处理成功结果
            callback && callback(result.message);
        }
        this.loading = false;
    }

    /**
     * 更新用户信息
     * @param vo         用户信息
     * @param callback   回调处理数据
     */
    @asyncAction
    * edit(vo: UserVo, callback) {
        this.loading = true;
        const result = yield update(vo);
        if (result.success) {
            const newList = this.users.map(bean => {
                if (bean.id === vo.id) {
                    return vo;
                }
                return bean;
            });
            this.users = newList;
            // 回调显示处理成功结果
            callback && callback(result.message);
        }
        this.loading = false;
    }

    /**
     * 删除用户信息
     * @param userId    用户id
     * @param callback  回调处理数据
     */
    @asyncAction
    * delete(userId: number, callback) {
        this.loading = true;
        const result = yield remove({userId: userId});
        if (result.success) {
            const newList = this.users.filter(bean => bean.id !== userId);
            this.users = newList;
            this.page.total = this.page.total -1;
            // 回调显示处理成功结果
            callback && callback(result.message);
        }
        this.loading = false;
    }
}
