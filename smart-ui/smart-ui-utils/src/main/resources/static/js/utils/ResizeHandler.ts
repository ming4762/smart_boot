/**
 * 页面大小变化时调整
 */
export default class ResizeHandler {

  private static handlers: Array<Function> = []

  private static listener = null

  /**
   * 绑定操作
   * @param handler
   */
  public static bind(handler: Function) {
    this.handlers.push(handler)
    if (this.listener === null) {
      this.addListener()
    }
  }

  /**
   * 添加事件
   */
  private static addListener () {
    window.addEventListener('resize', this.resize)
    this.listener = {}
  }

  /**
   * 执行resize
   */
  private static resize () {
    ResizeHandler.handlers.forEach((handler: Function) => {
      handler.apply(this)
    })
  }
}