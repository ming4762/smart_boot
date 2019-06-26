define(["require", "exports", "utils/StoreUtil"], function (require, exports, StoreUtil_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var service = axios.create({
        // baseURL: StoreUtil.getStore('API_URL'),
        baseURL: localStorage.getItem('API_URL'),
        timeout: 10000
    });
    var STORE_TOKEN_KEY = 'SMART_AUTHORIATION';
    /**
     * 获取token
     */
    var getToken = function () {
        return StoreUtil_1.default.getStore(STORE_TOKEN_KEY);
    };
    /**
     * token请求头key
     */
    var TOKEN_KEY = 'Authorization';
    var ApiService = /** @class */ (function () {
        function ApiService() {
        }
        ApiService.postAjax = function (url, parameter) {
            var headers = {};
            var token = getToken();
            if (token) {
                headers[TOKEN_KEY] = token;
            }
            parameter = parameter || {};
            return service.post(url, parameter, {
                headers: headers
            }).then(function (result) {
                if (result && result.status === 200 && result.data['code'] === 200) {
                    return result.data.data;
                }
                else {
                    // @ts-ignore
                    return Promise.reject(result ? result.data : result);
                }
            }).catch(function (error) {
                // @ts-ignore
                return Promise.reject(error);
            });
        };
        /**
         * 保存token操作
         * @param token
         */
        ApiService.saveToken = function (token) {
            StoreUtil_1.default.setStore(STORE_TOKEN_KEY, token, StoreUtil_1.default.SESSION_TYPE);
        };
        return ApiService;
    }());
    exports.default = ApiService;
});
