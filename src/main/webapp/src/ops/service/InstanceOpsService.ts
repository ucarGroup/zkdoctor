import {request} from "../../common/utils/request";

export function removeInstance(instanceId) {
    let params = {instanceId: instanceId};
    let formData = new FormData();
    for (let name in params) {
        formData.append(name, params[name]);
    }
    return request('/manage/instance/removeInstance', {
        method: 'POST',
        body: formData,
    });
}
export function offLineInstance(instanceId) {
    let params = {instanceId: instanceId};
    let formData = new FormData();
    for (let name in params) {
        formData.append(name, params[name]);
    }
    return request('/manage/instance/offLineInstance', {
        method: 'POST',
        body: formData,
    });
}

export function restartInstance(instanceId) {
    let params = {instanceId: instanceId};
    let formData = new FormData();
    for (let name in params) {
        formData.append(name, params[name]);
    }
    return request('/manage/instance/restartInstance', {
        method: 'POST',
        body: formData,
    });
}

export function queryInstanceConfig(host) {
    let params = {host: host};
    let formData = new FormData();
    for (let name in params) {
        formData.append(name, params[name]);
    }
    return request('/manage/instance/queryInstanceConfig', {
        method: 'POST',
        body: formData,
    });
}

export function instanceConfOps(params) {
    let formData = new FormData();
    for (let name in params) {
        formData.append(name, params[name]);
    }
    return request('/manage/instance/instanceConfOps', {
        method: 'POST',
        body: formData,
    });
}

export function addNewInstance(params) {
    let formData = new FormData();
    for (let name in params) {
        formData.append(name, params[name]);
    }
    return request('/manage/instance/addNewInstance', {
        method: 'POST',
        body: formData,
    });
}

export function addNewConfigFile(params) {
    let formData = new FormData();
    for (let name in params) {
        formData.append(name, params[name]);
    }
    return request('/manage/instance/addNewConfigFile', {
        method: 'POST',
        body: formData,
    });
}

export function queryUploadedJarFile() {
    return request('/manage/instance/queryUploadedJarFile');
}

export function instanceUpdateServer(params) {
    let formData = new FormData();
    for (let name in params) {
        formData.append(name, params[name]);
    }
    return request('/manage/instance/instanceUpdateServer', {
        method: 'POST',
        body: formData,
    });
}


