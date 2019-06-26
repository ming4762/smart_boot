// @ts-ignore
import ComponentBuilder from 'ComponentBuilder'

export default class FlexAside extends ComponentBuilder {

  protected props () {
    return {
      // 测边栏标题
      asideTitle: String,
      // 侧边栏顶部高度
      asideHeaderHeight: {
        type: String,
        default: '30px'
      },
      // 侧边栏宽度
      asideWidth: {
        type: String,
        default: '200px'
      }
    }
  }

  data () {
    return {
      name: name,
      // 侧边栏显示装填
      asideVisible: true
    }
  }

  protected computed () {
    return {
      // aside header 计算属性
      getAsideHeaderLineHeight: function () {
        return `line-height: ${this.asideHeaderHeight};`
      },
      /**
       * 顶部图标计算属性
       */
      getAsideHeaderIconClass: function () {
        return this.asideVisible ? 'el-icon-d-arrow-left' : 'el-icon-d-arrow-right'
      },
      /**
       * 侧边栏宽度计算属性
       */
      getAsideWidth: function () {
        return this.asideVisible ? this.asideWidth : '30px'
      }
    }
  }

  protected methods () {
    return {
      /**
       * 切换侧边栏的显示状态
       */
      handleShowHideAside: function () {
        this.asideVisible = !this.asideVisible
      }
    }
  }

  protected template () {
    return `
    <el-container class="common-full-height">
      <!--  添加动画效果  -->
      <el-aside
        :width="getAsideWidth"
        class="common-full-height common-aside">
        <el-header
            :height="asideHeaderHeight"
            :style="getAsideHeaderLineHeight"
            class="aside-header">
        <span
            v-show="asideVisible">{{asideTitle}}</span>
          <i
            @click="handleShowHideAside"
            :class="getAsideHeaderIconClass"
            :style="getAsideHeaderLineHeight"
            class="el-icon-d-arrow-right cousor-pointer aside-header-icon"></i>
        </el-header>
        <div class="common-full-height" v-show="asideVisible">
          <slot name="aside"/>
        </div>
      </el-aside>
      <el-main style="padding: 0;" class="common-main">
        <slot/>
      </el-main>
    </el-container>
    `
  }
}