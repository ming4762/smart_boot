import PageBuilder from '../../PageBuilder.js';
import Layout from '../layout/Layout.js';
import SidebarItem from '../layout/sidebar/SidebarItem.js';
import initBus from '../SysBus.js';
ready(function () {
    const home = new Home();
    busVue = initBus();
    home.init();
});
class Home extends PageBuilder {
    initVue() {
        Vue.component('sidebar-item', SidebarItem);
        this.vue = new Vue({
            el: '#home-container',
            components: {
                'vue-main': Layout,
            }
        });
    }
}
