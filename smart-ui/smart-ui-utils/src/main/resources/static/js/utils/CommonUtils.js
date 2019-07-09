define(["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    class CommonUtil {
        static createUUID() {
            let s = [];
            let hexDigits = '0123456789abcdef';
            for (let i = 0; i < 36; i++) {
                s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
            }
            s[14] = '4';
            s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);
            s[8] = s[13] = s[18] = s[23] = '-';
            let uuid = s.join('');
            return uuid;
        }
        static clone(object) {
            if (object == null) {
                return null;
            }
            let objectStr = JSON.stringify(object);
            return JSON.parse(objectStr);
        }
        static getObjectByKeys(keys, objectList) {
            if (objectList && keys) {
                let resultList = [];
                objectList.forEach(object => {
                    let result = {};
                    keys.forEach(key => {
                        result[key] = object[key];
                    });
                    resultList.push(result);
                });
                return resultList;
            }
            return [];
        }
    }
    exports.default = CommonUtil;
});
