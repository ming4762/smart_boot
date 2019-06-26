define(["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var ValidateUtils = (function () {
        function ValidateUtils() {
        }
        ValidateUtils.validateMobile = function (phone) {
            var list = [];
            var result = false;
            var msg = '';
            var isPhone = /^0\d{2,3}-?\d{7,8}$/;
            var isMob = /^((\+?86)|(\(\+86\)))?(13[0123456789][0-9]{8}|15[012356789][0-9]{8}|18[012356789][0-9]{8}|14[57][0-9]{8}|17[3678][0-9]{8})$/;
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
        };
        ValidateUtils.validateNull = function (value) {
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
        };
        ValidateUtils.isValidUsername = function (str) {
            var validMap = ['admin', 'editor'];
            return validMap.indexOf(str.trim()) >= 0;
        };
        ValidateUtils.validateURL = function (textval) {
            var urlregex = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/;
            return urlregex.test(textval);
        };
        ValidateUtils.validateLowerCase = function (str) {
            var reg = /^[a-z]+$/;
            return reg.test(str);
        };
        ValidateUtils.validateUpperCase = function (str) {
            var reg = /^[A-Z]+$/;
            return reg.test(str);
        };
        ValidateUtils.validatAlphabets = function (str) {
            var reg = /^[A-Za-z]+$/;
            return reg.test(str);
        };
        ValidateUtils.vueValidateMobile = function (rule, value, callback) {
            var result = ValidateUtils.validateMobile(value);
            if (result[0] === false) {
                return callback(new Error(result[1]));
            }
            else {
                callback();
            }
        };
        return ValidateUtils;
    }());
    exports.default = ValidateUtils;
});
