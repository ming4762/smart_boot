define(["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    /**
     * common工具类
     * @author zhongming
     */
    var CommonUtil = /** @class */ (function () {
        function CommonUtil() {
        }
        /**
         * 生成UUID
         */
        CommonUtil.createUUID = function () {
            var s = [];
            var hexDigits = '0123456789abcdef';
            for (var i = 0; i < 36; i++) {
                s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
            }
            s[14] = '4'; // bits 12-15 of the time_hi_and_version field to 0010
            s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
            s[8] = s[13] = s[18] = s[23] = '-';
            var uuid = s.join('');
            return uuid;
        };
        /**
         * 深拷贝对象
         * @param object 被拷贝对象
         */
        CommonUtil.clone = function (object) {
            if (object == null) {
                return null;
            }
            var objectStr = JSON.stringify(object);
            return JSON.parse(objectStr);
        };
        /**
         * 从对象列表中获取指定key并构成一个新的对象数组
         * @param keys key数组
         * @param objectList 原始对象数组
         */
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
