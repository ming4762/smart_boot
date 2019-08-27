/**
 * 云台控制
 */
export default {
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
    }
  },
  template: `
  <div class="monitor-control">
    <el-button @mouseup.native="handlePtzStop(2)" @mousedown.native="handlePtzStart(2)" icon="el-icon-arrow-left"></el-button>
    <el-button @mouseup.native="handlePtzStop(0)" @mousedown.native="handlePtzStart(0)" icon="el-icon-arrow-up"></el-button>
    <el-button @mouseup.native="handlePtzStop(3)" @mousedown.native="handlePtzStart(3)" icon="el-icon-arrow-right"></el-button>
    <el-button @mouseup.native="handlePtzStop(1)" @mousedown.native="handlePtzStart(1)" icon="el-icon-arrow-down"></el-button>
  </div>
  `
}