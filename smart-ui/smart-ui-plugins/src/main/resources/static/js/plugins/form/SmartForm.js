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
define(["require", "exports", "ComponentBuilder", "plugins/form/SmartFormItem"], function (require, exports, ComponentBuilder_1, SmartFormItem_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var typeTriggerMap = {
        input: 'blur',
        select: 'change',
        boolean: 'change',
        number: 'change',
        radio: 'change'
    };
    var createRules = function (column) {
        var trigger = typeTriggerMap[column.type];
        return [{
                required: true,
                trigger: trigger || 'change',
                message: "\u8BF7\u8F93\u5165" + column.label
            }];
    };
    var SmartForm = (function (_super) {
        __extends(SmartForm, _super);
        function SmartForm() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        SmartForm.prototype.components = function () {
            return {
                'smart-form-item': new SmartFormItem_1.default().build()
            };
        };
        SmartForm.prototype.props = function () {
            return {
                columnOptions: {
                    type: Array,
                    required: true
                },
                model: {
                    type: Object,
                    default: function () {
                        return {};
                    }
                },
                inline: {
                    type: Boolean,
                    default: false
                },
                labelWidth: {
                    type: String
                }
            };
        };
        SmartForm.prototype.data = function () {
            return {
                formRules: {},
                showFormInlineColumns: [],
                showFormColumns: [],
                hiddenFormColumns: []
            };
        };
        SmartForm.prototype.created = function () {
            this.convertColumnOption(this.columnOptions);
        };
        SmartForm.prototype.beforeMount = function () {
            this.setDefaultValue();
        };
        SmartForm.prototype.beforeUpdate = function () {
            this.setDefaultValue();
        };
        SmartForm.prototype.computed = function () {
            return {
                getColumnMap: function () {
                    var result = {};
                    this.showFormColumns.forEach(function (columns) {
                        columns.forEach(function (item) {
                            result[item.key] = item;
                        });
                    });
                    this.showFormInlineColumns.forEach(function (item) {
                        result[item.key] = item;
                    });
                    return result;
                },
                getRules: function () {
                    var result = {};
                    for (var key in this.formRules) {
                        var item = this.formRules[key];
                        if (typeof item === 'boolean' && item === true) {
                            result[key] = createRules(this.getColumnMap[key]);
                        }
                        else {
                            result[key] = item;
                        }
                    }
                    return result;
                }
            };
        };
        SmartForm.prototype.watch = function () {
            return {
                columnOptions: function (_new) {
                    this.convertColumnOption(_new);
                }
            };
        };
        SmartForm.prototype.methods = function () {
            return {
                convertColumnOption: function (options) {
                    var _this = this;
                    var showInlineColumns = [];
                    var showColumns = [];
                    var hiddenFormColumns = [];
                    var formRules = {};
                    var index = 0;
                    options.forEach(function (item) {
                        if (!item.key)
                            item.key = item.prop;
                        if (!item.type)
                            item.type = 'input';
                        if (item.visible === false) {
                            hiddenFormColumns.push(item);
                        }
                        else {
                            if (_this.inline) {
                                showInlineColumns.push(item);
                            }
                            else {
                                var span = item.span ? item.span : 24;
                                if (index === 0) {
                                    showColumns.push([]);
                                }
                                showColumns[showColumns.length - 1].push(item);
                                index = index + span;
                                if (index === 24) {
                                    index = 0;
                                }
                            }
                        }
                        if (item.rules) {
                            formRules[item.key] = item.rules;
                        }
                    });
                    this.showFormInlineColumns = showInlineColumns;
                    this.showFormColumns = showColumns;
                    this.hiddenFormColumns = hiddenFormColumns;
                    this.formRules = formRules;
                },
                useSolt: function (item) {
                    return this.$scopedSlots[item.key];
                },
                setDefaultValue: function () {
                    var _this = this;
                    this.columnOptions.forEach(function (column) {
                        if (column.defaultValue !== null && column.defaultValue !== undefined && (_this.model[column.key] === null || _this.model[column.key] === undefined)) {
                            _this.$set(_this.model, column.key, column.defaultValue);
                        }
                    });
                },
                reset: function () {
                    this.$refs['form'].resetFields();
                },
                validate: function (callback) {
                    if (callback) {
                        this.$refs['form'].validate(function (valid, field) {
                            callback(valid, field);
                        });
                    }
                    else {
                        return this.$refs['form'].validate();
                    }
                }
            };
        };
        SmartForm.prototype.template = function () {
            return "\n    <el-form\n      v-bind=\"$attrs\"\n      v-on=\"$listeners\"\n      :model=\"model\"\n      :label-width=\"labelWidth\"\n      :inline=\"inline\"\n      ref=\"form\"\n      :rules=\"getRules\">\n      <!--  \u904D\u5386\u9690\u85CF\u5217  -->\n      <div v-if=\"hiddenFormColumns.length > 0\" style=\"display: none\">\n        <el-form-item\n          v-for=\"item in hiddenFormColumns\"\n          :label=\"item.label\"\n          :key=\"item.key\">\n          <el-input v-model=\"model[item.prop]\"></el-input>\n        </el-form-item>\n      </div>\n      <!--  \u904D\u5386\u884C\u5185\u5217  -->\n      <div :key=\"index + 'inline'\" v-for=\"(column, index) in showFormInlineColumns\">\n        <template v-if=\"useSolt(column)\">\n          <slot\n            :column=\"column\"\n            :model=\"model\"\n            :name=\"column.key\"></slot>\n        </template>\n        <smart-form-item\n          :model=\"model\"\n          :column=\"column\"\n          :name=\"column.key\"\n          v-else/>\n      </div>\n      <!--  \u904D\u5386\u975E\u884C\u5185\u5217  -->\n      <el-row\n        v-for=\"(columns, index1) in showFormColumns\"\n        :key=\"index1 + 'row'\">\n        <el-col\n          v-for=\"(column, index2) in columns\"\n          :span=\"column.span\"\n          :key=\"index2 + 'col'\">\n          <template v-if=\"useSolt(column)\">\n            <slot\n              :column=\"column\"\n              :model=\"model\"\n              :name=\"column.key\"></slot>\n          </template>\n          <smart-form-item\n            :model=\"model\"\n            :column=\"column\"\n            :name=\"column.key\"\n            v-else/>\n        </el-col>\n      </el-row>\n    </el-form>\n    ";
        };
        SmartForm.NAME = 'smart-form';
        return SmartForm;
    }(ComponentBuilder_1.default));
    exports.default = SmartForm;
});
