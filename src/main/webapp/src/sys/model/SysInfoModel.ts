import {action, observable} from "mobx";
import {asyncAction} from "mobx-utils";
import {provideSingleton} from "../../common/utils/IOC";
import {menuTreeList} from "../service/app";
import {notification} from "antd";

export class SysInfoVo {
    isNavbar: boolean = false;
    darkTheme: boolean = true;
    siderFold: boolean = false;
    menuPopoverVisible: boolean = false;
    navOpenKeys: Array<any> = [];
}

export class MessageVo {
    type: string;
    title: string;
    msg: string;


    constructor(type: string, title: string, msg: string) {
        this.type = type;
        this.title = title;
        this.msg = msg;
    }
}

@provideSingleton(SysInfoModel)
export class SysInfoModel {
    sysInfo: SysInfoVo = observable.object(new SysInfoVo());
    @observable serviceLine: string = 'none';
    @observable menu: Array<any> = [];

    routor: any = null;

    @action
    switchMenuPopver(): void {
        this.sysInfo.menuPopoverVisible = !this.sysInfo.menuPopoverVisible;
    }

    @action
    switchSider(): void {
        this.sysInfo.siderFold = !this.sysInfo.siderFold;
    }

    @action
    switchTheme(): void {
        this.sysInfo.darkTheme = !this.sysInfo.darkTheme;
    }

    @action
    handleNavOpenKeys(openKeys): void {
        this.sysInfo.navOpenKeys = openKeys;
    }

    @asyncAction
    * loadMenuTree() {
        const result = yield menuTreeList();
        if (result.success) {
            this.menu = result.data;
        }
    }

    // 记录上一次提示信息
    lastMessage = "";

    sendMessage(message: MessageVo) {
        if (message.title == '登录超时') {
            if (this.routor) {
                this.routor.push("/login");
            }
        }
        if ((this.lastMessage != '登录超时' || message.title != '登录超时') &&
            message.title != 'NO_USER') {
            notification[message.type]({
                message: message.title,
                description: message.msg
            });
            this.lastMessage = message.title;
        }
    }
}