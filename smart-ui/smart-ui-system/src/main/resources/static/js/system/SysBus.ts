// @ts-ignore
import StoreUtil from '../utils/StoreUtil.js'
// @ts-ignore
import { STORE_KEYS } from '../Constants.js'

// 标识开发模式 TODO: 可配置
const debug = true

// 默认的样式
const defaultTheme: any = {
  topColor: '#0b1c2f',
  menuTheme: 'dark',
  themeColor: '#46a0fc'
}
// 侧边栏打开的宽度
const openWidth = 1000

const initBus = () => {
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
      activeMenu: StoreUtil.getStore(STORE_KEYS.ACTIVE_MENU, debug) || {},
      // 转换的人员菜单列表
      convertUserMenuList: []
    },
    methods: {
      // 页面宽度变化
      handleWidthChange () {
        const width = document.body.offsetWidth
        this.hideOpenSidebar(width)
        this.convertTopMenuByWidth()
      },
      hideOpenSidebar (width) {
        this.sidebar.opened = width > openWidth
      },
      /**
       * 设置
       */
      convertTopMenuByWidth () {
        // 获取顶部宽度
        const width = this.getTopMenuAvailableWidth()
        //
        const oneWidth = 130
        const num = parseInt(width / oneWidth + '')
        if (this.userMenuList.length <= num) {
          this.convertUserMenuList = this.userMenuList
        } else {
          const convertUserMenuList = []
          const moreChildren = num === 0 ? this.userMenuList : this.userMenuList.slice(num - 1, this.userMenuList.length)
          if (num > 0) {
            convertUserMenuList.push(...this.userMenuList.slice(0, num - 1))
          }
          convertUserMenuList.push({
            id: 'more',
            name: '更多',
            icon: 'el-icon-more',
            isCatalog: true,
            topId: 'more',
            children: moreChildren
          })
          this.convertUserMenuList = convertUserMenuList
        }
      },
      getTopMenuAvailableWidth () {
        // 减去顶部按钮宽度
        const width = document.body.offsetWidth - (227 + 35)
        // 侧边栏宽度
        const sibarWidth = this.sidebar.opened ? 180 : 36
        // 40是切换按钮长度
        return width - sibarWidth - 40
      },
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
      setActiveTopMenu (menu?: any) {
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
        this.dealActionTopMenu()
      },
      /**
       * 获取顶级菜单
       */
      dealActionTopMenu () {
        const activeTopId = this.activeMenu.topId
        if (activeTopId) {
          if (this.activeTopMenu == null || this.activeTopMenu.id !== activeTopId) {
            for (let i in this.userMenuList) {
              if (activeTopId === this.userMenuList[i].topId) {
                this.setActiveTopMenu(this.userMenuList[i])
                break
              }
            }
          }
        } else {
          this.setActiveTopMenu(null)
        }
      },
      /**
       * 添加菜单
       * @param menu
       */
      addMenu (menu: any, active: boolean): Promise<any> {
        return new Promise(() => {
          if (active !== false) {
            // 设置激活的菜单
            this.setActiveMenu(menu)
          }
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
       * 通过ID删除菜单
       * @param id
       */
      deleteMenuById (id: string): Promise<any> {
        return new Promise<any>((resolve, reject) => {
          for (let i=0; i<this.openMenuList.length; i++) {
            const menu = this.openMenuList[i]
            if (menu.id === id) {
              this.openMenuList.splice(i, 1)
              break
            }
          }
          StoreUtil.setStore(STORE_KEYS.OPEN_MENU_LIST, this.openMenuList, StoreUtil.SESSION_TYPE)
          // 如果关闭的是激活的菜单，则设置下一个激活的菜单
          if (this.activeMenu.id === id) {
            const activeMenu = this.openMenuList.slice(-1)[0]
            // 设置激活的菜单 TODO: 判断activeMenu是否存在
            this.setActiveMenu(activeMenu)
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

export default initBus