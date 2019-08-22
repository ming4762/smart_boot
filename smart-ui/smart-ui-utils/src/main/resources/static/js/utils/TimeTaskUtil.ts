
/**
 * 时间任务工具
 */
export default class TimeTaskUtil {

  /**
   * 延迟执行
   * @param parameters 需要返回的参数
   * @param timeout 延迟事件
   */
  public static delay<T> (timeout: number, ...parameters: Array<T>): Promise<Array<T>> {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        resolve(parameters)
      }, timeout)
    })
  }

  /**
   * 循环执行
   * @param targetFunction 执行函数
   * @param times 循环次数
   * @param delay 延迟事件
   * @param parameter
   */
  public static loop (targetFunction: Function, times: number, delay: number, ...parameter: any[]): void {
    let num: number = 0
    const loop = setInterval(() => {
      // 执行目标函数
      try {
        targetFunction(loop, ...parameter)
      } catch (error) {
        console.error(error)
      }
      num ++
      if (num === times) {
        clearInterval(loop)
      }
    }, delay)
  }
}
