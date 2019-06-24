declare const Vue
import UsernameLogin from './UsernameLogin'

/**
 * 页面加载完毕执行
 */
export class Login {
  private vue: any

  /**
   * 初始化函数
   */
  public init () {
    this.initVue()
  }

  /**
   * 初始化vue
   */
  private initVue () {
    this.vue = new Vue({
      el: '#login-container',
      components: {
        // @ts-ignore
        'username-login': new UsernameLogin().build()
      }
    })
  }
}