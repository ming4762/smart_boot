axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8';
const service = axios.create({
    baseURL: localStorage.getItem('API_URL'),
    timeout: 10000
});
export default class RestUtils {
    static getService() {
        return service;
    }
}
