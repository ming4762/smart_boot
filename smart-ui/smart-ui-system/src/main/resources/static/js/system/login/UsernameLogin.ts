// @ts-ignore
import ApiService from '../../utils/ApiService.js'
// @ts-ignore
import MessageMixins from '../../mixins/MessageMixins.js'
// 引入MD5工具类
// @ts-ignore
import { createPassword } from '../utils/AuthUtils.js'
// @ts-ignore
import { STORE_KEYS } from '../../Constants.js'
// @ts-ignore
import StoreUtil from '../../utils/StoreUtil.js'
// 项目跟路径
declare var contextPath: string


export default {
  mixins: [
    MessageMixins
  ],
  data () {
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
    }
  },
  methods: {
    /**
     * 执行登录
     */
    handleLogin: function () {
      // 清除token
      ApiService.saveToken(null)
      this.$refs['form'].validate(valid => {
        if (valid) {
          ApiService.postAjax('public/login', {
            username: this.loginFormModel.username,
            password: createPassword(this.loginFormModel.username, this.loginFormModel.password)
          }).then(data => {
            // 保存token
            ApiService.saveToken(data.Authorization)
            // 保存权限信息
            StoreUtil.setStore(STORE_KEYS.USER_PREMISSION, data.permission, StoreUtil.SESSION_TYPE)
            // 保存用户信息
            StoreUtil.setStore(STORE_KEYS.USER_KEY, data.user, StoreUtil.SESSION_TYPE)
            // 注册秘钥
            if (ApiService.useIde()) {
              return ApiService.registerKey()
            }
          }).then(() => {
            // 跳转到主页 TODO：可配置
            window.location.href = `${contextPath}ui/system/home`
          }).catch(error => {
            this.errorMessage(error.message, error)
          })
        }
      })
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
}

