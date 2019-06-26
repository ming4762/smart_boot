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
    var SmartFormItem = (function (_super) {
        __extends(SmartFormItem, _super);
        function SmartFormItem() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        SmartFormItem.prototype.props = function () {
            return {
                column: {
                    required: true,
                    type: Object
                },
                model: {
                    required: true,
                    type: Object
                }
            };
        };
        SmartFormItem.prototype.template = function () {
            var vModel = 'v-model="model[column.key]"';
            return "\n    <el-form-item\n      :label=\"column.label\"\n      :prop=\"column.key\">\n      <el-switch " + this.createVIf('boolean') + " " + vModel + "/>\n      <el-select " + this.createVIf('select') + " " + vModel + " placeholder='\u8BF7\u9009\u62E9'>\n        <el-option\n          v-for=\"(dic, index) in (column.dicData ? column.dicData : [])\"\n          :label=\"dic.label\"\n          :value=\"dic.value\"\n          :key=\"index + 'option'\"/>\n      </el-select>\n      <el-input-number " + this.createVIf('number') + " " + vModel + " :disabled=\"column.disabled\"/>\n      <el-radio-group " + this.createVIf('radio') + " " + vModel + ">\n        <el-radio\n          v-for=\"(dic, index) in (column.dicData ? column.dicData : [])\"\n          :label=\"dic.label\" \n          :key=\"index + 'radio'\">{{dic.value}}</el-radio>\n      </el-radio-group>\n      <el-input placeholder='\u8BF7\u8F93\u5165\u5BC6\u7801' " + this.createVIf('password') + " " + vModel + " show-password/>\n      <el-input type='textarea' " + this.createVIf('textarea') + " " + vModel + " :placeholder=\"'\u8BF7\u8F93\u5165' + column.label\"/>\n      <el-input :placeholder=\"'\u8BF7\u8F93\u5165' + column.label\" " + this.createVIf('input') + " " + vModel + "/>\n    </el-form-item>\n    ";
        };
        SmartFormItem.prototype.createVIf = function (type) {
            return "v-if=\"column.type === '" + type + "'\"";
        };
        SmartFormItem.NAME = 'smart-form-item';
        return SmartFormItem;
    }(ComponentBuilder_1.default));
    exports.default = SmartFormItem;
});
