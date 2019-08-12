import PageBuilder from '../../PageBuilder.js';
import Layout from '../layout/Layout.js';
import StoreUtil from '../../utils/StoreUtil.js';
import { STORE_KEYS } from '../../Constants.js';
import SidebarItem from '../layout/sidebar/SidebarItem.js';
const defaultTheme = {
    topColor: '#0b1c2f',
    menuTheme: 'dark',
    themeColor: '#46a0fc'
};
ready(function () {
    const home = new Home();
    busVue = home.initBus();
    home.init();
});
const debug = true;
class Home extends PageBuilder {
    initVue() {
        Vue.component('sidebar-item', SidebarItem);
        this.vue = new Vue({
            el: '#home-container',
            components: {
                'vue-main': Layout,
            }
        });
    }
    initBus() {
        return new Vue({
            data: {
                sidebar: {
                    opened: true,
                    withoutAnimation: false
                },
                device: 'Desktop',
                theme: StoreUtil.getStore(STORE_KEYS.THEME_KEY, debug) || defaultTheme,
                userMenuList: StoreUtil.getStore(STORE_KEYS.USER_MENU_LIST, debug) || [],
                activeTopMenu: StoreUtil.getStore(STORE_KEYS.ACTIVE_TOP_MENU, debug) || {},
                openMenuList: StoreUtil.getStore(STORE_KEYS.OPEN_MENU_LIST, debug) || [],
                activeMenu: StoreUtil.getStore(STORE_KEYS.ACTIVE_MENU, debug) || {}
            },
            methods: {
                setUserMenulist(userMenuList) {
                    this.userMenuList = userMenuList;
                    StoreUtil.setStore(STORE_KEYS.USER_MENU_LIST, userMenuList, StoreUtil.SESSION_TYPE);
                },
                setActiveTopMenu(menu) {
                    this.activeTopMenu = menu;
                    StoreUtil.setStore(STORE_KEYS.ACTIVE_TOP_MENU, menu, StoreUtil.SESSION_TYPE);
                },
                setActiveMenu(menu) {
                    this.activeMenu = menu;
                    StoreUtil.setStore(STORE_KEYS.ACTIVE_MENU, menu, StoreUtil.SESSION_TYPE);
                },
                addMenu(menu) {
                    return new Promise(() => {
                        this.setActiveMenu(menu);
                        const notHasMenu = this.openMenuList.every((value) => {
                            return value.path !== menu.path;
                        });
                        if (notHasMenu) {
                            this.openMenuList.push(menu);
                            StoreUtil.setStore(STORE_KEYS.OPEN_MENU_LIST, this.openMenuList, StoreUtil.SESSION_TYPE);
                        }
                    });
                },
                deleteMenuById(id) {
                    return new Promise((resolve, reject) => {
                        for (let i = 0; i < this.openMenuList.length; i++) {
                            const menu = this.openMenuList[i];
                            if (menu.id === id) {
                                this.openMenuList.splice(i, 1);
                                break;
                            }
                        }
                        StoreUtil.setStore(STORE_KEYS.OPEN_MENU_LIST, this.openMenuList, StoreUtil.SESSION_TYPE);
                        if (this.activeMenu.id === id) {
                            const activeMenu = this.openMenuList.slice(-1)[0];
                            this.setActiveMenu(activeMenu);
                        }
                    });
                },
                deleteMenu(menuPath) {
                    return new Promise((resolve, reject) => {
                        for (let i = 0; i < this.openMenuList.length; i++) {
                            const menu = this.openMenuList[i];
                            if (menu.path === menuPath) {
                                this.openMenuList.splice(i, 1);
                                break;
                            }
                        }
                        StoreUtil.setStore(STORE_KEYS.OPEN_MENU_LIST, this.openMenuList, StoreUtil.SESSION_TYPE);
                        if (this.activeMenu.path === menuPath) {
                            const activeMenu = this.openMenuList.slice(-1)[0];
                            this.setActiveMenu(activeMenu);
                        }
                    });
                },
                deleteOtherMenu(menu) {
                    return new Promise(() => {
                        this.openMenuList = [];
                        this.openMenuList.push(menu);
                        StoreUtil.setStore(STORE_KEYS.OPEN_MENU_LIST, this.openMenuList, StoreUtil.SESSION_TYPE);
                    });
                },
                deleteAllMenu() {
                    this.openMenuList = [];
                    StoreUtil.setStore(STORE_KEYS.OPEN_MENU_LIST, this.openMenuList, StoreUtil.SESSION_TYPE);
                }
            }
        });
    }
}
