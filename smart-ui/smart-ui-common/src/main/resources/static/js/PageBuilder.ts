// @ts-ignore
import ComponentBuilder from 'ComponentBuilder'

/**
 * 页面构造函数
 */
export default class PageBuilder extends ComponentBuilder {

  public vue: any

  /**
   * 初始化方法
   */
  public init () {
    this.initVue()
  }

  /**
   * 初始化vue
   */
  private initVue () {
    // @ts-ignore
    this.vue = new Vue({
      el: '#vue-container',
      components: {
        // @ts-ignore
        'vue-main': this.build(),
      }
    })
  }
}