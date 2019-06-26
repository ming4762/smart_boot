// @ts-ignore
import ComponentBuilder from 'ComponentBuilder'
// @ts-ignore
import CommonUtils from 'utils/CommonUtils'

export default class SmartColumnVisible extends ComponentBuilder {

  protected props () {
    return {
      columnShow: {
        type: Object,
        default: () => { return {} }
      },
      // 列数
      lineNumber: {
        type: Number,
        default: 4
      }
    }
  }

  protected data () {
    return {
      result: {}
    }
  }

  protected beforeMount () {
    // @ts-ignore
    let columnShow = CommonUtils.clone(this.columnShow)
    for (let column in columnShow) {
      // @ts-ignore
      this.$set(this.result, column, !columnShow[column]['hidden'])
    }
  }

  protected watch () {
    return {
      /**
       * 监控结果变化
       */
      result: {
        deep: true,
        handler: function (_new) {
          const listener = 'result-change'
          if (this.$listeners[listener]) {
            this.$emit(listener, _new)
          }
        }
      }
    }
  }

  protected computed () {
    return {
      /**
       * 计算列数
       */
      computedSpanNumber: function () {
        return 24 / this.lineNumber
      },
      /**
       * 计算表格显示
       * @returns {Array}
       */
      computedColumnShow: function () {
        let result = []
        let i = 0
        for (let column in this.columnShow) {
          if (i % this.lineNumber === 0) {
            result.push([])
          }
          let object = this.columnShow[column]
          object['key'] = column
          result[result.length - 1].push(object)
          i++
        }
        return result
      }
    }
  }

  /**
   * 模板构造器
   */
  protected template() {
    return `
    <div>
      <el-row :key="index + 'out'" v-for="(columnGroup, index) in computedColumnShow">
        <el-col :span="computedSpanNumber" :key="index + 'in'" v-for="(column, index) in columnGroup">
          <el-checkbox
              :label="column.name"
              v-model="result[column.key]"
          ></el-checkbox>
        </el-col>
      </el-row>
    </div>
    `
  }
}