define(["require", "exports", "utils/ApiService", "mixins/MessageMixins", "utils/Md5Utils", "Constants", "utils/StoreUtil"], function (require, exports, ApiService_1, MessageMixins_1, Md5Utils_1, Constants_1, StoreUtil_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var createPassword = function (username, password) {
        var salt = '1qazxsw2';
        var passwordValue = username + password + salt;
        return Md5Utils_1.default.md5(passwordValue, 2);
    };
    exports.default = {
        mixins: [
            MessageMixins_1.default
        ],
        data: function () {
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
                var _this = this;
                this.$refs['form'].validate(function (valid) {
                    if (valid) {
                        ApiService_1.default.postAjax('public/login', {
                            username: _this.loginFormModel.username,
                            password: createPassword(_this.loginFormModel.username, _this.loginFormModel.password)
                        }).then(function (data) {
                            console.log(data);
                            ApiService_1.default.saveToken(data.Authorization);
                            StoreUtil_1.default.setStore(Constants_1.STORE_KEYS.USER_PREMISSION, data.permission, StoreUtil_1.default.SESSION_TYPE);
                            window.location.href = contextPath + "ui/system/home";
                        }).catch(function (error) {
                            _this.errorMessage(error.message, error);
                        });
                    }
                });
            }
        },
        template: "\n  <el-form ref=\"form\" :rules=\"rules\" :model=\"loginFormModel\">\n    <el-form-item prop=\"username\">\n      <el-input\n        placeholder=\"\u8BF7\u8F93\u5165\u8D26\u53F7\"\n        prefix-icon=\"el-icon-user\"\n        v-model=\"loginFormModel.username\"></el-input>\n    </el-form-item>\n    <el-form-item prop=\"password\">\n      <el-input\n        placeholder=\"\u8BF7\u8F93\u5165\u5BC6\u7801\"\n        show-password\n        v-model=\"loginFormModel.password\"\n        prefix-icon=\"el-icon-unlock\"></el-input>\n    </el-form-item>\n    <el-form-item>\n      <el-button v-loading=\"loginLoading\" @click=\"handleLogin\" class=\"full-width\" type=\"primary\">\u767B\u5F55</el-button>\n    </el-form-item>\n  </el-form>\n  "
    };
});
