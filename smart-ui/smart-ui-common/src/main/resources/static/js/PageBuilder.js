export default class PageBuilder {
    init() {
        this.initVue();
    }
    build() {
        return {};
    }
    initVue() {
        this.vue = new Vue({
            el: '#vue-container',
            components: {
                'vue-main': this.build(),
            }
        });
    }
}
