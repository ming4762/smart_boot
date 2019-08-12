import ApiService from '../../utils/ApiService.js';
import MessageMixins from '../../mixins/MessageMixins.js';
import { createPassword } from '../utils/AuthUtils.js';
import { STORE_KEYS } from '../../Constants.js';
import StoreUtil from '../../utils/StoreUtil.js';
export default {
    mixins: [
        MessageMixins
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
            ApiService.saveToken(null);
            this.$refs['form'].validate(valid => {
                if (valid) {
                    ApiService.postAjax('public/login', {
                        username: this.loginFormModel.username,
                        password: createPassword(this.loginFormModel.username, this.loginFormModel.password)
                    }).then(data => {
                        ApiService.saveToken(data.Authorization);
                        StoreUtil.setStore(STORE_KEYS.USER_PREMISSION, data.permission, StoreUtil.SESSION_TYPE);
                        StoreUtil.setStore(STORE_KEYS.USER_KEY, data.user, StoreUtil.SESSION_TYPE);
                        if (ApiService.useIde()) {
                            return ApiService.registerKey();
                        }
                    }).then(() => {
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
