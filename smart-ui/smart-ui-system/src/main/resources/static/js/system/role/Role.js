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
define(["require", "exports", "ComponentBuilder", "plugins/container/FlexAside", "system/role/RoleTreeWithOrgan", "system/role/RoleDetail", "utils/ApiService", "utils/TimeUtils", "mixins/PermissionMixins"], function (require, exports, ComponentBuilder_1, FlexAside_1, RoleTreeWithOrgan_1, RoleDetail_1, ApiService_1, TimeUtils_1, PermissionMixins_1) {
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
                new PermissionMixins_1.default().build()
            ];
        };
        Role.prototype.components = function () {
            return {
                'flex-aside': FlexAside_1.default,
                'role-tree': RoleTreeWithOrgan_1.default,
                'role-detail': RoleDetail_1.default
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
                searchValue: '',
                activeTab: 'detail',
                selectedOrgan: {},
                selectRole: {}
            };
        };
        Role.prototype.methods = function () {
            return {
                handleShowSetUser: function (role) {
                    console.log(role);
                },
                handleShowAuthorizeDialog: function (role) {
                    console.log(role);
                },
                handleClickRoleTree: function (roleOrgan) {
                    var type = roleOrgan.attributes ? roleOrgan.attributes.type : '';
                    if (type === 'role') {
                        this.selectRole = roleOrgan;
                    }
                    else if (type === 'organ') {
                        this.selectedOrgan = roleOrgan;
                    }
                },
                handleAddRole: function () {
                    this.selectRole = {};
                },
                handleAfterSave: function () {
                    this.$refs['roleTree'].load();
                }
            };
        };
        Role.prototype.template = function () {
            return "\n    <flex-aside\n      :hasAsideHeader=\"false\">\n      <template slot=\"aside\">\n        <div class=\"organ-aside full-height\">\n          <div class=\"organ-aside-search\">\n            <el-input\n              v-model=\"searchValue\"\n              suffix-icon=\"el-icon-search\"\n              placeholder=\"\u641C\u7D22\u90E8\u95E8\"></el-input>\n          </div>\n          <!--\u90E8\u95E8\u6811-->\n          <div style=\"height: calc(100% - 190px); padding-top: 22px\">\n            <role-tree\n              ref=\"roleTree\"\n              @node-click=\"handleClickRoleTree\"/>\n          </div>\n          <!--\u6DFB\u52A0\u6309\u94AE-->\n          <!--  TODO: v-if=\"validatePermission('system:role:add')\" -->\n          <div \n            class=\"organ-aside-add\">\n            <el-button \n              type=\"primary\"\n              @click=\"handleAddRole\"\n              style=\"width: 100%\"\n              icon=\"el-icon-plus\">\u6DFB\u52A0\u89D2\u8272</el-button>\n          </div>\n        </div>\n      </template>\n      <!--\u4E3B\u9898\u90E8\u5206-->\n      <template>\n        <div style=\"padding: 15px\">\n          <role-detail\n            @after-save=\"handleAfterSave\"\n            :organId=\"selectedOrgan.id\"\n            :organName=\"selectedOrgan.text\"\n            :roleId=\"selectRole.id\"/>\n        </div>\n      </template>\n      <!--\u6DFB\u52A0\u89D2\u8272\u5F39\u7A97-->\n    </flex-aside>\n    ";
        };
        return Role;
    }(ComponentBuilder_1.default));
    exports.Role = Role;
});
