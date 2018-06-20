import {request} from "../../common/utils/request";
import qs from "qs";

export function getInitTrend(params) {
    return request(`/cluster/stat/init/trend?${qs.stringify(params)}`,{});
}

export function getReceivedTrend(params) {
    return request(`/cluster/stat/received/trend?${qs.stringify(params)}`,{});
}

export function getOutstandingsTrend(params) {
    return request(`/cluster/stat/outstandings/trend?${qs.stringify(params)}`,{});
}

export function getMaxLatencyMax(params) {
    return request(`/cluster/stat/maxLatencyMax/trend?${qs.stringify(params)}`,{});
}

export function getAvgLatencyMax(params) {
    return request(`/cluster/stat/avgLatencyMax/trend?${qs.stringify(params)}`,{});
}

export function getZnodeCount(params) {
    return request(`/cluster/stat/znodeCount/trend?${qs.stringify(params)}`,{});
}
export function getEphemerals(params) {
    return request(`/cluster/stat/ephemerals/trend?${qs.stringify(params)}`,{});
}
export function getWatcherTotal(params) {
    return request(`/cluster/stat/watcherTotal/trend?${qs.stringify(params)}`,{});
}
export function getConnectionTotal(params) {
    return request(`/cluster/stat/connectionTotal/trend?${qs.stringify(params)}`,{});
}

export function getReceivedAllInsTrend(params) {
    return request(`/instance/stat/receivedAllIns/trend?${qs.stringify(params)}`,{});
}
export function getSendAllInsTrend(params) {
    return request(`/instance/stat/sendAllIns/trend?${qs.stringify(params)}`,{});
}
export function getOutstandingsAllInsTrend(params) {
    return request(`/instance/stat/outstandingsAllIns/trend?${qs.stringify(params)}`,{});
}
export function getMaxLatencyAllInsTrend(params) {
    return request(`/instance/stat/maxLatencyAllIns/trend?${qs.stringify(params)}`,{});
}
export function getAvgLatencyAllInsTrend(params) {
    return request(`/instance/stat/avgLatencyAllIns/trend?${qs.stringify(params)}`,{});
}
export function getMinLatencyAllInsTrend(params) {
    return request(`/instance/stat/minLatencyAllIns/trend?${qs.stringify(params)}`,{});
}
export function getZnodeCountAllInsTrend(params) {
    return request(`/instance/stat/znodeCountAllIns/trend?${qs.stringify(params)}`,{});
}
export function getEphemeralsAllInsTrend(params) {
    return request(`/instance/stat/ephemeralsdAllIns/trend?${qs.stringify(params)}`,{});
}
export function getWatcherCountAllInsTrend(params) {
    return request(`/instance/stat/watcherCountAllIns/trend?${qs.stringify(params)}`,{});
}
export function getConnectionsAllInsTrend(params) {
    return request(`/instance/stat/connectionsAllIns/trend?${qs.stringify(params)}`,{});
}
export function getApproximateDataSizeAllInsTrend(params) {
    return request(`/instance/stat/approximateDataSizeAllIns/trend?${qs.stringify(params)}`,{});
}
export function getOpenFileDescriptorCountAllInsTrend(params) {
    return request(`/instance/stat/openFileDescriptorCountAllIns/trend?${qs.stringify(params)}`,{});
}
export function getMaxFileDescriptorCountAllInsTrend(params) {
    return request(`/instance/stat/maxFileDescriptorCountAllIns/trend?${qs.stringify(params)}`,{});
}
export function getFollowersAllInsTrend(params) {
    return request(`/instance/stat/followersAllIns/trend?${qs.stringify(params)}`,{});
}
export function getSyncedFollowersAllInsTrend(params) {
    return request(`/instance/stat/syncedFollowersAllIns/trend?${qs.stringify(params)}`,{});
}
export function getPendingSyncsAllInsTrend(params) {
    return request(`/instance/stat/pendingSyncsAllIns/trend?${qs.stringify(params)}`,{});
}
export function getServerStateLagAllInsTrend(params) {
    return request(`/instance/stat/serverStateLagAllIns/trend?${qs.stringify(params)}`,{});
}



