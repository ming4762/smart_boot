// @ts-ignore
import ComponentBuilder from 'ComponentBuilder'
declare var busVue

import Navbar from 'layout/navbar/Navbar'
import SideBar from 'layout/sidebar/SideBar'

/**
 * 框架主体
 */
export default class Layout extends ComponentBuilder {

  /**
   * 创建组件
   */
  protected components () {
    return {
      // @ts-ignore
      'navbar': new Navbar().build(),
      // @ts-ignore
      'SideBar': new SideBar().build()
    }
  }

  /**
   * 计算属性
   */
  protected computed () {
    return {
      getBus () {
        return busVue
      },
      getClassObj () {
        return {
          hideSidebar: !this.getBus.sidebar.opened,
          openSidebar: this.getBus.sidebar.opened,
          withoutAnimation: this.getBus.sidebar.withoutAnimation,
          mobile: this.getBus.device === 'Mobile'
        }
      }
    }
  }

  /**
   * 创建模板
   */
  protected template () {
    return `
<div :class="getClassObj" class="full-height app-wrapper">
  <!--侧边栏组件-->
  <SideBar class="sidebar-container"/>
  <div class="main-container full-height main-outer">
    <!--顶部-->
    <div class="top-div">
      <!--顶部菜单-->
      <navbar/>
    </div>
  </div>
</div>
    `
  }
}