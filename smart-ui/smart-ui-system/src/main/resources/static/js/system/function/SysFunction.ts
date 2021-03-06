// @ts-ignore
import PageBuilder from '../../PageBuilder.js'
// @ts-ignore
import ApiService from '../../utils/ApiService.js'
// @ts-ignore
import LayoutMixins from '../../mixins/LayoutMixins.js'
// @ts-ignore
import SmartIconSelect from '../../plugins/icon/SmartIconSelect.js'
// @ts-ignore
import ValidateUtils from '../../utils/ValidateUtils.js'
// @ts-ignore
import TreeUtils from '../../utils/TreeUtils.js'
// @ts-ignore
import CommonUtil from '../../utils/CommonUtils.js'

declare const ready, smartModuleLoader

ready(function () {
  CommonUtil.loadCSS('/font-awesome/4.7.0/css/font-awesome.min.css', '/css/plugins/icon/icon.css')
  smartModuleLoader('smart-table').then(() => {
    // @ts-ignore
    new SysFunction().init()
  })
})

/**
 * 功能管理页面
 * TODO:添加修改弹窗上级显示不正确
 */
class SysFunction extends PageBuilder {
  /**
   * 构建函数
   */
  protected build () {
    return page
  }

  private static format(data, key) {
    return data
    // return data['object'][key]
  }

}

const page = {
  components: {
    'smart-icon-select': SmartIconSelect
  },
  mixins: [LayoutMixins],
  data () {
    return {
      apiService: ApiService,
      columnOptions: [
        {
          label: '上级功能',
          prop: 'parentId',
          table: {
            visible: false,
            displayControl: false
          },
          form: {
            span: 12
          }
        }, {
          label: '功能ID',
          prop: 'functionId',
          table: {
            visible: false
          },
          form: {
            visible: false
          }
        }, {
          label: '方法名',
          prop: 'functionName',
          table: {
            fixed: true,
            align: 'left',
            headerAlign: 'center',
            width: 160
          },
          form: {
            span: 24,
            rules: true
          }
        }, {
          label: '图标',
          prop: 'icon',
          table: {
          },
          form: {
            span: 12
          }
        }, {
          label: '类型',
          prop: 'functionType',
          type: 'radio',
          table: {
            width: 100
          },
          form: {
            defaultValue: '0',
            span: 12,
            dicData: [{
              label: '0',
              value: '目录'
            }, {
              label: '1',
              value: '菜单'
            }, {
              label: '2',
              value: '按钮'
            }]
          }
        }, {
          label: '是否菜单',
          prop: 'menuIs',
          type: 'radio',
          table: {
          },
          form: {
            span: 12,
            defaultValue: true,
            dicData: [
              {
                label: true,
                value: '是'
              }, {
                label: false,
                value: '否'
              }
            ]
          }
        }, {
          label: '权限',
          prop: 'premission',
          table: {
            width: 200
          },
          form: {
            span: 12
          }
        }, {
          label: 'url',
          prop: 'url',
          table: {
            minWidth: 200
          },
          form: {
            span: 12
          }
        }, {
          label: '序号',
          prop: 'seq',
          type: 'number',
          table: {
            sortable: true
          },
          form: {
            defaultValue: 1,
            span: 12
          }
        }
      ],
      defaultButtonConfig: {}
    }
  },
  methods: {
    /**
     * 添加修改弹窗打开事件
     */
    handleAddEditDialogShow (ident: string, model: any, callBack: Function, row?) {
      if (ident === 'add') {
        if (ValidateUtils.validateNull(row)) {
          model.parentId = '0'
          model.parentName = '根目录'
        } else {
          model.parentId = row.functionId
          model.parentName = row.functionName
        }
        callBack(model)
      } else {
        callBack()
      }
    },
    // 格式化图标样式
    formatIcon (row: any): string {
      if (row['icon']) {
        return row['icon'] + ' fa-lg'
      } else {
        return ''
      }
    },
    // 格式化类型的样式
    formatTypeClass (type: string): string {
      if (type === '0') {
        return ''
      } else if (type === '1') {
        return 'success'
      } else if (type === '2') {
        return 'warning'
      }
    },
    // 格式化类型名称
    formatTypeName (type: string): string {
      if (type === '0') {
        return '目录'
      } else if (type === '1') {
        return '菜单'
      } else if (type === '2') {
        return '按钮'
      }
    },
    handleTableDataFormatter (tableData: any[]): any[] {
      return TreeUtils.convertList2Tree(tableData, ['functionId', 'parentId'], '0')
    }
  },
  template: `
    <div style="padding: 15px;">
      <smart-table-crud
        :defaultButtonConfig="defaultButtonConfig"
        queryUrl="sys/function/list"
        deleteUrl="sys/function/batchDelete"
        saveUpdateUrl="sys/function/saveUpdate"
        :keys="['functionId']"
        :paging="false"
        defaultSortColumn="seq"
        :tableDataFormatter="handleTableDataFormatter"
        tableName="功能" 
        :apiService="apiService"
        @add-edit-dialog-show="handleAddEditDialogShow"
        labelWidth="80px"
        :height="computedTableHeight"
        :columnOptions="columnOptions">
        <!-- 表格上级菜单插槽 -->
        <template slot="form-parentId" slot-scope=" {model }">
          <el-form-item label="上级功能">
            <el-input style="display: none;" v-model="model.parentId"></el-input>
            <el-input :disabled="true" :value="model.parentName"></el-input>
          </el-form-item>
        </template>
         <!-- 图标form插槽 -->
        <template slot="form-icon" slot-scope="{ model }">
          <el-form-item label="图标">
            <smart-icon-select v-model="model.icon"></smart-icon-select>
          </el-form-item>
        </template>
        <!-- 图标插槽 -->
        <template slot-scope="{row}" slot="table-icon">
          <i :class="formatIcon(row)"></i>
        </template>
        <!-- 类型插槽 -->
        <template slot-scope="{row}" slot="table-functionType">
          <el-tag :type="formatTypeClass(row.functionType)">{{formatTypeName(row.functionType)}}</el-tag>
        </template>
        <!-- 是否菜单表格插槽 -->
        <template slot-scope="{row}" slot="table-menuIs">
          <i :class="row['menuIs'] === true ? 'el-icon-success' : 'el-icon-error'"></i>
        </template>
      </smart-table-crud>
    </div>
    `
}