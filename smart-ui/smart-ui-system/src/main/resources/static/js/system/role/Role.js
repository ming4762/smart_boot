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
define(["require", "exports", "ComponentBuilder", "mixins/LayoutMixins", "plugins/table/SmartTableCRUD", "utils/ApiService", "utils/TimeUtils"], function (require, exports, ComponentBuilder_1, LayoutMixins_1, SmartTableCRUD_1, ApiService_1, TimeUtils_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var roleTypeMap = {
        '1': '业务角色',
        '2': '管理角色',
        '3': '系统内置角色'
    };
    var Role = (function (_super) {
        __extends(Role, _super);
        function Role() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        Role.prototype.init = function () {
            this.initVue();
        };
        Role.prototype.initVue = function () {
            this.vue = new Vue({
                el: '#vue-container',
                components: {
                    'home-main': this.build()
                }
            });
        };
        Role.prototype.mixins = function () {
            return [
                new LayoutMixins_1.default().build()
            ];
        };
        Role.prototype.components = function () {
            return {
                'smart-table-crud': new SmartTableCRUD_1.default().build()
            };
        };
        Role.prototype.data = function () {
            var roleTypeDic = [];
            Object.keys(roleTypeMap).forEach(function (key) {
                roleTypeDic.push({
                    label: key,
                    value: roleTypeMap[key]
                });
            });
            return {
                apiService: ApiService_1.default,
                roleTypeMap: roleTypeMap,
                columnOptions: [
                    {
                        prop: 'roleId',
                        label: '角色ID',
                        form: {
                            visible: false
                        },
                        table: {
                            visible: false,
                            displayControl: false
                        }
                    },
                    {
                        label: '角色名',
                        prop: 'roleName',
                        table: {
                            width: 120,
                            fixed: true
                        },
                        form: {
                            rules: true
                        }
                    },
                    {
                        label: '备注',
                        prop: 'remark',
                        table: {
                            minWidth: 200
                        },
                        form: {}
                    },
                    {
                        label: '类型',
                        prop: 'roleType',
                        type: 'radio',
                        table: {
                            width: 100
                        },
                        form: {
                            defaultValue: true,
                            dicData: roleTypeDic
                        }
                    },
                    {
                        label: '启用',
                        prop: 'enable',
                        type: 'boolean',
                        table: {},
                        form: {}
                    },
                    {
                        label: '创建时间',
                        prop: 'createTime',
                        table: {
                            formatter: function (row, column, value) { return TimeUtils_1.default.formatTime(value); },
                            width: 160
                        },
                        form: {}
                    },
                    {
                        label: '创建用户',
                        prop: 'createUserId',
                        table: {
                            formatter: function (row) {
                                var user = row.createUser;
                                return user ? user.name : '-';
                            }
                        },
                        form: {}
                    },
                    {
                        label: '更新时间',
                        prop: 'updateTime',
                        table: {
                            formatter: function (row, column, value) { return TimeUtils_1.default.formatTime(value); },
                            width: 160
                        },
                        form: {}
                    },
                    {
                        label: '更新人员',
                        prop: 'updateUserId',
                        table: {
                            formatter: function (row) {
                                var user = row.updateUser;
                                return user ? user.name : '-';
                            }
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
                defaultButtonConfig: {
                    add: {
                        rowShow: false,
                        permission: 'system:role:save'
                    },
                    delete: {
                        rowShow: false,
                        permission: 'system:role:delete'
                    },
                    edit: {
                        permission: 'system:role:update'
                    }
                }
            };
        };
        Role.prototype.template = function () {
            return "\n    <div style=\"padding: 15px\">\n      <smart-table-crud\n        :defaultButtonConfig=\"defaultButtonConfig\"\n        queryUrl=\"sys/role/listWithAll\"\n        deleteUrl=\"sys/role/batchDelete\"\n        saveUpdateUrl=\"sys/role/saveUpdate\"\n        :keys=\"['roleId']\"\n        tableName=\"\u89D2\u8272\" \n        :apiService=\"apiService\"\n        labelWidth=\"80px\"\n        :height=\"computedTableHeight\"\n        :columnOptions=\"columnOptions\">\n        <!--\u89D2\u8272\u7C7B\u578B\u63D2\u69FD-->\n        <template v-slot:table-roleType=\"{row}\">\n          <el-tag>{{roleTypeMap[row.roleType]}}</el-tag>\n        </template>\n        <!--\u89D2\u8272\u72B6\u6001\u63D2\u69FD-->\n        <template v-slot:table-enable=\"{row}\">\n          <el-switch\n            v-model=\"row.enable\"\n            disabled\n            active-color=\"#13ce66\"\n            inactive-color=\"#ff4949\">\n          </el-switch>\n        </template>\n      </smart-table-crud>\n    </div>\n    ";
        };
        return Role;
    }(ComponentBuilder_1.default));
    exports.Role = Role;
});
