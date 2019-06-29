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
define(["require", "exports", "ComponentBuilder", "plugins/table/SmartTableCRUD", "utils/ApiService", "mixins/LayoutMixins", "plugins/icon/SmartIconSelect", "utils/ValidateUtils", "utils/TreeUtils"], function (require, exports, ComponentBuilder_1, SmartTableCRUD_1, ApiService_1, LayoutMixins_1, SmartIconSelect_1, ValidateUtils_1, TreeUtils_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var SysFunction = (function (_super) {
        __extends(SysFunction, _super);
        function SysFunction() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        SysFunction.prototype.init = function () {
            this.initVue();
        };
        SysFunction.prototype.initVue = function () {
            this.vue = new Vue({
                el: '#vue-container',
                components: {
                    'home-main': this.build()
                }
            });
        };
        SysFunction.prototype.components = function () {
            return {
                'smart-table-crud': new SmartTableCRUD_1.default().build(),
                'smart-icon-select': new SmartIconSelect_1.default().build()
            };
        };
        SysFunction.prototype.mixins = function () {
            return [
                new LayoutMixins_1.default().build()
            ];
        };
        SysFunction.format = function (data, key) {
            return data;
        };
        SysFunction.prototype.data = function () {
            return {
                apiService: ApiService_1.default,
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
                            width: 180,
                            align: 'left',
                            headerAlign: 'center'
                        },
                        form: {
                            span: 24,
                            rules: true
                        }
                    }, {
                        label: '图标',
                        prop: 'icon',
                        table: {},
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
                        table: {},
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
            };
        };
        SysFunction.prototype.methods = function () {
            return {
                handleAddEditDialogShow: function (ident, model, callBack, row) {
                    if (ident === 'add') {
                        if (ValidateUtils_1.default.validateNull(row)) {
                            model.parentId = '0';
                            model.parentName = '根目录';
                        }
                        else {
                            model.parentId = row.functionId;
                            model.parentName = row.functionName;
                        }
                        callBack(model);
                    }
                    else {
                        callBack();
                    }
                },
                formatIcon: function (row) {
                    if (row['icon']) {
                        return row['icon'] + ' fa-lg';
                    }
                    else {
                        return '';
                    }
                },
                formatTypeClass: function (type) {
                    if (type === '0') {
                        return '';
                    }
                    else if (type === '1') {
                        return 'success';
                    }
                    else if (type === '2') {
                        return 'warning';
                    }
                },
                formatTypeName: function (type) {
                    if (type === '0') {
                        return '目录';
                    }
                    else if (type === '1') {
                        return '菜单';
                    }
                    else if (type === '2') {
                        return '按钮';
                    }
                },
                handleTableDataFormatter: function (tableData) {
                    return TreeUtils_1.default.convertList2Tree(tableData, ['functionId', 'parentId'], '0');
                }
            };
        };
        SysFunction.prototype.template = function () {
            return "\n    <div style=\"padding: 15px;\">\n      <smart-table-crud\n        :defaultButtonConfig=\"defaultButtonConfig\"\n        queryUrl=\"sys/function/list\"\n        deleteUrl=\"sys/function/batchDelete\"\n        saveUpdateUrl=\"sys/function/saveUpdate\"\n        :keys=\"['functionId']\"\n        :paging=\"false\"\n        defaultSortColumn=\"seq\"\n        :tableDataFormatter=\"handleTableDataFormatter\"\n        tableName=\"\u529F\u80FD\" \n        :apiService=\"apiService\"\n        @add-edit-dialog-show=\"handleAddEditDialogShow\"\n        labelWidth=\"80px\"\n        :height=\"computedTableHeight\"\n        :columnOptions=\"columnOptions\">\n        <!-- \u8868\u683C\u4E0A\u7EA7\u83DC\u5355\u63D2\u69FD -->\n        <template slot=\"form-parentId\" slot-scope=\" {model }\">\n          <el-form-item label=\"\u4E0A\u7EA7\u529F\u80FD\">\n            <el-input style=\"display: none;\" v-model=\"model.parentId\"></el-input>\n            <el-input :disabled=\"true\" :value=\"model.parentName\"></el-input>\n          </el-form-item>\n        </template>\n         <!-- \u56FE\u6807form\u63D2\u69FD -->\n        <template slot=\"form-icon\" slot-scope=\"{ model }\">\n          <el-form-item label=\"\u56FE\u68071\">\n            <smart-icon-select v-model=\"model.icon\"></smart-icon-select>\n          </el-form-item>\n        </template>\n        <!-- \u56FE\u6807\u63D2\u69FD -->\n        <template slot-scope=\"{row}\" slot=\"table-icon\">\n          <i :class=\"formatIcon(row)\"></i>\n        </template>\n        <!-- \u7C7B\u578B\u63D2\u69FD -->\n        <template slot-scope=\"{row}\" slot=\"table-functionType\">\n          <el-tag :type=\"formatTypeClass(row.functionType)\">{{formatTypeName(row.functionType)}}</el-tag>\n        </template>\n        <!-- \u662F\u5426\u83DC\u5355\u8868\u683C\u63D2\u69FD -->\n        <template slot-scope=\"{row}\" slot=\"table-menuIs\">\n          <i :class=\"row['menuIs'] === true ? 'el-icon-success' : 'el-icon-error'\"></i>\n        </template>\n      </smart-table-crud>\n    </div>\n    ";
        };
        return SysFunction;
    }(ComponentBuilder_1.default));
    exports.SysFunction = SysFunction;
});
