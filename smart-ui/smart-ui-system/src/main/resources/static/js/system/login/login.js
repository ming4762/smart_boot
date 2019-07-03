define(["require", "exports", "./UsernameLogin"], function (require, exports, UsernameLogin_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var Login = (function () {
        function Login() {
        }
        Login.prototype.init = function () {
            this.initVue();
        };
        Login.prototype.initVue = function () {
            this.vue = new Vue({
                el: '#login-container',
                components: {
                    'username-login': UsernameLogin_1.default
                }
            });
        };
        return Login;
    }());
    exports.Login = Login;
});
