// @ts-ignore
import ComponentBuilder from 'ComponentBuilder'

// @ts-ignore
import ResizeHandler from 'utils/ResizeHandler'

export default class LayoutMixins extends ComponentBuilder {

  protected data () {
    return {
      clientHeight: 0
    }
  }

  protected computed () {
    return {
      /**
       * 计算表格高度
       */
      computedTableHeight () {
        return this.clientHeight - 30
      }
    }
  }

  /**
   * 加载前状态
   */
  protected beforeMount () {
    const $this: any = this
    $this.clientHeight = document.body.clientHeight
    // 绑定事件
    ResizeHandler.bind(() => {
      $this.clientHeight = document.body.clientHeight
    })
  }
}