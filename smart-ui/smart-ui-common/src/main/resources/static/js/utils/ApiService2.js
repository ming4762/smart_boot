axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8';
class ApiService2 {
    static setApiUrl(url) {
        localStorage.setItem('API_URL', url);
    }
    static getApiUrl() {
        return localStorage.getItem('API_URL');
    }
    static getToken() {
        return sessionStorage.getItem(this.STORE_TOKEN_KEY);
    }
    static saveToken(token) {
        sessionStorage.setItem(this.STORE_TOKEN_KEY, token);
    }
    static ajax(url, ajaxType, parameter, customParameter) {
        const serverParameter = Object.assign({
            method: ajaxType,
            url: url,
            data: parameter || {},
            headers: {},
            validateStatus: status => status >= 200 && status < 300,
        }, customParameter);
        const token = this.getToken();
        if (token) {
            serverParameter.headers[this.TOKEN_KEY] = token;
        }
        return ApiService2.api_service(serverParameter)
            .then(result => {
            if (result && result.status === 200) {
                const data = result.data;
                if (data['code'] === 200) {
                    return data.data;
                }
            }
            return Promise.reject(result ? result.data : result);
        });
    }
    static postAjax(url, parameter, customParameter) {
        return this.ajax(url, this.POST, parameter, customParameter);
    }
    static getAjax(url, parameter, customParameter) {
        return this.ajax(url, this.GET, parameter, customParameter);
    }
}
ApiService2.POST = 'POST';
ApiService2.GET = 'GET';
ApiService2.STORE_TOKEN_KEY = 'SMART_AUTHORIATION';
ApiService2.TOKEN_KEY = 'Authorization';
ApiService2.api_service = axios.create({
    baseURL: ApiService2.getApiUrl(),
    timeout: 10000
});
