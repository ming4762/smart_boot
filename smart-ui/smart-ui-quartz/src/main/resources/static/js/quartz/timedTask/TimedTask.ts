// @ts-ignore
import PageBuilder from 'PageBuilder'
// @ts-ignore
import SmartTableCRUD from 'plugins/table/SmartTableCRUD'
// @ts-ignore
import ApiService from 'utils/ApiService'
// @ts-ignore
import LayoutMixins from 'mixins/LayoutMixins'
// @ts-ignore
import MessageMixins from 'mixins/MessageMixins'

/**
 * 定时任务管理页面
 */
export class TimedTask extends PageBuilder {
  protected build () {
    return page
  }
}

const page = {
  components: {
    'smart-table-crud': SmartTableCRUD
  },
  mixins: [ LayoutMixins, MessageMixins ],
  data () {
    return {
      apiService: ApiService,
      columnOptions: [
        {
          label: '任务id',
          prop: 'taskId',
          table: {
            visible: false,
            displayControl: false
          },
          form: {
            visible: false
          }
        },
        {
          label: '任务名称',
          prop: 'taskName',
          table: {
            width: 100,
            fixed: true
          },
          form: {
            span: 12,
            rules: true
          }
        },
        {
          label: '任务类',
          prop: 'clazz',
          table: {
            width: 200
          },
          form: {}
        },
        {
          label: '表达式',
          prop: 'cron',
          table: {
            width: 120
          },
          form: {
            rules: true
          }
        },
        {
          label: '启用',
          prop: 'used',
          type: 'boolean',
          table: {
            sortable: true
          },
          form: {
            span: 12,
            defaultValue: true
          }
        },
        {
          label: '预设类',
          prop: 'presetClass',
          table: {
            width: 180
          },
          form: {
            span: 12
          }
        },
        {
          label: '任务参数',
          prop: 'taskParameter',
          table: {
            minWidth: 220
          },
          form: {}
        },
        {
          label: '备注',
          prop: 'remark',
          table: {
            minWidth: 220
          },
          form: {}
        },
        {
          label: '序号',
          prop: 'seq',
          type: 'number',
          table: {
            sortable: true
          },
          form: {
            defaultValue: 1
          }
        }
      ],
      // TODO:权限
      defaultButtonConfig: {
        add: {
          rowShow: false
        }
      }
    }
  },
  methods: {
    /**
     * 关闭开启任务
     * @param task
     */
    handleOpenClose (task) {
      const used = !task.used
      const status = used ? '启用' : '关闭'
      this.$confirm(`确定要${status}该任务吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        ApiService.postAjax('quartz/timeTask/openClose', [
          {
            taskId: task.taskId,
            used: used
          }
        ]).then(() => {
          this.successMessage(`${status}任务成功`)
          this.$refs['table'].load()
        }).catch(error => {
          if (error !== 'cancel') {
            this.errorMessage(`${status}任务失败`, error)
          }
        })
      })
    }
  },
  template: `
  <div style="padding: 15px;">
    <smart-table-crud
      ref="table"
      queryUrl="quartz/timeTask/list"
      deleteUrl="quartz/timeTask/batchDelete"
      saveUpdateUrl="quartz/timeTask/saveUpdate"
      :keys="['taskId']"
      labelWidth="80px"
      :height="computedTableHeight"
      :columnOptions="columnOptions"
      tableName="定时任务"
      :defaultButtonConfig="defaultButtonConfig"
      :apiService="apiService">
      <template v-slot:table-used="{row}">
        <el-tag v-if="row.used === true" type="success">启用</el-tag>
        <el-tag v-else type="danger">关闭</el-tag>
      </template>
      <template v-slot:row-operation="{row}">
        <el-tooltip class="item" effect="dark" :content="row.used === true ? '关闭' : '启用'" placement="top">
          <el-button @click="handleOpenClose(row)" type="success" :icon="row.used === true ? 'el-icon-video-pause' : 'el-icon-video-play'" size="mini"/>
        </el-tooltip>
      </template>
      
      <!--启用form插槽-->
      
    </smart-table-crud>
  </div>
  `
}