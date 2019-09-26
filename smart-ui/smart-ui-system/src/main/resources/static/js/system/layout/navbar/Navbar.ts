// 样式混入
// @ts-ignore
import ThemeMixins from '../../../mixins/ThemeMixins.js'

import Item from '../sidebar/Item.js'

import NavbarButton from './NavbarButton.js'


export default {
  components: {
    'menu-item': Item,
    'navbar-button': NavbarButton
  },
  data () {
    return {
      // 激活菜单的标识 TODO:废弃
      activeIndex: '',
      // sidebar 对象
      sidebar: {},
    }
  },
  mixins: [
    ThemeMixins
  ],
  computed: {
    // 获取按钮激活状态
    getIsActive () {
      return this.getBus.sidebar.opened === true
    },
    getBus (): any {
      // @ts-ignore
      return busVue
    },
    // 获取样式
    computedStyle (): string {
      return `fill: ${this.getTopTextColor}`
    },
    // 获取顶部样式
    getTopDivStyle (): string {
      return 'background-color:' + this.getBus.theme.topColor
    },
    /**
     * 获取用户菜单列表
     */
    computedUserMenuList (): any[] {
      return this.getBus.convertUserMenuList
    },
    /**
     * 获取激活的菜单
     */
    computedActiveMenu (): any {
      return this.getBus.activeMenu
    },
    /**
     * 普通菜单计算属性
     */
    computedNormalMenu () {
      const lastMenu = this.computedUserMenuList[this.computedUserMenuList.length - 1]
      if (lastMenu.id === 'more') {
        return this.computedUserMenuList.splice(0, this.computedUserMenuList.length - 1)
      } else {
        return this.computedUserMenuList
      }
    },
    computedMoreMenu () {
      const lastMenu = this.computedUserMenuList[this.computedUserMenuList.length - 1]
      if (lastMenu.id === 'more') {
        return lastMenu
      }
      return null
    }
  },
  watch: {
    'computedActiveMenu': function (_new, old) {
      if (_new && _new.topId !== old.topId) {
        // 获取当前激活的顶级菜单
        this.setActiveTopMenu()
      }
    }
  },
  mounted () {
    // console.log(this)
    // this.setActiveTopMenu()
  },
  methods: {
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
    },
    /**
     * 打开菜单
     * @param menu
     */
    handleOpenMenu (menu: any) {
      if (menu.id !== 'more') {
        // 设置激活的顶部菜单
        this.getBus.setActiveTopMenu(menu)
        // 如果是菜单直接打开
        if (!menu.isCatalog) {
          this.getBus.addMenu(menu)
        }
      }
    },
    /**
     * 获取默认的顶级菜单ID
     */
    getDefaultTopMenuId () {
      if (this.computedUserMenuList.length > 0) {
        const menu = this.computedUserMenuList[0]
        if (menu) {
          return menu.topId
        }
      }
      return ''
    },
    /**
     * 获取激活的顶部菜单
     */
    setActiveTopMenu (): any {
      const activeTopMenu = this.getBus.activeTopMenu
      const activeTopId = this.computedActiveMenu.topId
      if (activeTopMenu.id !== activeTopId) {
        const userMenuList = this.getBus.userMenuList
        for (let i in userMenuList) {
          if (activeTopId === userMenuList[i].topId) {
            this.getBus.setActiveTopMenu(userMenuList[i])
            break
          }
        }
      }
    },
    /**
     * 判断菜单是否激活
     */
    isActive (menuId: string) {
      if (!this.computedActiveMenu.topId) {
        return false
      } else {
        return this.getBus.activeTopMenu.id === menuId
      }
    }
  },
  template: `
  <div :style="getTopDivStyle" class="navbar-outer-a">
    <el-menu
      menu-trigger="click"
      ref="topMenu"
      :default-active="computedActiveMenu.topId"
      :background-color="getTopColor"
      :text-color="getTopTextColor"
      :active-text-color="topActiveTextColor"
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
      <!--遍历菜单-->
      <el-menu-item
        :key="menu.id"
        :index="menu.id"
        @click="handleOpenMenu(menu)"
        v-for="menu in computedNormalMenu">
        <menu-item
          :color="getTopTextColor"
          :active="isActive(menu.id)"
          :icon="menu.icon"
          :activeColor="topActiveTextColor"
          :title="menu.name"/>
      </el-menu-item>
      <el-submenu v-if="computedMoreMenu !== null" index="computedMoreMenu.id">
        <template slot="title">
          <!-- TODO: 激活状态待处理-->
          <menu-item
            :color="getTopTextColor"
            :active="isActive(computedMoreMenu.id)"
            :icon="computedMoreMenu.icon"
            :activeColor="topActiveTextColor"
            :title="computedMoreMenu.name"/>
          <!--<i class="el-icon-location"></i>-->
          <!--<span>{{computedMoreMenu.name}}</span>-->
        </template>
        <el-menu-item 
          :key="menu.id"
          @click="handleOpenMenu(menu)"
          v-for="menu in computedMoreMenu.children"
          :index="menu.id">
          <menu-item
            :color="getTopTextColor"
            :active="isActive(menu.id)"
            :icon="menu.icon"
            :activeColor="topActiveTextColor"
            :title="menu.name"/>
        </el-menu-item>
      </el-submenu>
    </el-menu>
    <!--顶部按钮列-->
    <navbar-button class="navbar-button-container"/>
  </div>
  `
}
