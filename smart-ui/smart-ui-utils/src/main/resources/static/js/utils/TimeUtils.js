define(["require", "exports", "utils/ValidateUtils"], function (require, exports, ValidateUtils_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    class TimeUtils {
        static formatTime(time) {
            if (ValidateUtils_1.default.validateNull(time)) {
                return '';
            }
            const date = new Date(time);
            const year = date.getFullYear();
            const month = date.getMonth() + 1;
            const day = date.getDate();
            const hour = date.getHours();
            const minute = date.getMinutes();
            const second = date.getSeconds();
            return year + '-' + (month < 10 ? ('0' + month) : month) + '-' + (day < 10 ? ('0' + day) : day) + ' ' + (hour < 10 ? ('0' + hour) : hour) + ':' + (minute < 10 ? ('0' + minute) : minute) + ':' + (second < 10 ? ('0' + second) : second);
        }
        static convertMsec(msec) {
            let second = 0;
            let minute = 0;
            let hour = 0;
            let day = 0;
            if (msec > 1000) {
                second = parseInt(msec / 1000 + '', 0);
                if (second > 60) {
                    minute = parseInt(second / 60 + '', 0);
                    second = parseInt(second % 60 + '', 0);
                }
                if (minute > 60) {
                    hour = parseInt(minute / 60 + '', 0);
                    minute = parseInt(minute % 60 + '', 0);
                }
                if (hour > 24) {
                    day = parseInt(hour / 24 + '', 0);
                    hour = parseInt(hour % 24 + '', 0);
                }
            }
            let result = second + '秒';
            if (minute > 0) {
                result = minute + '分钟' + result;
            }
            if (hour > 0) {
                result = hour + '小时' + result;
            }
            if (day > 0) {
                result = day + '天' + result;
            }
            return result;
        }
        static contrastTime(contrastTime, beContrastTime) {
            let result = '';
            if (!beContrastTime) {
                beContrastTime = new Date().getTime();
            }
            let isBefore = contrastTime < beContrastTime;
            let str = isBefore ? '前' : '后';
            let difference = Math.abs(beContrastTime - contrastTime);
            if (difference < 60 * 1000) {
                if (isBefore) {
                    result = '刚刚';
                }
                else {
                    result = '马上';
                }
            }
            else if (difference < 60 * 60 * 1000) {
                result = parseInt((difference % (1000 * 60 * 60)) / (1000 * 60) + '', 10) + '分钟' + str;
            }
            else if (difference < 24 * 60 * 60 * 1000) {
                result = parseInt((difference % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60) + '', 10) + '小时' + str;
            }
            else if (difference < 30 * 24 * 60 * 60 * 1000) {
                result = parseInt(difference / (1000 * 60 * 60 * 24) + '', 10) + '天' + str;
            }
            else if (difference < 365 * 24 * 60 * 60 * 1000) {
                result = parseInt(difference / (1000 * 60 * 60 * 24 * 30) + '', 10) + '月' + str;
            }
            else {
                result = parseInt(difference / (1000 * 60 * 60 * 24 * 365) + '', 10) + '年' + str;
            }
            return result;
        }
        static formatDate(time) {
            if (ValidateUtils_1.default.validateNull(time)) {
                return '';
            }
            let date = new Date(time);
            let year = date.getFullYear();
            let month = date.getMonth() + 1;
            let day = date.getDate();
            return year + '-' + (month < 10 ? ('0' + month) : month) + '-' + (day < 10 ? ('0' + day) : day);
        }
    }
    exports.default = TimeUtils;
});
