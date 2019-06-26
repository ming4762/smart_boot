
/**
 * common工具类
 * @author zhongming
 */
export default class CommonUtil {
  /**
   * 生成UUID
   */
  public static createUUID (): string {
    let s: Array<any> = []
    let hexDigits = '0123456789abcdef'
    for (let i = 0; i < 36; i++) {
      s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1)
    }
    s[14] = '4'  // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1)  // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23] = '-'
    let uuid = s.join('')
    return uuid
  }

  /**
   * 深拷贝对象
   * @param object 被拷贝对象
   */
  public static clone<T> (object: T): T | null {
    if (object == null) {
      return null
    }
    let objectStr: string = JSON.stringify(object)
    return JSON.parse(objectStr)
  }

  /**
   * 从对象列表中获取指定key并构成一个新的对象数组
   * @param keys key数组
   * @param objectList 原始对象数组
   */
  public static getObjectByKeys (keys: string[], objectList: any[]): any[] {
    if (objectList && keys) {
      let resultList: any[] = []
      objectList.forEach(object => {
        let result: any = {}
        keys.forEach(key => {
          result[key] = object[key]
        })
        resultList.push(result)
      })
      return resultList
    }
    return []
  }
}
