define(["require", "exports", "./UsernameLogin"], function (require, exports, UsernameLogin_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    class Login {
        init() {
            Login.pageToTop();
            this.initVue();
        }
        initVue() {
            this.vue = new Vue({
                el: '#login-container',
                components: {
                    'username-login': UsernameLogin_1.default
                }
            });
        }
        static pageToTop() {
            if (window.frames.length != parent.frames.length) {
                parent.location.href = window.location.href;
            }
        }
    }
    exports.Login = Login;
});
