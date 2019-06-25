import Md5 from 'utils/Md5'
/**
 * MD5工具类
 */
export default class Md5Utils {

  /**
   * 进行MD5加密
   * @param value 需要加密的值
   * @param hashIterations 加密次数
   */
  public static md5 (value: string, hashIterations: number = 1): string {
    let hash = Md5.hashStr(value).toString()
    if (hashIterations > 1) {
      for (let i=1; i<hashIterations; i++) {
        hash = Md5.hashStr(hash).toString()
      }
    }
    return hash
  }
}