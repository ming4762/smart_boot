declare var busVue

import ScrollPane from './ScrollPane.js'

export default {
  components: {
    'scroll-pane': ScrollPane
  },
  computed: {
    getBus (): any {
      return busVue
    },
    /**
     * 获取打开的菜单列表
     */
    computedOpenMenuList (): any[] {
      return this.getBus.openMenuList
    }
  },
  methods: {
    /**
     * 判断菜单是否激活的
     */
    isActive (menu: any): boolean {
      return menu.path === this.getBus.activeMenu.path
    },
    /**
     * 打开操作面板
     * @param menu
     * @param event
     */
    handleOpenOperate (menu: any, event: Event) {
      // TODO: 待开发
      console.log(menu)
    },
    /**
     * 关闭选中的菜单
     * @param menu
     */
    handleCloseSelectedMenu (menu: any): void {
      this.getBus.deleteMenu(menu.path)
    },
    /**
     * 激活菜单
     * @param menu
     */
    handleActiveMenu (menu: any): void {
      this.getBus.addMenu(menu)
    }
  },
  template: `
  <div class="tags-view-container">
    <scroll-pane class="tags-view-wrapper" ref="scrollPane">
      <a
        @click="handleActiveMenu(menu)"
        v-for="(menu, index) in computedOpenMenuList"
        :class="isActive(menu)?'active':''"
        class="tags-view-item"
        @contextmenu.prevent.native="handleOpenOperate(menu, $event)"
        :key="menu.path">
        {{menu.name}}
        <!--第一菜单不显示关闭按钮-->
        <span v-if="index !== 0" class="el-icon-close" @click.prevent.stop="handleCloseSelectedMenu(menu)" />
      </a>
    </scroll-pane>
  </div>
  `
}