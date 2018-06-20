import {request} from "../../common/utils/request";

export function listAll() {
    return request('/users/listAll');
}

export function create(params) {
    let formData = new FormData();
    for (let name in params) {
        formData.append(name, params[name]);
    }
    return request('/users/create', {
        method: 'POST',
        body: formData
    });
}

export function update(params) {
    let formData = new FormData();
    for (let name in params) {
        formData.append(name, params[name]);
    }
    return request('/users/update', {
        method: 'POST',
        body: formData
    });
}

export function remove(params) {
    let formData = new FormData();
    for (let name in params) {
        formData.append(name, params[name]);
    }
    return request('/users/delete', {
        method: 'POST',
        body: formData,
    });
}
