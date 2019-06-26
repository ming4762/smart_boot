define(["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var StoreUtil = (function () {
        function StoreUtil() {
        }
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
        StoreUtil.clearSession = function () {
            window.sessionStorage.clear();
        };
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
