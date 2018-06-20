import {request} from "../../common/utils/request";
import qs from "qs";

export function menuTreeList () {
    return request(`/menu/treeList`)
}

export function userInfo (params) {
    return request('/user/info')
}

export function loginSubmit (params) {
    var formData  = new FormData();
    for(var name in params) {
        formData.append(name, params[name]);
    }
    return request('/user/login', {
        method: 'POST',
        body: formData,
    });
}

export function logoutSubmit () {
    return request('/user/logout');
}

export function register(params) {
    var formData  = new FormData();
    for(var name in params) {
        formData.append(name, params[name]);
    }
    return request('/user/register', {
        method: 'POST',
        body: formData,
    });
}

export function checkUserExists(userName) {
    let params = {userName: userName};
    return request(`/user/checkUserExists?${qs.stringify(params)}`, {});
}


