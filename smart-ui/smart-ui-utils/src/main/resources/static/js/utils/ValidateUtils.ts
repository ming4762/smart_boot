
/**
 * 验证工具类
 * @author zhongming
 * @since 3.0
 */
export default class ValidateUtils {
  /**
   * 验证手机号
   * @param phone 电话号码
   */
  public static validateMobile(phone: string): Array<any> {
    let list: Array<any> = []
    let result: boolean = false
    let msg: string = ''
    const isPhone = /^0\d{2,3}-?\d{7,8}$/
    // 增加134 减少|1349[0-9]{7}，增加181,增加145，增加17[678]
    const isMob = /^((\+?86)|(\(\+86\)))?(13[0123456789][0-9]{8}|15[012356789][0-9]{8}|18[012356789][0-9]{8}|14[57][0-9]{8}|17[3678][0-9]{8})$/
    if (!ValidateUtils.validateNull(phone)) {
      if (phone.length === 11) {
        if (isPhone.test(phone)) {
          msg = '手机号码格式不正确'
        } else {
          result = true
        }
      } else {
        msg = '手机号码长度不为11位'
      }
    } else {
      msg = '手机号码不能为空'
    }
    list.push(result)
    list.push(msg)
    return list
  }

  /**
   * vue验证手机号
   * @param rule 验证规则
   * @param value 手机号
   * @param callback 回调
   */
  public static vueValidateMobile = (rule: any, value: any, callback: Function) => {
    const result = ValidateUtils.validateMobile(value)
    if (result[0] === false) {
      return callback(new Error(result[1]))
    } else {
      callback()
    }
  }

  /**
   * 验证是否为null
   * @param value
   */
  public static validateNull(value: any): boolean {
    if (typeof value === 'boolean') {
      return false
    }
    if (value instanceof Array) {
      if (value.length === 0) return true
    } else if (value instanceof Object) {
      if (JSON.stringify(value) === '{}') return true
    } else {
      if (value === 'null' || value === null || value === 'undefined' || value === undefined || value === '') return true
      return false
    }
    return false
  }

  public static isValidUsername(str: string) {
    const validMap = ['admin', 'editor']
    return validMap.indexOf(str.trim()) >= 0
  }

  public static validateURL(textval: string) {
    const urlregex = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/
    return urlregex.test(textval)
  }

  public static validateLowerCase(str: string) {
    const reg = /^[a-z]+$/
    return reg.test(str)
  }

  public static validateUpperCase(str: string) {
    const reg = /^[A-Z]+$/
    return reg.test(str)
  }


  public static validatAlphabets(str: string) {
    const reg = /^[A-Za-z]+$/
    return reg.test(str)
  }
}

