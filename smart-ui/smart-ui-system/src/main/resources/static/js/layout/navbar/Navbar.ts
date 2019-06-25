// @ts-ignore
import ComponentBuilder from 'ComponentBuilder'
// 样式混入
import ThemeMixins from 'mixins/ThemeMixins'


/**
 * 顶部组件
 */
export default class Navbar extends ComponentBuilder {

  /**
   * VUE数据
   */
  protected data () {
    return {
      // 激活菜单的标识
      activeIndex: '',
      // sidebar 对象
      sidebar: {}
    }
  }

  /**
   * 混入
   */
  protected mixins (): any[] {
    return [
      // @ts-ignore
      new ThemeMixins().build()
    ]
  }

  /**
   * 计算属性
   */
  protected computed () {
    return {
      // 获取按钮激活状态
      getIsActive () {
        return this.getBus.sidebar.opened === true
      },
      getBus () {
        // @ts-ignore
        return busVue
      },
      // 获取样式
      computedStyle () {
        return `fill: ${this.getTopTextColor}`
      },
      // 获取顶部样式
      getTopDivStyle () {
        return 'background-color:' + this.getBus.theme.topColor
      }
    }
  }

  /**
   * 函数
   */
  protected methods () {
    return {
      /**
       * 菜单选中时触发
       */
      handleMenuSelectEvent (index) {
        this.activeIndex = index
      },
      /**
       * 点击显示隐藏侧边栏触发
       */
      handleToggleClick () {
        this.getBus.sidebar.opened = !this.getBus.sidebar.opened
      }
    }
  }

  /**
   * 创建模板
   */
  protected template () {
    return `
    <div :style="getTopDivStyle" class="navbar-outer-a">
      <el-menu
        ref="topMenu"
        :background-color="getTopColor"
        :text-color="getTopTextColor"
        class="navbar"
        @select="handleMenuSelectEvent"
        mode="horizontal">
        <div class="hamburger-container">
          <svg
              :class="{'is-active':getIsActive}"
              t="1492500959545"
              class="hamburger"
              :style="computedStyle"
              viewBox="0 0 1024 1024"
              version="1.1"
              xmlns="http://www.w3.org/2000/svg"
              p-id="1691"
              xmlns:xlink="http://www.w3.org/1999/xlink"
              width="64"
              height="64"
              @click="handleToggleClick">
            <path
                d="M966.8023 568.849776 57.196677 568.849776c-31.397081 0-56.850799-25.452695-56.850799-56.850799l0 0c0-31.397081 25.452695-56.849776 56.850799-56.849776l909.605623 0c31.397081 0 56.849776 25.452695 56.849776 56.849776l0 0C1023.653099 543.397081 998.200404 568.849776 966.8023 568.849776z"
                p-id="1692" />
            <path
                d="M966.8023 881.527125 57.196677 881.527125c-31.397081 0-56.850799-25.452695-56.850799-56.849776l0 0c0-31.397081 25.452695-56.849776 56.850799-56.849776l909.605623 0c31.397081 0 56.849776 25.452695 56.849776 56.849776l0 0C1023.653099 856.07443 998.200404 881.527125 966.8023 881.527125z"
                p-id="1693" />
            <path
                d="M966.8023 256.17345 57.196677 256.17345c-31.397081 0-56.850799-25.452695-56.850799-56.849776l0 0c0-31.397081 25.452695-56.850799 56.850799-56.850799l909.605623 0c31.397081 0 56.849776 25.452695 56.849776 56.850799l0 0C1023.653099 230.720755 998.200404 256.17345 966.8023 256.17345z"
                p-id="1694" />
          </svg>
        </div>
      </el-menu>
    </div>
    `
  }
}