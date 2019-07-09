
/**
 * 侧边栏样式
 */
const sideBarTheme: any = {
  dark: {
    'background-color': '#304156',
    'button-color': '#ffffff',
    'text-color': '#bfcbd9',
    'active-text-color': '#409EFF'
  },
  light: {
    'background-color': '#DCDCDC',
    'button-color': '#606266',
    'text-color': '#606266',
    'active-text-color': '#4391f4'
  }
}

export default {
  computed: {
    /**
     * 获取消息总线
     */
    getBus () {
      // @ts-ignore
      return busVue
    },
    /**
     * 获取顶部样式
     */
    getTopColor () {
      return this.getBus.theme.topColor
    },
    /**
     * 获取侧边栏样式
     */
    getSideBarTheme () {
      return sideBarTheme[this.getBus.theme.menuTheme]
    },
    /**
     * 获取顶部文字颜色
     * TODO:待完善
     */
    getTopTextColor () {
      return sideBarTheme.dark['text-color']
    },
    /**
     * 顶部激活的菜单字体颜色
     * TODO: 待完善
     */
    topActiveTextColor () {
      // const isDark = ColorUtil.isDark(this.topColor)
      const isDark = true
      return isDark ? '#ffd04b' : '#00008B'
    }
  }
}
