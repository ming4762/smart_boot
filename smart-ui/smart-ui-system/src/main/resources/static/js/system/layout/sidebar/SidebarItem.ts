import Link from 'system/layout/sidebar/Link'
import Item from 'system/layout/sidebar/Item'
// @ts-ignore 样式混入
import ThemeMixins from 'mixins/ThemeMixins'

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
      return this.$parent.activeIndex
    }
  },
  methods: {
    handleOpenMenu (menu: any) {
      this.getBus.addMenu(menu)
    }
  },
  template: `
  <div class="menu-wrapper">
    <template v-if="!item.isCatalog">
      <app-link
        @click.native="handleOpenMenu(item)"
        :to="item.path">
        <el-tooltip :ref="item.id + 'tip'" manual class="item" effect="dark" :content="item.name" placement="right">
          <el-menu-item :index="item.id" :class="{'submenu-title-noDropdown': !isNest}">
            <menu-item
              :color="getTopTextColor"
              :activeColor="topActiveTextColor"
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
          :color="getTopTextColor"
          :activeColor="topActiveTextColor"
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