// @ts-ignore
import ComponentBuilder from 'ComponentBuilder'
import SmartIconList from 'plugins/icon/SmartIconList'


export default class SmartIconSelect extends ComponentBuilder {

  protected components () {
    return {
      // @ts-ignore
      'smart-icon-list': new SmartIconList().build()
    }
  }

  protected props () {
    return {
      value: String
    }
  }

  protected data () {
    return {
      // 图标弹窗显示控制
      iconDialogShow: false,
      icon: ''
    }
  }

  protected watch () {
    return {
      /**
       * 监控图标选择变化
       * @param _new
       * @param old
       */
      icon (_new, old) {
        if (_new !== old) {
          this.$emit('input', _new)
          this.iconDialogShow = false
        }
      }
    }
  }

  protected methods () {
    return {
      handleShowIconDialog (): void {
        this.iconDialogShow = true
      }
    }
  }

  protected template () {
    return `
    <el-row>
      <el-col :span="11">
        <el-input placeholder="请选择图标" v-model="value">
        </el-input>
      </el-col>
      <el-col :span="2">
        <i :class="value"></i>
      </el-col>
      <el-col :span="1">
        &ensp; 
      </el-col>
      <el-col :span="10">
        <el-button type="primary" @click="handleShowIconDialog">选择图标</el-button>
      </el-col>
  
      <el-dialog append-to-body :visible.sync="iconDialogShow">
        <smart-icon-list :icon.sync="icon"></smart-icon-list>
      </el-dialog>
    </el-row>
    `
  }
}