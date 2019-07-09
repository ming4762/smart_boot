define(["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    class ValidateUtils {
        static validateMobile(phone) {
            let list = [];
            let result = false;
            let msg = '';
            const isPhone = /^0\d{2,3}-?\d{7,8}$/;
            const isMob = /^((\+?86)|(\(\+86\)))?(13[0123456789][0-9]{8}|15[012356789][0-9]{8}|18[012356789][0-9]{8}|14[57][0-9]{8}|17[3678][0-9]{8})$/;
            if (!ValidateUtils.validateNull(phone)) {
                if (phone.length === 11) {
                    if (isPhone.test(phone)) {
                        msg = '手机号码格式不正确';
                    }
                    else {
                        result = true;
                    }
                }
                else {
                    msg = '手机号码长度不为11位';
                }
            }
            else {
                msg = '手机号码不能为空';
            }
            list.push(result);
            list.push(msg);
            return list;
        }
        static validateNull(value) {
            if (typeof value === 'boolean') {
                return false;
            }
            if (value instanceof Array) {
                if (value.length === 0)
                    return true;
            }
            else if (value instanceof Object) {
                if (JSON.stringify(value) === '{}')
                    return true;
            }
            else {
                if (value === 'null' || value === null || value === 'undefined' || value === undefined || value === '')
                    return true;
                return false;
            }
            return false;
        }
        static isValidUsername(str) {
            const validMap = ['admin', 'editor'];
            return validMap.indexOf(str.trim()) >= 0;
        }
        static validateURL(textval) {
            const urlregex = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/;
            return urlregex.test(textval);
        }
        static validateLowerCase(str) {
            const reg = /^[a-z]+$/;
            return reg.test(str);
        }
        static validateUpperCase(str) {
            const reg = /^[A-Z]+$/;
            return reg.test(str);
        }
        static validatAlphabets(str) {
            const reg = /^[A-Za-z]+$/;
            return reg.test(str);
        }
    }
    ValidateUtils.vueValidateMobile = (rule, value, callback) => {
        const result = ValidateUtils.validateMobile(value);
        if (result[0] === false) {
            return callback(new Error(result[1]));
        }
        else {
            callback();
        }
    };
    exports.default = ValidateUtils;
});
