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
define(["require", "exports", "ComponentBuilder", "utils/ApiService", "mixins/MessageMixins", "utils/Md5Utils"], function (require, exports, ComponentBuilder_1, ApiService_1, MessageMixins_1, Md5Utils_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    /**
     * 创建密码
     * @param username
     * @param password
     */
    var createPassword = function (username, password) {
        var salt = '1qazxsw2';
        var passwordValue = username + password + salt;
        return Md5Utils_1.default.md5(passwordValue, 2);
    };
    var UsernameLogin = /** @class */ (function (_super) {
        __extends(UsernameLogin, _super);
        function UsernameLogin() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        /**
         * 混入
         */
        UsernameLogin.prototype.mixins = function () {
            return [
                new MessageMixins_1.default().build()
            ];
        };
        /**
         * 返回数据
         */
        UsernameLogin.prototype.data = function () {
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
        };
        UsernameLogin.prototype.methods = function () {
            return {
                /**
                 * 执行登录
                 */
                handleLogin: function () {
                    var _this = this;
                    this.$refs['form'].validate(function (valid) {
                        if (valid) {
                            ApiService_1.default.postAjax('public/login', {
                                username: _this.loginFormModel.username,
                                password: createPassword(_this.loginFormModel.username, _this.loginFormModel.password)
                            }).then(function (data) {
                                // 保存token
                                ApiService_1.default.saveToken(data.Authorization);
                                // 跳转到主页 TODO：可配置
                                console.log(_this);
                                window.location.href = contextPath + "ui/system/home";
                            }).catch(function (error) {
                                _this.errorMessage(error.message, error);
                            });
                        }
                    });
                }
            };
        };
        /**
         * 创建模板
         */
        UsernameLogin.prototype.template = function () {
            return "\n        <el-form ref=\"form\" :rules=\"rules\" :model=\"loginFormModel\">\n          <el-form-item prop=\"username\">\n            <el-input\n              placeholder=\"\u8BF7\u8F93\u5165\u8D26\u53F7\"\n              prefix-icon=\"el-icon-user\"\n              v-model=\"loginFormModel.username\"></el-input>\n          </el-form-item>\n          <el-form-item prop=\"password\">\n            <el-input\n              placeholder=\"\u8BF7\u8F93\u5165\u5BC6\u7801\"\n              show-password\n              v-model=\"loginFormModel.password\"\n              prefix-icon=\"el-icon-unlock\"></el-input>\n          </el-form-item>\n          <el-form-item>\n            <el-button v-loading=\"loginLoading\" @click=\"handleLogin\" class=\"full-width\" type=\"primary\">\u767B\u5F55</el-button>\n          </el-form-item>\n        </el-form>\n        ";
        };
        return UsernameLogin;
    }(ComponentBuilder_1.default));
    exports.default = UsernameLogin;
});