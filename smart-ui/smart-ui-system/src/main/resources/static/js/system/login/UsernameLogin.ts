// @ts-ignore
import ApiService from 'utils/ApiService'
// @ts-ignore
import MessageMixins from 'mixins/MessageMixins'
// 引入MD5工具类
// @ts-ignore
import Md5Utils from 'utils/Md5Utils'
// @ts-ignore
import { STORE_KEYS } from 'Constants'
// @ts-ignore
import StoreUtil from 'utils/StoreUtil'
// 项目跟路径
declare var contextPath: string

/**
 * 创建密码
 * @param username
 * @param password
 */
const createPassword = (username: string, password: string): string => {
  const salt = '1qazxsw2'
  const passwordValue = username + password + salt
  return Md5Utils.md5(passwordValue, 2)
}

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
            console.log(data)
            // 保存token
            ApiService.saveToken(data.Authorization)
            // 保存权限信息
            StoreUtil.setStore(STORE_KEYS.USER_PREMISSION, data.permission, StoreUtil.SESSION_TYPE)
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

