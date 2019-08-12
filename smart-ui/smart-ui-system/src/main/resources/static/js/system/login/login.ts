declare const Vue
import UsernameLogin from './UsernameLogin.js'
declare const ready

ready(function () {
  new Login().init()
})

/**
 * 页面加载完毕执行
 */
export class Login {
  private vue: any

  /**
   * 初始化函数
   */
  public init () {
    Login.pageToTop()
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
        'username-login': UsernameLogin
      }
    })
  }

  /**
   * 跳转到顶层页面
   * TODO: 待测试
   */
  private static pageToTop () {
    // 判断当前页面是否在iframe中，如果在iframe中，跳转到顶层页面
    if (window.frames.length != parent.frames.length) {
      parent.location.href = window.location.href
    }
  }
}