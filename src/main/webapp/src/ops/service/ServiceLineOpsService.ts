import {request} from "../../common/utils/request";

export function insertServiceLine(params) {
    let formData = new FormData();
    for (let name in params) {
        formData.append(name, params[name]);
    }
    return request('/manage/system/serviceLine/insert', {
        method: 'POST',
        body: formData
    });
}

export function updateServiceLine(params) {
    let formData = new FormData();
    for (let name in params) {
        formData.append(name, params[name]);
    }
    return request('/manage/system/serviceLine/update', {
        method: 'POST',
        body: formData
    });
}

export function deleteServiceLine(params) {
    let formData = new FormData();
    for (let name in params) {
        formData.append(name, params[name]);
    }
    return request('/manage/system/serviceLine/delete', {
        method: 'POST',
        body: formData
    });
}
