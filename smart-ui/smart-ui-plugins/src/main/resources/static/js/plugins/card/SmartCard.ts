// @ts-ignore
import ComponentBuilder from 'ComponentBuilder'

/**
 * 卡片组件
 */
export default class SmartCard extends ComponentBuilder {
  protected props () {
    return {
      // 悬停颜色
      hoverColor: String
    }
  }

  protected data () {
    return {
      bodyStyle: {
        background: 'blue'
      }
    }
  }

  protected methods () {
    return {
      /**
       * 鼠标进入事件
       */
      handleMouseOver (event) {
        console.log(event)
      },
      /**
       * 鼠标移出事件
       * @param event
       */
      handleMouseLeave (event) {
        console.log(event)
      }
    }
  }

  protected template () {
    return `
    <el-card
      :body-style="bodyStyle"
      @mouseover.native="handleMouseOver"
      @mouseleave.native="handleMouseLeave"
      v-bind="$attrs">
      <slot></slot>
    </el-card>
    `
  }
}