// @ts-ignore
import ComponentBuilder from 'ComponentBuilder'

export default class MessageMixins extends ComponentBuilder {

  /**
   * 方法
   */
  protected methods (): {[index: string]: Function} {
    return {
      /**
       * 发送错误信息
       * @param message
       * @param error
       * @param options
       */
      errorMessage (message: string, error: any, options?: any): void {
        console.error(error)
        const defaultParameter = {
          showClose: true,
          message: message,
          type: 'error'
        }
        // @ts-ignore
        const parameter = options ? Object.assign(defaultParameter, options) : defaultParameter
        this.$message(parameter)
      },
      /**
       * 发送成功消息
       * @param message
       * @param options
       */
      successMessage (message: string, options?: any): void {
        const defaultParameter = { message: message, type: 'success' }
        // @ts-ignore
        const parameter = options ? Object.assign(defaultParameter, options) : defaultParameter
        this.$message(parameter)
      }
    }
  }
}