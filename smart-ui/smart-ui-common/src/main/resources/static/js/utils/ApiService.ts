declare var axios
// 项目跟路径
declare var contextPath: string
// @ts-ignore
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
  public static postAjax(url: string, parameter?: {[index: string]: any}, headers?: {[index: string]: any}): Promise<any> {
    headers = headers || {}
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
      if (error.code === 403) {
        // 跳转到无权限页面
        ApiService.toNoPremissionPage()
      }
      if (error.response && error.response.status === 401) {
        // 未登录跳转到登录页面
        ApiService.toLoginPage()
      }
      // @ts-ignore
      return Promise.reject(error)
    })
  }

  /**
   * 保存token操作
   * @param token
   */
  public static saveToken (token?: string) {
    StoreUtil.setStore(STORE_TOKEN_KEY, token, StoreUtil.SESSION_TYPE)
  }

  /**
   * 跳转到登录页面，TODO：登录页面可配置
   */
  private static toLoginPage () {
    const loginUrl = `${contextPath}ui/system/login`
    // 判断是否在iframe中
    if (window.frames.length != parent.frames.length) {
      parent.location.href = loginUrl
    } else {
      window.location.href = loginUrl
    }
  }

  /**
   * 跳转到无权限页面
   */
  private static toNoPremissionPage () {

  }
}