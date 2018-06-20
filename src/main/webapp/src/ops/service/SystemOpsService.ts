import {request} from "../../common/utils/request";

export function updateConfig(params) {
    let formData = new FormData();
    for (let name in params) {
        formData.append(name, params[name]);
    }
    return request('/manage/system/config/update', {
        method: 'POST',
        body: formData
    });
}

export function deleteConfig(params) {
    let formData = new FormData();
    for (let name in params) {
        formData.append(name, params[name]);
    }
    return request('/manage/system/config/delete', {
        method: 'POST',
        body: formData
    });
}
