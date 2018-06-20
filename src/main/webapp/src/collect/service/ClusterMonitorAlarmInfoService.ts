import {request} from "../../common/utils/request";
// import qs from "qs";
export  async function getAllZkAlarmInfo() {
    return request('/alarmInfo/getAllZkAlarmInfo',{})
}
export function getCollectInfo() {
    return request('/alarmInfo/getCollectInfo',{})
}