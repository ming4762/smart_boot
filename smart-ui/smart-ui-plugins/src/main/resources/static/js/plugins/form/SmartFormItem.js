define(["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var createVIf = function (type) {
        return "v-if=\"column.type === '" + type + "'\"";
    };
    var createTemplate = function () {
        var vModel = 'v-model="model[column.key]"';
        var disabled = ':disabled="column.disabled"';
        return "\n    <el-form-item\n      :label=\"column.label\"\n      :prop=\"column.key\">\n      <el-switch " + createVIf('boolean') + " " + vModel + "/>\n      <el-select " + createVIf('select') + " " + vModel + " placeholder='\u8BF7\u9009\u62E9'>\n        <el-option\n          v-for=\"(dic, index) in (column.dicData ? column.dicData : [])\"\n          :label=\"dic.label\"\n          :value=\"dic.value\"\n          :key=\"index + 'option'\"/>\n      </el-select>\n      <el-input-number " + createVIf('number') + " " + vModel + " :disabled=\"column.disabled\"/>\n      <el-radio-group " + createVIf('radio') + " " + vModel + ">\n        <el-radio\n          v-for=\"(dic, index) in (column.dicData ? column.dicData : [])\"\n          :label=\"dic.label\" \n          :key=\"index + 'radio'\">{{dic.value}}</el-radio>\n      </el-radio-group>\n      <el-input placeholder='\u8BF7\u8F93\u5165\u5BC6\u7801' " + createVIf('password') + " " + vModel + " show-password/>\n      <el-input type='textarea' " + createVIf('textarea') + " " + vModel + " :placeholder=\"'\u8BF7\u8F93\u5165' + column.label\"/>\n      <el-input " + disabled + " :placeholder=\"'\u8BF7\u8F93\u5165' + column.label\" " + createVIf('input') + " " + vModel + "/>\n    </el-form-item>\n    ";
    };
    exports.default = {
        name: 'smart-form-item',
        props: {
            column: {
                required: true,
                type: Object
            },
            model: {
                required: true,
                type: Object
            }
        },
        template: createTemplate()
    };
});
