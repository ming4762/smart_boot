// @ts-ignore
import ComponentBuilder from 'ComponentBuilder'
// 样式混入
import ThemeMixins from 'mixins/ThemeMixins'

/**
 * 侧边栏组件
 */
export default class SideBar extends ComponentBuilder {

  protected data () {
    return {
      // 激活的菜单
      activeIndex: ''
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
      // 侧边栏底部演示计算属性
      getSidebarHeaderStyle () {
        return `background-color:${this.getTopColor}`
      }
    }
  }

  protected template () {
    return `
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <div class="left-outer">
        <div :style="getSidebarHeaderStyle" class="left-header">
        </div>
        <el-menu
          class="left-menu"
          :show-timeout="200"
          :background-color="getSideBarTheme['background-color']"
          :text-color="getSideBarTheme['text-color']"
          :active-text-color="getSideBarTheme['active-text-color']"
          mode="vertical"
          :default-active="activeIndex">
        
        </el-menu>
      </div>
    </el-scrollbar>
    `
  }
}