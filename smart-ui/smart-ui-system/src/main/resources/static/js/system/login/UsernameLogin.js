define(["require", "exports", "utils/ApiService", "mixins/MessageMixins", "utils/Md5Utils", "Constants", "utils/StoreUtil"], function (require, exports, ApiService_1, MessageMixins_1, Md5Utils_1, Constants_1, StoreUtil_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    const createPassword = (username, password) => {
        const salt = '1qazxsw2';
        const passwordValue = username + password + salt;
        return Md5Utils_1.default.md5(passwordValue, 2);
    };
    exports.default = {
        mixins: [
            MessageMixins_1.default
        ],
        data() {
            return {
                loginFormModel: {
                    username: '',
                    password: ''
                },
                rules: {
                    username: [
                        { required: true, message: '请输入用户名', trigger: 'blur' }
                    ],
                    password: [
                        { required: true, message: '请输入密码', trigger: 'blur' }
                    ]
                },
                loginLoading: false
            };
        },
        methods: {
            handleLogin: function () {
                ApiService_1.default.saveToken(null);
                this.$refs['form'].validate(valid => {
                    if (valid) {
                        ApiService_1.default.postAjax('public/login', {
                            username: this.loginFormModel.username,
                            password: createPassword(this.loginFormModel.username, this.loginFormModel.password)
                        }).then(data => {
                            console.log(data);
                            ApiService_1.default.saveToken(data.Authorization);
                            StoreUtil_1.default.setStore(Constants_1.STORE_KEYS.USER_PREMISSION, data.permission, StoreUtil_1.default.SESSION_TYPE);
                            window.location.href = `${contextPath}ui/system/home`;
                        }).catch(error => {
                            this.errorMessage(error.message, error);
                        });
                    }
                });
            }
        },
        template: `
  <el-form ref="form" :rules="rules" :model="loginFormModel">
    <el-form-item prop="username">
      <el-input
        placeholder="请输入账号"
        prefix-icon="el-icon-user"
        v-model="loginFormModel.username"></el-input>
    </el-form-item>
    <el-form-item prop="password">
      <el-input
        placeholder="请输入密码"
        show-password
        v-model="loginFormModel.password"
        prefix-icon="el-icon-unlock"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button v-loading="loginLoading" @click="handleLogin" class="full-width" type="primary">登录</el-button>
    </el-form-item>
  </el-form>
  `
    };
});
