export default class StoreUtil {
    static setStore(key, data, type) {
        let dealData = {
            dataType: typeof data,
            content: data,
            type: type,
            datetime: new Date().getTime()
        };
        if (this.SESSION_TYPE === type) {
            window.sessionStorage.setItem(key, JSON.stringify(dealData));
        }
        else {
            window.localStorage.setItem(key, JSON.stringify(dealData));
        }
    }
    static clearSession() {
        window.sessionStorage.clear();
    }
    static getStore(key, debug) {
        let data = window.localStorage.getItem(key);
        if (data === undefined || data === null) {
            data = window.sessionStorage.getItem(key);
        }
        if (data === undefined || data === null) {
            return null;
        }
        const dataObject = JSON.parse(data);
        if (debug) {
            console.log(dataObject);
        }
        return dataObject['content'];
    }
}
StoreUtil.SESSION_TYPE = 'session';
