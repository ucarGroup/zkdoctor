import fetch from "isomorphic-fetch";
import config from "../config";

function checkStatus(response) {
    if (response.status >= 200 && response.status < 300) {
        return response;
    }
    const error = new Error(response.statusText);
    throw error;
}

var app;

export function appContainer(appParam) {
    app = appParam;
}

function sendMessage(message) {
    if (app) {
        app.sendMessage(message);
    }
}

export function generateUrl(url) {
    return url.startsWith("http") ? url : '/' + config.prefix + url;
}

export async function request(url, options?: any) {
    let newUrl = generateUrl(url);
    const response = await fetch(newUrl, Object.assign({credentials: 'same-origin'}, options));
    let result = {success: false};
    try {
        checkStatus(response);
        const data = await response.json();
        if (data) {
            if (data.success == false) {
                if (data.message == 'NO_USER') {
                    sendMessage({type: 'error', title: 'NO_USER', msg: 'NO_USER'});
                } else {
                    sendMessage({type: 'error', title: '操作失败', msg: `${data.message}`})
                }
            } else if (data.message == 'Timeout') { // 用户登录超时
                sendMessage({type: 'error', title: '登录超时', msg: '登录超时，请重新登录'});
            } else if (data.message == 'Permission denied') { // 用户操作权限不足
                sendMessage({type: 'error', title: '操作失败', msg: `权限不足，不能进行操作`})
            } else {
                result = data;
            }
        } else {
            const error = new Error("json解析错误");
            throw error;
        }

    } catch (error) {
        sendMessage({type: 'error', title: '操作失败', msg: `后台操作出现错误，错误信息:${error ? error : ''}`});
    }
    return result;
}
