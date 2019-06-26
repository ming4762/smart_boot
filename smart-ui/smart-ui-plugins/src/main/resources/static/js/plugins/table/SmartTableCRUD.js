var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
        return extendStatics(d, b);
    }
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
define(["require", "exports", "ComponentBuilder", "mixins/MessageMixins", "utils/CommonUtils", "plugins/form/SmartForm", "plugins/table/SmartButtonGroup", "plugins/table/SmartTable", "plugins/table/SmartColumnVisible"], function (require, exports, ComponentBuilder_1, MessageMixins_1, CommonUtils_1, SmartForm_1, SmartButtonGroup_1, SmartTable_1, SmartColumnVisible_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    /**
     * 验证权限
     */
    var validatePermission = function (permission, permissions) {
        if (!permissions || permission === undefined || permission === null || permission === '') {
            return true;
        }
        return permissions.indexOf(permission) !== -1;
    };
    var SmartTableCRUD = /** @class */ (function (_super) {
        __extends(SmartTableCRUD, _super);
        function SmartTableCRUD() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        /**
         * 混入
         */
        SmartTableCRUD.prototype.mixins = function () {
            return [
                new MessageMixins_1.default().build()
            ];
        };
        /**
         * 组件
         */
        SmartTableCRUD.prototype.components = function () {
            return {
                // @ts-ignore
                'smart-form': new SmartForm_1.default().build(),
                // @ts-ignore
                'smart-button-group': new SmartButtonGroup_1.default().build(),
                // @ts-ignore
                'smart-table': new SmartTable_1.default().build(),
                // @ts-ignore
                'smart-table-column-visible': new SmartColumnVisible_1.default().build()
            };
        };
        SmartTableCRUD.prototype.props = function () {
            return {
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
                    default: function () { return [10, 20, 40, 100, 200]; }
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
                    default: function () {
                        return {
                            'background-color': '#f2f2f2'
                        };
                    }
                },
                // 列配置
                columnOptions: {
                    type: Array,
                    required: true
                },
                // 用户权限信息
                permissions: Array
            };
        };
        // 生命周期钩子
        SmartTableCRUD.prototype.created = function () {
            var _this = this;
            _this.convertColumnOption();
            _this.order.sortName = _this.defaultSortColumn;
            _this.order.sortOrder = _this.defaultSortOrder;
        };
        SmartTableCRUD.prototype.mounted = function () {
            var _this = this;
            if (!_this.data) {
                _this.load();
            }
            else {
                _this.tableData = _this.data;
            }
        };
        SmartTableCRUD.prototype.data = function () {
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
            };
        };
        SmartTableCRUD.prototype.methods = function () {
            return {
                /**
                 * 配置转换
                 */
                convertColumnOption: function () {
                    var _this_1 = this;
                    // 表格配置
                    var tableColumnOptions = [];
                    // 添加修改form配置
                    var addEditFromColumnOptions = [];
                    // 搜索form配置
                    var searchFormColumnOptions = [];
                    this.columnOptions.forEach(function (item) {
                        // 设置key
                        if (!item.key)
                            item.key = item.prop;
                        var commonColumn = {
                            key: item.key,
                            label: item.label,
                            prop: item.prop,
                            type: item.type
                        };
                        // @ts-ignore
                        var tableColumn = Object.assign({}, commonColumn, item.table);
                        tableColumnOptions.push(tableColumn);
                        // 添加修改弹窗配置
                        // @ts-ignore
                        var addEditFromColumn = Object.assign({}, commonColumn, item.form);
                        addEditFromColumnOptions.push(addEditFromColumn);
                        // 添加搜索配置
                        if (item.search) {
                            // @ts-ignore
                            searchFormColumnOptions.push(Object.assign({}, commonColumn, item.search));
                            // search符号，默认=
                            _this_1.searchSymbol[item.key] = item.search.symbol ? item.search.symbol : '=';
                        }
                        // 列显示隐藏配置
                        if (item.table.displayControl !== false) {
                            _this_1.columnVisibleOption[item.key] = {
                                name: item.label,
                                hidden: item.table.visible === false
                            };
                        }
                    });
                    // 添加操作列
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
                // 创造查询条件
                createQueryParameter: function () {
                    var _this_1 = this;
                    // @ts-ignore
                    var parameters = Object.assign({}, this.queryParameters, this.order);
                    // 添加searchModel条件
                    if (this.searchWithSymbol) {
                        Object.keys(this.searchModel).forEach(function (key) {
                            var value = _this_1.searchModel[key];
                            var symbol = _this_1.searchSymbol[key];
                            var searchKey = key + "@" + symbol;
                            parameters[searchKey] = value;
                        });
                    }
                    else {
                        // @ts-ignore
                        parameters = Object.assign(parameters, this.searchModel);
                    }
                    if (this.paging === true) {
                        //  添加分页条件
                        // @ts-ignore
                        parameters = Object.assign(parameters, this.page);
                    }
                    return parameters;
                },
                /**
                 * 加载完毕处理数据
                 */
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
                /**
                 * 加载数据方法
                 */
                load: function () {
                    var _this_1 = this;
                    this.tableLoading = true;
                    // 执行加载前事件
                    if (this.$listeners['before-load']) {
                        this.$emit('before-load');
                    }
                    // 创造查询条件
                    var parameter = this.createQueryParameter();
                    if (this.queryParameterFormatter) {
                        parameter = this.queryParameterFormatter(parameter);
                    }
                    // 执行查询
                    this.apiService.postAjax(this.queryUrl, parameter)
                        .then(function (data) {
                        _this_1.tableLoading = false;
                        var dealData = _this_1.dealData(data);
                        if (_this_1.tableDataFormatter) {
                            // 执行数据格式化函数
                            dealData = _this_1.tableDataFormatter(dealData);
                        }
                        _this_1.tableData = dealData;
                    }).catch(function (error) {
                        _this_1.tableLoading = false;
                        _this_1.errorMessage('加载数据失败', error);
                    });
                },
                /**
                 * 监控排序变化，并重新加载数据
                 * TODO: 数据已设置的情况下，排序的设定
                 * // 如果数据已存在，则该方法触发事件，由上层处理排序
                 */
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
                /**
                 * 分页器pageSize改变是触发
                 */
                handleSizeChange: function (pageSize) {
                    this.page.limit = pageSize;
                    this.page.offset = (this.page.currentPage - 1) * this.page.limit;
                    this.load();
                },
                // 添加前操作
                handleBeforeAdd: function (row) {
                    if (row)
                        this.selectedRow = row;
                    this.handleAddEditDialogShow('add', row);
                },
                /**
                 * 添加修改弹窗打开时执行
                 */
                handleAddEditDialogShow: function (ident, row) {
                    var _this_1 = this;
                    // 回调函数
                    var callBack = function (model) {
                        // @ts-ignore
                        _this_1.oldAddEditModel = Object.assign({}, _this_1.addEditModel);
                        _this_1.addEditDialog.visible = true;
                        // TODO: 可能存在bug
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
                    // 重置表单
                    this.addEditModel = {};
                    // 如果没有监听事件，执行callback
                    if (!this.$listeners['add-edit-dialog-show']) {
                        callBack(null);
                    }
                    else {
                        this.$emit('add-edit-dialog-show', ident, this.addEditModel, callBack, row);
                    }
                },
                /**
                 * 编辑前操作
                 */
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
                /**
                 * 执行删除操作
                 * @param row
                 */
                handleDelete: function (row) {
                    var _this_1 = this;
                    var _this = this;
                    var rowList = [];
                    if (row) {
                        this.selectedRow = row;
                        rowList.push(row);
                    }
                    else {
                        // 删除警告
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
                        // 删除警告语
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
                            // 执行删除后事件
                            if (_this_1.$listeners['after-delete']) {
                                _this_1.$emit('after-delete', result);
                            }
                            _this_1.load();
                        }).catch(function (error) {
                            if (error !== 'cancel') {
                                // todo:待完善
                                _this_1.errorMessage('删除时发生错误', error);
                            }
                        });
                    }
                },
                /**
                 * 复选框列发生变化时
                 * @param selectionList
                 */
                handleSelectionChange: function (selectionList) {
                    this.selectionList = selectionList;
                    if (this.$listeners['selection-change']) {
                        this.$emit('selection-change', selectionList);
                    }
                },
                /**
                 * 分页器currentPage 改变时会触发
                 */
                handleCurrentChange: function (currentPage) {
                    this.page.currentPage = currentPage;
                    this.page.offset = (currentPage - 1) * this.page.limit;
                    this.load();
                },
                // 查询一个
                getOne: function (ident, row) {
                    var _this_1 = this;
                    if (ident === 'edit') {
                        var get_1 = !!this.getUrl;
                        var parameters_1 = {};
                        // 查询结果
                        if (get_1) {
                            parameters_1 = CommonUtils_1.default.getObjectByKeys(this.keys, [row])[0];
                        }
                        else {
                            this.keys.forEach(function (key) {
                                parameters_1[key + '@='] = row[key];
                            });
                        }
                        // 查询
                        this.apiService.postAjax(get_1 ? this.getUrl : this.queryUrl, parameters_1)
                            .then(function (result) {
                            if (result) {
                                var model = get_1 ? result : (result.length === 1 ? result[0] : {});
                                _this_1.addEditModel = model;
                            }
                        });
                    }
                },
                /**
                 * 执行查询
                 */
                handleSearch: function () {
                    this.load();
                },
                handleRestSearch: function () {
                    this.$refs['searchForm'].reset();
                    this.searchModel = {};
                },
                /**
                 * 列显示隐藏配置事件
                 */
                handleColumnVisibleResult: function (result) {
                    this.columnVisibleResult = result;
                    this.dealColumnVisible();
                },
                /**
                 * 处理列显示隐藏
                 */
                dealColumnVisible: function () {
                    var _this_1 = this;
                    this.tableColumnOptions.forEach(function (item) {
                        var columnVisible = _this_1.columnVisibleResult[item.key];
                        if (columnVisible !== undefined && columnVisible !== null) {
                            _this_1.$set(item, 'visible', columnVisible);
                        }
                    });
                },
                /**
                 * 点击顶部按钮组
                 */
                handleClickGroupButton: function (ident) {
                    console.log(ident);
                    switch (ident) {
                        case 'delete':
                            this.handleDelete();
                            break;
                        case 'columnVisible':
                            // 显示列选择弹窗
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
                // 获取添加修改弹窗form
                getAddEditDialogTitle: function () {
                    return (this.addEditDialog.isAdd === true ? '新增' : '编辑') + (this.tableName ? this.tableName : '');
                },
                /**
                 * 显示搜索窗口
                 * TODO: 开发中
                 */
                handleShowSearch: function () {
                    this.searchDivVisible = !this.searchDivVisible;
                },
                /**
                 * 添加/修改方法
                 */
                saveUpdate: function (event) {
                    var _this_1 = this;
                    this.$refs['addEditForm'].validate()
                        .then(function (validate) {
                        if (validate === true) {
                            // 开始加载
                            _this_1.addEditDialog.loading = true;
                            if (_this_1.addEditDialog.isAdd) {
                                // 添加前事件
                                var listener = 'before-add';
                                if (_this_1.$listeners[listener]) {
                                    _this_1.$emit(listener, _this_1.addEditModel);
                                }
                            }
                            else {
                                // 编辑前事件
                                var listener = 'before-edit';
                                if (_this_1.$listeners[listener]) {
                                    _this_1.$emit(listener, _this_1.addEditModel, _this_1.oldAddEditModel);
                                }
                            }
                            // 执行操作
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
                /**
                 * 判断按钮是否显示
                 */
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
            };
        };
        SmartTableCRUD.prototype.computed = function () {
            return {
                /**
                 * 获取分页器样式
                 */
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
                /**
                 * 获取表格高度
                 * @returns {null|*}
                 */
                getTableHeight: function () {
                    var height = this.height;
                    if (height) {
                        // 去除顶部按钮组高度
                        if (this.hasTopLeft === true || this.hasTopRight === true) {
                            height = height - 40;
                        }
                        // 去除分页器高度
                        if (this.paging === true) {
                            height = height - 42;
                        }
                        return height - 1;
                    }
                    else {
                        return null;
                    }
                },
                /**
                 * 默认按钮配置
                 */
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
                // 表格列插槽
                getTableColumnSolt: function () {
                    var result = {};
                    for (var key in this.$scopedSlots) {
                        if (key.indexOf('table-') === 0) {
                            result[key] = key.substring(6);
                        }
                    }
                    return result;
                },
                // 获取form插槽
                getFormSolts: function () {
                    var result = {};
                    for (var key in this.$scopedSlots) {
                        if (key.indexOf('form-') === 0) {
                            result[key] = key.substring(5);
                        }
                    }
                    return result;
                },
                // 获取搜索插槽
                getSearchSolts: function () {
                    var result = {};
                    for (var key in this.$scopedSlots) {
                        if (key.indexOf('search-') === 0) {
                            result[key] = key.substring(7);
                        }
                    }
                    return result;
                }
            };
        };
        SmartTableCRUD.prototype.template = function () {
            // TODO: 中文采用I18N
            return "\n    <div>\n      <!--\u641C\u7D22form-->\n      <transition v-if=\"searchFormColumnOptions.length > 0\" name=\"fade\">\n        <div className=\"table-search-container\" v-show=\"searchDivVisible\">\n          <smart-form\n            :columnOptions=\"searchFormColumnOptions\"\n            :model=\"searchModel\"\n            size=\"small\"\n            inline\n            ref=\"searchForm\">\n            <!--\u904D\u5386\u63D2\u69FD-->\n            <template\n              v-for=\"(value, key) in getSearchSolts\"\n              slot-scope=\"{column, model}\"\n              :slot=\"value\">\n              <slot\n                :column=\"column\"\n                :model=\"model\"\n                :name=\"key\"></slot>\n            </template>\n            <!--\u641C\u7D22\u91CD\u7F6E\u63D2\u69FD-->\n            <template v-slot:search-button=\"{model}\">\n              <el-form-item>\n                <el-button\n                  type=\"primary\"\n                  @click=\"handleSearch\"\n                  icon=\"el-icon-search\">\u67E5\u8BE2</el-button>\n              </el-form-item>\n            </template>\n            <template v-slot:reset-button=\"{model}\">\n              <el-form-item>\n                <el-button\n                  type=\"info\"\n                  @click=\"handleRestSearch\"\n                  icon=\"el-icon-delete\">\u91CD\u7F6E</el-button>\n              </el-form-item>\n            </template>\n          </smart-form>\n        </div>\n      </transition>\n      <!--\u64CD\u4F5C\u7EC4-->\n      <smart-button-group\n        v-if=\"hasTopLeft || hasTopRight\"\n        :has-left=\"hasTopLeft\"\n        :has-right=\"hasTopRight\"\n        :add-show=\"getDefaultButtonShow.add.top\"\n        :edit-show=\"getDefaultButtonShow.edit.top\"\n        :delete-show=\"getDefaultButtonShow.delete.top\"\n        @button-click=\"handleClickGroupButton\"\n        class=\"cloud-table-menu\">\n        <!--\u6309\u94AE\u63D2\u69FD-->\n        <template slot=\"buttonLeft\">\n          <slot name=\"top-left\"></slot>\n        </template>\n        <template slot=\"buttonRight\">\n          <slot name=\"top-right\"></slot>\n        </template>\n      </smart-button-group>\n      \n      <!--\u8868\u683C\u4E3B\u4F53-->\n      <smart-table\n        v-bind=\"$attrs\"\n        v-on=\"$listeners\"\n        v-loading=\"tableLoading\"\n        :keys=\"keys\"\n        :data=\"tableData\"\n        :columnOptions=\"tableColumnOptions\"\n        :height=\"getTableHeight\"\n        @sort-change=\"handleSortChange\"\n        @selection-change=\"handleSelectionChange\"\n        :header-row-style=\"headerRowStyle\">\n        <!--\u64CD\u4F5C\u5217\u63D2\u69FD-->\n        <template v-if=\"hasOpreaColumn\" slot=\"operation_ming\" slot-scope=\"{row, column, $index}\">\n          <el-tooltip v-if=\"getDefaultButtonShow.add.row\" effect=\"dark\" content=\"\u6DFB\u52A0\" placement=\"top\">\n            <el-button\n              size=\"mini\"\n              type=\"primary\"\n              icon=\"el-icon-plus\"\n              @click=\"handleBeforeAdd(row)\"/>\n          </el-tooltip>\n          <el-tooltip v-if=\"getDefaultButtonShow.edit.row\" effect=\"dark\" content=\"\u7F16\u8F91\" placement=\"top\">\n            <el-button\n              size=\"mini\"\n              type=\"warning\"\n              icon=\"el-icon-edit\"\n              @click=\"handleBeforeEdit(row)\"/>\n          </el-tooltip>\n          <el-tooltip effect=\"dark\" content=\"\u5220\u9664\" placement=\"top\">\n            <el-button\n              v-if=\"getDefaultButtonShow.delete.row\"\n              size=\"mini\"\n              type=\"danger\"\n              icon=\"el-icon-delete\"\n              @click=\"handleDelete(row)\"/>\n          </el-tooltip>\n          <slot\n            :row=\"row\"\n            :column=\"column\"\n            :$index=\"$index\"\n            name=\"row-operation\"/>\n        </template>\n        <!--\u8868\u683C\u5217\u63D2\u69FD--> \n        <template\n          slot-scope=\"{row, column, $index}\"\n          :slot=\"value\"\n          v-for=\"(value, key) in getTableColumnSolt\">\n          <slot :column=\"column\" :row=\"row\" :$index=\"$index\" :name=\"key\"></slot>\n        </template>\n      </smart-table>\n      <!--\u5206\u9875\u5668-->\n      <div\n        :style=\"getPaginationStyle\"\n        class=\"cloud-table-pagination\"\n        v-if=\"paging\">\n        <el-pagination\n          :page-sizes=\"pageSizes\"\n          :page-size=\"pageSize\"\n          :layout=\"pageLayout\"\n          :total=\"page.total\"\n          :current-page=\"1\"\n          @current-change=\"handleCurrentChange\"\n          @size-change=\"handleSizeChange\"/>  \n      </div>\n      <!--\u5217\u663E\u793A\u9690\u85CF\u5F39\u7A97-->\n      <el-dialog\n        title=\"\u9009\u62E9\u5217\"\n        :visible.sync=\"columnVisibleDialogVisible\"\n        append-to-body>\n        <smart-table-column-visible\n          @result-change=\"handleColumnVisibleResult\"\n          :column-show=\"columnVisibleOption\"></smart-table-column-visible>\n      </el-dialog>\n      <!--\u6DFB\u52A0\u4FEE\u6539\u5F39\u7A97-->\n      <el-dialog\n        append-to-body\n        :visible.sync=\"addEditDialog.visible\"\n        :title=\"getAddEditDialogTitle()\">\n        <smart-form\n          :model=\"addEditModel\"\n          :label-width=\"labelWidth\"\n          :columnOptions=\"addEditFromColumnOptions\"\n          v-loading=\"addEditDialog.loading\"\n          ref=\"addEditForm\"></smart-form>\n        <div slot=\"footer\">\n          <el-button @click=\"addEditDialog.visible = false\">\u53D6 \u6D88</el-button>\n          <el-button type=\"primary\" @click=\"saveUpdate\">\u4FDD\u5B58</el-button>\n        </div>  \n      </el-dialog>\n  </div>\n    ";
        };
        return SmartTableCRUD;
    }(ComponentBuilder_1.default));
    exports.default = SmartTableCRUD;
});
