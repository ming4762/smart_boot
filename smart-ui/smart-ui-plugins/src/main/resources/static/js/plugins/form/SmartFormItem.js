define(["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    const createVIf = (type) => {
        return `v-if="column.type === '${type}'"`;
    };
    const createTemplate = () => {
        const vModel = 'v-model="model[column.key]"';
        const disabled = ':disabled="column.disabled"';
        return `
    <el-form-item
      :label="column.label"
      :prop="column.key">
      <el-switch ${createVIf('boolean')} ${vModel}/>
      <el-select ${createVIf('select')} ${vModel} placeholder='请选择'>
        <el-option
          v-for="(dic, index) in (column.dicData ? column.dicData : [])"
          :label="dic.label"
          :value="dic.value"
          :key="index + 'option'"/>
      </el-select>
      <el-input-number ${createVIf('number')} ${vModel} :disabled="column.disabled"/>
      <el-radio-group ${createVIf('radio')} ${vModel}>
        <el-radio
          v-for="(dic, index) in (column.dicData ? column.dicData : [])"
          :label="dic.label" 
          :key="index + 'radio'">{{dic.value}}</el-radio>
      </el-radio-group>
      <el-input :placeholder="getPlaceholder(column)" ${createVIf('password')} ${vModel} show-password/>
      <el-input type='textarea' ${createVIf('textarea')} ${vModel} :placeholder="getPlaceholder(column)"/>
      <el-input ${disabled} :placeholder="getPlaceholder(column)" ${createVIf('input')} ${vModel}/>
    </el-form-item>
    `;
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
        methods: {
            getPlaceholder(column) {
                if (column.placeholder) {
                    return column.placeholder;
                }
                else {
                    return '请输入' + column.label;
                }
            }
        },
        template: createTemplate()
    };
});
