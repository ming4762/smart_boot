// @ts-ignore
import StoreUtil from './StoreUtil.js'
// 存储token的key
const STORE_TOKEN_KEY = 'SMART_AUTHORIATION'
/**
 * 认证工具类
 */
export default class AuthUtils {

  /**
   * 获取token
   */
  public static getToken () {
    return StoreUtil.getStore(STORE_TOKEN_KEY)
  }
}