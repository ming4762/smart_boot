import UsernameLogin from './UsernameLogin.js';
import ConfigUtils from '../../utils/ConfigUtils.js';
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
                ConfigUtils.loadConfig()
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
