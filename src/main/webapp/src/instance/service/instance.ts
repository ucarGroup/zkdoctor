import {request} from "../../common/utils/request";
import qs from "qs";

export function queryByClusterId(clusterId) {
    let params = {clusterId: clusterId};
    return request(`/instance/queryByClusterId?${qs.stringify(params)}`, {});
}
export function queryByInstanceStatus(status) {
    let params = {status: status};
    return request(`/instance/queryByInstanceStatus?${qs.stringify(params)}`, {});
}
export function getInstanceDetailInfo(instanceId) {
    let params = {instanceId: instanceId};
    return request(`/instance/instanceDetailInfo?${qs.stringify(params)}`, {});
}
export function getInstanceConfig(instanceId) {
    let params = {instanceId: instanceId};
    return request(`/instance/instanceConfig?${qs.stringify(params)}`, {});
}
export function getInstanceConnections(instanceId) {
    let params = {instanceId: instanceId};
    return request(`/instance/instanceConnections?${qs.stringify(params)}`, {});
}

export function getMachineStateInitTrend(params) {
    return request(`/instance/machine/init/trend?${qs.stringify(params)}`, {});
}

export function queryNetTraffic(params) {
    return request(`/instance/machine/netTraffic/trend?${qs.stringify(params)}`, {});
}

export function queryAvgLoad(params) {
    return request(`/instance/machine/avgLoad/trend?${qs.stringify(params)}`, {});
}

export function queryCpu(params) {
    return request(`/instance/machine/cpu/trend?${qs.stringify(params)}`, {});
}

export function queryCpuSingle(params) {
    return request(`/instance/machine/cpuSingle/trend?${qs.stringify(params)}`, {});
}

export function queryMemory(params) {
    return request(`/instance/machine/memory/trend?${qs.stringify(params)}`, {});
}

export function queryDisk(params) {
    return request(`/instance/machine/disk/trend?${qs.stringify(params)}`, {});
}

export function updateInstanceConnCollectMonitor(params) {
    return request(`/manage/instance/updateInstanceConnCollectMonitor?${qs.stringify(params)}`, {});
}

export function searchConnectionHistory(params) {
    return request(`/instance/searchConnectionHistory?${qs.stringify(params)}`, {});
}

export function getConnectionCollectTime(instanceId) {
    let params = {instanceId: instanceId};
    return request(`/instance/getConnectionCollectTime?${qs.stringify(params)}`, {});
}

export function queryNetTrafficDetail(params) {
    return request(`/instance/queryNetTrafficDetail?${qs.stringify(params)}`, {});
}


