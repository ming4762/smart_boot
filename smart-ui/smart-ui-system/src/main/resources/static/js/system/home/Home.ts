import Layout from 'layout/Layout'
// @ts-ignore
import StoreUtil from 'utils/StoreUtil'
// 默认的样式
const defaultTheme: any = {
  topColor: '#4391F4',
  menuTheme: 'dark',
  themeColor: '#46a0fc'
}
// 存储的key
const STORE_KEYS: any = {
  TOKEN_KEY: 'cloud_user_token',
  USER_PREMISSION: 'cloud_user_premission',
  USER_MENU_LIST: 'cloud_user_menulist',
  USER_KEY: 'cloud_current_user',
  OPEN_MENU_LIST: 'cloud_open_menu_list',
  THEME_KEY: 'smart_theme',
  MENU_URL_MAP_KEY: 'cloud_url_menu_map',
  ACTIVE_TOP_MENU: 'cloud_active_top_menu'
}
// 标识开发模式 TODO: 可配置
const debug = true
export class Home {

  private vue: any

  /**
   * 初始化方法
   */
  public init () {
    this.initVue()
  }

  /**
   * 初始化vue
   */
  private initVue () {
    // @ts-ignore
    this.vue = new Vue({
      el: '#home-container',
      components: {
        // @ts-ignore
        'layout-vue': new Layout().build()
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
        theme: StoreUtil.getStore(STORE_KEYS.THEME_KEY, debug) || defaultTheme
      }
    })
  }
}