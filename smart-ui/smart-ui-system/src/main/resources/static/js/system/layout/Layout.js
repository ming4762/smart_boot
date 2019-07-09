define(["require", "exports", "utils/ApiService", "mixins/MessageMixins", "system/layout/navbar/Navbar", "system/layout/sidebar/SideBar", "system/layout/TagsView/TagsView", "system/layout/mainPage/PageContainer"], function (require, exports, ApiService_1, MessageMixins_1, Navbar_1, SideBar_1, TagsView_1, PageContainer_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.default = {
        components: {
            'navbar': Navbar_1.default,
            'SideBar': SideBar_1.default,
            'tags-view': TagsView_1.default,
            'page-container': PageContainer_1.default
        },
        mixins: [
            MessageMixins_1.default
        ],
        mounted() {
            this.loadUserMenu();
        },
        computed: {
            getBus() {
                return busVue;
            },
            getClassObj() {
                return {
                    hideSidebar: !this.getBus.sidebar.opened,
                    openSidebar: this.getBus.sidebar.opened,
                    withoutAnimation: this.getBus.sidebar.withoutAnimation,
                    mobile: this.getBus.device === 'Mobile'
                };
            }
        },
        methods: {
            loadUserMenu() {
                ApiService_1.default.postAjax('sys/menu/queryUserMenu', {})
                    .then(data => {
                    const resultList = [];
                    if (data !== null) {
                        const menuUrlMap = {};
                        this.dealMenuData(data, resultList, null, menuUrlMap);
                    }
                    this.getBus.setUserMenulist(resultList);
                }).catch(error => {
                    this.errorMessage('记载菜单数据失败，请刷新重试', error);
                });
            },
            dealMenuData(menuList, resultList, topId, menuUrlMap) {
                menuList.forEach(menu => {
                    let topIdNew = '';
                    topId === null ? topIdNew = menu.id : topIdNew = topId;
                    const url = this.getUrl(menu.object.url);
                    const dealMenu = {
                        id: menu.id,
                        name: menu.text,
                        icon: menu.object.icon,
                        path: url,
                        isCatalog: menu.object.functionId === null,
                        topId: topIdNew,
                        children: []
                    };
                    if (url !== null) {
                        menuUrlMap[url] = dealMenu;
                    }
                    resultList.push(dealMenu);
                    if (menu.children && menu.children.length > 0) {
                        this.dealMenuData(menu.children, dealMenu.children, topIdNew, menuUrlMap);
                    }
                });
            },
            getUrl(url) {
                if (url !== null) {
                    if (url.startsWith('/')) {
                        return url;
                    }
                    else if (url.startsWith('http')) {
                        return url;
                    }
                    else {
                        return '/' + url;
                    }
                }
                return null;
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
    };
});
