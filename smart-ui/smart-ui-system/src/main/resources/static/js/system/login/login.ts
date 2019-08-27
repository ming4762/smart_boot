// @ts-ignore
import PageBuilder from '../../PageBuilder.js'

import LoginMixins from './LoginMixins.js'

declare const ready

ready(function () {
  // @ts-ignore
  new Login().init()
})

class Login extends PageBuilder {
  /**
   * 构建函数
   */
  protected build () {
    return page
  }
}

const page = {
  mixins: [
    LoginMixins
  ],
  template: `
  <div class="login-container">
    <div class="login-main">
      <div class="login-title">
        <h2>smart-ui</h2>
        <p>欢迎访问smart</p>
      </div>
      <div class="login-form-container">
        <username-login/>
      </div>
    </div>
  </div>
  `
}

