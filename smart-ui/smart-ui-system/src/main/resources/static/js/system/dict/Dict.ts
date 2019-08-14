// @ts-ignore
import PageBuilder from '../../PageBuilder.js'
// @ts-ignore
import ApiService from '../../utils/ApiService.js'
// @ts-ignore
import LayoutMixins from '../../mixins/LayoutMixins.js'
// @ts-ignore
import TimeUtils from '../../utils/TimeUtils.js'

declare const ready, smartModuleLoader

ready(function () {
  smartModuleLoader('smart-table').then(() => {
    // @ts-ignore
    new Dict().init()
  })
})

class Dict extends PageBuilder {
  /**
   * 构建函数
   */
  protected build () {
    return page
  }
}

/**
 * 字典项组件
 */
const DictItem = {
  components: {
  },
  mixins: [
    LayoutMixins
  ],
  props: {
    dictCode: {
      type: String,
      required: true
    },
    // 是否显示
    show: Boolean
  },
  data () {
    return {
      apiService: ApiService,
      // TODO: 权限信息，待测试
      defaultButtonConfig: {
        add: {
          rowShow: false,
          permission: 'sys:dictItem:save'
        },
        edit: {
          permission: 'sys:dictItem:update'
        },
        delete: {
          permission: 'sys:dictItem:delete'
        }
      },
      columnOptions: [
        {
          prop: 'id',
          label: 'id',
          table: {
            visible: false,
            displayControl: false
          },
          form: {
            visible: false
          }
        },
        {
          prop: 'dictCode',
          label: '字典编码',
          table: {
            fixed: true,
            visible: false
          },
          form: {
          }
        },
        {
          prop: 'itemCode',
          label: '字典项编码',
          table: {
            width: 150,
            fixed: true
          },
          form: {
            rules: true
          }
        },
        {
          prop: 'itemValue',
          label: '字典项值',
          table: {
            fixed: true,
            width: 180
          },
          form: {
            rules: true
          }
        },
        {
          prop: 'remark',
          label: '描述',
          table: {
            minWidth: 200
          },
          form: {}
        },
        {
          prop: 'createUserId',
          label: '创建人员',
          table: {
            width: 120
          },
          form: {
            visible: false
          }
        },
        {
          prop: 'createTime',
          label: '创建时间',
          table: {
            width: 170,
            formatter: (row, column, cellValue) => {
              return TimeUtils.formatTime(cellValue)
            }
          },
          form: {
            visible: false
          }
        },
        {
          prop: 'updateUserId',
          label: '更新人员',
          table: {
            width: 120
          },
          form: {
            visible: false
          }
        },
        {
          prop: 'updateTime',
          label: '创建人员',
          table: {
            width: 160
          },
          form: {
            visible: false
          }
        },
        {
          prop: 'inUse',
          type: 'boolean',
          label: '是否启用',
          table: {
            slot: true,
            width: 120,
            sortable: true
          },
          form: {
            defaultValue: true
          }
        }, {
          prop: 'seq',
          label: '序号',
          type: 'number',
          table: {
            sortable: true
          },
          form: {
            defaultValue: 1
          }
        }
      ]
    }
  },
  watch: {
    dictCode () {
      this.$refs['itemTable'].load()
    }
  },
  methods: {
    /**
     * 参数格式化
     */
    handleQueryParameterFormatter (parameter) {
      parameter['dictCode@='] = this.dictCode
      return parameter
    },
    /**
     * 添加/编辑弹窗
     */
    handleAddEditDialogShow (ident: string, model: any, callBack, row) {
      if (ident === 'edit') {
        ApiService.postAjax('sys/dictItem/get', { id: row.id })
            .then(result => {
              callBack(result)
            }).catch(error => {
          this.errorMessage('获取字典项信息失败', error)
        })
      } else {
        model.dictCode = this.dictCode
        callBack(model)
      }
    },
    /**
     * 返回字典管理
     */
    handleGoBack () {
      this.$emit('update:show', false)
    }
  },
  template: `
  <smart-table-crud
    ref="itemTable"
    :height="computedTableHeight"
    queryUrl="sys/dictItem/list"
    deleteUrl="sys/dictItem/batchDelete"
    saveUpdateUrl="sys/dictItem/saveUpdate"
    defaultSortColumn="seq,inUse"
    labelWidth="100px"
    :defaultButtonConfig="defaultButtonConfig"
    tableName="字典项"
    :apiService="apiService"
    :opreaColumnWidth="150"
    :keys="['id']"
    @add-edit-dialog-show="handleAddEditDialogShow"
    :queryParameterFormatter="handleQueryParameterFormatter"
    :columnOptions="columnOptions">
    <!-- 字典编码form -->
    <template slot="form-dictCode" slot-scope="{model}">
      <el-form-item prop="dictCode" label="字典项">
        <el-input disabled v-model="model.dictCode"></el-input>
      </el-form-item>
    </template>
    <!-- 是否启用插槽 -->
    <template slot="table-inUse" slot-scope="{row}">
      <el-switch disabled v-model="row.inUse"></el-switch>
    </template>
    <!-- 上方右侧侧按钮 -->
    <template slot="top-right">
      <el-tooltip class="item" effect="dark" content="返回" placement="top">
        <el-button
          circle
          @click="handleGoBack()"
          size="small"
          type="primary"
          icon="el-icon-back"></el-button>
      </el-tooltip>
    </template>
  </smart-table-crud>
  `
}

