declare var busVue, indexPage
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
    this.addSetIndexPage()
  },
  data () {
    return {
      // 是否设置了主页
      isSetIndexPage: indexPage !== ""
    }
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
    addSetIndexPage () {
      if (this.isSetIndexPage) {
        this.getBus.addMenu({
          name: '首页',
          path: indexPage
        })
      }
    },
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
              // 如果为设置主页，则菜单的第一个页面作为菜单
              if (!this.isSetIndexPage) {
                this.setIndexPageFromMenuList(resultList)
              }
              console.log(resultList)
              // 设置菜单
              this.getBus.setUserMenulist(resultList)
            }).catch(error => {
              this.errorMessage('加载菜单数据失败，请刷新重试', error)
            })
      }
    },
    /**
     * 从菜单列表中设置第一个菜单
     * @param menuList
     */
    setIndexPageFromMenuList (menuList) {
      new Promise(() => {
        const firstMenu = this.getFirstMenu(menuList)
        if (firstMenu) {
          this.getBus.addMenu(firstMenu)
        }
      })
    },
    /**
     * 使用递归获取第一个菜单
     * @param menuList
     */
    getFirstMenu (menuList) {
      const menu = menuList[0]
      if (!menu.children || menu.children.length === 0) {
        return menu
      } else {
        return this.getFirstMenu(menu.children)
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
