declare var busVue
export default {
  data () {
    return {
      // 存储已经打开的菜单
      menuPath: {}
    }
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
    },
    /**
     * 激活的菜单
     */
    computedActiveMenu (): any {
      return this.getBus.activeMenu
    }
  },
  methods: {
    getMenuPath (menu): string {
      if (menu.id === this.computedActiveMenu.id) {
        const path = menu.path
        if (!this.menuPath[menu.id]) {
          this.menuPath[menu.id] = path
        }
        return path
      } else {
        return this.menuPath[menu.id]
      }
    }
  },
  template: `
  <div class="page-container">
    <iframe
      :class="menu.id === computedActiveMenu.id ? 'active' : ''"
      class="animation-fade page-frame"
      v-for="menu in computedOpenMenuList"
      :src="getMenuPath(menu)"
      :key="menu.id"/>
  </div>
  `
}