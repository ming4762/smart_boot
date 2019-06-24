define(["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    /**
     * 存储工具类
     */
    var StoreUtil = /** @class */ (function () {
        function StoreUtil() {
        }
        /**
         * 数据存储到strong
         * @param key 数据的key
         * @param data 数据
         * @param type 存储类型
         */
        StoreUtil.setStore = function (key, data, type) {
            var dealData = {
                dataType: typeof data,
                content: data,
                type: type,
                datetime: new Date().getTime()
            };
            if (this.SESSION_TYPE === type) {
                window.sessionStorage.setItem(key, JSON.stringify(dealData));
            }
            else {
                window.localStorage.setItem(key, JSON.stringify(dealData));
            }
        };
        /**
         * 清空session
         */
        StoreUtil.clearSession = function () {
            window.sessionStorage.clear();
        };
        /**
         * 从strong获取数据
         * @param key 数据的key
         * @param debug 是否使用debug模式
         */
        StoreUtil.getStore = function (key, debug) {
            var data = window.localStorage.getItem(key);
            if (data === undefined || data === null) {
                data = window.sessionStorage.getItem(key);
            }
            if (data === undefined || data === null) {
                return null;
            }
            var dataObject = JSON.parse(data);
            if (debug) {
                console.log(dataObject);
            }
            return dataObject['content'];
        };
        StoreUtil.SESSION_TYPE = 'session';
        return StoreUtil;
    }());
    exports.default = StoreUtil;
});
