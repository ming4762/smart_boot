/**
 * 组件构造器
 */
export default class ComponentBuilder {

  /**
   * 数据构造方法
   */
  protected data (): any {
    return {}
  }

  /**
   * props
   */
  protected props (): any {
    return {}
  }

  /**
   * 模板构造器
   */
  protected template() {
    return ''
  }

  /**
   * 方法
   */
  protected methods (): {[index: string]: Function} {
    return {}
  }

  /**
   * 组件
   */
  protected components () {
    return {}
  }

  /**
   * 计算属性
   */
  protected computed () {
    return {}
  }

  /**
   * 混入
   */
  protected mixins (): any[] {
    return []
  }

  protected watch (): any {}


  /**
   * 生命周期钩子创建完毕状态
   */
  protected created () {}


  /**
   * 创建组件
   */
  public build () {
    const component = {
      template: this.template(),
      data: this.data,
      methods: this.methods(),
      components: this.components(),
      computed: this.computed(),
      mixins: this.mixins(),
      props: this.props(),
      watch: this.watch()
    }
    if (this['beforeMount']) {
      component['beforeMount'] = this['beforeMount']
    }
    if (this['created']) {
      component['created'] = this['created']
    }
    if (this['mounted']) {
      component['mounted'] = this['mounted']
    }
    if (this['beforeUpdate']) {
      component['beforeUpdate'] = this['beforeUpdate']
    }
    return component
  }
}