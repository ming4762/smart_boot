/**
 * 菜单项
 */
export default {
  props: {
    menuId: String,
    icon: {
      type: String,
      default: ''
    },
    title: {
      type: String,
      default: ''
    },
    color: {
      type: String,
      default: '#FFFFFF'
    },
    activeColor: {
      type: String,
      default: '#FFFFFF'
    },
    active: Boolean,
  },
  computed: {
    computedColorStyle () {
      const color = this.computedActive ? this.activeColor : this.color
      return 'color: ' + color
    },
    getBus (): any {
      // @ts-ignore
      return busVue
    },
    /**
     * 激活状态计算属性
     */
    computedActive () {
      if (this.menuId && this.getBus.activeTopMenu) {
        if (this.getBus.activeTopMenu.id === this.menuId) {
          return true
        }
      }
      if (this.active !== undefined) {
        return this.active
      }
      return false
    }
  },
  template: `
  <div>
    <i v-if="icon" style="margin-right: 10px;" :class="icon" :style="computedColorStyle"></i>
    <span v-if="title" :style="computedColorStyle" slot="title">{{title}}</span>
  </div>
  `
}