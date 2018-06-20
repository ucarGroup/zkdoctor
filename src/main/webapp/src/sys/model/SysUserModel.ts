import {action, observable} from "mobx";
import {inject, provideSingleton} from "../../common/utils/IOC";
import {asyncAction} from "mobx-utils";
import {SysInfoModel} from "./SysInfoModel";
import {checkUserExists, loginSubmit, logoutSubmit, register} from "../service/app";
import {UserVo} from "../../user/model/UserModel";

export class SysUserVo {
    userName: string;
    chName: string;
    userRole: number;
    userId: number;
}

@provideSingleton(SysUserModel)
export class SysUserModel {
    /**
     * 数据加载标志
     * @type {boolean}
     */
    @observable loading: boolean = false;

    /**
     * 系统用户信息
     * @type {any}
     */
    @observable sysUser: SysUserVo = null;

    /**
     * 注册用户名是否已经存在
     * @type {any}
     */
    @observable userNameExists: boolean = null;


    @inject(SysInfoModel)
    sysInfoModel: SysInfoModel;

    /**
     * 登录成功处理
     * @param vo
     */
    @action
    loginSuccess(vo: SysUserVo): void {
        this.sysUser = vo;
        this.sysInfoModel.loadMenuTree();
    }

    /**
     * 登录
     * @param username  用户名
     * @param password  密码
     * @param router
     */
    @asyncAction
    * login(userName: string, password: string, router: any) {
        this.sysInfoModel.routor = router;
        this.loading = true;
        const result = yield loginSubmit({userName, password});
        this.loading = false;
        if (result.success) {
            this.sysInfoModel.lastMessage = "";
            this.loginSuccess(result.data);
            router.push("/dashboard")
        }
    }

    /**
     * 注销
     * @param router
     */
    @asyncAction
    * logout(router: any) {
        this.loading = true;
        const result = yield logoutSubmit();
        this.loading = false;
        if (result.success) {
            router.push("/login")
        }
    }

    /**
     * 用户注册
     * @param router    router
     * @param user      待注册用户信息
     * @param callback  回调处理数据
     */
    @asyncAction
    * register(router: any, user: UserVo, callback) {
        this.loading = true;
        const result = yield register(user);
        this.loading = false;
        if (result.success) {
            callback && callback(result.message);
            router.push("/login")
        }
    }

    /**
     * 校验注册的用户名是否已经存在
     * @param userName 用户名
     * @param callback 回调处理数据
     */
    @asyncAction
    * checkUserExists(userName, callback) {
        const result = yield checkUserExists(userName);
        if (result.success) {
            this.userNameExists = result.data;
            if (this.userNameExists == true) { // 如果用户名已经存在，则回调处理显示
                callback && callback();
            }
        }
    }
}