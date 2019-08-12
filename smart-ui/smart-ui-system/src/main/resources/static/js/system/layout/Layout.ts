declare var busVue
// @ts-ignore
import ApiService from '../../utils/ApiService.js'
// @ts-ignore
import MessageMixins from '../../mixins/MessageMixins.js'

import Navbar from './navbar/Navbar.js'
import SideBar from './sidebar/SideBar.js'
import TagsView from './TagsView/TagsView.js'

import PageContainer from './mainPage/PageContainer.js'

export default {
  components: {
    'navbar': Navbar,
    'SideBar': SideBar,
    'tags-view': TagsView,
    'page-container': PageContainer
  },
  mixins: [
    MessageMixins
  ],
  mounted () {
    this.loadUserMenu()
  },
  computed: {
    getBus () {
      return busVue
    },
    getClassObj () {
      return {
        hideSidebar: !this.getBus.sidebar.opened,
        openSidebar: this.getBus.sidebar.opened,
        withoutAnimation: this.getBus.sidebar.withoutAnimation,
        mobile: this.getBus.device === 'Mobile'
      }
    }
  },
  methods: {
    // 加载用户菜单信息
    loadUserMenu () {
      if (!this.getBus.userMenuList || this.getBus.userMenuList.length === 0) {
        ApiService.postAjax('sys/menu/queryUserMenu', {})
            .then(data => {
              const resultList = []
              if (data !== null) {
                const menuUrlMap = {}
                this.dealMenuData(data, resultList, null, menuUrlMap)
              }
              // 设置菜单
              this.getBus.setUserMenulist(resultList)
            }).catch(error => {
              this.errorMessage('记载菜单数据失败，请刷新重试', error)
            })
      }
    },
    /**
     * 使用递归处理菜单数据
     */
    dealMenuData (menuList: any[], resultList: any[], topId: string, menuUrlMap: any) {
      menuList.forEach(menu => {
        let topIdNew: string = ''
        topId === null ? topIdNew = menu.id : topIdNew = topId
        // 获取url
        const url = this.getUrl(menu.object.url)
        const dealMenu: any = {
          id: menu.id,
          name: menu.text,
          icon: menu.object.icon,
          path: url,
          isCatalog: menu.object.functionId === null,
          topId: topIdNew,
          children: []
        }
        if (url !== null) {
          menuUrlMap[url] = dealMenu
        }
        resultList.push(dealMenu)
        if (menu.children && menu.children.length > 0) {
          this.dealMenuData(menu.children, dealMenu.children, topIdNew, menuUrlMap)
        }
      })
    },
    /**
     * 获取url
     * @param url
     */
    getUrl (url: string): string {
      if (url !== null) {
        if (url.startsWith('/')) {
          return url
        } else if (url.startsWith('http')) {
          return url
        } else {
          return '/' + url
        }
      }
      return null
    }
  },
  template: `
  <div :class="getClassObj" class="full-height app-wrapper">
    <!--侧边栏组件-->
    <SideBar class="sidebar-container"/>
    <div class="main-container full-height main-outer">
      <!--顶部-->
      <div class="top-div">
        <!--顶部菜单-->
        <navbar/>
        <!--打开的菜单列表-->
        <tags-view/>
      </div>
      <!--页面主体-->
      <section class="app-main">
        <page-container/>
      </section>
    </div>
  </div>
  `
}
