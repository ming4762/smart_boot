declare var axios
// 设置默认的请求头
axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8'
const service = axios.create({
  // baseURL: StoreUtil.getStore('API_URL'),
  baseURL: localStorage.getItem('API_URL'),
  timeout: 10000
})

export default class RestUtils {
  /**
   * 获取服务
   */
  public static getService () {
    return service
  }
}