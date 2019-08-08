define(["require", "exports", "utils/Md5Utils", "Constants", "utils/StoreUtil"], function (require, exports, Md5Utils_1, Constants_1, StoreUtil_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.createPassword = (username, password) => {
        const salt = '1qazxsw2';
        const passwordValue = username + password + salt;
        return Md5Utils_1.default.md5(passwordValue, 2);
    };
    class AuthUtils {
        static getCurrentUser() {
            return StoreUtil_1.default.getStore(Constants_1.STORE_KEYS.USER_KEY);
        }
        static getCurrentUserId() {
            const user = this.getCurrentUser();
            return user === null ? null : user.userId;
        }
    }
    exports.default = AuthUtils;
});
