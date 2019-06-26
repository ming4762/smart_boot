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
define(["require", "exports", "ComponentBuilder", "plugins/table/SmartTableCRUD", "utils/ApiService", "utils/ValidateUtils"], function (require, exports, ComponentBuilder_1, SmartTableCRUD_1, ApiService_1, ValidateUtils_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    /**
     * 用户界面构建
     */
    var User = /** @class */ (function (_super) {
        __extends(User, _super);
        function User() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        /**
         * 初始化方法
         */
        User.prototype.init = function () {
            this.initVue();
        };
        /**
         * 初始化vue
         */
        User.prototype.initVue = function () {
            // @ts-ignore
            this.vue = new Vue({
                el: '#vue-container',
                components: {
                    // @ts-ignore
                    'home-main': this.build()
                }
            });
        };
        /**
         * 验证手机号
         */
        User.validateMobile = function (rule, value, callback) {
            if (value && value.trim() !== '') {
                var result = ValidateUtils_1.default.validateMobile(value.trim());
                if (result[0]) {
                    callback();
                }
                else {
                    callback(new Error(result[1]));
                }
            }
            else {
                callback();
            }
        };
        User.prototype.data = function () {
            return {
                apiService: ApiService_1.default,
                /**
                 * 表格配置
                 */
                columnOptions: [
                    {
                        prop: 'userId',
                        label: '用户Id',
                        form: {
                            visible: false
                        },
                        table: {
                            visible: false,
                            displayControl: false
                        }
                    },
                    {
                        prop: 'username',
                        label: '用户名',
                        form: {
                            span: 12,
                            rules: true
                        },
                        table: {
                            fixed: true
                        }
                    },
                    {
                        prop: 'name',
                        label: '姓名',
                        form: {
                            span: 12,
                            rules: true
                        },
                        table: {
                            width: 150,
                            fixed: true
                        }
                    },
                    {
                        prop: 'status',
                        label: '状态',
                        form: {
                            visible: true
                        },
                        table: {
                            slot: true
                        }
                    }, {
                        prop: 'mobile',
                        label: '电话',
                        table: {
                            minWidth: 150
                        },
                        form: {
                            span: 12,
                            rules: [{
                                    required: false,
                                    tigger: 'blur',
                                    validator: User.validateMobile
                                }]
                        }
                    },
                    {
                        prop: 'email',
                        label: '邮箱',
                        table: {
                            minWidth: 200,
                            slot: true
                        },
                        form: {
                            span: 12
                        }
                    },
                    {
                        prop: 'sex',
                        label: '性别',
                        type: 'radio',
                        table: {
                            formatter: function (row, column, cellValue) {
                                if (cellValue === 0) {
                                    return '男';
                                }
                                if (cellValue === 1) {
                                    return '女';
                                }
                            }
                        },
                        form: {
                            span: 12,
                            defaultValue: '0',
                            dicData: [{
                                    label: '0',
                                    value: '男'
                                }, {
                                    label: '1',
                                    value: '女'
                                }]
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
                            span: 12,
                            defaultValue: 1
                        }
                    }
                ],
                // 默认按钮配置
                defaultButtonConfig: {
                    add: {
                        rowShow: false,
                        permission: 'system:user:save'
                    },
                    edit: {
                        permission: 'system:user:update'
                    },
                    delete: {
                        permission: 'system:user:delete'
                    }
                }
            };
        };
        User.prototype.components = function () {
            return {
                'smart-table-crud': new SmartTableCRUD_1.default().build()
            };
        };
        User.prototype.methods = function () {
            return {
                formatStatusType: function (row) {
                    var result = '';
                    switch (row.status) {
                        case '1':
                            result = 'success';
                            break;
                        case '0':
                            result = 'danger';
                            break;
                        case '2':
                            result = 'warning';
                            break;
                    }
                    return result;
                },
                formatStateValue: function (row) {
                    var result = '';
                    switch (row.status) {
                        case '1':
                            result = '正常';
                            break;
                        case '0':
                            result = '禁用';
                            break;
                        case '2':
                            result = '锁定';
                            break;
                    }
                    return result;
                }
            };
        };
        User.prototype.template = function () {
            return "\n    <div style=\"padding: 15px;\">\n      <smart-table-crud\n        :defaultButtonConfig=\"defaultButtonConfig\"\n        queryUrl=\"sys/user/list\"\n        deleteUrl=\"sys/user/batchDelete\"\n        saveUpdateUrl=\"sys/user/saveUpdate\"\n        :keys=\"['userId']\"\n        tableName=\"\u7CFB\u7EDF\u7528\u6237\"\n        :apiService=\"apiService\"\n        labelWidth=\"80px\"\n        :columnOptions=\"columnOptions\">\n        <!-- \u72B6\u6001status\u63D2\u69FD -->\n        <template slot=\"table-status\" slot-scope=\"{ row }\">\n          <el-tag\n            :type=\"formatStatusType(row)\">{{formatStateValue(row)}}</el-tag>\n        </template>\n      </smart-table-crud>\n    </div>\n    ";
        };
        return User;
    }(ComponentBuilder_1.default));
    exports.User = User;
});
