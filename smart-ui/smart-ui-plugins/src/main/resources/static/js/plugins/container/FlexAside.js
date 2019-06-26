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
    var FlexAside = (function (_super) {
        __extends(FlexAside, _super);
        function FlexAside() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        FlexAside.prototype.props = function () {
            return {
                asideTitle: String,
                asideHeaderHeight: {
                    type: String,
                    default: '30px'
                },
                asideWidth: {
                    type: String,
                    default: '200px'
                }
            };
        };
        FlexAside.prototype.data = function () {
            return {
                name: name,
                asideVisible: true
            };
        };
        FlexAside.prototype.computed = function () {
            return {
                getAsideHeaderLineHeight: function () {
                    return "line-height: " + this.asideHeaderHeight + ";";
                },
                getAsideHeaderIconClass: function () {
                    return this.asideVisible ? 'el-icon-d-arrow-left' : 'el-icon-d-arrow-right';
                },
                getAsideWidth: function () {
                    return this.asideVisible ? this.asideWidth : '30px';
                }
            };
        };
        FlexAside.prototype.methods = function () {
            return {
                handleShowHideAside: function () {
                    this.asideVisible = !this.asideVisible;
                }
            };
        };
        FlexAside.prototype.template = function () {
            return "\n    <el-container class=\"common-full-height\">\n      <!--  \u6DFB\u52A0\u52A8\u753B\u6548\u679C  -->\n      <el-aside\n        :width=\"getAsideWidth\"\n        class=\"common-full-height common-aside\">\n        <el-header\n            :height=\"asideHeaderHeight\"\n            :style=\"getAsideHeaderLineHeight\"\n            class=\"aside-header\">\n        <span\n            v-show=\"asideVisible\">{{asideTitle}}</span>\n          <i\n            @click=\"handleShowHideAside\"\n            :class=\"getAsideHeaderIconClass\"\n            :style=\"getAsideHeaderLineHeight\"\n            class=\"el-icon-d-arrow-right cousor-pointer aside-header-icon\"></i>\n        </el-header>\n        <div class=\"common-full-height\" v-show=\"asideVisible\">\n          <slot name=\"aside\"/>\n        </div>\n      </el-aside>\n      <el-main style=\"padding: 0;\" class=\"common-main\">\n        <slot/>\n      </el-main>\n    </el-container>\n    ";
        };
        return FlexAside;
    }(ComponentBuilder_1.default));
    exports.default = FlexAside;
});
