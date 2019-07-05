define(["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var PageBuilder = (function () {
        function PageBuilder() {
        }
        PageBuilder.prototype.init = function () {
            this.initVue();
        };
        PageBuilder.prototype.build = function () {
            return {};
        };
        PageBuilder.prototype.initVue = function () {
            this.vue = new Vue({
                el: '#vue-container',
                components: {
                    'vue-main': this.build(),
                }
            });
        };
        return PageBuilder;
    }());
    exports.default = PageBuilder;
});
