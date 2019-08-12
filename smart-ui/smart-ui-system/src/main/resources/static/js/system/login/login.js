import UsernameLogin from './UsernameLogin.js';
ready(function () {
    new Login().init();
});
export class Login {
    init() {
        Login.pageToTop();
        this.initVue();
    }
    initVue() {
        this.vue = new Vue({
            el: '#login-container',
            components: {
                'username-login': UsernameLogin
            }
        });
    }
    static pageToTop() {
        if (window.frames.length != parent.frames.length) {
            parent.location.href = window.location.href;
        }
    }
}
