import {request} from "../../common/utils/request";
import qs from "qs";

export async function updateIndicatorSwitchOn(params) {
    return request(`/manage/monitor/indicator/updateSwitchOn?${qs.stringify(params)}`,{});
}

export async function updateTaskSwitchOn(params) {
    return request(`/monitor/task/updateSwitchOn?${qs.stringify(params)}`,{});
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
