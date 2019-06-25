define(["require", "exports", "utils/Md5"], function (require, exports, Md5_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    /**
     * MD5工具类
     */
    var Md5Utils = /** @class */ (function () {
        function Md5Utils() {
        }
        /**
         * 进行MD5加密
         * @param value 需要加密的值
         * @param hashIterations 加密次数
         */
        Md5Utils.md5 = function (value, hashIterations) {
            if (hashIterations === void 0) { hashIterations = 1; }
            var hash = Md5_1.default.hashStr(value).toString();
            if (hashIterations > 1) {
                for (var i = 1; i < hashIterations; i++) {
                    hash = Md5_1.default.hashStr(hash).toString();
                }
            }
            return hash;
        };
        return Md5Utils;
    }());
    exports.default = Md5Utils;
});
