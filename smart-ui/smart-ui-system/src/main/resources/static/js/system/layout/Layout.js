import ApiService from '../../utils/ApiService.js';
import ResizeHandler from '../../utils/ResizeHandler.js';
import MessageMixins from '../../mixins/MessageMixins.js';
import Navbar from './navbar/Navbar.js';
import SideBar from './sidebar/SideBar.js';
import TagsView from './TagsView/TagsView.js';
import PageContainer from './mainPage/PageContainer.js';
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
    created() {
        this.bindWidthChange();
        this.getBus.handleWidthChange();
    },
    mounted() {
        this.loadUserMenu();
        this.addSetIndexPage();
    },
    data() {
        return {
            isSetIndexPage: indexPage !== ""
        };
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
        bindWidthChange() {
            ResizeHandler.bind(() => {
                this.getBus.handleWidthChange();
            });
        },
        addSetIndexPage() {
            if (this.isSetIndexPage) {
                this.getBus.addMenu({
                    name: '首页',
                    path: indexPage
                });
            }
        },
        loadUserMenu() {
            if (!this.getBus.userMenuList || this.getBus.userMenuList.length === 0) {
                ApiService.postAjax('sys/menu/queryUserMenu', {})
                    .then(data => {
                    const resultList = [];
                    if (data !== null) {
                        const menuUrlMap = {};
                        this.dealMenuData(data, resultList, null, menuUrlMap);
                    }
                    if (!this.isSetIndexPage) {
                        this.setIndexPageFromMenuList(resultList);
                    }
                    console.log(resultList);
                    this.getBus.setUserMenulist(resultList);
                }).catch(error => {
                    this.errorMessage('加载菜单数据失败，请刷新重试', error);
                });
            }
        },
        setIndexPageFromMenuList(menuList) {
            new Promise(() => {
                const firstMenu = this.getFirstMenu(menuList);
                if (firstMenu) {
                    this.getBus.addMenu(firstMenu);
                }
            });
        },
        getFirstMenu(menuList) {
            const menu = menuList[0];
            if (!menu.children || menu.children.length === 0) {
                return menu;
            }
            else {
                return this.getFirstMenu(menu.children);
            }
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
