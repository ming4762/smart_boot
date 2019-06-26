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
define(["require", "exports", "ComponentBuilder", "mixins/ThemeMixins"], function (require, exports, ComponentBuilder_1, ThemeMixins_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    /**
     * 顶部组件
     */
    var Navbar = /** @class */ (function (_super) {
        __extends(Navbar, _super);
        function Navbar() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        /**
         * VUE数据
         */
        Navbar.prototype.data = function () {
            return {
                // 激活菜单的标识
                activeIndex: '',
                // sidebar 对象
                sidebar: {}
            };
        };
        /**
         * 混入
         */
        Navbar.prototype.mixins = function () {
            return [
                // @ts-ignore
                new ThemeMixins_1.default().build()
            ];
        };
        /**
         * 计算属性
         */
        Navbar.prototype.computed = function () {
            return {
                // 获取按钮激活状态
                getIsActive: function () {
                    return this.getBus.sidebar.opened === true;
                },
                getBus: function () {
                    // @ts-ignore
                    return busVue;
                },
                // 获取样式
                computedStyle: function () {
                    return "fill: " + this.getTopTextColor;
                },
                // 获取顶部样式
                getTopDivStyle: function () {
                    return 'background-color:' + this.getBus.theme.topColor;
                }
            };
        };
        /**
         * 函数
         */
        Navbar.prototype.methods = function () {
            return {
                /**
                 * 菜单选中时触发
                 */
                handleMenuSelectEvent: function (index) {
                    this.activeIndex = index;
                },
                /**
                 * 点击显示隐藏侧边栏触发
                 */
                handleToggleClick: function () {
                    this.getBus.sidebar.opened = !this.getBus.sidebar.opened;
                }
            };
        };
        /**
         * 创建模板
         */
        Navbar.prototype.template = function () {
            return "\n    <div :style=\"getTopDivStyle\" class=\"navbar-outer-a\">\n      <el-menu\n        ref=\"topMenu\"\n        :background-color=\"getTopColor\"\n        :text-color=\"getTopTextColor\"\n        class=\"navbar\"\n        @select=\"handleMenuSelectEvent\"\n        mode=\"horizontal\">\n        <div class=\"hamburger-container\">\n          <svg\n              :class=\"{'is-active':getIsActive}\"\n              t=\"1492500959545\"\n              class=\"hamburger\"\n              :style=\"computedStyle\"\n              viewBox=\"0 0 1024 1024\"\n              version=\"1.1\"\n              xmlns=\"http://www.w3.org/2000/svg\"\n              p-id=\"1691\"\n              xmlns:xlink=\"http://www.w3.org/1999/xlink\"\n              width=\"64\"\n              height=\"64\"\n              @click=\"handleToggleClick\">\n            <path\n                d=\"M966.8023 568.849776 57.196677 568.849776c-31.397081 0-56.850799-25.452695-56.850799-56.850799l0 0c0-31.397081 25.452695-56.849776 56.850799-56.849776l909.605623 0c31.397081 0 56.849776 25.452695 56.849776 56.849776l0 0C1023.653099 543.397081 998.200404 568.849776 966.8023 568.849776z\"\n                p-id=\"1692\" />\n            <path\n                d=\"M966.8023 881.527125 57.196677 881.527125c-31.397081 0-56.850799-25.452695-56.850799-56.849776l0 0c0-31.397081 25.452695-56.849776 56.850799-56.849776l909.605623 0c31.397081 0 56.849776 25.452695 56.849776 56.849776l0 0C1023.653099 856.07443 998.200404 881.527125 966.8023 881.527125z\"\n                p-id=\"1693\" />\n            <path\n                d=\"M966.8023 256.17345 57.196677 256.17345c-31.397081 0-56.850799-25.452695-56.850799-56.849776l0 0c0-31.397081 25.452695-56.850799 56.850799-56.850799l909.605623 0c31.397081 0 56.849776 25.452695 56.849776 56.850799l0 0C1023.653099 230.720755 998.200404 256.17345 966.8023 256.17345z\"\n                p-id=\"1694\" />\n          </svg>\n        </div>\n      </el-menu>\n    </div>\n    ";
        };
        return Navbar;
    }(ComponentBuilder_1.default));
    exports.default = Navbar;
});