const page = {
  components: {
    DictItem
  },
  mixins: [
    LayoutMixins
  ],
  data () {
    return {
      apiService: ApiService,
      // TODO: 权限信息, 待测试
      defaultButtonConfig: {
        add: {
          rowShow: false,
          permission: 'sys:dict:save'
        },
        edit: {
          permission: 'sys:dict:update'
        },
        delete: {
          permission: 'sys:dict:delete'
        }
      },
      // 当前的字典
      currentDict: {
        dictCode: 'xx'
      },
      showItem: false,
      columnOptions: [
        {
          prop: 'dictCode',
          label: '字典编码',
          table: {
            width: 150,
            fixed: true,
            sortable: true
          },
          form: {
            rules: true
          }
        },
        {
          prop: 'dictName',
          label: '字典名称',
          table: {
            width: 120,
            fixed: true,
            sortable: true
          },
          form: {
            rules: true
          }
        },
        {
          prop: 'createUserId',
          label: '创建人员',
          table: {
            width: 120
          },
          form: {
            visible: false
          }
        },
        {
          prop: 'createTime',
          label: '创建时间',
          table: {
            sortable: true,
            width: 170,
            formatter: (row, column, cellValue) => {
              return TimeUtils.formatTime(cellValue)
            }
          },
          form: {
            visible: false
          }
        },
        {
          prop: 'remark',
          label: '描述',
          table: {
            minWidth: 200
          },
          form: {}
        },
        {
          prop: 'updateUserId',
          label: '更新人员',
          table: {
            width: 120,
            visible: false
          },
          form: {
            visible: false
          }
        },
        {
          prop: 'inUse',
          type: 'boolean',
          label: '是否启用',
          table: {
            slot: true,
            width: 120,
            sortable: true
          },
          form: {
            defaultValue: true
          }
        },
        {
          prop: 'seq',
          label: '序号',
          type: 'number',
          table: {
            sortable: true
          },
          form: {
            defaultValue: 1
          }
        }
      ]
    }
  },
  methods: {
    handleShowDictItem (dict: any) {
      this.currentDict = dict
      this.showItem = true
    }
  },
  template: `
  <div style="padding: 15px;">
    <DictItem
      :dictCode="currentDict.dictCode"
      :show.sync="showItem"
      v-show="showItem"/>
    <smart-table-crud
      v-show="!showItem"
      :height="computedTableHeight"
      queryUrl="sys/dict/list"
      deleteUrl="sys/dict/batchDelete"
      saveUpdateUrl="sys/dict/saveUpdate"
      defaultSortColumn="seq,inUse"
      :defaultButtonConfig="defaultButtonConfig"
      tableName="字典管理"
      :keys="['dictCode']"
      :apiService="apiService"
      :columnOptions="columnOptions">
      <!-- 行按钮 -->
      <template slot="row-operation" slot-scope="{row}">
        <el-tooltip class="item" effect="dark" content="字典项" placement="top">
          <el-button
            type="primary"
            @click="handleShowDictItem(row)"
            icon="el-icon-tickets"
            size="mini"></el-button>
        </el-tooltip>
      </template>
      <!-- 是否启用插槽 -->
      <template slot="table-inUse" slot-scope="{row}">
        <el-switch disabled v-model="row.inUse"></el-switch>
      </template>
    </smart-table-crud>
  </div>
  `
}