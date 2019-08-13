import UsernameLogin from './UsernameLogin.js';
import ApiService from '../../utils/ApiService.js';
import MessageMixins from '../../mixins/MessageMixins.js';
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
            },
            mixins: [MessageMixins],
            created() {
                ApiService.readLocalConfig()
                    .catch(error => {
                    this.errorMessage(error.message, error);
                });
            }
        });
    }
    static pageToTop() {
        if (window.frames.length != parent.frames.length) {
            parent.location.href = window.location.href;
        }
    }
}
