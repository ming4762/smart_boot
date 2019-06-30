define(["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var CollectionUtils = (function () {
        function CollectionUtils() {
        }
        CollectionUtils.group = function (list, groupHandle) {
            var _this = this;
            var result = new Map();
            list.forEach(function (item) {
                var value = _this.getValue(item, groupHandle);
                var getValue = result.get(value);
                if (getValue) {
                    getValue.push(item);
                }
                else {
                    result.set(value, [item]);
                }
            });
            return result;
        };
        CollectionUtils.listToMap = function (list, keyHandle, valueHandle) {
            var _this = this;
            var result = new Map();
            list.forEach(function (item) {
                var key = _this.getValue(item, keyHandle);
                var value = _this.getValue(item, valueHandle);
                result.set(key, value);
            });
            return result;
        };
        CollectionUtils.mapToObject = function (map) {
            var result = {};
            map.forEach(function (value, key) {
                result[key] = value;
            });
            return result;
        };
        CollectionUtils.getValue = function (item, handle) {
            if (typeof handle === 'function') {
                return handle(item);
            }
            else {
                return item[handle];
            }
        };
        CollectionUtils.splitArray = function (list, number) {
            if (!list)
                return [];
            var resultList = [];
            var num = 0;
            list.forEach(function (item) {
                if (num === 0) {
                    resultList.push([]);
                }
                resultList[resultList.length - 1].push(item);
                num++;
                if (num === number) {
                    num = 0;
                }
            });
            return resultList;
        };
        return CollectionUtils;
    }());
    exports.default = CollectionUtils;
});
