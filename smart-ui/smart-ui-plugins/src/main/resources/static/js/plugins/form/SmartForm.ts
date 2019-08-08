
import SmartFormItem from 'plugins/form/SmartFormItem'

const typeTriggerMap = {
  input: 'blur',
  select: 'change',
  boolean: 'change',
  number: 'change',
  radio: 'change',
  password: 'blur'
}

/**
 * 创建验证规则
 * @param column
 * @returns {{trigger: (*|string), message: string, required: boolean}[]}
 */
const createRules = (column) => {
  const trigger = typeTriggerMap[column.type]
  return [{
    required: true,
    trigger: trigger || 'change',
    message: `请输入${column.label}`
  }]
}

export default {
  name: 'smart-form',
  components: {
    'smart-form-item': SmartFormItem
  },
  props: {
    // 表格配置
    columnOptions: {
      type: Array,
      required: true
    },
    // 数据绑定
    model: {
      type: Object,
      default: () => {
        return {}
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
  },
  data () {
    return {
      // 表单验证规则
      formRules: {},
      // 行内表单显示列信息
      showFormInlineColumns: [],
      // 表单显示列信息
      showFormColumns: [],
      // 隐藏列信息
      hiddenFormColumns: []
    }
  },
  created () {
    // @ts-ignore
    this.convertColumnOption(this.columnOptions)
  },
  beforeMount () {
    // @ts-ignore
    this.setDefaultValue()
  },
  beforeUpdate () {
    // @ts-ignore
    this.setDefaultValue()
  },
  computed: {
    /**
     * 列映射计算属性
     */
    getColumnMap: function () {
      const result = {}
      this.showFormColumns.forEach(columns => {
        columns.forEach(item => {
          result[item.key] = item
        })
      })
      this.showFormInlineColumns.forEach(item => {
        result[item.key] = item
      })
      return result
    },
    /**
     * 验证规则计算属性
     */
    getRules: function () {
      const result = {}
      for (let key in this.formRules) {
        const item = this.formRules[key]
        if (typeof item === 'boolean' && item === true) {
          result[key] = createRules(this.getColumnMap[key])
        } else {
          result[key] = item
        }
      }
      return result
    }
  },
  watch: {
    columnOptions: function (_new) {
      this.convertColumnOption(_new)
    }
  },
  methods: {
    /**
     * 转换列信息
     * @param options
     */
    convertColumnOption: function (options) {
      // 显示列
      const showInlineColumns = []
      const showColumns = []
      // 隐藏列
      const hiddenFormColumns = []
      // 验证规则
      const formRules = {}
      let index = 0
      options.forEach(item => {
        // 设置key
        if (!item.key) item.key = item.prop
        // 设置默认类型
        if (!item.type) item.type = 'input'
        if (item.visible === false) {
          hiddenFormColumns.push(item)
        } else {
          if (this.inline) {
            // 行内表单
            showInlineColumns.push(item)
          } else {
            // 非行内表单
            // 获取span，默认值24
            let span = item.span ? item.span : 24
            if (index === 0) {
              showColumns.push([])
            }
            showColumns[showColumns.length - 1].push(item)
            index = index + span
            // 重启一行
            if (index === 24) {
              index = 0
            }
          }
        }
        // 添加验证规则
        if (item.rules) {
          formRules[item.key] = item.rules
        }
      })
      this.showFormInlineColumns = showInlineColumns
      this.showFormColumns = showColumns
      this.hiddenFormColumns = hiddenFormColumns
      this.formRules = formRules
    },
    // 是否使用插槽
    useSolt: function (item) {
      return this.$scopedSlots[item.key]
    },
    /**
     * 设置默认值
     */
    setDefaultValue: function () {
      this.columnOptions.forEach(column => {
        if (column.defaultValue !== null && column.defaultValue !== undefined && (this.model[column.key] === null || this.model[column.key] === undefined)) {
          this.$set(this.model, column.key, column.defaultValue)
        }
      })
    },
    /**
     * 重置表单
     */
    reset: function () {
      this.$refs['form'].resetFields()
    },
    /**
     * 验证表单
     * @param callback
     */
    validate: function (callback) {
      if (callback) {
        this.$refs['form'].validate((valid, field) => {
          callback(valid, field)
        })
      } else {
        return this.$refs['form'].validate()
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
}
