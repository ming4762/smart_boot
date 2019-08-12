import Validate from './ValidateUtils.js'

/**
 * 时间工具类
 * @author zhongming
 */
export default class TimeUtils {

  /**
   * 格式化时间
   * @param time 时间
   */
  public static formatTime (time: number): string {
    if (Validate.validateNull(time)) {
      return ''
    }
    const date = new Date(time)
    const year = date.getFullYear()
    const month = date.getMonth() + 1
    const day = date.getDate()
    const hour = date.getHours()
    const minute = date.getMinutes()
    const second = date.getSeconds()
    return year + '-' + (month < 10 ? ('0' + month) : month) + '-' + (day < 10 ? ('0' + day) : day) + ' ' + (hour < 10 ? ('0' + hour) : hour) + ':' + (minute < 10 ? ('0' + minute) : minute) + ':' + (second < 10 ? ('0' + second) : second)
  }

  /**
   * 毫秒转天时分秒
   * @param msec 毫秒
   */
  public static convertMsec (msec: number): string {
    let second = 0
    let minute = 0
    let hour = 0
    let day = 0
    if (msec > 1000) {
      // 计算秒
      second = parseInt(msec/1000 + '', 0)
      // 计算分
      if(second > 60) {
        minute = parseInt(second/60 + '', 0)
        second = parseInt(second % 60 + '', 0)
      }
      // 计算小时
      if (minute > 60) {
        hour = parseInt(minute/60 + '', 0)
        minute = parseInt(minute % 60 + '', 0)
      }
      // 计算天
      if (hour > 24) {
        day = parseInt(hour/24 + '', 0)
        hour = parseInt(hour % 24 + '', 0)
      }
    }
    let result = second + '秒'
    if (minute > 0) {
      result = minute + '分钟' + result
    }
    if (hour > 0) {
      result = hour + '小时' + result
    }
    if (day > 0) {
      result = day + '天' + result
    }
    return result
  }

  /**
   *
   * @param {number} contrastTime 需要比对的时间
   * @param {number} beContrastTime 被比对的时间
   * @returns {string} 2分钟前、刚刚、1天前、一天后
   */
  public static contrastTime(contrastTime: number, beContrastTime?: number): string {
    let result = ''
    if (!beContrastTime) {
      beContrastTime = new Date().getTime()
    }
    // 标识比对时间是否在被比对之前
    let isBefore: boolean = contrastTime < beContrastTime
    let str: string = isBefore ? '前' : '后'
    // 获取时间差值
    let difference: number = Math.abs(beContrastTime - contrastTime)
    if(difference < 60 * 1000) {
      // 如果时间小于1分钟
      if (isBefore) {
        result = '刚刚'
      } else {
        result = '马上'
      }
    } else if (difference < 60 * 60 * 1000) {
      // 不超过1小时
      result = parseInt((difference % (1000 * 60 * 60)) / (1000 * 60) + '', 10) + '分钟' + str
    } else if (difference < 24 * 60 * 60 * 1000) {
      // 不超过一天
      result = parseInt((difference % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60) + '', 10) + '小时' + str
    } else if (difference < 30 * 24 * 60 * 60 * 1000) {
      // 不超过一个月
      result = parseInt(difference / (1000 * 60 * 60 * 24) + '', 10) + '天' + str
    } else if (difference < 365 * 24 * 60 * 60 * 1000) {
      // 不超过1年
      result = parseInt(difference / (1000 * 60 * 60 * 24 * 30) + '', 10) + '月' + str
    } else {
      result = parseInt(difference / (1000 * 60 * 60 * 24 * 365) + '', 10) + '年' + str
    }
    return result
  }

  /**
   * 格式化日期
   * @param time 时间
   */
  public static formatDate (time: number): string {
    if (Validate.validateNull(time)) {
      return ''
    }
    let date = new Date(time)
    let year = date.getFullYear()
    let month = date.getMonth() + 1
    let day = date.getDate()
    return year + '-' + (month < 10 ? ('0' + month) : month) + '-' + (day < 10 ? ('0' + day) : day)
  }
}
