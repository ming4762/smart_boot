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
define(["require", "exports", "ComponentBuilder", "system/layout/navbar/Navbar", "system/layout/sidebar/SideBar"], function (require, exports, ComponentBuilder_1, Navbar_1, SideBar_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    /**
     * 框架主体
     */
    var Layout = /** @class */ (function (_super) {
        __extends(Layout, _super);
        function Layout() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        /**
         * 创建组件
         */
        Layout.prototype.components = function () {
            return {
                // @ts-ignore
                'navbar': new Navbar_1.default().build(),
                // @ts-ignore
                'SideBar': new SideBar_1.default().build()
            };
        };
        /**
         * 计算属性
         */
        Layout.prototype.computed = function () {
            return {
                getBus: function () {
                    return busVue;
                },
                getClassObj: function () {
                    return {
                        hideSidebar: !this.getBus.sidebar.opened,
                        openSidebar: this.getBus.sidebar.opened,
                        withoutAnimation: this.getBus.sidebar.withoutAnimation,
                        mobile: this.getBus.device === 'Mobile'
                    };
                }
            };
        };
        /**
         * 创建模板
         */
        Layout.prototype.template = function () {
            return "\n<div :class=\"getClassObj\" class=\"full-height app-wrapper\">\n  <!--\u4FA7\u8FB9\u680F\u7EC4\u4EF6-->\n  <SideBar class=\"sidebar-container\"/>\n  <div class=\"main-container full-height main-outer\">\n    <!--\u9876\u90E8-->\n    <div class=\"top-div\">\n      <!--\u9876\u90E8\u83DC\u5355-->\n      <navbar/>\n    </div>\n  </div>\n</div>\n    ";
        };
        return Layout;
    }(ComponentBuilder_1.default));
    exports.default = Layout;
});
