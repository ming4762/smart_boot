define(["require", "exports", "plugins/icon/SmartIconList"], function (require, exports, SmartIconList_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.default = {
        components: {
            'smart-icon-list': SmartIconList_1.default
        },
        props: {
            value: String
        },
        data: function () {
            return {
                iconDialogShow: false,
                icon: ''
            };
        },
        watch: {
            icon: function (_new, old) {
                if (_new !== old) {
                    this.$emit('input', _new);
                    this.iconDialogShow = false;
                }
            }
        },
        methods: {
            handleShowIconDialog: function () {
                this.iconDialogShow = true;
            }
        },
        template: "\n  <el-row>\n    <el-col :span=\"11\">\n      <el-input placeholder=\"\u8BF7\u9009\u62E9\u56FE\u6807\" v-model=\"value\">\n      </el-input>\n    </el-col>\n    <el-col :span=\"2\">\n      <i :class=\"value\"></i>\n    </el-col>\n    <el-col :span=\"1\">\n      &ensp; \n    </el-col>\n    <el-col :span=\"10\">\n      <el-button type=\"primary\" @click=\"handleShowIconDialog\">\u9009\u62E9\u56FE\u6807</el-button>\n    </el-col>\n\n    <el-dialog append-to-body :visible.sync=\"iconDialogShow\">\n      <smart-icon-list :icon.sync=\"icon\"></smart-icon-list>\n    </el-dialog>\n  </el-row>\n  "
    };
});
