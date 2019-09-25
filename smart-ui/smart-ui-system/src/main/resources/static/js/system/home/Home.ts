// @ts-ignore
import PageBuilder from '../../PageBuilder.js'
import Layout from '../layout/Layout.js'

import SidebarItem from '../layout/sidebar/SidebarItem.js'
import initBus from '../SysBus.js'

declare const ready
declare let busVue



ready(function () {
  const home = new Home()
  busVue = initBus()
  // @ts-ignore
  home.init()
})

class Home extends PageBuilder {

  /**
   * 初始化vue
   */
  protected initVue () {
    // @ts-ignore
    Vue.component('sidebar-item', SidebarItem)
    // @ts-ignore
    this.vue = new Vue({
      el: '#home-container',
      components: {
        // @ts-ignore
        'vue-main': Layout,
      }
    })
  }

}