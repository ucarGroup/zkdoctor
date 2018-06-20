import {observable} from "mobx";
import {provideSingleton} from "../../common/utils/IOC";
import {asyncAction} from "mobx-utils";
import {sshCommandExecute} from "../service/machine";
import Pagination from "../../common/model/Pagination";

@provideSingleton(SSHModel)
export class SSHModel extends Pagination {
    /**
     * 登录的机器ip信息
     * @type {string}
     */
    @observable hostIp: string = "";

    /**
     * 当前登录的服务器目录信息
     * @type {string}
     */
    @observable currentDirectory: string = "/home/zkdoctor";

    constructor() {
        super();
    }

    /**
     * 执行ssh命令
     * @param command   命令信息
     * @param console   控制台输出
     * @param callback  回调处理
     */
    @asyncAction
    * sshCommandExecute(command: string, console: any, callback: any) {
        command = command.trim();
        if (command == "") {
            callback && callback();
            return;
        }
        let toConnect = command.indexOf("ssh") == 0 && command != "ssh";
        if (this.hostIp == "" && toConnect) { // 未连接，进行连接
            this.hostIp = command.split(/\s+/g)[1];
            callback && callback();
            return;
        } else if (this.hostIp == "") { // 未连接，未进行有效连接
            console.log("请输入：ssh 'hostIp' 命令进行连接!");
            callback && callback();
            return;
        } else if (this.hostIp != "" && toConnect) { // 已连接，再次连接
            console.log("请先退出该服务器，再进行连接！");
            callback && callback();
            return;
        } else if (this.hostIp != "" && command == "exit") { // 已连接，退出
            console.log("exit!");
            this.hostIp = "";
            this.currentDirectory = "/home/zkdoctor";
            callback && callback();
            return;
        }
        const result = yield sshCommandExecute({
            command: command,
            hostIp: this.hostIp,
            currentDirectory: this.currentDirectory
        });
        if (result.success) {
            console.log(result.data.data);
            this.currentDirectory = result.data.currentDirectory;
            callback && callback();
        }
    }
}