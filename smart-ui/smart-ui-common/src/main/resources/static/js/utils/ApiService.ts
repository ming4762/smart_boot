declare var axios
// 项目跟路径
declare var contextPath: string
// @ts-ignore
import StoreUtil from 'utils/StoreUtil'
// @ts-ignore
import RsaUtils from 'utils/RsaUtils'
// @ts-ignore
import Md5Utils from 'utils/Md5Utils'
// 设置默认的请求头
axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8'
const service = axios.create({
    // baseURL: StoreUtil.getStore('API_URL'),
    baseURL: localStorage.getItem('API_URL'),
    timeout: 10000
})
// 存储token的key
const STORE_TOKEN_KEY = 'SMART_AUTHORIATION'
const RSA_CLIENT_PRIVATE_KEY = "RSA_CLIENT_PRIVATE_KEY"
const RSA_SERVER_PUBLIC_KEYU = "RSA_SERVER_PUBLIC_KEYU"
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

  /**
   * 发送ajax请求
   * @param url
   * @param parameter
   * @param headers
   * @param ideConfig 接口加密配置
   */
  public static postAjax(url: string, parameter?: any, headers?: {[index: string]: any}, ideConfig?: IdeConfig): Promise<any> {
    headers = headers || {}
    const token = getToken()
    if (token) {
      headers[TOKEN_KEY] = token
    }
    parameter = parameter || {}
    if (this.isEncrypt(ideConfig)) {
      parameter = RsaUtils.rsaEncrypt(this.getServerPublicKey(), JSON.stringify(this.createEncryptParameter(parameter, ideConfig)))
    }
    return service.post(url, parameter, {
      headers: headers
    }).then((result: any) => {
      let resultSuccess = true
      let data
      if (result && result.status === 200) {
        // 判断是否进行参数解密
        if (this.isDecrypt(ideConfig)) {
          // 解密
          data = JSON.parse(RsaUtils.rsaDecrypt(this.getClientPrivateKey(), result.data))
        } else {
          data = result.data
        }
        if (data['code'] !== 200) {
          resultSuccess = false
        } else {
          return data.data
        }
      } else {
        resultSuccess = false
      }
      if (!resultSuccess) {
        // @ts-ignore
        return Promise.reject(result ? data : result)
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
   * 登出操作
   */
  public static logout (): Promise<any> {
    return this.postAjax('logout', {
    }).then(result => {
      window.sessionStorage.clear()
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
   * 使用是否接口加密
   */
  public static useIde(): boolean {
    return localStorage.getItem('useIde') === 'true'
  }

  /**
   * 获取客户端私钥
   */
  public static getClientPrivateKey(): string {
    return StoreUtil.getStore(RSA_CLIENT_PRIVATE_KEY)
  }

  /**
   * 获取服务器公钥
   */
  public static getServerPublicKey (): string {
    return StoreUtil.getStore(RSA_SERVER_PUBLIC_KEYU)
  }

  /**
   * 注册秘钥
   */
  public static registerKey() {
    // 生成秘钥秘钥
    const rasKey = RsaUtils.createKey()
    // 执行注册
    return this.postAjax('auth/registerKey', rasKey.pubKey).then((serverPublicKey: string) => {
      // 保存私钥
      StoreUtil.setStore(RSA_CLIENT_PRIVATE_KEY, rasKey.priKey, StoreUtil.SESSION_TYPE)
      // 保存公钥
      StoreUtil.setStore(RSA_SERVER_PUBLIC_KEYU, serverPublicKey, StoreUtil.SESSION_TYPE)
    })
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
   * 判断是否要进行参数加密
   * @param ideConfig
   */
  private static isEncrypt(ideConfig?: IdeConfig) {
    return ideConfig && ideConfig.encrypt === true && this.useIde()
  }

  /**
   *
   * @param ideConfig
   */
  private static isDecrypt(ideConfig?: IdeConfig) {
    return ideConfig && ideConfig.decrypt === true && this.useIde()
  }

  /**
   * 创建加密参数
   * @param parameter
   * @param ideConfig
   */
  private static createEncryptParameter(parameter: any, ideConfig?: IdeConfig): any {
    const encryptParameter = {
      data: parameter,
      md5: '',
      timestamp: 0
    }
    //md5
    if (ideConfig && ideConfig.md5 === true) {
      encryptParameter.md5 = Md5Utils.md5(JSON.stringify(parameter))
    }
    if (ideConfig && ideConfig.timestamp) {
      encryptParameter.timestamp = new Date().getTime()
    }
    return encryptParameter
  }

  /**
   * 跳转到无权限页面
   */
  private static toNoPremissionPage () {

  }
}

interface IdeConfig {
  encrypt: boolean,
  decrypt: boolean,
  md5: boolean,
  timestamp: boolean
}