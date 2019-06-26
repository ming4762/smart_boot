define(["require", "exports", "layout/Layout", "utils/StoreUtil"], function (require, exports, Layout_1, StoreUtil_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    // 默认的样式
    var defaultTheme = {
        topColor: '#4391F4',
        menuTheme: 'dark',
        themeColor: '#46a0fc'
    };
    // 存储的key
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
    // 标识开发模式 TODO: 可配置
    var debug = true;
    var Home = /** @class */ (function () {
        function Home() {
        }
        /**
         * 初始化方法
         */
        Home.prototype.init = function () {
            this.initVue();
        };
        /**
         * 初始化vue
         */
        Home.prototype.initVue = function () {
            // @ts-ignore
            this.vue = new Vue({
                el: '#home-container',
                components: {
                    // @ts-ignore
                    'layout-vue': new Layout_1.default().build()
                }
            });
        };
        /**
         * 初始化消息总线
         */
        Home.prototype.initBus = function () {
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
                    theme: StoreUtil_1.default.getStore(STORE_KEYS.THEME_KEY, debug) || defaultTheme
                }
            });
        };
        return Home;
    }());
    exports.Home = Home;
});
