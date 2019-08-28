import StoreUtil from './StoreUtil.js';
import Md5Utils from './Md5Utils.js';
import { STORE_KEYS } from '../Constants.js';
const STORE_TOKEN_KEY = 'SMART_AUTHORIATION';
export default class AuthUtils {
    static getToken() {
        return StoreUtil.getStore(STORE_TOKEN_KEY);
    }
    static getCurrentUser() {
        return StoreUtil.getStore(STORE_KEYS.USER_KEY);
    }
    static getCurrentUserId() {
        const user = this.getCurrentUser();
        return user === null ? null : user.userId;
    }
    static createPassword(username, password) {
        const salt = '1qazxsw2';
        const passwordValue = username + password + salt;
        return Md5Utils.md5(passwordValue, 2);
    }
}
