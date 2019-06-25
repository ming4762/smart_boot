/**
 * 存储工具类
 */
export default class StoreUtil {
    public static SESSION_TYPE: string = 'session'
    /**
     * 数据存储到strong
     * @param key 数据的key
     * @param data 数据
     * @param type 存储类型
     */
    public static setStore (key: string, data: any, type?: string): void {
        let dealData: any = {
            dataType: typeof data,
            content: data,
            type: type,
            datetime: new Date().getTime()
        }
        if (this.SESSION_TYPE === type) {
            window.sessionStorage.setItem(key, JSON.stringify(dealData))
        } else {
            window.localStorage.setItem(key, JSON.stringify(dealData))
        }
    }
    /**
     * 清空session
     */
    public static clearSession (): void {
        window.sessionStorage.clear()
    }

    /**
     * 从strong获取数据
     * @param key 数据的key
     * @param debug 是否使用debug模式
     */
    public static getStore (key: string, debug?: boolean): any {
        let data = window.localStorage.getItem(key)
        if (data === undefined || data === null) {
            data = window.sessionStorage.getItem(key)
        }
        if (data === undefined || data === null) {
            return null
        }
        const dataObject = JSON.parse(data!)
        if (debug) {
            console.log(dataObject)
        }
        return dataObject['content']
    }
}