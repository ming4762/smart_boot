var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
        return extendStatics(d, b);
    }
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
define(["require", "exports", "ComponentBuilder"], function (require, exports, ComponentBuilder_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var PageBuilder = (function (_super) {
        __extends(PageBuilder, _super);
        function PageBuilder() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        PageBuilder.prototype.init = function () {
            this.initVue();
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
    }(ComponentBuilder_1.default));
    exports.default = PageBuilder;
});
