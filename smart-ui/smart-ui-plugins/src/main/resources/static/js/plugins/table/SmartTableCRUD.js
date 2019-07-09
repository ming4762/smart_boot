define(["require", "exports", "mixins/MessageMixins", "utils/CommonUtils", "plugins/form/SmartForm", "plugins/table/SmartButtonGroup", "plugins/table/SmartTable", "plugins/table/SmartColumnVisible"], function (require, exports, MessageMixins_1, CommonUtils_1, SmartForm_1, SmartButtonGroup_1, SmartTable_1, SmartColumnVisible_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    const validatePermission = (permission, permissions) => {
        if (!permissions || permission === undefined || permission === null || permission === '') {
            return true;
        }
        return permissions.indexOf(permission) !== -1;
    };
    exports.default = {
        mixins: [
            MessageMixins_1.default
        ],
        components: {
            'smart-form': SmartForm_1.default,
            'smart-button-group': SmartButtonGroup_1.default,
            'smart-table': SmartTable_1.default,
            'smart-table-column-visible': SmartColumnVisible_1.default
        },
        props: {
            queryUrl: String,
            saveUpdateUrl: String,
            deleteUrl: String,
            getUrl: String,
            keys: {
                type: Array,
                required: true
            },
            apiService: Function,
            data: Array,
            tableName: String,
            paging: {
                type: Boolean,
                default: true
            },
            searchWithSymbol: {
                type: Boolean,
                default: true
            },
            opreaColumnWidth: {
                type: Number,
                default: 200
            },
            hasOpreaColumn: {
                type: Boolean,
                default: true
            },
            hasTopLeft: {
                type: Boolean,
                default: true
            },
            hasTopRight: {
                type: Boolean,
                default: true
            },
            tableDataFormatter: Function,
            defaultSortColumn: String,
            defaultSortOrder: String,
            defaultButtonConfig: Object,
            height: Number,
            pageLayout: {
                type: String,
                default: 'total, sizes, prev, pager, next, jumper'
            },
            pageSizes: {
                type: Array,
                default: () => { return [10, 20, 40, 100, 200]; }
            },
            pageSize: {
                type: Number,
                default: 20
            },
            position: {
                type: String,
                default: 'right'
            },
            deleteWarning: Function,
            labelWidth: {
                type: String,
                default: '80px'
            },
            formSize: String,
            queryParameterFormatter: Function,
            headerRowStyle: {
                type: Object,
                default: () => {
                    return {
                        'background-color': '#f2f2f2'
                    };
                }
            },
            columnOptions: {
                type: Array,
                required: true
            },
            permissions: Array
        },
        created() {
            const _this = this;
            _this.convertColumnOption();
            _this.order.sortName = _this.defaultSortColumn;
            _this.order.sortOrder = _this.defaultSortOrder;
        },
        mounted() {
            const _this = this;
            if (!_this.data) {
                _this.load();
            }
            else {
                _this.tableData = _this.data;
            }
        },
        data() {
            return {
                columnVisibleOption: {},
                tableColumnOptions: [],
                addEditFromColumnOptions: [],
                searchFormColumnOptions: [],
                order: {
                    sortName: '',
                    sortOrder: ''
                },
                searchDivVisible: false,
                tableLoading: false,
                selectedRow: null,
                addEditDialog: {
                    isAdd: true,
                    visible: false,
                    loading: false
                },
                selectionList: [],
                page: {
                    limit: this.pageSize,
                    offset: 0,
                    total: 0,
                    currentPage: 1
                },
                tableData: [],
                columnVisibleDialogVisible: false,
                columnVisibleResult: {},
                addEditModel: {},
                oldAddEditModel: {},
                searchModel: {},
                searchSymbol: {}
            };
        },
        methods: {
            convertColumnOption: function () {
                const tableColumnOptions = [];
                const addEditFromColumnOptions = [];
                const searchFormColumnOptions = [];
                this.columnOptions.forEach(item => {
                    if (!item.key)
                        item.key = item.prop;
                    const commonColumn = {
                        key: item.key,
                        label: item.label,
                        prop: item.prop,
                        type: item.type
                    };
                    const tableColumn = Object.assign({}, commonColumn, item.table);
                    tableColumnOptions.push(tableColumn);
                    const addEditFromColumn = Object.assign({}, commonColumn, item.form);
                    addEditFromColumnOptions.push(addEditFromColumn);
                    if (item.search) {
                        searchFormColumnOptions.push(Object.assign({}, commonColumn, item.search));
                        this.searchSymbol[item.key] = item.search.symbol ? item.search.symbol : '=';
                    }
                    if (item.table.displayControl !== false) {
                        this.columnVisibleOption[item.key] = {
                            name: item.label,
                            hidden: item.table.visible === false
                        };
                    }
                });
                if (this.hasOpreaColumn === true) {
                    tableColumnOptions.push({
                        key: 'operation_ming',
                        label: '操作',
                        prop: 'operation_ming',
                        fixed: 'right',
                        width: this.opreaColumnWidth,
                        slot: true
                    });
                }
                if (searchFormColumnOptions.length > 0) {
                    searchFormColumnOptions.push({
                        label: '',
                        prop: 'search-button'
                    });
                    searchFormColumnOptions.push({
                        label: '',
                        prop: 'reset-button'
                    });
                }
                this.searchFormColumnOptions = searchFormColumnOptions;
                this.tableColumnOptions = tableColumnOptions;
                this.addEditFromColumnOptions = addEditFromColumnOptions;
            },
            createQueryParameter: function () {
                let parameters = Object.assign({}, this.queryParameters, this.order);
                if (this.searchWithSymbol) {
                    Object.keys(this.searchModel).forEach(key => {
                        const value = this.searchModel[key];
                        const symbol = this.searchSymbol[key];
                        const searchKey = `${key}@${symbol}`;
                        parameters[searchKey] = value;
                    });
                }
                else {
                    parameters = Object.assign(parameters, this.searchModel);
                }
                if (this.paging === true) {
                    parameters = Object.assign(parameters, this.page);
                }
                return parameters;
            },
            dealData: function (data) {
                let result;
                if (this.paging === true) {
                    this.page.total = data['total'];
                    result = data['rows'];
                }
                else {
                    result = data;
                }
                return result;
            },
            load: function () {
                this.tableLoading = true;
                if (this.$listeners['before-load']) {
                    this.$emit('before-load');
                }
                let parameter = this.createQueryParameter();
                if (this.queryParameterFormatter) {
                    parameter = this.queryParameterFormatter(parameter);
                }
                this.apiService.postAjax(this.queryUrl, parameter)
                    .then(data => {
                    this.tableLoading = false;
                    let dealData = this.dealData(data);
                    if (this.tableDataFormatter) {
                        dealData = this.tableDataFormatter(dealData);
                    }
                    this.tableData = dealData;
                }).catch(error => {
                    this.tableLoading = false;
                    this.errorMessage('加载数据失败', error);
                });
            },
            handleSortChange: function (option) {
                let prop = option.prop;
                let order = option.order;
                if (order) {
                    order === 'ascending' ? (order = 'asc') : (order = 'desc');
                    this.order.sortName = prop;
                    this.order.sortOrder = order;
                    this.load();
                }
            },
            handleSizeChange: function (pageSize) {
                this.page.limit = pageSize;
                this.page.offset = (this.page.currentPage - 1) * this.page.limit;
                this.load();
            },
            handleBeforeAdd: function (row) {
                if (row)
                    this.selectedRow = row;
                this.handleAddEditDialogShow('add', row);
            },
            handleAddEditDialogShow: function (ident, row) {
                const callBack = model => {
                    this.oldAddEditModel = Object.assign({}, this.addEditModel);
                    this.addEditDialog.visible = true;
                    if (this.$refs['addEditForm']) {
                        this.$refs['addEditForm'].reset();
                    }
                    if (model) {
                        this.addEditModel = model;
                    }
                    else {
                        this.getOne(ident, row);
                    }
                };
                this.addEditModel = {};
                if (!this.$listeners['add-edit-dialog-show']) {
                    callBack(null);
                }
                else {
                    this.$emit('add-edit-dialog-show', ident, this.addEditModel, callBack, row);
                }
            },
            handleBeforeEdit: function (row) {
                if (row)
                    this.selectedRow = row;
                let rowList = [];
                if (row) {
                    rowList.push(row);
                }
                else {
                    rowList = this.selectionList;
                }
                let notifyMessage = '';
                if (rowList.length === 0) {
                    notifyMessage = '请选择一条要修改的数据';
                }
                else if (rowList.length >= 1) {
                    notifyMessage = '只能选择一条修改的数据';
                }
                if (rowList.length !== 1) {
                    this.$notify.error({
                        title: '错误',
                        message: notifyMessage,
                        duration: 5000
                    });
                }
                else {
                    this.addEditDialog.isAdd = false;
                    this.handleAddEditDialogShow('edit', rowList[0]);
                }
            },
            handleDelete: function (row) {
                const _this = this;
                let rowList = [];
                if (row) {
                    this.selectedRow = row;
                    rowList.push(row);
                }
                else {
                    if (this.selectionList.length === 0) {
                        this.$notify.error({
                            title: '错误',
                            message: '请选择要删除的数据',
                            duration: 5000
                        });
                    }
                    else {
                        rowList = this.selectionList;
                    }
                }
                const deleteList = CommonUtils_1.default.getObjectByKeys(this.keys, rowList);
                if (rowList.length > 0) {
                    let warningMessage = `您确定要删除【${rowList.length}】条数据吗？`;
                    if (this.deleteWarning) {
                        warningMessage = this.deleteWarning(rowList);
                    }
                    this.$confirm(warningMessage, '警告', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        return _this.apiService.postAjax(this.deleteUrl, deleteList);
                    }).then(result => {
                        if (this.$listeners['after-delete']) {
                            this.$emit('after-delete', result);
                        }
                        this.load();
                    }).catch(error => {
                        if (error !== 'cancel') {
                            this.errorMessage('删除时发生错误', error);
                        }
                    });
                }
            },
            handleSelectionChange: function (selectionList) {
                this.selectionList = selectionList;
                if (this.$listeners['selection-change']) {
                    this.$emit('selection-change', selectionList);
                }
            },
            handleCurrentChange: function (currentPage) {
                this.page.currentPage = currentPage;
                this.page.offset = (currentPage - 1) * this.page.limit;
                this.load();
            },
            getOne: function (ident, row) {
                if (ident === 'edit') {
                    const get = !!this.getUrl;
                    let parameters = {};
                    if (get) {
                        parameters = CommonUtils_1.default.getObjectByKeys(this.keys, [row])[0];
                    }
                    else {
                        this.keys.forEach(key => {
                            parameters[key + '@='] = row[key];
                        });
                    }
                    this.apiService.postAjax(get ? this.getUrl : this.queryUrl, parameters)
                        .then(result => {
                        if (result) {
                            const model = get ? result : (result.length === 1 ? result[0] : {});
                            this.addEditModel = model;
                        }
                    });
                }
            },
            handleSearch: function () {
                this.load();
            },
            handleRestSearch: function () {
                this.$refs['searchForm'].reset();
                this.searchModel = {};
            },
            handleColumnVisibleResult: function (result) {
                this.columnVisibleResult = result;
                this.dealColumnVisible();
            },
            dealColumnVisible: function () {
                this.tableColumnOptions.forEach(item => {
                    const columnVisible = this.columnVisibleResult[item.key];
                    if (columnVisible !== undefined && columnVisible !== null) {
                        this.$set(item, 'visible', columnVisible);
                    }
                });
            },
            handleClickGroupButton: function (ident) {
                console.log(ident);
                switch (ident) {
                    case 'delete':
                        this.handleDelete();
                        break;
                    case 'columnVisible':
                        this.columnVisibleDialogVisible = true;
                        break;
                    case 'add':
                        this.addEditDialog.isAdd = true;
                        this.handleBeforeAdd();
                        break;
                    case 'edit':
                        this.handleBeforeEdit();
                        break;
                    case 'refresh':
                        this.load();
                        break;
                    case 'search':
                        this.handleShowSearch();
                        break;
                }
            },
            getAddEditDialogTitle: function () {
                return (this.addEditDialog.isAdd === true ? '新增' : '编辑') + (this.tableName ? this.tableName : '');
            },
            handleShowSearch: function () {
                this.searchDivVisible = !this.searchDivVisible;
            },
            saveUpdate: function (event) {
                this.$refs['addEditForm'].validate()
                    .then(validate => {
                    if (validate === true) {
                        this.addEditDialog.loading = true;
                        if (this.addEditDialog.isAdd) {
                            const listener = 'before-add';
                            if (this.$listeners[listener]) {
                                this.$emit(listener, this.addEditModel);
                            }
                        }
                        else {
                            const listener = 'before-edit';
                            if (this.$listeners[listener]) {
                                this.$emit(listener, this.addEditModel, this.oldAddEditModel);
                            }
                        }
                        return this.apiService.postAjax(this.saveUpdateUrl, this.addEditModel);
                    }
                }).then(result => {
                    this.addEditDialog.loading = false;
                    this.addEditDialog.visible = false;
                    this.$notify({
                        title: '保存成功',
                        message: '',
                        type: 'success'
                    });
                    this.load();
                }).catch(error => {
                    this.addEditDialog.loading = false;
                    this.errorMessage(`保存${this.tableName}发生错误`, error);
                });
            },
            isButtonShow: function (buttonConfig) {
                let topShow = true;
                let rowShow = true;
                if (buttonConfig.rowShow === false || !validatePermission(buttonConfig.permission, this.permissions)) {
                    rowShow = false;
                }
                if (buttonConfig.topShow === false || !validatePermission(buttonConfig.permission, this.permissions)) {
                    topShow = false;
                }
                return [topShow, rowShow];
            }
        },
        computed: {
            getPaginationStyle: function () {
                let style = '';
                switch (this.position) {
                    case 'left':
                        style = 'float: left;';
                        break;
                    case 'right':
                        style = 'float: right;';
                        break;
                    case 'center':
                        style = 'text-align: center;';
                }
                return style;
            },
            getTableHeight: function () {
                let height = this.height;
                if (height) {
                    if (this.hasTopLeft === true || this.hasTopRight === true) {
                        height = height - 40;
                    }
                    if (this.paging === true) {
                        height = height - 42;
                    }
                    return height - 1;
                }
                else {
                    return null;
                }
            },
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
                };
                let defaultButton = this.defaultButtonConfig;
                if (defaultButton) {
                    for (let key in result) {
                        if (defaultButton[key]) {
                            let showConfig = this.isButtonShow(defaultButton[key]);
                            result[key].top = showConfig[0];
                            result[key].row = showConfig[1];
                        }
                    }
                }
                return result;
            },
            getTableColumnSolt: function () {
                const result = {};
                for (let key in this.$scopedSlots) {
                    if (key.indexOf('table-') === 0) {
                        result[key] = key.substring(6);
                    }
                }
                return result;
            },
            getFormSolts: function () {
                let result = {};
                for (let key in this.$scopedSlots) {
                    if (key.indexOf('form-') === 0) {
                        result[key] = key.substring(5);
                    }
                }
                return result;
            },
            getSearchSolts: function () {
                let result = {};
                for (let key in this.$scopedSlots) {
                    if (key.indexOf('search-') === 0) {
                        result[key] = key.substring(7);
                    }
                }
                return result;
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
    };
});
