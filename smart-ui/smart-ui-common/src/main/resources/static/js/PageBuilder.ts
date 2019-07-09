
/**
 * 页面构造函数
 */
export default class PageBuilder {

  public vue: any

  /**
   * 初始化方法
   */
  public init () {
    this.initVue()
  }

  protected build () {
    return {}
  }

  /**
   * 初始化vue
   */
  protected initVue () {
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