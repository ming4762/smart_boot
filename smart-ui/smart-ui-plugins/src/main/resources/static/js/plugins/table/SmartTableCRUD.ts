// @ts-ignore
import MessageMixins from '../../mixins/MessageMixins.js'
// @ts-ignore
import CommonUtils from '../../utils/CommonUtils.js'


//引入组件
import SmartForm from '../form/SmartForm.js'
import SmartButtonGroup from './SmartButtonGroup.js'
import SmartTable from './SmartTable.js'
import SmartColumnVisible from './SmartColumnVisible.js'

/**
 * 验证权限
 */
const validatePermission = (permission, permissions) => {
  if (!permissions || permission === undefined || permission === null || permission === '') {
    return true
  }
  return permissions.indexOf(permission) !== -1
}

export default {
  mixins: [
    MessageMixins
  ],
  components: {
    'smart-form': SmartForm,
    'smart-button-group': SmartButtonGroup,
    'smart-table': SmartTable,
    'smart-table-column-visible': SmartColumnVisible
  },
  props: {
    // 查询url
    queryUrl: String,
    // 保存修改url
    saveUpdateUrl: String,
    // 删除url
    deleteUrl: String,
    // 查询URL
    getUrl: String,
    // 表格对应实体类的key
    keys: {
      type: Array,
      required: true
    },
    // 后台请求工具
    apiService: Function,
    // 表格数据
    data: Array,
    // 表格名称
    tableName: String,
    // 是否分页
    paging: {
      type: Boolean,
      default: true
    },
    // 搜索是否添加符号
    searchWithSymbol: {
      type: Boolean,
      default: true
    },
    // 操作列宽度
    opreaColumnWidth: {
      type: Number,
      default: 200
    },
    // 是否有操作列
    hasOpreaColumn: {
      type: Boolean,
      default: true
    },
    // 是否有顶部左侧按钮
    hasTopLeft: {
      type: Boolean,
      default: true
    },
    // 是否有顶部右侧按钮
    hasTopRight: {
      type: Boolean,
      default: true
    },
    // 数据格式化函数
    tableDataFormatter: Function,
    // 默认的排序列
    defaultSortColumn: String,
    // 默认的排序方向
    defaultSortOrder: String,
    // 默认按钮配置
    defaultButtonConfig: Object,
    // 表格高度
    height: Number,
    // 分页器组件
    pageLayout: {
      type: String,
      default: 'total, sizes, prev, pager, next, jumper'
    },
    // 分页大小可选
    pageSizes: {
      type: Array,
      default: () => { return [10, 20, 40, 100, 200] }
    },
    // 分页大小
    pageSize: {
      type: Number,
      default: 20
    },
    // 分页器位置
    position: {
      type: String,
      default: 'right'
    },
    // 删除警告
    deleteWarning: Function,
    // form的labelwidht
    labelWidth: {
      type: String,
      default: '80px'
    },
    // form大小
    formSize: String,
    // 参数格式化函数
    queryParameterFormatter: Function,
    // 头部样式
    headerRowStyle: {
      type: Object,
      default: () => {
        return {
          'background-color': '#f2f2f2'
        }
      }
    },
    // 列配置
    columnOptions: {
      type: Array,
      required: true
    },
    // 用户权限信息
    permissions: Array
  },
  // 生命周期钩子
  created () {
    const _this: any = this
    _this.convertColumnOption()
    _this.order.sortName = _this.defaultSortColumn
    _this.order.sortOrder = _this.defaultSortOrder
  },
  mounted () {
    const _this: any = this
    if (!_this.data) {
      _this.load()
    } else {
      _this.tableData = _this.data
    }
  },
  data () {
    return {
      // 列显示隐藏配置
      columnVisibleOption: {},
      // 表格列配置
      tableColumnOptions: [],
      // 添加修改弹窗配置
      addEditFromColumnOptions: [],
      // 搜索form配置
      searchFormColumnOptions: [],
      // 排序信息
      order: {
        sortName: '',
        sortOrder: ''
      },
      // 搜索DIV显示状态
      searchDivVisible: false,
      // 表格加载状态
      tableLoading: false,
      // 选中列
      selectedRow: null,
      // 添加修改弹窗配置
      addEditDialog: {
        isAdd: true,
        visible: false,
        loading: false
      },
      // 选中的列
      selectionList: [],
      // 分页参数
      page: {
        // @ts-ignore
        limit: this.pageSize,
        // 起始记录数
        offset: 0,
        // 总记录数
        total: 0,
        // 当前页
        currentPage: 1
      },
      // 表格数据
      tableData: [],
      // 控制列显示隐藏
      columnVisibleDialogVisible: false,
      // 列显示隐藏的结果
      columnVisibleResult: {},
      // 添加/修改model
      addEditModel: {},
      oldAddEditModel: {},
      // 搜索model绑定
      searchModel: {},
      // 搜索符号
      searchSymbol: {}
    }
  },
  methods: {
    /**
     * 配置转换
     */
    convertColumnOption: function () {
      // 表格配置
      const tableColumnOptions = []
      // 添加修改form配置
      const addEditFromColumnOptions = []
      // 搜索form配置
      const searchFormColumnOptions = []
      this.columnOptions.forEach(item => {
        // 设置key
        if (!item.key) item.key = item.prop
        const commonColumn = {
          key: item.key,
          label: item.label,
          prop: item.prop,
          type: item.type
        }
        // @ts-ignore
        const tableColumn = Object.assign({}, commonColumn, item.table)
        tableColumnOptions.push(tableColumn)

        // 添加修改弹窗配置
        // @ts-ignore
        const addEditFromColumn = Object.assign({}, commonColumn, item.form)
        addEditFromColumnOptions.push(addEditFromColumn)
        // 添加搜索配置
        if (item.search) {
          // @ts-ignore
          searchFormColumnOptions.push(Object.assign({}, commonColumn, item.search))
          // search符号，默认=
          this.searchSymbol[item.key] = item.search.symbol ? item.search.symbol : '='
        }
        // 列显示隐藏配置
        if (item.table.displayControl !== false) {
          this.columnVisibleOption[item.key] = {
            name: item.label,
            hidden: item.table.visible === false
          }
        }
      })

      // 添加操作列
      if (this.hasOpreaColumn === true) {
        tableColumnOptions.push({
          key: 'operation_ming',
          label: '操作',
          prop: 'operation_ming',
          fixed: 'right',
          width: this.opreaColumnWidth,
          slot: true
        })
      }
      if (searchFormColumnOptions.length > 0) {
        searchFormColumnOptions.push({
          label: '',
          prop: 'search-button'
        })
        searchFormColumnOptions.push({
          label: '',
          prop: 'reset-button'
        })
      }
      this.searchFormColumnOptions = searchFormColumnOptions
      this.tableColumnOptions = tableColumnOptions
      this.addEditFromColumnOptions = addEditFromColumnOptions
    },
    // 创造查询条件
    createQueryParameter: function () {
      // @ts-ignore
      let parameters = Object.assign({}, this.queryParameters, this.order)
      // 添加searchModel条件
      if (this.searchWithSymbol) {
        Object.keys(this.searchModel).forEach(key => {
          const value = this.searchModel[key]
          const symbol = this.searchSymbol[key]
          const searchKey = `${key}@${symbol}`
          parameters[searchKey] = value
        })
      } else {
        // @ts-ignore
        parameters = Object.assign(parameters, this.searchModel)
      }
      if (this.paging === true) {
        //  添加分页条件
        // @ts-ignore
        parameters = Object.assign(parameters, this.page)
      }
      return parameters
    },
    /**
     * 加载完毕处理数据
     */
    dealData: function (data) {
      let result
      if (this.paging === true) {
        this.page.total = data['total']
        result = data['rows']
      } else {
        result = data
      }
      return result
    },
    /**
     * 加载数据方法
     */
    load: function () {
      this.tableLoading = true
      // 执行加载前事件
      if (this.$listeners['before-load']) {
        this.$emit('before-load')
      }
      // 创造查询条件
      let parameter = this.createQueryParameter()
      if (this.queryParameterFormatter) {
        parameter = this.queryParameterFormatter(parameter)
      }
      // 执行查询
      this.apiService.postAjax(this.queryUrl, parameter)
          .then(data => {
            this.tableLoading = false
            let dealData = this.dealData(data)
            if (this.tableDataFormatter) {
              // 执行数据格式化函数
              dealData = this.tableDataFormatter(dealData)
            }
            this.tableData = dealData
          }).catch(error => {
        this.tableLoading = false
        this.errorMessage('加载数据失败', error)
      })
    },
    /**
     * 监控排序变化，并重新加载数据
     * TODO: 数据已设置的情况下，排序的设定
     * // 如果数据已存在，则该方法触发事件，由上层处理排序
     */
    handleSortChange: function (option) {
      let prop = option.prop
      let order = option.order
      if (order) {
        order === 'ascending' ? (order = 'asc') : (order = 'desc')
        this.order.sortName = prop
        this.order.sortOrder = order
        this.load()
      }
    },

    /**
     * 分页器pageSize改变是触发
     */
    handleSizeChange: function (pageSize) {
      this.page.limit = pageSize
      this.page.offset = (this.page.currentPage - 1) * this.page.limit
      this.load()
    },
    // 添加前操作
    handleBeforeAdd: function (row) {
      if (row) this.selectedRow = row
      this.handleAddEditDialogShow('add', row)
    },
    /**
     * 添加修改弹窗打开时执行
     */
    handleAddEditDialogShow: function (ident, row) {
      // 回调函数
      const callBack = model => {
        // @ts-ignore
        this.oldAddEditModel = Object.assign({}, this.addEditModel)
        this.addEditDialog.visible = true
        // TODO: 可能存在bug
        if (this.$refs['addEditForm']) {
          this.$refs['addEditForm'].reset()
        }
        if (model) {
          this.addEditModel = model
        } else {
          this.getOne(ident, row)
        }
      }
      // 重置表单
      this.addEditModel = {}
      // 如果没有监听事件，执行callback
      if (!this.$listeners['add-edit-dialog-show']) {
        callBack(null)
      } else {
        this.$emit('add-edit-dialog-show', ident, this.addEditModel, callBack, row)
      }
    },
    /**
     * 编辑前操作
     */
    handleBeforeEdit: function (row) {
      if (row) this.selectedRow = row
      let rowList = []
      if (row) {
        rowList.push(row)
      } else {
        rowList = this.selectionList
      }
      let notifyMessage = ''
      if (rowList.length === 0) {
        notifyMessage = '请选择一条要修改的数据'
      } else if (rowList.length >= 1) {
        notifyMessage = '只能选择一条修改的数据'
      }
      if (rowList.length !== 1) {
        this.$notify.error({
          title: '错误',
          message: notifyMessage,
          duration: 5000
        })
      } else {
        this.addEditDialog.isAdd = false
        this.handleAddEditDialogShow('edit', rowList[0])
      }
    },
    /**
     * 执行删除操作
     * @param row
     */
    handleDelete: function (row) {
      const _this = this
      let rowList = []
      if (row) {
        this.selectedRow = row
        rowList.push(row)
      } else {
        // 删除警告
        if (this.selectionList.length === 0) {
          this.$notify.error({
            title: '错误',
            message: '请选择要删除的数据',
            duration: 5000
          })
        } else {
          rowList = this.selectionList
        }
      }
      const deleteList = CommonUtils.getObjectByKeys(this.keys, rowList)
      if (rowList.length > 0) {
        // 删除警告语
        let warningMessage = `您确定要删除【${rowList.length}】条数据吗？`
        if (this.deleteWarning) {
          warningMessage = this.deleteWarning(rowList)
        }
        this.$confirm(warningMessage, '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return _this.apiService.postAjax(this.deleteUrl, deleteList)
        }).then(result => {
          // 执行删除后事件
          if (this.$listeners['after-delete']) {
            this.$emit('after-delete', result)
          }
          this.load()
        }).catch(error => {
          if (error !== 'cancel') {
            // todo:待完善
            this.errorMessage('删除时发生错误', error)
          }
        })
      }
    },
    /**
     * 复选框列发生变化时
     * @param selectionList
     */
    handleSelectionChange: function (selectionList) {
      this.selectionList = selectionList
      if (this.$listeners['selection-change']) {
        this.$emit('selection-change', selectionList)
      }
    },
    /**
     * 分页器currentPage 改变时会触发
     */
    handleCurrentChange: function (currentPage) {
      this.page.currentPage = currentPage
      this.page.offset = (currentPage - 1) * this.page.limit
      this.load()
    },
    // 查询一个
    getOne: function (ident, row) {
      if (ident === 'edit') {
        const get = !!this.getUrl
        let parameters = {}
        // 查询结果
        if (get) {
          parameters = CommonUtils.getObjectByKeys(this.keys, [row])[0]
        } else {
          this.keys.forEach(key => {
            parameters[key + '@='] = row[key]
          })
        }
        // 查询
        this.apiService.postAjax(get ? this.getUrl : this.queryUrl, parameters)
            .then(result => {
              if (result) {
                const model = get ? result : (result.length === 1 ? result[0] : {})
                this.addEditModel = model
              }
            })
      }
    },
    /**
     * 执行查询
     */
    handleSearch: function () {
      this.load()
    },
    handleRestSearch: function () {
      this.$refs['searchForm'].reset()
      this.searchModel = {}
    },
    /**
     * 列显示隐藏配置事件
     */
    handleColumnVisibleResult: function (result) {
      this.columnVisibleResult = result
      this.dealColumnVisible()
    },
    /**
     * 处理列显示隐藏
     */
    dealColumnVisible: function () {
      this.tableColumnOptions.forEach(item => {
        const columnVisible = this.columnVisibleResult[item.key]
        if (columnVisible !== undefined && columnVisible !== null) {
          this.$set(item, 'visible', columnVisible)
        }
      })
    },
    /**
     * 点击顶部按钮组
     */
    handleClickGroupButton: function (ident) {
      console.log(ident)
      switch (ident) {
        case 'delete':
          this.handleDelete()
          break
        case 'columnVisible':
          // 显示列选择弹窗
          this.columnVisibleDialogVisible = true
          break
        case 'add':
          this.addEditDialog.isAdd = true
          this.handleBeforeAdd()
          break
        case 'edit':
          this.handleBeforeEdit()
          break
        case 'refresh':
          this.load()
          break
        case 'search':
          this.handleShowSearch()
          break
      }
    },
    // 获取添加修改弹窗form
    getAddEditDialogTitle: function () {
      return (this.addEditDialog.isAdd === true ? '新增' : '编辑') + (this.tableName ? this.tableName : '')
    },
    /**
     * 显示搜索窗口
     * TODO: 开发中
     */
    handleShowSearch: function () {
      this.searchDivVisible = !this.searchDivVisible
    },
    /**
     * 添加/修改方法
     */
    saveUpdate: function (event) {
      this.$refs['addEditForm'].validate()
          .then(validate => {
            if (validate === true) {
              // 开始加载
              this.addEditDialog.loading = true
              if (this.addEditDialog.isAdd) {
                // 添加前事件
                const listener = 'before-add'
                if (this.$listeners[listener]) {
                  this.$emit(listener, this.addEditModel)
                }
              } else {
                // 编辑前事件
                const listener = 'before-edit'
                if (this.$listeners[listener]) {
                  this.$emit(listener, this.addEditModel, this.oldAddEditModel)
                }
              }
              // 执行操作
              return this.apiService.postAjax(this.saveUpdateUrl, this.addEditModel)
            }
          }).then(result => {
        this.addEditDialog.loading = false
        this.addEditDialog.visible = false
        this.$notify({
          title: '保存成功',
          message: '',
          type: 'success'
        })
        this.load()
      }).catch(error => {
        this.addEditDialog.loading = false
        this.errorMessage(`保存${this.tableName}发生错误`, error)
      })
    },
    /**
     * 判断按钮是否显示
     */
    isButtonShow: function (buttonConfig) {
      let topShow = true
      let rowShow = true
      if (buttonConfig.rowShow === false || !validatePermission(buttonConfig.permission, this.permissions)) {
        rowShow = false
      }
      if (buttonConfig.topShow === false || !validatePermission(buttonConfig.permission, this.permissions)) {
        topShow = false
      }
      return [topShow, rowShow]
    }
  },
  computed: {
    /**
     * 获取分页器样式
     */
    getPaginationStyle: function () {
      let style = ''
      switch (this.position) {
        case 'left':
          style = 'float: left;'
          break
        case 'right':
          style = 'float: right;'
          break
        case 'center':
          style = 'text-align: center;'
      }
      return style
    },
    /**
     * 获取表格高度
     * @returns {null|*}
     */
    getTableHeight: function () {
      let height = this.height
      if (height) {
        // 去除顶部按钮组高度
        if (this.hasTopLeft === true || this.hasTopRight === true) {
          height = height - 40
        }
        // 去除分页器高度
        if (this.paging === true) {
          height = height - 42
        }
        return height - 1
      } else {
        return null
      }
    },
    /**
     * 默认按钮配置
     */
    getDefaultButtonShow: function () {
      let result = {
        add: {
          row: true,
          top: true
        },
        edit: {
          row: true,
          top: true
        },
        delete: {
          row: true,
          top: true
        }
      }
      let defaultButton = this.defaultButtonConfig
      if (defaultButton) {
        for (let key in result) {
          if (defaultButton[key]) {
            let showConfig = this.isButtonShow(defaultButton[key])
            result[key].top = showConfig[0]
            result[key].row = showConfig[1]
          }
        }
      }
      return result
    },
    // 表格列插槽
    getTableColumnSolt: function () {
      const result = {}
      for (let key in this.$scopedSlots) {
        if (key.indexOf('table-') === 0) {
          result[key] = key.substring(6)
        }
      }
      return result
    },
    // 获取form插槽
    getFormSolts: function () {
      let result = {}
      for (let key in this.$scopedSlots) {
        if (key.indexOf('form-') === 0) {
          result[key] = key.substring(5)
        }
      }
      return result
    },
    // 获取搜索插槽
    getSearchSolts: function () {
      let result = {}
      for (let key in this.$scopedSlots) {
        if (key.indexOf('search-') === 0) {
          result[key] = key.substring(7)
        }
      }
      return result
    }
  },
  template: `
  <div>
    <!--搜索form-->
    <transition v-if="searchFormColumnOptions.length > 0" name="fade">
      <div className="table-search-container" v-show="searchDivVisible">
        <smart-form
          :columnOptions="searchFormColumnOptions"
          :model="searchModel"
          size="small"
          inline
          ref="searchForm">
          <!--遍历插槽-->
          <template
            v-for="(value, key) in getSearchSolts"
            slot-scope="{column, model}"
            :slot="value">
            <slot
              :column="column"
              :model="model"
              :name="key"></slot>
          </template>
          <!--搜索重置插槽-->
          <template v-slot:search-button="{model}">
            <el-form-item>
              <el-button
                type="primary"
                @click="handleSearch"
                icon="el-icon-search">查询</el-button>
            </el-form-item>
          </template>
          <template v-slot:reset-button="{model}">
            <el-form-item>
              <el-button
                type="info"
                @click="handleRestSearch"
                icon="el-icon-delete">重置</el-button>
            </el-form-item>
          </template>
        </smart-form>
      </div>
    </transition>
    <!--操作组-->
    <smart-button-group
      v-if="hasTopLeft || hasTopRight"
      :has-left="hasTopLeft"
      :has-right="hasTopRight"
      :add-show="getDefaultButtonShow.add.top"
      :edit-show="getDefaultButtonShow.edit.top"
      :delete-show="getDefaultButtonShow.delete.top"
      @button-click="handleClickGroupButton"
      class="cloud-table-menu">
      <!--按钮插槽-->
      <template slot="buttonLeft">
        <slot name="top-left"></slot>
      </template>
      <template slot="buttonRight">
        <slot name="top-right"></slot>
      </template>
    </smart-button-group>
    
    <!--表格主体-->
    <smart-table
      v-bind="$attrs"
      v-on="$listeners"
      v-loading="tableLoading"
      :keys="keys"
      :data="tableData"
      :columnOptions="tableColumnOptions"
      :height="getTableHeight"
      @sort-change="handleSortChange"
      @selection-change="handleSelectionChange"
      :header-row-style="headerRowStyle">
      <!--操作列插槽-->
      <template v-if="hasOpreaColumn" slot="operation_ming" slot-scope="{row, column, $index}">
        <el-tooltip v-if="getDefaultButtonShow.add.row" effect="dark" content="添加" placement="top">
          <el-button
            size="mini"
            type="primary"
            icon="el-icon-plus"
            @click="handleBeforeAdd(row)"/>
        </el-tooltip>
        <el-tooltip v-if="getDefaultButtonShow.edit.row" effect="dark" content="编辑" placement="top">
          <el-button
            size="mini"
            type="warning"
            icon="el-icon-edit"
            @click="handleBeforeEdit(row)"/>
        </el-tooltip>
        <el-tooltip effect="dark" content="删除" placement="top">
          <el-button
            v-if="getDefaultButtonShow.delete.row"
            size="mini"
            type="danger"
            icon="el-icon-delete"
            @click="handleDelete(row)"/>
        </el-tooltip>
        <slot
          :row="row"
          :column="column"
          :$index="$index"
          name="row-operation"/>
      </template>
      <!--表格列插槽--> 
      <template
        slot-scope="{row, column, $index}"
        :slot="value"
        v-for="(value, key) in getTableColumnSolt">
        <slot :column="column" :row="row" :$index="$index" :name="key"></slot>
      </template>
    </smart-table>
    <!--分页器-->
    <div
      :style="getPaginationStyle"
      class="cloud-table-pagination"
      v-if="paging">
      <el-pagination
        :page-sizes="pageSizes"
        :page-size="pageSize"
        :layout="pageLayout"
        :total="page.total"
        :current-page="1"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"/>  
    </div>
    <!--列显示隐藏弹窗-->
    <el-dialog
      title="选择列"
      :visible.sync="columnVisibleDialogVisible"
      append-to-body>
      <smart-table-column-visible
        @result-change="handleColumnVisibleResult"
        :column-show="columnVisibleOption"></smart-table-column-visible>
    </el-dialog>
    <!--添加修改弹窗-->
    <el-dialog
      append-to-body
      :visible.sync="addEditDialog.visible"
      :title="getAddEditDialogTitle()">
      <smart-form
        :model="addEditModel"
        :label-width="labelWidth"
        :columnOptions="addEditFromColumnOptions"
        v-loading="addEditDialog.loading"
        ref="addEditForm">
        <!--form插槽-->
        <template
          v-for="(value, key) in getFormSolts"
          slot-scope="{column, model}"
          :slot="value">
          <slot
            :column="column"
            :model="model"
            :name="key"></slot>
        </template>
      </smart-form>
      <div slot="footer">
        <el-button @click="addEditDialog.visible = false">取 消</el-button>
        <el-button type="primary" @click="saveUpdate">保存</el-button>
      </div>  
    </el-dialog>
  </div>
  `
}
