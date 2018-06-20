import {request} from "../../common/utils/request";
import qs from "qs";

export function pauseTrigger(triggerName, triggerGroup) {
    let params = {triggerName: triggerName, triggerGroup: triggerGroup};
    return request(`/manage/quartz/pauseTrigger?${qs.stringify(params)}`, {});
}

export function resumeTrigger(triggerName, triggerGroup) {
    let params = {triggerName: triggerName, triggerGroup: triggerGroup};
    return request(`/manage/quartz/resumeTrigger?${qs.stringify(params)}`, {});
}

export function removeTrigger(triggerName, triggerGroup) {
    let params = {triggerName: triggerName, triggerGroup: triggerGroup};
    return request(`/manage/quartz/removeTrigger?${qs.stringify(params)}`, {});
}

