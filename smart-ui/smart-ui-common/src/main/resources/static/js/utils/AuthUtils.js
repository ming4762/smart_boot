import StoreUtil from './StoreUtil.js';
const STORE_TOKEN_KEY = 'SMART_AUTHORIATION';
export default class AuthUtils {
    static getToken() {
        return StoreUtil.getStore(STORE_TOKEN_KEY);
    }
}
