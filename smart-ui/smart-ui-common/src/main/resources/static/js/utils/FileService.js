import AuthUtils from './AuthUtils.js';
const baseUrl = localStorage.getItem('API_URL');
const service = axios.create({
    baseURL: baseUrl,
    timeout: 10000
});
const TOKEN_KEY = 'Authorization';
export default class FileService {
    static upload(file, parameter) {
        const formData = new FormData();
        formData.append('file', file);
        return this.doUpload(formData, false, parameter);
    }
    static batchUpload(fileList, parameter) {
        if (fileList.length > 0) {
            const formData = new FormData();
            fileList.forEach((file) => {
                formData.append('files', file);
            });
            return this.doUpload(formData, true, parameter);
        }
        return Promise.resolve(null);
    }
    static doUpload(formData, batch, parameter) {
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
        const url = batch ? 'public/file/batchUpload' : 'public/file/upload';
        return service.post(url, formData, headers)
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
    static getImageUrl(fileId) {
        return `${baseUrl}/public/image/show/${fileId}`;
    }
    static getDownloadURL(fileId) {
        return `${baseUrl}//public/file/download/${fileId}`;
    }
}
