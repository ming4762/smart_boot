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
     * 侧边栏组件
     */
    var SideBar = /** @class */ (function (_super) {
        __extends(SideBar, _super);
        function SideBar() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        SideBar.prototype.data = function () {
            return {
                // 激活的菜单
                activeIndex: ''
            };
        };
        /**
         * 混入
         */
        SideBar.prototype.mixins = function () {
            return [
                // @ts-ignore
                new ThemeMixins_1.default().build()
            ];
        };
        /**
         * 计算属性
         */
        SideBar.prototype.computed = function () {
            return {
                // 侧边栏底部演示计算属性
                getSidebarHeaderStyle: function () {
                    return "background-color:" + this.getTopColor;
                }
            };
        };
        SideBar.prototype.template = function () {
            return "\n    <el-scrollbar wrap-class=\"scrollbar-wrapper\">\n      <div class=\"left-outer\">\n        <div :style=\"getSidebarHeaderStyle\" class=\"left-header\">\n        </div>\n        <el-menu\n          class=\"left-menu\"\n          :show-timeout=\"200\"\n          :background-color=\"getSideBarTheme['background-color']\"\n          :text-color=\"getSideBarTheme['text-color']\"\n          :active-text-color=\"getSideBarTheme['active-text-color']\"\n          mode=\"vertical\"\n          :default-active=\"activeIndex\">\n        \n        </el-menu>\n      </div>\n    </el-scrollbar>\n    ";
        };
        return SideBar;
    }(ComponentBuilder_1.default));
    exports.default = SideBar;
});
