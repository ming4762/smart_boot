// @ts-ignore
import PageBuilder from 'PageBuilder'
// @ts-ignore
import SmartForm from 'plugins/form/SmartForm'
// @ts-ignore
import ApiService from 'utils/ApiService'
// @ts-ignore
import MessageMixins from 'mixins/MessageMixins'
// @ts-ignore
import AuthUtils, { createPassword } from 'system/utils/AuthUtils'

export class AccountMessage extends PageBuilder {
  protected build () {
    return page
  }
}

/**
 * 修改密码组件
 */
const ChangePassword = {
  components: {
    SmartForm
  },
  mixins: [MessageMixins],
  data () {
    // 校验密码输入是否一致
    const validateConfirmPassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.model.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
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
    }
  },
  methods : {
    handleSave () {
      this.$refs['form'].validate()
          .then(valid => {
            const username = AuthUtils.getCurrentUser().username
            // 获取用户ID
            return ApiService.postAjax('auth/updatePassword', {
              oldPassword: createPassword(username, this.model.oldPassword),
              password: createPassword(username, this.model.password)
            })
          }).then(result => {
            this.successMessage('修改成功')
      }).catch(error => {
        this.errorMessage("修改密码失败，请稍后重试", error)
      })
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
}

const page = {
  components: {
    ChangePassword
  },
  data () {
    return {
      activeName: 'password'
    }
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
}