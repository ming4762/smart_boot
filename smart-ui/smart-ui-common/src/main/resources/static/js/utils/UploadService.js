import AuthUtils from './AuthUtils.js';
const service = axios.create({
    baseURL: localStorage.getItem('API_URL'),
    timeout: 10000
});
const TOKEN_KEY = 'Authorization';
export default class UploadService {
    static upload(file, filename, parameter) {
        const formData = new FormData();
        formData.append('file', file, filename);
        if (parameter) {
            Object.keys(parameter)
                .forEach(key => {
                formData.append(key, parameter[key]);
            });
        }
        const headers = {
            'Content-Type': 'multipart/form-data'
        };
        const token = AuthUtils.getToken();
        if (token) {
            headers[TOKEN_KEY] = token;
        }
        return service.post('file/upload', formData, headers)
            .then(response => {
            if (response.status === 200) {
                if (response.data) {
                    if (response.data.ok === true) {
                        return response.data.data;
                    }
                    else {
                        return Promise.reject(response);
                    }
                }
            }
            else {
                return Promise.reject(response);
            }
        });
    }
}
