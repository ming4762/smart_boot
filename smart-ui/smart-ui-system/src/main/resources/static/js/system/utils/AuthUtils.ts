// @ts-ignore
import Md5Utils from 'utils/Md5Utils'

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