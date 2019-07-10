// @ts-ignore
import CommonUtils from 'utils/CommonUtils'
/**
 * 顶部操作按钮
 */
export default {

  data () {
    return {
      buttonList: [],
      // 标识全屏状态
      isFullScreen: false
    }
  },
  created () {
    this.buttonList = [
      {
        // title: this.$t('navbarButton.notice'),
        id: 'message',
        title: '通知',
        icon: 'el-icon-message-solid',
        handler: this.handleClickMessage
      },
      {
        // title: this.$t('navbarButton.fullScreen'),
        title: '全屏',
        id: 'fullScreen',
        icon: 'el-icon-full-screen',
        handler: this.handleClickFullScreen
      },
      {
        // title: this.$t('navbarButton.user'),
        id: 'user',
        title: '用户',
        icon: 'el-icon-user-solid',
        handler: this.handleClickUser
      }
    ]
  },
  methods: {
    handleClickMessage () {

    },
    /**
     * 全屏
     */
    handleClickFullScreen () {
      this.isFullScreen ? CommonUtils.exitFullscreen() : CommonUtils.fullScreen()
      this.isFullScreen = !this.isFullScreen
    },
    /**
     * 点击用户
     */
    handleClickUser () {

    }
  },
  template: `
  <div>
    <ul class="full-height navbar-button-list">
      <li
        v-for="(button, i) in buttonList"
        @click="button.handler"
        class="navbar-button-li"
        :key="i">
        <el-tooltip
          :content="button.title"
          placement="top">
          <el-dropdown v-if=""></el-dropdown>
          <a :class="button.icon"></a>
        </el-tooltip>
      </li>
    </ul>  
  </div>
  `
}