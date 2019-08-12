export default {
    props: {
        icon: {
            type: String,
            default: ''
        },
        title: {
            type: String,
            default: ''
        },
        color: {
            type: String,
            default: '#FFFFFF'
        },
        activeColor: {
            type: String,
            default: '#FFFFFF'
        },
        active: {
            type: Boolean,
            default: false
        }
    },
    computed: {
        computedIconStyle() {
            const color = this.active ? this.activeColor : this.color;
            return 'color: ' + color;
        }
    },
    template: `
  <div>
    <i v-if="icon" style="margin-right: 10px;" :class="icon" :style="computedIconStyle"></i>
    <span v-if="title" slot="title">{{title}}</span>
  </div>
  `
};
