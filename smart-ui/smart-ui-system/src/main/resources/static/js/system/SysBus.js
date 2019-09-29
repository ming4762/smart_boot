import StoreUtil from '../utils/StoreUtil.js';
import { STORE_KEYS } from '../Constants.js';
const debug = true;
const defaultTheme = {
    topColor: '#0b1c2f',
    menuTheme: 'dark',
    themeColor: '#46a0fc'
};
const openWidth = 1000;
const initBus = () => {
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
            activeMenu: StoreUtil.getStore(STORE_KEYS.ACTIVE_MENU, debug) || {},
            convertUserMenuList: []
        },
        watch: {
            "userMenuList.length"() {
                this.convertTopMenuByWidth();
            }
        },
        methods: {
            handleWidthChange() {
                const width = document.body.offsetWidth;
                this.hideOpenSidebar(width);
                this.convertTopMenuByWidth();
            },
            hideOpenSidebar(width) {
                this.sidebar.opened = width > openWidth;
            },
            convertTopMenuByWidth() {
                const width = this.getTopMenuAvailableWidth();
                const oneWidth = 120;
                const num = parseInt(width / oneWidth + '');
                if (this.userMenuList.length <= num) {
                    this.convertUserMenuList = this.userMenuList;
                }
                else {
                    const convertUserMenuList = [];
                    const moreChildren = num === 0 ? this.userMenuList : this.userMenuList.slice(num - 1, this.userMenuList.length);
                    if (num > 0) {
                        convertUserMenuList.push(...this.userMenuList.slice(0, num - 1));
                    }
                    convertUserMenuList.push({
                        id: 'more',
                        name: '更多',
                        icon: 'el-icon-more',
                        isCatalog: true,
                        topId: 'more',
                        children: moreChildren
                    });
                    this.convertUserMenuList = convertUserMenuList;
                }
            },
            getTopMenuAvailableWidth() {
                const width = document.body.offsetWidth - (227 + 35);
                const sibarWidth = this.sidebar.opened ? 180 : 36;
                return width - sibarWidth - 40;
            },
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
                this.dealActionTopMenu();
            },
            dealActionTopMenu() {
                const activeTopId = this.activeMenu.topId;
                if (activeTopId) {
                    if (this.activeTopMenu == null || this.activeTopMenu.id !== activeTopId) {
                        for (let i in this.userMenuList) {
                            if (activeTopId === this.userMenuList[i].topId) {
                                this.setActiveTopMenu(this.userMenuList[i]);
                                break;
                            }
                        }
                    }
                }
                else {
                    this.setActiveTopMenu(null);
                }
            },
            addMenu(menu, active) {
                return new Promise(() => {
                    if (active !== false) {
                        this.setActiveMenu(menu);
                    }
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
};
export default initBus;
