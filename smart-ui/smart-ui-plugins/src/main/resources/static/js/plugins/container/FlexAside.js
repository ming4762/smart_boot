define(["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.default = {
        props: {
            asideTitle: String,
            asideHeaderHeight: {
                type: String,
                default: '30px'
            },
            asideWidth: {
                type: String,
                default: '200px'
            },
            hasAsideHeader: {
                type: Boolean,
                default: true
            }
        },
        data: function () {
            return {
                name: name,
                asideVisible: true
            };
        },
        computed: {
            getAsideHeaderLineHeight: function () {
                return "line-height: " + this.asideHeaderHeight + ";";
            },
            getAsideHeaderIconClass: function () {
                return this.asideVisible ? 'el-icon-d-arrow-left' : 'el-icon-d-arrow-right';
            },
            getAsideWidth: function () {
                return this.asideVisible ? this.asideWidth : '30px';
            }
        },
        methods: {
            handleShowHideAside: function () {
                this.asideVisible = !this.asideVisible;
            }
        },
        template: "\n  <el-container class=\"full-height\">\n    <!--  \u6DFB\u52A0\u52A8\u753B\u6548\u679C  -->\n    <el-aside\n      :width=\"getAsideWidth\"\n      class=\"full-height common-aside\">\n      <el-header\n        v-if=\"hasAsideHeader\"\n        :height=\"asideHeaderHeight\"\n        :style=\"getAsideHeaderLineHeight\"\n        class=\"aside-header\">\n      <span\n          v-show=\"asideVisible\">{{asideTitle}}</span>\n        <i\n          @click=\"handleShowHideAside\"\n          :class=\"getAsideHeaderIconClass\"\n          :style=\"getAsideHeaderLineHeight\"\n          class=\"el-icon-d-arrow-right cousor-pointer aside-header-icon\"></i>\n      </el-header>\n      <div class=\"full-height\" v-show=\"asideVisible\">\n        <slot name=\"aside\"/>\n      </div>\n    </el-aside>\n    <el-main style=\"padding: 0;\" class=\"common-main\">\n      <slot/>\n    </el-main>\n  </el-container>\n  "
    };
});
