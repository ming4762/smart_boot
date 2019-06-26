define(["require", "exports", "./UsernameLogin"], function (require, exports, UsernameLogin_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    /**
     * 页面加载完毕执行
     */
    var Login = /** @class */ (function () {
        function Login() {
        }
        /**
         * 初始化函数
         */
        Login.prototype.init = function () {
            this.initVue();
        };
        /**
         * 初始化vue
         */
        Login.prototype.initVue = function () {
            this.vue = new Vue({
                el: '#login-container',
                components: {
                    // @ts-ignore
                    'username-login': new UsernameLogin_1.default().build()
                }
            });
        };
        return Login;
    }());
    exports.Login = Login;
});
