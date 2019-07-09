define(["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    class CollectionUtils {
        static group(list, groupHandle) {
            const result = new Map();
            list.forEach(item => {
                let value = this.getValue(item, groupHandle);
                const getValue = result.get(value);
                if (getValue) {
                    getValue.push(item);
                }
                else {
                    result.set(value, [item]);
                }
            });
            return result;
        }
        static listToMap(list, keyHandle, valueHandle) {
            const result = new Map();
            list.forEach(item => {
                const key = this.getValue(item, keyHandle);
                const value = this.getValue(item, valueHandle);
                result.set(key, value);
            });
            return result;
        }
        static mapToObject(map) {
            const result = {};
            map.forEach((value, key) => {
                result[key] = value;
            });
            return result;
        }
        static getValue(item, handle) {
            if (typeof handle === 'function') {
                return handle(item);
            }
            else {
                return item[handle];
            }
        }
        static splitArray(list, number) {
            if (!list)
                return [];
            const resultList = [];
            let num = 0;
            list.forEach(item => {
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
        }
    }
    exports.default = CollectionUtils;
});
