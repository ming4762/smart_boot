define(["require", "exports", "mixins/MessageMixins", "utils/CommonUtils", "plugins/form/SmartForm", "plugins/table/SmartButtonGroup", "plugins/table/SmartTable", "plugins/table/SmartColumnVisible"], function (require, exports, MessageMixins_1, CommonUtils_1, SmartForm_1, SmartButtonGroup_1, SmartTable_1, SmartColumnVisible_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var validatePermission = function (permission, permissions) {
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
                default: function () { return [10, 20, 40, 100, 200]; }
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
                default: function () {
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
        created: function () {
            var _this = this;
            _this.convertColumnOption();
            _this.order.sortName = _this.defaultSortColumn;
            _this.order.sortOrder = _this.defaultSortOrder;
        },
        mounted: function () {
            var _this = this;
            if (!_this.data) {
                _this.load();
            }
            else {
                _this.tableData = _this.data;
            }
        },
        data: function () {
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
                var _this_1 = this;
                var tableColumnOptions = [];
                var addEditFromColumnOptions = [];
                var searchFormColumnOptions = [];
                this.columnOptions.forEach(function (item) {
                    if (!item.key)
                        item.key = item.prop;
                    var commonColumn = {
                        key: item.key,
                        label: item.label,
                        prop: item.prop,
                        type: item.type
                    };
                    var tableColumn = Object.assign({}, commonColumn, item.table);
                    tableColumnOptions.push(tableColumn);
                    var addEditFromColumn = Object.assign({}, commonColumn, item.form);
                    addEditFromColumnOptions.push(addEditFromColumn);
                    if (item.search) {
                        searchFormColumnOptions.push(Object.assign({}, commonColumn, item.search));
                        _this_1.searchSymbol[item.key] = item.search.symbol ? item.search.symbol : '=';
                    }
                    if (item.table.displayControl !== false) {
                        _this_1.columnVisibleOption[item.key] = {
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
                var _this_1 = this;
                var parameters = Object.assign({}, this.queryParameters, this.order);
                if (this.searchWithSymbol) {
                    Object.keys(this.searchModel).forEach(function (key) {
                        var value = _this_1.searchModel[key];
                        var symbol = _this_1.searchSymbol[key];
                        var searchKey = key + "@" + symbol;
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
                var result;
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
                var _this_1 = this;
                this.tableLoading = true;
                if (this.$listeners['before-load']) {
                    this.$emit('before-load');
                }
                var parameter = this.createQueryParameter();
                if (this.queryParameterFormatter) {
                    parameter = this.queryParameterFormatter(parameter);
                }
                this.apiService.postAjax(this.queryUrl, parameter)
                    .then(function (data) {
                    _this_1.tableLoading = false;
                    var dealData = _this_1.dealData(data);
                    if (_this_1.tableDataFormatter) {
                        dealData = _this_1.tableDataFormatter(dealData);
                    }
                    _this_1.tableData = dealData;
                }).catch(function (error) {
                    _this_1.tableLoading = false;
                    _this_1.errorMessage('加载数据失败', error);
                });
            },
            handleSortChange: function (option) {
                var prop = option.prop;
                var order = option.order;
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
                var _this_1 = this;
                var callBack = function (model) {
                    _this_1.oldAddEditModel = Object.assign({}, _this_1.addEditModel);
                    _this_1.addEditDialog.visible = true;
                    if (_this_1.$refs['addEditForm']) {
                        _this_1.$refs['addEditForm'].reset();
                    }
                    if (model) {
                        _this_1.addEditModel = model;
                    }
                    else {
                        _this_1.getOne(ident, row);
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
                var rowList = [];
                if (row) {
                    rowList.push(row);
                }
                else {
                    rowList = this.selectionList;
                }
                var notifyMessage = '';
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
                var _this_1 = this;
                var _this = this;
                var rowList = [];
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
                var deleteList = CommonUtils_1.default.getObjectByKeys(this.keys, rowList);
                if (rowList.length > 0) {
                    var warningMessage = "\u60A8\u786E\u5B9A\u8981\u5220\u9664\u3010" + rowList.length + "\u3011\u6761\u6570\u636E\u5417\uFF1F";
                    if (this.deleteWarning) {
                        warningMessage = this.deleteWarning(rowList);
                    }
                    this.$confirm(warningMessage, '警告', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(function () {
                        return _this.apiService.postAjax(_this_1.deleteUrl, deleteList);
                    }).then(function (result) {
                        if (_this_1.$listeners['after-delete']) {
                            _this_1.$emit('after-delete', result);
                        }
                        _this_1.load();
                    }).catch(function (error) {
                        if (error !== 'cancel') {
                            _this_1.errorMessage('删除时发生错误', error);
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
                var _this_1 = this;
                if (ident === 'edit') {
                    var get_1 = !!this.getUrl;
                    var parameters_1 = {};
                    if (get_1) {
                        parameters_1 = CommonUtils_1.default.getObjectByKeys(this.keys, [row])[0];
                    }
                    else {
                        this.keys.forEach(function (key) {
                            parameters_1[key + '@='] = row[key];
                        });
                    }
                    this.apiService.postAjax(get_1 ? this.getUrl : this.queryUrl, parameters_1)
                        .then(function (result) {
                        if (result) {
                            var model = get_1 ? result : (result.length === 1 ? result[0] : {});
                            _this_1.addEditModel = model;
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
                var _this_1 = this;
                this.tableColumnOptions.forEach(function (item) {
                    var columnVisible = _this_1.columnVisibleResult[item.key];
                    if (columnVisible !== undefined && columnVisible !== null) {
                        _this_1.$set(item, 'visible', columnVisible);
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
                var _this_1 = this;
                this.$refs['addEditForm'].validate()
                    .then(function (validate) {
                    if (validate === true) {
                        _this_1.addEditDialog.loading = true;
                        if (_this_1.addEditDialog.isAdd) {
                            var listener = 'before-add';
                            if (_this_1.$listeners[listener]) {
                                _this_1.$emit(listener, _this_1.addEditModel);
                            }
                        }
                        else {
                            var listener = 'before-edit';
                            if (_this_1.$listeners[listener]) {
                                _this_1.$emit(listener, _this_1.addEditModel, _this_1.oldAddEditModel);
                            }
                        }
                        return _this_1.apiService.postAjax(_this_1.saveUpdateUrl, _this_1.addEditModel);
                    }
                }).then(function (result) {
                    _this_1.addEditDialog.loading = false;
                    _this_1.addEditDialog.visible = false;
                    _this_1.$notify({
                        title: '保存成功',
                        message: '',
                        type: 'success'
                    });
                    _this_1.load();
                }).catch(function (error) {
                    _this_1.addEditDialog.loading = false;
                    _this_1.errorMessage("\u4FDD\u5B58" + _this_1.tableName + "\u53D1\u751F\u9519\u8BEF", error);
                });
            },
            isButtonShow: function (buttonConfig) {
                var topShow = true;
                var rowShow = true;
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
                var style = '';
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
                var height = this.height;
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
                var result = {
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
                var defaultButton = this.defaultButtonConfig;
                if (defaultButton) {
                    for (var key in result) {
                        if (defaultButton[key]) {
                            var showConfig = this.isButtonShow(defaultButton[key]);
                            result[key].top = showConfig[0];
                            result[key].row = showConfig[1];
                        }
                    }
                }
                return result;
            },
            getTableColumnSolt: function () {
                var result = {};
                for (var key in this.$scopedSlots) {
                    if (key.indexOf('table-') === 0) {
                        result[key] = key.substring(6);
                    }
                }
                return result;
            },
            getFormSolts: function () {
                var result = {};
                for (var key in this.$scopedSlots) {
                    if (key.indexOf('form-') === 0) {
                        result[key] = key.substring(5);
                    }
                }
                return result;
            },
            getSearchSolts: function () {
                var result = {};
                for (var key in this.$scopedSlots) {
                    if (key.indexOf('search-') === 0) {
                        result[key] = key.substring(7);
                    }
                }
                return result;
            }
        },
        template: "\n  <div>\n    <!--\u641C\u7D22form-->\n    <transition v-if=\"searchFormColumnOptions.length > 0\" name=\"fade\">\n      <div className=\"table-search-container\" v-show=\"searchDivVisible\">\n        <smart-form\n          :columnOptions=\"searchFormColumnOptions\"\n          :model=\"searchModel\"\n          size=\"small\"\n          inline\n          ref=\"searchForm\">\n          <!--\u904D\u5386\u63D2\u69FD-->\n          <template\n            v-for=\"(value, key) in getSearchSolts\"\n            slot-scope=\"{column, model}\"\n            :slot=\"value\">\n            <slot\n              :column=\"column\"\n              :model=\"model\"\n              :name=\"key\"></slot>\n          </template>\n          <!--\u641C\u7D22\u91CD\u7F6E\u63D2\u69FD-->\n          <template v-slot:search-button=\"{model}\">\n            <el-form-item>\n              <el-button\n                type=\"primary\"\n                @click=\"handleSearch\"\n                icon=\"el-icon-search\">\u67E5\u8BE2</el-button>\n            </el-form-item>\n          </template>\n          <template v-slot:reset-button=\"{model}\">\n            <el-form-item>\n              <el-button\n                type=\"info\"\n                @click=\"handleRestSearch\"\n                icon=\"el-icon-delete\">\u91CD\u7F6E</el-button>\n            </el-form-item>\n          </template>\n        </smart-form>\n      </div>\n    </transition>\n    <!--\u64CD\u4F5C\u7EC4-->\n    <smart-button-group\n      v-if=\"hasTopLeft || hasTopRight\"\n      :has-left=\"hasTopLeft\"\n      :has-right=\"hasTopRight\"\n      :add-show=\"getDefaultButtonShow.add.top\"\n      :edit-show=\"getDefaultButtonShow.edit.top\"\n      :delete-show=\"getDefaultButtonShow.delete.top\"\n      @button-click=\"handleClickGroupButton\"\n      class=\"cloud-table-menu\">\n      <!--\u6309\u94AE\u63D2\u69FD-->\n      <template slot=\"buttonLeft\">\n        <slot name=\"top-left\"></slot>\n      </template>\n      <template slot=\"buttonRight\">\n        <slot name=\"top-right\"></slot>\n      </template>\n    </smart-button-group>\n    \n    <!--\u8868\u683C\u4E3B\u4F53-->\n    <smart-table\n      v-bind=\"$attrs\"\n      v-on=\"$listeners\"\n      v-loading=\"tableLoading\"\n      :keys=\"keys\"\n      :data=\"tableData\"\n      :columnOptions=\"tableColumnOptions\"\n      :height=\"getTableHeight\"\n      @sort-change=\"handleSortChange\"\n      @selection-change=\"handleSelectionChange\"\n      :header-row-style=\"headerRowStyle\">\n      <!--\u64CD\u4F5C\u5217\u63D2\u69FD-->\n      <template v-if=\"hasOpreaColumn\" slot=\"operation_ming\" slot-scope=\"{row, column, $index}\">\n        <el-tooltip v-if=\"getDefaultButtonShow.add.row\" effect=\"dark\" content=\"\u6DFB\u52A0\" placement=\"top\">\n          <el-button\n            size=\"mini\"\n            type=\"primary\"\n            icon=\"el-icon-plus\"\n            @click=\"handleBeforeAdd(row)\"/>\n        </el-tooltip>\n        <el-tooltip v-if=\"getDefaultButtonShow.edit.row\" effect=\"dark\" content=\"\u7F16\u8F91\" placement=\"top\">\n          <el-button\n            size=\"mini\"\n            type=\"warning\"\n            icon=\"el-icon-edit\"\n            @click=\"handleBeforeEdit(row)\"/>\n        </el-tooltip>\n        <el-tooltip effect=\"dark\" content=\"\u5220\u9664\" placement=\"top\">\n          <el-button\n            v-if=\"getDefaultButtonShow.delete.row\"\n            size=\"mini\"\n            type=\"danger\"\n            icon=\"el-icon-delete\"\n            @click=\"handleDelete(row)\"/>\n        </el-tooltip>\n        <slot\n          :row=\"row\"\n          :column=\"column\"\n          :$index=\"$index\"\n          name=\"row-operation\"/>\n      </template>\n      <!--\u8868\u683C\u5217\u63D2\u69FD--> \n      <template\n        slot-scope=\"{row, column, $index}\"\n        :slot=\"value\"\n        v-for=\"(value, key) in getTableColumnSolt\">\n        <slot :column=\"column\" :row=\"row\" :$index=\"$index\" :name=\"key\"></slot>\n      </template>\n    </smart-table>\n    <!--\u5206\u9875\u5668-->\n    <div\n      :style=\"getPaginationStyle\"\n      class=\"cloud-table-pagination\"\n      v-if=\"paging\">\n      <el-pagination\n        :page-sizes=\"pageSizes\"\n        :page-size=\"pageSize\"\n        :layout=\"pageLayout\"\n        :total=\"page.total\"\n        :current-page=\"1\"\n        @current-change=\"handleCurrentChange\"\n        @size-change=\"handleSizeChange\"/>  \n    </div>\n    <!--\u5217\u663E\u793A\u9690\u85CF\u5F39\u7A97-->\n    <el-dialog\n      title=\"\u9009\u62E9\u5217\"\n      :visible.sync=\"columnVisibleDialogVisible\"\n      append-to-body>\n      <smart-table-column-visible\n        @result-change=\"handleColumnVisibleResult\"\n        :column-show=\"columnVisibleOption\"></smart-table-column-visible>\n    </el-dialog>\n    <!--\u6DFB\u52A0\u4FEE\u6539\u5F39\u7A97-->\n    <el-dialog\n      append-to-body\n      :visible.sync=\"addEditDialog.visible\"\n      :title=\"getAddEditDialogTitle()\">\n      <smart-form\n        :model=\"addEditModel\"\n        :label-width=\"labelWidth\"\n        :columnOptions=\"addEditFromColumnOptions\"\n        v-loading=\"addEditDialog.loading\"\n        ref=\"addEditForm\">\n        <!--form\u63D2\u69FD-->\n        <template\n          v-for=\"(value, key) in getFormSolts\"\n          slot-scope=\"{column, model}\"\n          :slot=\"value\">\n          <slot\n            :column=\"column\"\n            :model=\"model\"\n            :name=\"key\"></slot>\n        </template>\n      </smart-form>\n      <div slot=\"footer\">\n        <el-button @click=\"addEditDialog.visible = false\">\u53D6 \u6D88</el-button>\n        <el-button type=\"primary\" @click=\"saveUpdate\">\u4FDD\u5B58</el-button>\n      </div>  \n    </el-dialog>\n  </div>\n  "
    };
});
