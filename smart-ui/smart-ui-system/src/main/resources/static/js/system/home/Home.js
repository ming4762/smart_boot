define(["require", "exports", "PageBuilder", "system/layout/Layout", "utils/StoreUtil", "Constants", "system/layout/sidebar/SidebarItem"], function (require, exports, PageBuilder_1, Layout_1, StoreUtil_1, Constants_1, SidebarItem_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    const defaultTheme = {
        topColor: '#0b1c2f',
        menuTheme: 'dark',
        themeColor: '#46a0fc'
    };
    const debug = true;
    class Home extends PageBuilder_1.default {
        initVue() {
            Vue.component('sidebar-item', SidebarItem_1.default);
            this.vue = new Vue({
                el: '#home-container',
                components: {
                    'vue-main': Layout_1.default,
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
                    theme: StoreUtil_1.default.getStore(Constants_1.STORE_KEYS.THEME_KEY, debug) || defaultTheme,
                    userMenuList: StoreUtil_1.default.getStore(Constants_1.STORE_KEYS.USER_MENU_LIST, debug) || [],
                    activeTopMenu: StoreUtil_1.default.getStore(Constants_1.STORE_KEYS.ACTIVE_TOP_MENU, debug) || {},
                    openMenuList: StoreUtil_1.default.getStore(Constants_1.STORE_KEYS.OPEN_MENU_LIST, debug) || [],
                    activeMenu: StoreUtil_1.default.getStore(Constants_1.STORE_KEYS.ACTIVE_MENU, debug) || {}
                },
                methods: {
                    setUserMenulist(userMenuList) {
                        this.userMenuList = userMenuList;
                        StoreUtil_1.default.setStore(Constants_1.STORE_KEYS.USER_MENU_LIST, userMenuList, StoreUtil_1.default.SESSION_TYPE);
                    },
                    setActiveTopMenu(menu) {
                        this.activeTopMenu = menu;
                        StoreUtil_1.default.setStore(Constants_1.STORE_KEYS.ACTIVE_TOP_MENU, menu, StoreUtil_1.default.SESSION_TYPE);
                    },
                    setActiveMenu(menu) {
                        this.activeMenu = menu;
                        StoreUtil_1.default.setStore(Constants_1.STORE_KEYS.ACTIVE_MENU, menu, StoreUtil_1.default.SESSION_TYPE);
                    },
                    addMenu(menu) {
                        return new Promise(() => {
                            this.setActiveMenu(menu);
                            const notHasMenu = this.openMenuList.every((value) => {
                                return value.path !== menu.path;
                            });
                            if (notHasMenu) {
                                this.openMenuList.push(menu);
                                StoreUtil_1.default.setStore(Constants_1.STORE_KEYS.OPEN_MENU_LIST, this.openMenuList, StoreUtil_1.default.SESSION_TYPE);
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
                            StoreUtil_1.default.setStore(Constants_1.STORE_KEYS.OPEN_MENU_LIST, this.openMenuList, StoreUtil_1.default.SESSION_TYPE);
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
                            StoreUtil_1.default.setStore(Constants_1.STORE_KEYS.OPEN_MENU_LIST, this.openMenuList, StoreUtil_1.default.SESSION_TYPE);
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
                            StoreUtil_1.default.setStore(Constants_1.STORE_KEYS.OPEN_MENU_LIST, this.openMenuList, StoreUtil_1.default.SESSION_TYPE);
                        });
                    },
                    deleteAllMenu() {
                        this.openMenuList = [];
                        StoreUtil_1.default.setStore(Constants_1.STORE_KEYS.OPEN_MENU_LIST, this.openMenuList, StoreUtil_1.default.SESSION_TYPE);
                    }
                }
            });
        }
    }
    exports.Home = Home;
});
