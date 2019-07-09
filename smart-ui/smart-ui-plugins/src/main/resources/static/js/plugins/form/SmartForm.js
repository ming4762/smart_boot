define(["require", "exports", "plugins/form/SmartFormItem"], function (require, exports, SmartFormItem_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    const typeTriggerMap = {
        input: 'blur',
        select: 'change',
        boolean: 'change',
        number: 'change',
        radio: 'change'
    };
    const createRules = (column) => {
        const trigger = typeTriggerMap[column.type];
        return [{
                required: true,
                trigger: trigger || 'change',
                message: `请输入${column.label}`
            }];
    };
    exports.default = {
        name: 'smart-form',
        components: {
            'smart-form-item': SmartFormItem_1.default
        },
        props: {
            columnOptions: {
                type: Array,
                required: true
            },
            model: {
                type: Object,
                default: () => {
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
        },
        data() {
            return {
                formRules: {},
                showFormInlineColumns: [],
                showFormColumns: [],
                hiddenFormColumns: []
            };
        },
        created() {
            this.convertColumnOption(this.columnOptions);
        },
        beforeMount() {
            this.setDefaultValue();
        },
        beforeUpdate() {
            this.setDefaultValue();
        },
        computed: {
            getColumnMap: function () {
                const result = {};
                this.showFormColumns.forEach(columns => {
                    columns.forEach(item => {
                        result[item.key] = item;
                    });
                });
                this.showFormInlineColumns.forEach(item => {
                    result[item.key] = item;
                });
                return result;
            },
            getRules: function () {
                const result = {};
                for (let key in this.formRules) {
                    const item = this.formRules[key];
                    if (typeof item === 'boolean' && item === true) {
                        result[key] = createRules(this.getColumnMap[key]);
                    }
                    else {
                        result[key] = item;
                    }
                }
                return result;
            }
        },
        watch: {
            columnOptions: function (_new) {
                this.convertColumnOption(_new);
            }
        },
        methods: {
            convertColumnOption: function (options) {
                const showInlineColumns = [];
                const showColumns = [];
                const hiddenFormColumns = [];
                const formRules = {};
                let index = 0;
                options.forEach(item => {
                    if (!item.key)
                        item.key = item.prop;
                    if (!item.type)
                        item.type = 'input';
                    if (item.visible === false) {
                        hiddenFormColumns.push(item);
                    }
                    else {
                        if (this.inline) {
                            showInlineColumns.push(item);
                        }
                        else {
                            let span = item.span ? item.span : 24;
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
                this.columnOptions.forEach(column => {
                    if (column.defaultValue !== null && column.defaultValue !== undefined && (this.model[column.key] === null || this.model[column.key] === undefined)) {
                        this.$set(this.model, column.key, column.defaultValue);
                    }
                });
            },
            reset: function () {
                this.$refs['form'].resetFields();
            },
            validate: function (callback) {
                if (callback) {
                    this.$refs['form'].validate((valid, field) => {
                        callback(valid, field);
                    });
                }
                else {
                    return this.$refs['form'].validate();
                }
            }
        },
        template: `
  <el-form
    v-bind="$attrs"
    v-on="$listeners"
    :model="model"
    :label-width="labelWidth"
    :inline="inline"
    ref="form"
    :rules="getRules">
    <!--  遍历隐藏列  -->
    <div v-if="hiddenFormColumns.length > 0" style="display: none">
      <el-form-item
        v-for="item in hiddenFormColumns"
        :label="item.label"
        :key="item.key">
        <el-input v-model="model[item.prop]"></el-input>
      </el-form-item>
    </div>
    <!--  遍历行内列  -->
    <div :key="index + 'inline'" v-for="(column, index) in showFormInlineColumns">
      <template v-if="useSolt(column)">
        <slot
          :column="column"
          :model="model"
          :name="column.key"></slot>
      </template>
      <smart-form-item
        :model="model"
        :column="column"
        :name="column.key"
        v-else/>
    </div>
    <!--  遍历非行内列  -->
    <el-row
      v-for="(columns, index1) in showFormColumns"
      :key="index1 + 'row'">
      <el-col
        v-for="(column, index2) in columns"
        :span="column.span"
        :key="index2 + 'col'">
        <template v-if="useSolt(column)">
          <slot
            :column="column"
            :model="model"
            :name="column.key"></slot>
        </template>
        <smart-form-item
          :model="model"
          :column="column"
          :name="column.key"
          v-else/>
      </el-col>
    </el-row>
  </el-form>
  `
    };
});
