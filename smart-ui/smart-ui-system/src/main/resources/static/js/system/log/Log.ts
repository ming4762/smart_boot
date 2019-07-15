// @ts-ignore
import PageBuilder from 'PageBuilder'
// @ts-ignore
import TimeUtils from 'utils/TimeUtils'
// @ts-ignore
import SmartTableCRUD from 'plugins/table/SmartTableCRUD'
// @ts-ignore
import ApiService from 'utils/ApiService'
// @ts-ignore
import LayoutMixins from 'mixins/LayoutMixins'

export class Log extends PageBuilder {
  protected build () {
    return page
  }
}

const page = {
  components: {
    'smart-table-crud': SmartTableCRUD
  },
  mixins: [ LayoutMixins ],
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
            fixed: 'right'
          },
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
            formatter: (row, column, value) => {
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
      :apiService="apiService">
      <!--参数插槽-->
      <template v-slot:table-params="{row}">
        <el-tooltip effect="dark" content="查看详情" placement="left">
          <a @click="handleShowParamsDetail(row.params)">{{formatParams(row.params)}}</a>
        </el-tooltip>
      </template>
    </smart-table-crud>
  </div>
  `
}