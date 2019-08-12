import Md5Utils from '../../utils/Md5Utils.js';
import { STORE_KEYS } from '../../Constants.js';
import StoreUtil from '../../utils/StoreUtil.js';
export const createPassword = (username, password) => {
    const salt = '1qazxsw2';
    const passwordValue = username + password + salt;
    return Md5Utils.md5(passwordValue, 2);
};
export default class AuthUtils {
    static getCurrentUser() {
        return StoreUtil.getStore(STORE_KEYS.USER_KEY);
    }
    static getCurrentUserId() {
        const user = this.getCurrentUser();
        return user === null ? null : user.userId;
    }
}
