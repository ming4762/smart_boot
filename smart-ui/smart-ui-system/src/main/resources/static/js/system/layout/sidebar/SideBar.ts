
declare var busVue
// @ts-ignore 样式混入
import ThemeMixins from '../../../mixins/ThemeMixins.js'
// @ts-ignore
import ValidateUtils from '../../../utils/ValidateUtils.js'

import SidebarItem from './SidebarItem.js'

export default {
  mixins: [
    ThemeMixins
  ],
  data () {
    return {
      // 激活的菜单
      activeIndex: ''
    }
  },
  computed: {
    getBus (): any {
      return busVue
    },
    // 侧边栏底部演示计算属性
    getSidebarHeaderStyle () {
      return `background-color:${this.getTopColor}`
    },
    /**
     * 菜单列表计算属性
     */
    computedMenuList (): any[] {
      const activeTopMenu = this.getBus.activeTopMenu
      if (ValidateUtils.validateNull(activeTopMenu)) {
        return []
      } else {
        return activeTopMenu.children
      }
    },
    /**
     * 是否展开侧边栏
     */
    computedIsCollapse (): boolean {
      return !this.getBus.sidebar.opened
    },
    /**
     * 获取激活的菜单
     */
    computedActiveMenu (): any {
      return this.getBus.activeMenu
    },
    /**
     * 获取激活的index计算属性
     */
    computedActiveIndex (): string {
      const activeMenu = this.getBus.activeMenu
      return activeMenu ? activeMenu.id : ''
    }
  },
  template: `
  <el-scrollbar wrap-class="scrollbar-wrapper">
    <div class="left-outer">
      <div :style="getSidebarHeaderStyle" class="left-header">
      </div>
      <el-menu
        class="left-menu"
        :collapse="computedIsCollapse"
        :show-timeout="200"
        :background-color="getSideBarTheme['background-color']"
        :text-color="getSideBarTheme['text-color']"
        :active-text-color="getSideBarTheme['active-text-color']"
        mode="vertical"
        :default-active="computedActiveIndex">
        <sidebar-item
          :key="menu.id"
          :item="menu"
          v-for="menu in computedMenuList"/>
      </el-menu>
    </div>
  </el-scrollbar>
  `
}
