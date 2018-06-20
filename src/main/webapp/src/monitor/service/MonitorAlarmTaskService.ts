import {request} from "../../common/utils/request";
import qs from "qs";

export async function updateIndicatorSwitchOn(params) {
    return request(`/manage/monitor/indicator/updateSwitchOn?${qs.stringify(params)}`,{});
}

export async function updateMonitorTaskSwitchOn(params) {

    //return request(`/manage/monitor/updateSwitchOn?${qs.stringify(params)}`,{});
    return request(`/monitor/task/updateSwitchOn?${qs.stringify(params)}`,{});
}
export function updateMonitorTask(params) {
    let formData = new FormData();
    for (let name in params) {
        formData.append(name, params[name]);
    }
    // return request(`/monitorAlarm/updateMonitorTask`, {
    return request(`/manage/monitor/updateMonitorTask`, {
        method: 'POST',
        body: formData

    });
}
export function getMonitorTaskByClusterId(clusterId) {
    let params = {clusterId: clusterId};
    return request(`/monitor/getZkMonitorTaskDataByClusterId?${qs.stringify(params)}`, {});
}

export function updateIndicator(params) {
    let formData = new FormData();
    for (let name in params) {
        formData.append(name, params[name]);
    }
    return request('/manage/monitor/indicator/updateIndicator', {
        method: 'POST',
        body: formData
    });
}

export function updateTask(params) {
    let formData = new FormData();
    for (let name in params) {
        formData.append(name, params[name]);
    }
    return request('/monitor/task/updateTask', {
        method: 'POST',
        body: formData
    });
}
