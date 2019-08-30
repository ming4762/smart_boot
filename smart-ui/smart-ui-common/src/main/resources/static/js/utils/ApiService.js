import StoreUtil from './StoreUtil.js';
import RsaUtils from './RsaUtils.js';
import Md5Utils from './Md5Utils.js';
axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8';
const service = axios.create({
    baseURL: localStorage.getItem('API_URL'),
    timeout: 10000
});
const STORE_TOKEN_KEY = 'SMART_AUTHORIATION';
const RSA_CLIENT_PRIVATE_KEY = "RSA_CLIENT_PRIVATE_KEY";
const RSA_SERVER_PUBLIC_KEYU = "RSA_SERVER_PUBLIC_KEYU";
const getToken = () => {
    return StoreUtil.getStore(STORE_TOKEN_KEY);
};
const TOKEN_KEY = 'Authorization';
export default class ApiService {
    static validateLogin() {
        const token = getToken();
        if (!token || token === '') {
            this.toLoginPage();
        }
        else {
            ApiService.postAjax('public/auth/validateToken', {})
                .then(result => {
                if (result === false) {
                    ApiService.toLoginPage();
                }
            }).catch(error => {
                if (error.response && error.response.status === 401) {
                    ApiService.toLoginPage();
                }
            });
        }
    }
    static postAjax(url, parameter, headers, ideConfig) {
        const rsaUtils = new RsaUtils();
        headers = headers || {};
        const token = getToken();
        if (token) {
            headers[TOKEN_KEY] = token;
        }
        parameter = parameter || {};
        if (this.isEncrypt(ideConfig)) {
            parameter = rsaUtils.rsaEncrypt(this.getServerPublicKey(), JSON.stringify(this.createEncryptParameter(parameter, ideConfig)));
        }
        return service.post(url, parameter, {
            headers: headers
        }).then((result) => {
            let resultSuccess = true;
            let data;
            if (result && result.status === 200) {
                if (this.isDecrypt(ideConfig)) {
                    data = JSON.parse(rsaUtils.rsaDecrypt(this.getClientPrivateKey(), result.data));
                }
                else {
                    data = result.data;
                }
                if (data['code'] !== 200) {
                    resultSuccess = false;
                }
                else {
                    return data.data;
                }
            }
            else {
                resultSuccess = false;
            }
            if (!resultSuccess) {
                return Promise.reject(result ? data : result);
            }
        }).catch((error) => {
            if (error.code === 403) {
                ApiService.toNoPremissionPage();
            }
            if (error.response && error.response.status === 401) {
                ApiService.toLoginPage();
            }
            return Promise.reject(error);
        });
    }
    static logout() {
        return this.postAjax('logout', {});
    }
    static toLoginPage() {
        const loginUrl = `${contextPath}ui/system/login`;
        window.sessionStorage.clear();
        if (window.frames.length != parent.frames.length) {
            parent.location.href = loginUrl;
        }
        else {
            window.location.href = loginUrl;
        }
    }
    static saveToken(token) {
        StoreUtil.setStore(STORE_TOKEN_KEY, token, StoreUtil.SESSION_TYPE);
    }
    static useIde() {
        return localStorage.getItem('useIde') === 'true';
    }
    static getClientPrivateKey() {
        return StoreUtil.getStore(RSA_CLIENT_PRIVATE_KEY);
    }
    static getServerPublicKey() {
        return StoreUtil.getStore(RSA_SERVER_PUBLIC_KEYU);
    }
    static registerKey() {
        const rasKey = RsaUtils.createKey();
        return this.postAjax('auth/registerKey', rasKey.pubKey).then((serverPublicKey) => {
            StoreUtil.setStore(RSA_CLIENT_PRIVATE_KEY, rasKey.priKey, StoreUtil.SESSION_TYPE);
            StoreUtil.setStore(RSA_SERVER_PUBLIC_KEYU, serverPublicKey, StoreUtil.SESSION_TYPE);
        });
    }
    static isEncrypt(ideConfig) {
        return ideConfig && ideConfig.encrypt === true && this.useIde();
    }
    static isDecrypt(ideConfig) {
        return ideConfig && ideConfig.decrypt === true && this.useIde();
    }
    static createEncryptParameter(parameter, ideConfig) {
        const encryptParameter = {
            data: parameter,
            md5: '',
            timestamp: 0
        };
        if (ideConfig && ideConfig.md5 === true) {
            encryptParameter.md5 = Md5Utils.md5(JSON.stringify(parameter));
        }
        if (ideConfig && ideConfig.timestamp) {
            encryptParameter.timestamp = new Date().getTime();
        }
        return encryptParameter;
    }
    static toNoPremissionPage() {
    }
}
