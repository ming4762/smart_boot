export default {
    props: {
        asideTitle: String,
        asideHeaderHeight: {
            type: String,
            default: '30px'
        },
        asideWidth: {
            type: String,
            default: '200px'
        },
        hasAsideHeader: {
            type: Boolean,
            default: true
        }
    },
    data() {
        return {
            name: name,
            asideVisible: true
        };
    },
    computed: {
        getAsideHeaderLineHeight: function () {
            return `line-height: ${this.asideHeaderHeight};`;
        },
        getAsideHeaderIconClass: function () {
            return this.asideVisible ? 'el-icon-d-arrow-left' : 'el-icon-d-arrow-right';
        },
        getAsideWidth: function () {
            return this.asideVisible ? this.asideWidth : '30px';
        }
    },
    methods: {
        handleShowHideAside: function () {
            this.asideVisible = !this.asideVisible;
        }
    },
    template: `
  <el-container class="full-height">
    <!--  添加动画效果  -->
    <el-aside
      :width="getAsideWidth"
      class="full-height common-aside">
      <el-header
        v-if="hasAsideHeader"
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
      <div class="full-height" v-show="asideVisible">
        <slot name="aside"/>
      </div>
    </el-aside>
    <el-main style="padding: 0;" class="common-main">
      <slot/>
    </el-main>
  </el-container>
  `
};
