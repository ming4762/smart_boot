// @ts-ignore
import PageBuilder from 'PageBuilder'
import Layout from 'system/layout/Layout'
// @ts-ignore
import StoreUtil from 'utils/StoreUtil'
// @ts-ignore
import { STORE_KEYS } from 'Constants'
import SidebarItem from 'system/layout/sidebar/SidebarItem'
// 默认的样式
const defaultTheme: any = {
  topColor: '#4391F4',
  menuTheme: 'dark',
  themeColor: '#46a0fc'
}

// 标识开发模式 TODO: 可配置
const debug = true
export class Home extends PageBuilder {

  /**
   * 初始化vue
   */
  protected initVue () {
    // @ts-ignore
    Vue.component('sidebar-item', SidebarItem)
    // @ts-ignore
    this.vue = new Vue({
      el: '#home-container',
      components: {
        // @ts-ignore
        'vue-main': Layout,
      }
    })
  }

  /**
   * 初始化消息总线
   */
  public initBus (): any {
    // @ts-ignore
    return new Vue({
      data: {
        // 侧边栏
        sidebar: {
          opened: true,
          withoutAnimation: false
        },
        // 设备类型
        device: 'Desktop',
        // 主题信息
        theme: StoreUtil.getStore(STORE_KEYS.THEME_KEY, debug) || defaultTheme,
        // 用户菜单信息
        userMenuList: StoreUtil.getStore(STORE_KEYS.USER_MENU_LIST, debug) || [],
        // 激活的顶部菜单
        activeTopMenu: StoreUtil.getStore(STORE_KEYS.ACTIVE_TOP_MENU, debug) || {},
        // 打开的菜单列表
        openMenuList: StoreUtil.getStore(STORE_KEYS.OPEN_MENU_LIST, debug) || [],
        // 激活的菜单
        activeMenu: StoreUtil.getStore(STORE_KEYS.ACTIVE_MENU, debug) || {}
      },
      methods: {
        /**
         * 设置用户菜单列表
         * @param userMenuList
         */
        setUserMenulist (userMenuList: any[]) {
          this.userMenuList = userMenuList
          StoreUtil.setStore(STORE_KEYS.USER_MENU_LIST, userMenuList, StoreUtil.SESSION_TYPE)
        },
        /**
         * 设置激活的顶部菜单
         * @param menu
         */
        setActiveTopMenu (menu: any) {
          console.log('------------')
          this.activeTopMenu = menu
          StoreUtil.setStore(STORE_KEYS.ACTIVE_TOP_MENU, menu, StoreUtil.SESSION_TYPE)
        },
        /**
         * 设置激活的菜单
         * @param menu
         */
        setActiveMenu (menu: any) {
          this.activeMenu = menu
          StoreUtil.setStore(STORE_KEYS.ACTIVE_MENU, menu, StoreUtil.SESSION_TYPE)
        },
        /**
         * 添加菜单
         * @param menu
         */
        addMenu (menu: any): Promise<any> {
          return new Promise(() => {
            // 设置激活的菜单
            this.setActiveMenu(menu)
            // 判断菜单是否已经存在
            const notHasMenu: boolean = this.openMenuList.every((value) => {
              return value.path !== menu.path;
            })
            if (notHasMenu) {
              this.openMenuList.push(menu)
              StoreUtil.setStore(STORE_KEYS.OPEN_MENU_LIST, this.openMenuList, StoreUtil.SESSION_TYPE)
            }
          })
        },
        /**
         * 删除菜单
         * @param menuPath
         */
        deleteMenu (menuPath: string): Promise<any> {
          return new Promise<any>((resolve, reject) => {
            for (let i=0; i<this.openMenuList.length; i++) {
              const menu = this.openMenuList[i]
              if (menu.path === menuPath) {
                this.openMenuList.splice(i, 1)
                break
              }
            }
            StoreUtil.setStore(STORE_KEYS.OPEN_MENU_LIST, this.openMenuList, StoreUtil.SESSION_TYPE)
            // 如果关闭的是激活的菜单，则设置下一个激活的菜单
            if (this.activeMenu.path === menuPath) {
              const activeMenu = this.openMenuList.slice(-1)[0]
              // 设置激活的菜单 TODO: 判断activeMenu是否存在
              this.setActiveMenu(activeMenu)
            }
          })
        },
        /**
         * 删除其他菜单
         * @param menu
         */
        deleteOtherMenu (menu: any): Promise<any> {
          return new Promise<any>(() => {
            this.openMenuList = []
            this.openMenuList.push(menu)
            StoreUtil.setStore(STORE_KEYS.OPEN_MENU_LIST, this.openMenuList, StoreUtil.SESSION_TYPE)
          })
        },
        /**
         * 删除所有菜单
         */
        deleteAllMenu () {
          this.openMenuList = []
          StoreUtil.setStore(STORE_KEYS.OPEN_MENU_LIST, this.openMenuList, StoreUtil.SESSION_TYPE)
        }
      }
    })
  }
}