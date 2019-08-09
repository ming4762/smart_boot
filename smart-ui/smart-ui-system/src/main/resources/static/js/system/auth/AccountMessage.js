define(["require", "exports", "PageBuilder", "plugins/form/SmartForm", "utils/ApiService", "mixins/MessageMixins", "system/utils/AuthUtils", "system/log/Log"], function (require, exports, PageBuilder_1, SmartForm_1, ApiService_1, MessageMixins_1, AuthUtils_1, Log_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    class AccountMessage extends PageBuilder_1.default {
        build() {
            return page;
        }
    }
    exports.AccountMessage = AccountMessage;
    const ChangePassword = {
        components: {
            SmartForm: SmartForm_1.default
        },
        mixins: [MessageMixins_1.default],
        data() {
            const validateConfirmPassword = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请再次输入密码'));
                }
                else if (value !== this.model.password) {
                    callback(new Error('两次输入密码不一致!'));
                }
                else {
                    callback();
                }
            };
            return {
                model: {},
                columnOptions: [
                    {
                        prop: 'oldPassword',
                        type: 'password',
                        placeholder: '请输入原密码',
                        rules: {
                            message: '请输入原密码',
                            required: true,
                            trigger: 'blur'
                        }
                    },
                    {
                        prop: 'password',
                        type: 'password',
                        placeholder: '请输入新密码',
                        rules: {
                            message: '请输入新密码',
                            required: true,
                            trigger: 'blur'
                        }
                    },
                    {
                        prop: 'confirmPassword',
                        type: 'password',
                        placeholder: '请再次输入新密码',
                        rules: [
                            { validator: validateConfirmPassword, trigger: 'blur' }
                        ]
                    }
                ]
            };
        },
        methods: {
            handleSave() {
                this.$refs['form'].validate()
                    .then(valid => {
                    const username = AuthUtils_1.default.getCurrentUser().username;
                    return ApiService_1.default.postAjax('auth/updatePassword', {
                        oldPassword: AuthUtils_1.createPassword(username, this.model.oldPassword),
                        password: AuthUtils_1.createPassword(username, this.model.password)
                    });
                }).then(result => {
                    this.$alert('修改成功，请重新登录', '成功', {
                        confirmButtonText: '确定',
                        callback: action => {
                            ApiService_1.default.goToLogin();
                        }
                    });
                }).catch(error => {
                    this.errorMessage("修改密码失败，请稍后重试", error);
                });
            }
        },
        template: `
  <div>
    <SmartForm
      ref="form"
      :columnOptions="columnOptions"
      :model="model"></SmartForm>
    <el-button
      @click="handleSave"
      type="primary">确认修改</el-button>
  </div>
  `
    };
    const page = {
        components: {
            ChangePassword,
            LogComponent: Log_1.LogComponent
        },
        data() {
            return {
                activeName: activeTab || 'message',
                userId: AuthUtils_1.default.getCurrentUserId()
            };
        },
        template: `
  <div style="padding: 20px;">
    <div class="row">
      <div class="col-lg-3">
        <div style="margin-bottom: 20px" class="text-center">
          <div class="user-card-header">
            <div class="user-card-header-content">
              <el-avatar style="background-color: white;" :size="130" src="http://cdn.admui.com/demo/iframe/2.1.0/images/avatar.svg"></el-avatar>
              <h6 style="color: #263238;">xiaxuan@admui_demo</h6>
              <p>上次登录2019-08-03 20:54:40</p>
            </div>
          </div>
          <div class="card-footer user-card-footer">
            <div class="row no-space">1231</div>
          </div>
        </div>
      </div>
      <div class="col-lg-9">
        <div class="tab-container">
          <el-tabs v-model="activeName">
            <el-tab-pane label="消息" name="message">
              <span slot="label"><i class="tab-icon el-icon-message"></i>消息</span>
            </el-tab-pane>
            <el-tab-pane label="日志" name="log">
              <span slot="label"><i class="tab-icon el-icon-document"></i>日志</span>
              <LogComponent :userId="userId"/>
            </el-tab-pane>
            <el-tab-pane label="密码" name="password">
              <span slot="label"><i class="tab-icon el-icon-key"></i>密码</span>
              <ChangePassword class="change-password-panel"/>
            </el-tab-pane>
            <el-tab-pane label="主题" name="theme">
              <span slot="label"><i class="tab-icon el-icon-s-claim"></i>主题</span>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
    </div>
  </div>
  `
    };
});
