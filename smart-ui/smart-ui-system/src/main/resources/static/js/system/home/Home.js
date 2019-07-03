define(["require", "exports", "system/layout/Layout", "utils/StoreUtil", "Constants"], function (require, exports, Layout_1, StoreUtil_1, Constants_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var defaultTheme = {
        topColor: '#4391F4',
        menuTheme: 'dark',
        themeColor: '#46a0fc'
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
                    theme: StoreUtil_1.default.getStore(Constants_1.STORE_KEYS.THEME_KEY, debug) || defaultTheme
                }
            });
        };
        return Home;
    }());
    exports.Home = Home;
});
