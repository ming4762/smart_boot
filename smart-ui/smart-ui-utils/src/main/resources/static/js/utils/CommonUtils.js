define(["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var CommonUtil = (function () {
        function CommonUtil() {
        }
        CommonUtil.createUUID = function () {
            var s = [];
            var hexDigits = '0123456789abcdef';
            for (var i = 0; i < 36; i++) {
                s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
            }
            s[14] = '4';
            s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);
            s[8] = s[13] = s[18] = s[23] = '-';
            var uuid = s.join('');
            return uuid;
        };
        CommonUtil.clone = function (object) {
            if (object == null) {
                return null;
            }
            var objectStr = JSON.stringify(object);
            return JSON.parse(objectStr);
        };
        CommonUtil.getObjectByKeys = function (keys, objectList) {
            if (objectList && keys) {
                var resultList_1 = [];
                objectList.forEach(function (object) {
                    var result = {};
                    keys.forEach(function (key) {
                        result[key] = object[key];
                    });
                    resultList_1.push(result);
                });
                return resultList_1;
            }
            return [];
        };
        return CommonUtil;
    }());
    exports.default = CommonUtil;
});
