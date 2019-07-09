define(["require", "exports", "utils/StoreUtil"], function (require, exports, StoreUtil_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    const service = axios.create({
        baseURL: localStorage.getItem('API_URL'),
        timeout: 10000
    });
    const STORE_TOKEN_KEY = 'SMART_AUTHORIATION';
    const getToken = () => {
        return StoreUtil_1.default.getStore(STORE_TOKEN_KEY);
    };
    const TOKEN_KEY = 'Authorization';
    class ApiService {
        static postAjax(url, parameter) {
            const headers = {};
            const token = getToken();
            if (token) {
                headers[TOKEN_KEY] = token;
            }
            parameter = parameter || {};
            return service.post(url, parameter, {
                headers: headers
            }).then((result) => {
                if (result && result.status === 200 && result.data['code'] === 200) {
                    return result.data.data;
                }
                else {
                    return Promise.reject(result ? result.data : result);
                }
            }).catch((error) => {
                return Promise.reject(error);
            });
        }
        static saveToken(token) {
            StoreUtil_1.default.setStore(STORE_TOKEN_KEY, token, StoreUtil_1.default.SESSION_TYPE);
        }
    }
    exports.default = ApiService;
});
