import {generateUrl, request} from "../../common/utils/request";
import qs from "qs";

export function editMachineInfo(params) {
    let formData = new FormData();
    for (let name in params) {
        formData.append(name, params[name]);
    }
    return request('/manage/machine/modifyMachine', {
        method: 'POST',
        body: formData
    });
}
export function deleteMachine(machineId) {
    let params = {machineId: machineId};
    return request(`/manage/machine/deleteMachine?${qs.stringify(params)}`, {});
}

export function addMachine(params) {
    let formData = new FormData();
    for (let name in params) {
        formData.append(name, params[name]);
    }
    return request('/manage/machine/addMachine', {
        method: 'POST',
        body: formData
    });
}

export function updateMonitorStatus(params) {
    return request(`/manage/machine/updateMonitorStatus?${qs.stringify(params)}`, {});
}

export function downloadMachineInitScript(args) {
    let {url, params, filename} = args;
    filename = filename ? filename : 'log.log';
    let fullUrl = generateUrl(url);
    let urlParms = qs.stringify(params);
    if (urlParms) {
        fullUrl = fullUrl.indexOf("?") > 0 ? fullUrl + "&" + urlParms : fullUrl + "?" + urlParms;
    }
    let link = document.createElement('a');
    link.setAttribute('href', fullUrl);
    link.setAttribute('download', filename);
    link.click();
}

export function queryMachineInstances(params) {
    return request(`/machine/queryMachineInstances?${qs.stringify(params)}`, {});
}

export function sshCommandExecute(params) {
    return request(`/manage/machine/supermanage/sshCommandExecute?${qs.stringify(params)}`,{});
}




