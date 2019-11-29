
declare var axios

// 项目跟路径
declare var contextPath: string

// 设置默认的请求头
axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8'

/**
 * API工具类
 */
class ApiService2 {

  public static POST: string = 'POST'

  public static GET: string = 'GET'

  private static STORE_TOKEN_KEY = 'SMART_AUTHORIATION'

  private static TOKEN_KEY: string = 'Authorization'

  private static api_service = axios.create({
    baseURL: ApiService2.getApiUrl(),
    timeout: 10000
  })

  public static setApiUrl(url: string) {
    localStorage.setItem('API_URL', url)
  }

  /**
   * 获取后台地址
   */
  public static getApiUrl (): string {
    return localStorage.getItem('API_URL')
  }

  /**
   * 获取token
   */
  public static getToken (): string {
    return sessionStorage.getItem(this.STORE_TOKEN_KEY)
  }

  /**
   * 保存token
   * @param token
   */
  public static saveToken (token: string) {
    sessionStorage.setItem(this.STORE_TOKEN_KEY, token)
  }
  /**
   * 发送ajax请求
   * @param url
   * @param ajaxType
   * @param parameter
   * @param headers
   * @param customParameter
   */
  private static ajax(url: string, ajaxType: string, parameter?: any, customParameter?: any) {
    const serverParameter = Object.assign({
      method: ajaxType,
      url: url,
      data: parameter || {},
      headers: {},
      validateStatus: status => status >= 200 && status < 300,
    }, customParameter)
    const token = this.getToken()
    if (token) {
      serverParameter.headers[this.TOKEN_KEY] = token
    }
    return ApiService2.api_service(serverParameter)
        .then(result => {
          // 请求是否发送成功
          if (result && result.status === 200) {
            const data = result.data
            if (data['code'] === 200) {
              return data.data
            }
          }
          return Promise.reject(result ? result.data : result)
        })
  }

  /**
   * 发送post请求
   * @param url
   * @param parameter
   * @param customParameter
   */
  public static postAjax(url: string, parameter?: any, customParameter?: any) {
    return this.ajax(url, this.POST, parameter, customParameter)
  }

  /**
   * 发送get请求
   * @param url
   * @param parameter
   * @param customParameter
   */
  public static getAjax(url: string, parameter?: any, customParameter?: any) {
    return this.ajax(url, this.GET, parameter, customParameter)
  }
}
