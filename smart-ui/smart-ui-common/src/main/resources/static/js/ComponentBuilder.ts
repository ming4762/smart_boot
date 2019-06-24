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
     * 生命周期钩子创建完毕状态
     */
    protected created () {}

    /**
     * 创建组件
     */
    public build () {
        return {
            template: this.template(),
            data: this.data,
            methods: this.methods(),
            created: this.created
        }
    }
}