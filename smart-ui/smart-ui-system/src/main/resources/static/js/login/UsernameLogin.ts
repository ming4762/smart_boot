// @ts-ignore
import ComponentBuilder from 'ComponentBuilder'
// @ts-ignore
import ApiService from 'utils/ApiService'
// @ts-ignore
import MessageMixins from 'mixins/MessageMixins'
export default class UsernameLogin extends ComponentBuilder {

  /**
   * 混入
   */
  protected mixins (): any[] {
    return [
        new MessageMixins().build()
    ]
  }

  /**
   * 返回数据
   */
  protected data () {
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
  }

  protected methods () {
    return {
      /**
       * 执行登录
       */
      handleLogin: function () {
        this.$refs['form'].validate(valid => {
          if (valid) {
            ApiService.postAjax('public/login', this.loginFormModel)
                .then(data => {
                  console.log(data)
                }).catch(error => {
                  this.errorMessage(error.message, error)
                })
          }
        })
      }
    }
  }


  /**
   * 创建模板
   */
  protected template () {
    return `
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
}