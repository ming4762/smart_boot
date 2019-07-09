define(["require", "exports", "utils/Md5"], function (require, exports, Md5_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    class Md5Utils {
        static md5(value, hashIterations = 1) {
            let hash = Md5_1.default.hashStr(value).toString();
            if (hashIterations > 1) {
                for (let i = 1; i < hashIterations; i++) {
                    hash = Md5_1.default.hashStr(hash).toString();
                }
            }
            return hash;
        }
    }
    exports.default = Md5Utils;
});
