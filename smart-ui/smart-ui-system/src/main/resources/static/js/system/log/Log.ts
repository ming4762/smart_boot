// @ts-ignore
import PageBuilder from '../../PageBuilder.js'
// @ts-ignore
import ApiService from '../../utils/ApiService.js'
// @ts-ignore
import LayoutMixins from '../../mixins/LayoutMixins.js'
// @ts-ignore
import TimeUtils from '../../utils/TimeUtils.js'
import {User} from "../user/User";

declare const ready, smartModuleLoader

ready(function () {
  smartModuleLoader('smart-table').then(() => {
    // @ts-ignore
    new Log().init()
  })
})

class Log extends PageBuilder {
  protected build () {
    return LogComponent
  }
}

export const LogComponent = {
  components: {
  },
  mixins: [ LayoutMixins ],
  props: {
    userId: String
  },
  data () {
    return {
      apiService: ApiService,
      columnOptions: [
        {
          label: '操作',
          prop: 'operation',
          table: {
            fixed: true,
            width: 180
          },
          form: {}
        },
        {
          label: '用时(ms)',
          prop: 'useTime',
          table: {
          },
          form: {}
        },
        {
          label: '方法',
          prop: 'method',
          table: {
            width: 260
          },
          form: {}
        },
        {
          label: '参数',
          prop: 'params',
          table: {
            minWidth: 240
          },
          form: {}
        },
        {
          label: 'ip',
          prop: 'ip',
          table: {
          },
          form: {}
        },
        {
          label: '时间',
          prop: 'createTime',
          table: {
            sortable: true,
            width: 165,
            formatter: (row, column, value) => TimeUtils.formatTime(value)
          },
          form: {}
        },
        {
          label: '请求路径',
          prop: 'requestPath',
          table: {
            width: 220
          },
          form: {}
        },
        {
          label: '状态码',
          prop: 'statusCode',
          table: {
            width: 100,
            fixed: 'right',
            sortable: true
          },
          search: {},
          form: {}
        },
        {
          label: '错误信息',
          prop: 'errorMessage',
          table: {
            width: 200
          },
          form: {}
        },
        {
          label: '用户',
          prop: 'userId',
          table: {
            width: 100,
            fixed: 'right',
            formatter: (row) => {
              if (row.user) {
                return row.user.name
              } else {
                return '-'
              }
            }
          },
          form: {}
        }
      ]
    }
  },
  methods: {
    formatParams (params) {
      if (params && params.length < 36) {
        return params
      }
      if (params.length >= 36) {
        return params.substring(0, 30) + '...'
      }
    },
    /**
     * 显示参数详情
     */
    handleShowParamsDetail (params) {
      this.$alert(params ? params : '')
    },
    /**
     * 参数格式化
     */
    handleQueryParameterFormatter (parameter) {
      if (this.userId) {
        return Object.assign({
          'userId@=': this.userId
        }, parameter)
      }
      return parameter
    }
  },
  template: `
  <div style="padding: 15px;">
    <smart-table-crud
      ref="table"
      queryUrl="sys/log/listWithAll"
      deleteUrl="sys/log/batchDelete"
      saveUpdateUrl="sys/log/saveUpdate"
      :keys="['logId']"
      defaultSortColumn="createTime"
      defaultSortOrder="desc"
      :hasTopLeft="false"
      :hasOpreaColumn="false"
      :height="computedTableHeight"
      :columnOptions="columnOptions"
      tableName="日志"
      :queryParameterFormatter="handleQueryParameterFormatter"
      :apiService="apiService">
      <!--参数插槽-->
      <template v-slot:table-params="{row}">
        <el-tooltip effect="dark" content="查看详情" placement="left">
          <a @click="handleShowParamsDetail(row.params)">{{formatParams(row.params)}}</a>
        </el-tooltip>
      </template>
      
      <!--状态码搜索插槽-->
      <template v-slot:search-statusCode="{model}">
        <el-form-item label="状态码">
          <el-select  v-model="model.statusCode" placeholder="请选择">
            <el-option
              label="200"
              value="200"/>
            <el-option
              label="500"
              value="500"/>
          </el-select> 
        </el-form-item> 
      </template>
    </smart-table-crud>
  </div>
  `
}