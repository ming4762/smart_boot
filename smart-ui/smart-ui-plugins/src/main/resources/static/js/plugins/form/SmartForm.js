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
    /**
     * 创建验证规则
     * @param column
     * @returns {{trigger: (*|string), message: string, required: boolean}[]}
     */
    var createRules = function (column) {
        var trigger = typeTriggerMap[column.type];
        return [{
                required: true,
                trigger: trigger || 'change',
                message: "\u8BF7\u8F93\u5165" + column.label
            }];
    };
    /**
     * 表格组件
     */
    var SmartForm = /** @class */ (function (_super) {
        __extends(SmartForm, _super);
        function SmartForm() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        SmartForm.prototype.components = function () {
            return {
                // @ts-ignore
                'smart-form-item': new SmartFormItem_1.default().build()
            };
        };
        SmartForm.prototype.props = function () {
            return {
                // 表格配置
                columnOptions: {
                    type: Array,
                    required: true
                },
                // 数据绑定
                model: {
                    type: Object,
                    default: function () {
                        return {};
                    }
                },
                // 是否是行内表单
                inline: {
                    type: Boolean,
                    default: false
                },
                // from的label-width
                labelWidth: {
                    type: String
                }
            };
        };
        SmartForm.prototype.data = function () {
            return {
                // 表单验证规则
                formRules: {},
                // 行内表单显示列信息
                showFormInlineColumns: [],
                // 表单显示列信息
                showFormColumns: [],
                // 隐藏列信息
                hiddenFormColumns: []
            };
        };
        SmartForm.prototype.created = function () {
            // @ts-ignore
            this.convertColumnOption(this.columnOptions);
        };
        SmartForm.prototype.beforeMount = function () {
            // @ts-ignore
            this.setDefaultValue();
        };
        SmartForm.prototype.beforeUpdate = function () {
            // @ts-ignore
            this.setDefaultValue();
        };
        /**
         * 计算属性
         */
        SmartForm.prototype.computed = function () {
            return {
                /**
                 * 列映射计算属性
                 */
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
                /**
                 * 验证规则计算属性
                 */
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
                /**
                 * 转换列信息
                 * @param options
                 */
                convertColumnOption: function (options) {
                    var _this = this;
                    // 显示列
                    var showInlineColumns = [];
                    var showColumns = [];
                    // 隐藏列
                    var hiddenFormColumns = [];
                    // 验证规则
                    var formRules = {};
                    var index = 0;
                    options.forEach(function (item) {
                        // 设置key
                        if (!item.key)
                            item.key = item.prop;
                        // 设置默认类型
                        if (!item.type)
                            item.type = 'input';
                        if (item.visible === false) {
                            hiddenFormColumns.push(item);
                        }
                        else {
                            if (_this.inline) {
                                // 行内表单
                                showInlineColumns.push(item);
                            }
                            else {
                                // 非行内表单
                                // 获取span，默认值24
                                var span = item.span ? item.span : 24;
                                if (index === 0) {
                                    showColumns.push([]);
                                }
                                showColumns[showColumns.length - 1].push(item);
                                index = index + span;
                                // 重启一行
                                if (index === 24) {
                                    index = 0;
                                }
                            }
                        }
                        // 添加验证规则
                        if (item.rules) {
                            formRules[item.key] = item.rules;
                        }
                    });
                    this.showFormInlineColumns = showInlineColumns;
                    this.showFormColumns = showColumns;
                    this.hiddenFormColumns = hiddenFormColumns;
                    this.formRules = formRules;
                },
                // 是否使用插槽
                useSolt: function (item) {
                    return this.$scopedSlots[item.key];
                },
                /**
                 * 设置默认值
                 */
                setDefaultValue: function () {
                    var _this = this;
                    console.log(this);
                    this.columnOptions.forEach(function (column) {
                        if (column.defaultValue !== null && column.defaultValue !== undefined && (_this.model[column.key] === null || _this.model[column.key] === undefined)) {
                            _this.$set(_this.model, column.key, column.defaultValue);
                        }
                    });
                },
                /**
                 * 重置表单
                 */
                reset: function () {
                    this.$refs['form'].resetFields();
                },
                /**
                 * 验证表单
                 * @param callback
                 */
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
