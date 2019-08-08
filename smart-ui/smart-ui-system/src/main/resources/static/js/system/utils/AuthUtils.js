define(["require", "exports", "utils/Md5Utils"], function (require, exports, Md5Utils_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.createPassword = (username, password) => {
        const salt = '1qazxsw2';
        const passwordValue = username + password + salt;
        return Md5Utils_1.default.md5(passwordValue, 2);
    };
});
