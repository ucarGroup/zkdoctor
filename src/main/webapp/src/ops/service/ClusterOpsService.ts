import {request} from "../../common/utils/request";


export function clusterDeploySubmit(params) {
    let formData = new FormData();
    for (let name in params) {
        formData.append(name, params[name]);
    }
    return request('/manage/cluster/clusterDeploy', {
        method: 'POST',
        body: formData
    });
}

export function clusterDynamicExpansion(params) {
    let formData = new FormData();
    for (let name in params) {
        formData.append(name, params[name]);
    }
    return request('/manage/cluster/dynamicExpansion', {
        method: 'POST',
        body: formData
    });
}


export function offLine(clusterId) {
    let params = {clusterId: clusterId};
    let formData = new FormData();
    for (let name in params) {
        formData.append(name, params[name]);
    }
    return request('/manage/cluster/offLine', {
        method: 'POST',
        body: formData,
    });
}
export function restartQuorum(params) {
    let formData = new FormData();
    for (let name in params) {
        formData.append(name, params[name]);
    }
    return request('/manage/cluster/restartQuorum', {
        method: 'POST',
        body: formData,
    });
}

export function getClusterDynamicExpansionResult(clusterId) {
    let params = {clusterId: clusterId};
    let formData = new FormData();
    for (let name in params) {
        formData.append(name, params[name]);
    }
    return request('/manage/cluster/getClusterDynamicExpansionResult', {
        method: 'POST',
        body: formData,
    });
}

export function getClusterRestartResult(clusterId) {
    let params = {clusterId: clusterId};
    let formData = new FormData();
    for (let name in params) {
        formData.append(name, params[name]);
    }
    return request('/manage/cluster/getClusterRestartResult', {
        method: 'POST',
        body: formData,
    });
}

export function getClusterDeployResult() {
    return request('/manage/cluster/getClusterDeployResult');
}


