import safeLocalStorage from "./safeLocalStorage";
import _ from "lodash";

function keyValueMirror(arr) {
    const obj = {};
    arr.forEach(item => {
        obj[item] = item;
    });
    return obj;
}

const PreferenceDefaultConfigs = {
    'navFolded': false, //左侧导航栏，默认不收起
};

const PreferenceKeys = keyValueMirror(_.keys(PreferenceDefaultConfigs));

const Preference = {
    set(key, value) {
        safeLocalStorage.setItem(key, value);
    },
    get(key) {
        return safeLocalStorage.getItem(key) || PreferenceDefaultConfigs[key];
    }
};

export default Preference;

export {PreferenceKeys}

