define(["require", "exports", "plugins/form/SmartForm", "system/role/RoleUser", "system/role/RoleMenuAuthorize", "utils/ApiService", "mixins/MessageMixins"], function (require, exports, SmartForm_1, RoleUser_1, RoleMenuAuthorize_1, ApiService_1, MessageMixins_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var roleTypeMap = {
        '1': '业务角色',
        '2': '管理角色',
        '3': '系统内置角色'
    };
    exports.default = {
        props: {
            organId: String,
            organName: String,
            roleId: String
        },
        components: {
            'smart-form': SmartForm_1.default,
            'role-user': RoleUser_1.default,
            'role-menu-authorize': RoleMenuAuthorize_1.default
        },
        mixins: [
            MessageMixins_1.default
        ],
        computed: {
            computedIsAdd: function () {
                return this.roleId === undefined || this.roleId === null;
            }
        },
        data: function () {
            var roleTypeDic = [];
            Object.keys(roleTypeMap).forEach(function (key) {
                roleTypeDic.push({
                    label: key,
                    value: roleTypeMap[key]
                });
            });
            return {
                activeName: 'first',
                roleModel: {},
                roleFormColumns: [
                    {
                        prop: 'roleId',
                        label: '角色ID',
                        disabled: true,
                        visible: false,
                        span: 12
                    },
                    {
                        prop: 'organId',
                        label: '所属组织',
                        span: 12
                    },
                    {
                        label: '角色名',
                        prop: 'roleName',
                        rules: true,
                        span: 12
                    },
                    {
                        label: '类型',
                        prop: 'roleType',
                        type: 'radio',
                        defaultValue: '1',
                        dicData: roleTypeDic
                    },
                    {
                        label: '备注',
                        prop: 'remark',
                    },
                    {
                        label: '启用',
                        prop: 'enable',
                        type: 'boolean',
                        defaultValue: true,
                        span: 12
                    },
                    {
                        label: '序号',
                        prop: 'seq',
                        type: 'number',
                        defaultValue: 1,
                        span: 12
                    }
                ],
                detailLoading: false
            };
        },
        watch: {
            roleId: function (_new, old) {
                if (_new !== old && _new) {
                    this.loadRoleDetal();
                }
                else if (this.computedIsAdd) {
                    this.roleModel = {
                        organName: this.organName
                    };
                }
            }
        },
        methods: {
            loadRoleDetal: function () {
                var _this = this;
                this.detailLoading = true;
                ApiService_1.default.postAjax('sys/role/queryDetail', {
                    roleId: this.roleId
                }).then(function (data) {
                    _this.detailLoading = false;
                    _this.roleModel = data;
                }).catch(function (error) {
                    _this.detailLoading = false;
                    _this.errorMessage('加载角色详情失败', error);
                });
            },
            handleSave: function () {
                var _this = this;
                var model = {};
                if (this.computedIsAdd) {
                    model['organId'] = this.organId;
                }
                ApiService_1.default.postAjax('sys/role/saveUpdate', Object.assign(model, this.roleModel))
                    .then(function () {
                    if (_this.computedIsAdd && _this.$listeners['after-save']) {
                        _this.$emit('after-save');
                    }
                }).catch(function (error) {
                    _this.errorMessage('保存角色时发生错误', error);
                });
            },
            handleDelete: function () {
                var _this = this;
                this.$confirm('您确定要删除该角色吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    return ApiService_1.default.postAjax('sys/role/delete', {
                        roleId: _this.roleId
                    });
                }).then(function () {
                    _this.successMessage('删除成功');
                    _this.$emit('after-save');
                    _this.roleModel = {};
                }).catch(function (error) {
                    if (error !== 'cancel') {
                        _this.errorMessage('删除角色发生错误', error);
                    }
                });
            }
        },
        template: "\n  <el-tabs v-model=\"activeName\">\n    <el-tab-pane label=\"\u89D2\u8272\u57FA\u672C\u4FE1\u606F\" name=\"first\">\n      <div v-loading=\"detailLoading\" class=\"role-operation-container\" style=\"width: 600px;\">\n        <smart-form\n          :model=\"roleModel\"\n          labelWidth=\"80px\"\n          :columnOptions=\"roleFormColumns\">\n          <template v-slot:organId=\"{model}\">\n            <el-form-item label=\"\u6240\u5C5E\u7EC4\u7EC7\">\n              <el-input v-if=\"computedIsAdd\" disabled :value=\"organName\"></el-input>\n              <el-input v-else disabled :value=\"model.organ ? model.organ.organName : '-'\"></el-input>\n              <el-input style=\"display: none\" v-model=\"model.organId\"></el-input>\n            </el-form-item>\n          </template>\n        </smart-form>\n        <el-button @click=\"handleSave\" style=\"margin: 10px 0 0 80px\" :type=\"computedIsAdd ? 'primary' : 'warning'\">\n          {{computedIsAdd ? '\u65B0\u589E' : '\u4FEE\u6539'}}\n        </el-button> \n        <el-button v-if=\"!computedIsAdd\" @click=\"handleDelete\" type=\"danger\">\u5220\u9664</el-button>\n      </div>\n    </el-tab-pane>\n    <el-tab-pane lazy label=\"\u83DC\u5355\u6388\u6743\" name=\"second\">\n      <div v-if=\"!computedIsAdd\">\n        <role-menu-authorize\n          :roleId=\"roleId\"/>\n      </div>\n    </el-tab-pane>\n    <el-tab-pane lazy label=\"\u975E\u83DC\u5355\u6388\u6743\" name=\"third\">\n      <div v-if=\"!computedIsAdd\">\n        \n      </div>\n    </el-tab-pane>\n    <el-tab-pane lazy label=\"\u4EBA\u5458\u6388\u6743\" name=\"forth\">\n      <div v-if=\"!computedIsAdd\">\n        <role-user\n          :roleId=\"roleId\"/>\n      </div>\n    </el-tab-pane>\n  </el-tabs>\n  "
    };
});
