import {request} from "../../common/utils/request";
import qs from "qs";

export function getClusterInfo(clusterId) {
    return request(`/cluster/info?${qs.stringify({clusterId: clusterId})}`, {});
}

export function getClusterDetail(clusterId) {
    return request(`/cluster/detail?${qs.stringify({clusterId: clusterId})}`, {});
}

export function getClusterUsers(clusterId) {
    return request(`/cluster/clusterAlarmUsers?${qs.stringify({clusterId: clusterId})}`, {});
}

export function deleteAlarmUser(params) {
    let formData = new FormData();
    for (let name in params) {
        formData.append(name, params[name]);
    }
    return request(`/cluster/deleteAlarmUser`, {
        method: 'POST',
        body: formData
    });
}

export function addClusterAlarmUser(params) {
    let formData = new FormData();
    for (let name in params) {
        formData.append(name, params[name]);
    }
    return request('/cluster/addClusterAlarmUser', {
        method: 'POST',
        body: formData
    });
}

export function modifyCluster(params) {
    return request(`/manage/cluster/modifyCluster?${qs.stringify(params)}`, {});
}

export function checkClusterNameExist(clusterName) {
    let params = {clusterName: clusterName};
    return request(`/cluster/checkClusterNameExist?${qs.stringify(params)}`, {});
}

export function addClusterSubmit(params) {
    let formData = new FormData();
    for (let name in params) {
        formData.append(name, params[name]);
    }
    return request('/manage/cluster/addCluster', {
        method: 'POST',
        body: formData
    });
}

export function updateMonitorStatus(params) {
    return request(`/manage/cluster/updateMonitorStatus?${qs.stringify(params)}`, {});
}

export function getClusterRootZnodes(clusterId) {
    let params = {clusterId: clusterId};
    return request(`/cluster/clusterRootZnodes?${qs.stringify(params)}`, {});
}
export function getClusterZnodesChildren(clusterId, znode) {
    let params = {clusterId: clusterId, znode: znode};
    return request(`/cluster/clusterZnodesChildren?${qs.stringify(params)}`, {});
}

export function deleteZnode(clusterId, znode) {
    let params = {clusterId: clusterId, znode: znode};
    return request(`/manage/cluster/deleteZnode?${qs.stringify(params)}`, {});
}

export function searchZnodeData(clusterId, znode, serializable) {
    let params = {clusterId: clusterId, znode: znode, serializable: serializable};
    return request(`/cluster/searchZnodeData?${qs.stringify(params)}`, {});
}

export function updateZnode(params) {
    let formData = new FormData();
    for (let name in params) {
        formData.append(name, params[name]);
    }
    return request('/manage/cluster/updateZnode', {
        method: 'POST',
        body: formData
    });
}

export function createZnode(params) {
    let formData = new FormData();
    for (let name in params) {
        formData.append(name, params[name]);
    }
    return request('/manage/cluster/createZnode', {
        method: 'POST',
        body: formData
    });
}
export function getMonitorTaskByClusterId(clusterId) {
    let params = {clusterId: clusterId};
    return request(`/monitorAlarm/getZkMonitorTaskDataByClusterId?${qs.stringify(params)}`, {});
}







