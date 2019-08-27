import MonitorView from './MonitorView.js'
import MonitorControl from './MonitorControl.js'

/**
 * 监控页面
 */
export default {
  components: {
    MonitorView,
    MonitorControl
  },
  methods: {
    /**
     * 开始云台控制
     * @param direction
     */
    handlePtzStart (direction) {
      const listener = 'ptz-start'
      if (this.$listeners[listener]) {
        this.$emit(listener, direction)
      }
    },
    /**
     * 结束云台控制
     * @param direction
     */
    handlePtzStop (direction) {
      const listener = 'ptz-stop'
      if (this.$listeners[listener]) {
        this.$emit(listener, direction)
      }
    },
    /**
     * 初始化播放器
     */
    initPlayer (token) {
      this.$refs['monitorView'].initPlayer(token)
    }
  },
  template: `
  <div>
    <MonitorView
      ref="monitorView"
      v-bind="$attrs"
      v-on="$listeners"/>
    <MonitorControl
      @ptz-start="handlePtzStart"
      @ptz-stop="handlePtzStop"/>
  </div>
  `
}