// @ts-ignore
import ConfigUtils from '../../utils/ConfigUtils.js'
import UsernameLogin from './UsernameLogin.js'

const pageToTop = () => {
  // 判断当前页面是否在iframe中，如果在iframe中，跳转到顶层页面
  if (window.frames.length != parent.frames.length) {
    parent.location.href = window.location.href
  }
}

/**
 * 登录混入
 */
export default {
  components: {
    'username-login': UsernameLogin
  },
  created () {
    pageToTop()
    ConfigUtils.loadConfig()
        .catch(error => {
          this.errorMessage(error.message, error)
        })
  }
}