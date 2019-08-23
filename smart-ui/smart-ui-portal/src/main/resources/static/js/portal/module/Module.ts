// @ts-ignore
import PageBuilder from '../../PageBuilder.js'
// @ts-ignore
import ApiService from '../../utils/ApiService.js'
// @ts-ignore
import LayoutMixins from '../../mixins/LayoutMixins.js'

// @ts-ignore
import TimeUtils from '../../utils/TimeUtils.js'
// @ts-ignore
import TreeUtils from '../../utils/TreeUtils.js'

declare const ready, smartModuleLoader

ready(function () {
  smartModuleLoader('smart-table').then(() => {
    console.log(this)
    // @ts-ignore
    new Module().init()
  })
})

class Module extends PageBuilder {
  /**
   * 构建函数
   */
  protected build () {
    return page
  }
}

/**
 * TODO：待完善，添加页面
 */
const page = {
  components: {
  },
  mixins: [
    LayoutMixins
  ],
  data () {
    return {
      apiService: ApiService,
      // TODO: 权限信息
      defaultButtonConfig: {},
      columnOptions: [
        {
          label: '模块ID',
          prop: 'moduleId',
          form: {
            visible: false
          },
          table: {
            visible: false,
            displayControl: false
          }
        },
        {
          label: '上级ID',
          prop: 'parentId',
          form: {
          },
          table: {
            visible: false,
            displayControl: false
          }
        },
        {
          label: '模块名',
          prop: 'moduleName',
          form: {
            rules: true
          },
          table: {
            align: 'left',
            width: 120,
            fixed: true
          }
        },
        {
          label: '模块图标',
          prop: 'moduleIcon',
          form: {
            visible: false
          },
          table: {
            visible: false,
            displayControl: false
          }
        },
        {
          label: '封面图片',
          prop: 'coverPicId',
          form: {},
          table: {
            visible: false
          }
        },
        {
          label: '备注',
          prop: 'remark',
          form: {},
          table: {
            minWidth: 200
          }
        },
        {
          label: '顶级ID',
          prop: 'topParentId',
          form: {
            visible: false
          },
          table: {
            visible: false,
            displayControl: false
          }
        },
        {
          label: '创建时间',
          prop: 'createTime',
          type: 'datetime',
          table: {
            width: 170,
            formatter: (row, column, cellValue, index) => {
              return TimeUtils.formatTime(cellValue)
            },
            sortable: true
          },
          form: {
            visible: false
          }
        },
        {
          label: '创建人员',
          prop: 'createUserId',
          table: {
            visible: false
          },
          form: {
            visible: false
          }
        },
        {
          label: '更新时间',
          prop: 'updateTime',
          table: {
            visible: false
          },
          form: {
            visible: false
          }
        },
        {
          label: '更新人员',
          prop: 'updateUserId',
          table: {
            visible: false
          },
          form: {
            visible: false
          }
        },
        {
          label: '序号',
          prop: 'seq',
          type: 'number',
          table: {
            sortable: true
          },
          form: {
            defaultValue: 0
          }
        }
      ]
    }
  },
  methods: {
    /**
     * 添加/编辑弹窗前事件
     */
    handleAddEditDialogShow (ident: string, model: any, callBack, row?: any): void {
      if (ident === 'add') {
        if (!row) {
          this.parentName = '根目录'
          model.parentId = '0'
        } else {
          this.parentName = row.moduleName
          model.parentId = row.moduleId
          model.topParentId = row.topParentId
        }
        callBack(model)
      } else {
        callBack()
      }
    },
    handleDataFormatter (data: any[]): any[] {
      return TreeUtils.convertList2Tree(data, ['moduleId', 'parentId'], '0')
    }
  },
  template: `
  <div style="padding: 15px;">
    <smart-table-crud
      :defaultButtonConfig="defaultButtonConfig"
      queryUrl="portal/module/list"
      deleteUrl="portal/module/batchDelete"
      saveUpdateUrl="portal/module/saveUpdate"
      :keys="['moduleId']"
      defaultSortColumn="seq"
      tableName="模块" 
      :apiService="apiService"
      labelWidth="80px"
      :height="computedTableHeight"
      @add-edit-dialog-show="handleAddEditDialogShow"
      :tableDataFormatter="handleDataFormatter"
      :columnOptions="columnOptions">
      <!-- form上级插槽 -->
      <template slot="form-parentId" slot-scope="{}">
        <el-form-item label="上级模块">
          <el-input disabled v-model="parentName"></el-input>
        </el-form-item>
      </template>
    </smart-table-crud>
  </div>
  `
}