define(["require", "exports", "system/layout/Layout", "utils/StoreUtil"], function (require, exports, Layout_1, StoreUtil_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var defaultTheme = {
        topColor: '#4391F4',
        menuTheme: 'dark',
        themeColor: '#46a0fc'
    };
    var STORE_KEYS = {
        TOKEN_KEY: 'cloud_user_token',
        USER_PREMISSION: 'cloud_user_premission',
        USER_MENU_LIST: 'cloud_user_menulist',
        USER_KEY: 'cloud_current_user',
        OPEN_MENU_LIST: 'cloud_open_menu_list',
        THEME_KEY: 'smart_theme',
        MENU_URL_MAP_KEY: 'cloud_url_menu_map',
        ACTIVE_TOP_MENU: 'cloud_active_top_menu'
    };
    var debug = true;
    var Home = (function () {
        function Home() {
        }
        Home.prototype.init = function () {
            this.initVue();
        };
        Home.prototype.initVue = function () {
            this.vue = new Vue({
                el: '#home-container',
                components: {
                    'layout-vue': new Layout_1.default().build()
                }
            });
        };
        Home.prototype.initBus = function () {
            return new Vue({
                data: {
                    sidebar: {
                        opened: true,
                        withoutAnimation: false
                    },
                    device: 'Desktop',
                    theme: StoreUtil_1.default.getStore(STORE_KEYS.THEME_KEY, debug) || defaultTheme
                }
            });
        };
        return Home;
    }());
    exports.Home = Home;
});
