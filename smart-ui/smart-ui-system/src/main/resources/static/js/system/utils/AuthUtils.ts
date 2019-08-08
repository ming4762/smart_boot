// @ts-ignore
import Md5Utils from 'utils/Md5Utils'

// @ts-ignore
import { STORE_KEYS } from 'Constants'
// @ts-ignore
import StoreUtil from 'utils/StoreUtil'

/**
 * 创建密码
 * @param username
 * @param password
 */
export const createPassword = (username: string, password: string): string => {
 const salt = '1qazxsw2'
 const passwordValue = username + password + salt
 return Md5Utils.md5(passwordValue, 2)
}

export default class AuthUtils {

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
}