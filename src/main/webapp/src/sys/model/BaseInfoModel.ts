import {provideSingleton} from "../../common/utils/IOC";

export class ServiceLine {
    index: number;
    tag: string;
    name: string;
}

export class ClusterStatus {
    index: number;
    tag: string;
    name: string;
    color: string;
}

export class DeployType {
    index: number;
    tag: string;
    name: string;
}

export class InstanceStatus {
    index: number;
    tag: string;
    name: string;
    color: string;
}

export class MachineStatus {
    index: boolean;
    tag: string;
    name: string;
    color: string;
}

export class ServerState {
    index: number;
    tag: string;
    name: string;
}

export class UserRole {
    index: number;
    tag: string;
    name: string;
}

export class AlertForm {
    index: number;
    tag: string;
    name: string;
}

@provideSingleton(BaseInfoModel)
export class BaseInfoModel {

    clusterStatus: Array<ClusterStatus> =
        [{
            tag: 'notMonitoring',
            name: '未监控',
            index: 1,
            color: '#3E9BBF'
        }, {
            tag: 'running',
            name: '运行中',
            index: 2,
            color: '#87d068'
        }, {
            tag: 'offline',
            name: '已下线',
            index: 3,
            color: '#BFBFBF'
        }, {
            tag: 'exception',
            name: '异常',
            index: 4,
            color: '#F0544F'
        }];

    getClusterStatusName = (index: number) => {
        let list = this.clusterStatus.filter(item => item.index == index);
        if (list[0]) {
            return list[0].name;
        }
        return '';
    };

    getClusterStatusColor = (index: number) => {
        let list = this.clusterStatus.filter(item => item.index == index);
        if (list[0]) {
            return list[0].color;
        }
        return '';
    };

    deployTypes: Array<DeployType> =
        [{
            tag: 'cluster',
            name: '集群模式',
            index: 1
        }, {
            tag: 'standalone',
            name: '独立模式',
            index: 2
        }];

    getDeployTypeName = (index: number) => {
        let list = this.deployTypes.filter(item => item.index == index);
        if (list[0]) {
            return list[0].name;
        }
        return '';
    };

    instanceStatus: Array<InstanceStatus> =
        [{
            tag: 'exception',
            name: '异常',
            index: 0,
            color: '#F0544F'
        }, {
            tag: 'running',
            name: '正在运行',
            index: 1,
            color: '#87d068'
        }, {
            tag: 'offline',
            name: '已下线',
            index: 3,
            color: '#BFBFBF'
        }, {
            tag: 'notRunning',
            name: '未运行',
            index: 4,
            color: '#3E9BBF'
        }];

    getInstanceStatusName = (index: number) => {
        let list = this.instanceStatus.filter(item => item.index == index);
        if (list[0]) {
            return list[0].name;
        }
        return '';
    };

    getInstanceStatusColor = (index: number) => {
        let list = this.instanceStatus.filter(item => item.index == index);
        if (list[0]) {
            return list[0].color;
        }
        return '';
    };

    machineStatus: Array<MachineStatus> =
        [{
            tag: 'exception',
            name: '异常',
            index: false,
            color: '#F0544F'
        }, {
            tag: 'runnning',
            name: '正常',
            index: true,
            color: '#87d068'
        }];

    getMachineStatusName = (index: boolean) => {
        let list = this.machineStatus.filter(item => item.index == index);
        if (list[0]) {
            return list[0].name;
        }
        return '';
    };

    getMachineStatusColor = (index: boolean) => {
        let list = this.machineStatus.filter(item => item.index == index);
        if (list[0]) {
            return list[0].color;
        }
        return '';
    };

    serverStates: Array<ServerState> =
        [{
            tag: 'follower',
            name: 'follower',
            index: 0,
        }, {
            tag: 'leader',
            name: 'leader',
            index: 1,
        }, {
            tag: 'observer',
            name: 'observer',
            index: 2,
        }, {
            tag: 'standalone',
            name: 'standalone',
            index: 3,
        }];

    getServerStatesName = (index: boolean) => {
        let list = this.serverStates.filter(item => item.index == index);
        if (list[0]) {
            return list[0].name;
        }
        return '';
    };

    userRoles: Array<UserRole> =
        [{
            tag: 'general',
            name: '普通用户',
            index: 0,
        }, {
            tag: 'admin',
            name: '管理员',
            index: 1,
        }, {
            tag: 'superadmin',
            name: '超级管理员',
            index: 2,
        }];

    getUserRoleName = (index: boolean) => {
        let list = this.userRoles.filter(item => item.index == index);
        if (list[0]) {
            return list[0].name;
        }
        return '';
    };

    alertForms: Array<AlertForm> =
        [{
            tag: 'mail',
            name: '邮件',
            index: 0,
        }, {
            tag: 'message',
            name: '短信',
            index: 1,
        }, {
            tag: 'mailandmessage',
            name: '邮件+短信',
            index: 2,
        }];

    getAlertFormName = (index: boolean) => {
        let list = this.alertForms.filter(item => item.index == index);
        if (list[0]) {
            return list[0].name;
        }
        return '';
    };

}