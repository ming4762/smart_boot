define(["require", "exports", "utils/ApiService", "mixins/MessageMixins"], function (require, exports, ApiService_1, MessageMixins_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.default = {
        mixins: [
            MessageMixins_1.default
        ],
        props: {
            roleId: {
                required: true,
                type: String
            }
        },
        data: function () {
            return {
                userTransferData: [],
                userTransferModel: [],
                loading: false
            };
        },
        mounted: function () {
            var _this = this;
            this.loading = true;
            ApiService_1.default.postAjax('sys/user/list', {})
                .then(function (userList) {
                _this.userTransferData = userList.map(function (user) {
                    return {
                        key: user.userId,
                        label: user.name
                    };
                });
                _this.loadRoleUser();
            }).catch(function (error) {
                _this.loading = false;
                _this.errorMessage('加载用户角色信息发生错误', error);
            });
        },
        watch: {
            roleId: function (_new, old) {
                if (_new !== old) {
                    this.loadRoleUser();
                }
            }
        },
        methods: {
            handleSetUser: function () {
                var _this = this;
                var parameter = {};
                parameter[this.roleId] = this.userTransferModel;
                ApiService_1.default.postAjax('sys/role/updateUser', parameter)
                    .then(function () {
                    _this.successMessage('保存成功');
                }).catch(function (error) {
                    _this.errorMessage('设置人员失败', error);
                });
            },
            loadRoleUser: function () {
                var _this = this;
                this.loading = true;
                ApiService_1.default.postAjax('sys/role/getRoleUserId', { roleId: this.roleId })
                    .then(function (data) {
                    _this.loading = false;
                    _this.userTransferModel = data;
                }).catch(function (error) {
                    _this.loading = false;
                    _this.errorMessage('加载用户角色信息发生错误', error);
                });
            }
        },
        template: "\n  <div v-loading=\"loading\" style=\"width: 496px; padding: 20px; border:1px dashed #A9A9A9;\">\n    <el-transfer class=\"user-transfer\"\n      :data=\"userTransferData\"\n      v-model=\"userTransferModel\"\n      filter-placeholder=\"\u8BF7\u8F93\u5165\u5173\u952E\u5B57\"\n      :titles=\"['\u7528\u6237', '\u7528\u6237']\"\n      filterable></el-transfer>\n    <div style=\"padding-top: 15px\">\n      <el-button type=\"primary\" @click=\"handleSetUser\">\u4FDD\u5B58</el-button> \n    </div> \n  </div>\n  "
    };
});
