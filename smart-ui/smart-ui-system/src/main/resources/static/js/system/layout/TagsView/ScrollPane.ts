export default {

  methods: {
    handleScroll (event: any) {
      const eventDelta = event.wheelDelta || -event.deltaY * 40
      const $scrollWrapper = this.$refs.scrollContainer.$refs.wrap
      $scrollWrapper.scrollLeft = $scrollWrapper.scrollLeft + eventDelta / 4
    }
  },

  template: `
  <el-scrollbar
    ref="scrollContainer"
    :vertical="false"
    class="scroll-container"
    @wheel.native.prevent="handleScroll">
    <slot/>
  </el-scrollbar>
  `
}