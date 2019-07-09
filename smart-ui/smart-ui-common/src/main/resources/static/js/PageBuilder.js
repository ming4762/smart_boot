define(["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    class PageBuilder {
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
    exports.default = PageBuilder;
});
