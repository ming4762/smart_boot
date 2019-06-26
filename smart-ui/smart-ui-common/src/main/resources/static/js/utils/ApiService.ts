declare var axios

import StoreUtil from 'utils/StoreUtil'

const service = axios.create({
    // baseURL: StoreUtil.getStore('API_URL'),
    baseURL: localStorage.getItem('API_URL'),
    timeout: 10000
})
const STORE_TOKEN_KEY = 'SMART_AUTHORIATION'
/**
 * 获取token
 */
const getToken = () => {
    return StoreUtil.getStore(STORE_TOKEN_KEY)
}

/**
 * token请求头key
 */
const TOKEN_KEY: string = 'Authorization'

export default class ApiService  {
  public static postAjax(url: string, parameter?: {[index: string]: any}): Promise<any> {
    const headers: {[index: string]: string} = {}
    const token = getToken()
    if (token) {
      headers[TOKEN_KEY] = token
    }
    parameter = parameter || {}
    return service.post(url, parameter, {
      headers: headers
    }).then((result: any) => {
      if (result && result.status === 200 && result.data['code'] === 200) {
        return result.data.data
      } else {
        // @ts-ignore
        return Promise.reject(result ? result.data : result)
      }
    }).catch((error: any) => {
      // @ts-ignore
      return Promise.reject(error)
    })
  }

  /**
   * 保存token操作
   * @param token
   */
  public static saveToken (token: string) {
    StoreUtil.setStore(STORE_TOKEN_KEY, token, StoreUtil.SESSION_TYPE)
  }
}