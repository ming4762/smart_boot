// @ts-ignore
import CommonUtils from '../../utils/CommonUtils.js'


/**
 * 视频播放组件
 */
export default {
  props: {
    // 视频监控数据
    data: {
      type: Object,
      required: true
    },
    // token
    token: {
      type: String,
      required: true
    },
    hd: {
      type: Boolean,
      default: false
    },
    // 播放器高度
    height: {
      type: Number,
      default: 600
    },
    // 播放器宽度
    width: {
      type: Number,
      default: 800
    },
    // 是否自动播放
    autoplay: {
      type: Boolean,
      default: true
    },
    // 延迟初始化
    lazyInit: {
      type: Boolean,
      default: false
    },
    // 解码器路径
    decoderPath: {
      type: String,
      default: CommonUtils.withContextPath('js/ezuikit')
    }
  },
  data () {
    return {
      // 播放器
      player: null
    }
  },
  /**
   * 生命周期钩子
   */
  mounted () {
    if (!this.lazyInit) {
      this.initPlayer()
    }
  },
  computed: {
    /**
     * 获取播放地址
     * @returns {*}
     */
    getUrl () {
      return this.hd ? this.data.hdUrl : this.data.url
    },
    /**
     * 获取播放器样式
     */
    getPlayDivStyle () {
      return `width:${this.width}px;height:${this.height}px;`
    }
  },
  watch: {
    /**
     * 监控数据变化
     */
    data: {
      deep: true,
      handler: function () {
        this.initPlayer()
      }
    }
  },
  methods: {
    play () {
      if (this.player) {
        this.player.play()
      }
    },
    stop () {
      if (this.player) {
        this.player.stop()
      }
    },
    initPlayer (token) {
      // @ts-ignore
      if (!window.EZUIKit) {
        CommonUtils.loadJS(CommonUtils.withContextPath('js/ezuikit/ezuikit.js'))
            .then(() => {
              this.createPlayer(token)
            })
      } else  {
        this.createPlayer(token)
      }
    },
    createPlayer (token) {
      let useToken = this.token
      if (token) useToken = token
      // @ts-ignore
      this.player = new window.EZUIKit.EZUIPlayer({
        id: 'playerDiv',
        url: this.getUrl,
        autoplay: this.autoplay,
        accessToken: useToken,
        decoderPath: this.decoderPath,
        width: this.width,
        height: this.height
      })
    }
  },
  template: `
  <div :style="getPlayDivStyle" id="playerDiv"></div>
  `
}