/**
 * 如果localStorage 可用，用之
 * 如果不可用（node环境或浏览器禁用），用对象模拟
 *
 *
 * 因为仅仅是简单使用localStorage，直接使用对象形式
 */

function isLocalStorageNameSupported() {
    if (window && window.localStorage) {
        let testKey = '__test', storage = window.localStorage;
        try {
            storage.setItem(testKey, '1');
            storage.removeItem(testKey);
            return true;
        } catch (error) {
            return false;
        }
    }
    return false;
}

let pool = {};
if (isLocalStorageNameSupported()) {
    pool = window.localStorage;
}
const Storage = {

    getItem(key) {
        return pool[key]
    },

    setItem(key, value) {
        pool[key] = value
    },

    removeItem(key) {
        delete pool[key]
    }
};

export default Storage