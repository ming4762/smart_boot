import Link from 'system/layout/sidebar/Link'
import Item from 'system/layout/sidebar/Item'
// @ts-ignore 样式混入
import ThemeMixins from 'mixins/ThemeMixins'
declare var busVue

export default {

  components: {
    'app-link': Link,
    'menu-item': Item
  },
  mixins: [
    ThemeMixins
  ],
  props: {
    item: {
      required: true
    },
    isNest: {
      type: Boolean,
      default: false
    }

  },
  computed: {
    /**
     * 获取激活的菜单列表
     */
    computedActiveIndex () {
      const activeMenu = this.getBus.activeMenu
      return activeMenu ? activeMenu.id : ''
    },
    getBus (): any {
      return busVue
    }
  },
  methods: {
    handleOpenMenu (menu: any) {
      this.getBus.addMenu(menu)
    },
    /**
     * 显示隐藏菜单提示
     * @param menu
     * @param status
     */
    handleShowTip (menu: any, status: boolean) {
      if (!this.getBus.sidebar.opened) {
        if (this.$refs[menu.id + 'tip']) {
          this.$refs[menu.id + 'tip'].showPopper = status
        }
      }
    }
  },
  template: `
  <div class="menu-wrapper">
    <template v-if="!item.isCatalog">
      <app-link
        @mouseenter.native="handleShowTip(item, true)"
        @mouseleave.native="handleShowTip(item, false)"
        @click.native="handleOpenMenu(item)"
        :to="item.path">
        <el-tooltip :ref="item.id + 'tip'" manual class="item" effect="dark" :content="item.name" placement="right">
          <el-menu-item :index="item.id" :class="{'submenu-title-noDropdown': !isNest}">
            <menu-item
              :color="getSideBarTheme['text-color']"
              :activeColor="getSideBarTheme['active-text-color']"
              :icon="item.icon"
              :active="computedActiveIndex === item.id"
              :title="item.name"/>
          </el-menu-item>
        </el-tooltip>
      </app-link>
    </template>
    <el-submenu v-else :index="item.id">
      <template slot="title">
        <menu-item
          :color="getSideBarTheme['text-color']"
          :activeColor="getSideBarTheme['active-text-color']"
          :icon="item.icon"
          :active="computedActiveIndex === item.id"
          :title="item.name"/>
      </template>
      <template
        v-for="child in item.children">
        <sidebar-item
          class="nest-menu"
          :is-nest="true"
          :item="child"
          :key="child.id"></sidebar-item>
      </template>
    </el-submenu>
  </div>
  `
}