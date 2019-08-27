import ConfigUtils from '../../utils/ConfigUtils.js';
import UsernameLogin from './UsernameLogin.js';
const pageToTop = () => {
    if (window.frames.length != parent.frames.length) {
        parent.location.href = window.location.href;
    }
};
export default {
    components: {
        'username-login': UsernameLogin
    },
    created() {
        pageToTop();
        ConfigUtils.loadConfig()
            .catch(error => {
            this.errorMessage(error.message, error);
        });
    }
};
