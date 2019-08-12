
// @ts-ignore
import ResizeHandler from '../utils/ResizeHandler.js'

export default {
  data () {
    return {
      clientHeight: 0
    }
  },
  computed: {
    /**
     * 计算表格高度
     */
    computedTableHeight () {
      return this.clientHeight - 30
    }
  },
  beforeMount () {
    const $this: any = this
    $this.clientHeight = document.body.clientHeight
    // 绑定事件
    ResizeHandler.bind(() => {
      $this.clientHeight = document.body.clientHeight
    })
  }
}
