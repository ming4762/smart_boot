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
define(["require", "exports", "ComponentBuilder", "plugins/icon/SmartIconList"], function (require, exports, ComponentBuilder_1, SmartIconList_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var SmartIconSelect = (function (_super) {
        __extends(SmartIconSelect, _super);
        function SmartIconSelect() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        SmartIconSelect.prototype.components = function () {
            return {
                'smart-icon-list': new SmartIconList_1.default().build()
            };
        };
        SmartIconSelect.prototype.props = function () {
            return {
                value: String
            };
        };
        SmartIconSelect.prototype.data = function () {
            return {
                iconDialogShow: false,
                icon: ''
            };
        };
        SmartIconSelect.prototype.watch = function () {
            return {
                icon: function (_new, old) {
                    if (_new !== old) {
                        this.$emit('input', _new);
                        this.iconDialogShow = false;
                    }
                }
            };
        };
        SmartIconSelect.prototype.methods = function () {
            return {
                handleShowIconDialog: function () {
                    this.iconDialogShow = true;
                }
            };
        };
        SmartIconSelect.prototype.template = function () {
            return "\n    <el-row>\n      <el-col :span=\"11\">\n        <el-input placeholder=\"\u8BF7\u9009\u62E9\u56FE\u6807\" v-model=\"value\">\n        </el-input>\n      </el-col>\n      <el-col :span=\"2\">\n        <i :class=\"value\"></i>\n      </el-col>\n      <el-col :span=\"1\">\n        &ensp; \n      </el-col>\n      <el-col :span=\"10\">\n        <el-button type=\"primary\" @click=\"handleShowIconDialog\">\u9009\u62E9\u56FE\u6807</el-button>\n      </el-col>\n  \n      <el-dialog append-to-body :visible.sync=\"iconDialogShow\">\n        <smart-icon-list :icon.sync=\"icon\"></smart-icon-list>\n      </el-dialog>\n    </el-row>\n    ";
        };
        return SmartIconSelect;
    }(ComponentBuilder_1.default));
    exports.default = SmartIconSelect;
});
