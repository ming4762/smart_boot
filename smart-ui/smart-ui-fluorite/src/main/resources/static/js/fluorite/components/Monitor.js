import MonitorView from './MonitorView.js';
import MonitorControl from './MonitorControl.js';
export default {
    components: {
        MonitorView,
        MonitorControl
    },
    methods: {
        handlePtzStart(direction) {
            const listener = 'ptz-start';
            if (this.$listeners[listener]) {
                this.$emit(listener, direction);
            }
        },
        handlePtzStop(direction) {
            const listener = 'ptz-stop';
            if (this.$listeners[listener]) {
                this.$emit(listener, direction);
            }
        },
        initPlayer(token) {
            this.$refs['monitorView'].initPlayer(token);
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
};
