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
        data() {
            return {
                userTransferData: [],
                userTransferModel: [],
                loading: false
            };
        },
        mounted() {
            this.loading = true;
            ApiService_1.default.postAjax('sys/user/list', {})
                .then(userList => {
                this.userTransferData = userList.map(user => {
                    return {
                        key: user.userId,
                        label: user.name
                    };
                });
                this.loadRoleUser();
            }).catch(error => {
                this.loading = false;
                this.errorMessage('加载用户角色信息发生错误', error);
            });
        },
        watch: {
            roleId(_new, old) {
                if (_new !== old) {
                    this.loadRoleUser();
                }
            }
        },
        methods: {
            handleSetUser() {
                const parameter = {};
                parameter[this.roleId] = this.userTransferModel;
                ApiService_1.default.postAjax('sys/role/updateUser', parameter)
                    .then(() => {
                    this.successMessage('保存成功');
                }).catch(error => {
                    this.errorMessage('设置人员失败', error);
                });
            },
            loadRoleUser() {
                this.loading = true;
                ApiService_1.default.postAjax('sys/role/getRoleUserId', { roleId: this.roleId })
                    .then(data => {
                    this.loading = false;
                    this.userTransferModel = data;
                }).catch(error => {
                    this.loading = false;
                    this.errorMessage('加载用户角色信息发生错误', error);
                });
            }
        },
        template: `
  <div v-loading="loading" style="width: 496px; padding: 20px; border:1px dashed #A9A9A9;">
    <el-transfer class="user-transfer"
      :data="userTransferData"
      v-model="userTransferModel"
      filter-placeholder="请输入关键字"
      :titles="['用户', '用户']"
      filterable></el-transfer>
    <div style="padding-top: 15px">
      <el-button type="primary" @click="handleSetUser">保存</el-button> 
    </div> 
  </div>
  `
    };
});
