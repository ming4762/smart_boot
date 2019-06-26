// @ts-ignore
import ComponentBuilder from 'ComponentBuilder'

/**
 * form列
 */
export default class SmartFormItem extends ComponentBuilder {
  // 组件名称
  public static NAME: string = 'smart-form-item'

  /**
   * props
   */
  protected props (): any {
    return {
      column: {
        required: true,
        type: Object
      },
      // 绑定model
      model: {
        required: true,
        type: Object
      }
    }
  }

  /**
   * 模板
   */
  protected template () {
    const vModel = 'v-model="model[column.key]"'
    return `
    <el-form-item
      :label="column.label"
      :prop="column.key">
      <el-switch ${this.createVIf('boolean')} ${vModel}/>
      <el-select ${this.createVIf('select')} ${vModel} placeholder='请选择'>
        <el-option
          v-for="(dic, index) in (column.dicData ? column.dicData : [])"
          :label="dic.label"
          :value="dic.value"
          :key="index + 'option'"/>
      </el-select>
      <el-input-number ${this.createVIf('number')} ${vModel} :disabled="column.disabled"/>
      <el-radio-group ${this.createVIf('radio')} ${vModel}>
        <el-radio
          v-for="(dic, index) in (column.dicData ? column.dicData : [])"
          :label="dic.label" 
          :key="index + 'radio'">{{dic.value}}</el-radio>
      </el-radio-group>
      <el-input placeholder='请输入密码' ${this.createVIf('password')} ${vModel} show-password/>
      <el-input type='textarea' ${this.createVIf('textarea')} ${vModel} :placeholder="'请输入' + column.label"/>
      <el-input :placeholder="'请输入' + column.label" ${this.createVIf('input')} ${vModel}/>
    </el-form-item>
    `
  }

  private createVIf (type): string {
    return `v-if="column.type === '${type}'"`
  }
}