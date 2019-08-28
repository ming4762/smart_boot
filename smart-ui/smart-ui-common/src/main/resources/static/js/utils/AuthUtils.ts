// @ts-ignore
import StoreUtil from './StoreUtil.js'
// @ts-ignore
import Md5Utils from './Md5Utils.js'
// @ts-ignore
import { STORE_KEYS } from '../Constants.js'
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

  /**
   * 获取当前用户
   */
  public static getCurrentUser (): any | null {
    return StoreUtil.getStore(STORE_KEYS.USER_KEY)
  }

  /**
   * 获取当前用户Id
   */
  public static getCurrentUserId (): string | null {
    const user = this.getCurrentUser()
    return user === null ? null : user.userId
  }

  /**
   * 创建密码
   * @param username
   * @param password
   */
  public static createPassword (username, password) {
    const salt = '1qazxsw2'
    const passwordValue = username + password + salt
    return Md5Utils.md5(passwordValue, 2)
  }
}