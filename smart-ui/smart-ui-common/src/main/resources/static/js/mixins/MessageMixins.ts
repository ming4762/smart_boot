
export default {
  data () {
    return {
      messageFullScreenloading: {}
    }
  },
  methods : {
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
    },
    /**
     * 显示打开全屏加载
     * @param loading 加载状态
     * @param options 参数
     */
    showHideFullScreenloading (loading: Boolean, options?: any): any {
      const defaultOption = {
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      }
      if (loading) {
        this.messageFullScreenloading = this.$loading(Object.assign({}, defaultOption, options || {}))
        return this.messageFullScreenloading
      } else {
        return this.messageFullScreenloading.close()
      }
    }
  }

}

