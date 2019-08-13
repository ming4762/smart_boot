import { STORE_KEYS } from '../Constants.js';
import ApiService from './ApiService.js';
import StoreUtil from './StoreUtil.js';
export default class ConfigUtils {
    static setConfig(config) {
        StoreUtil.setStore(STORE_KEYS.LOCAL_CONFIG_KEY, config);
    }
    static getConfig(key) {
        const configs = this.getConfigs();
        if (configs) {
            return configs[key] || null;
        }
        return null;
    }
    static getConfigs() {
        return StoreUtil.getStore(STORE_KEYS.LOCAL_CONFIG_KEY);
    }
    static loadConfig() {
        return ApiService.postAjax('public/sys/readConfig', {})
            .then(result => {
            ConfigUtils.setConfig(result);
        });
    }
}
