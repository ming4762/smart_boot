import ComponentBuilder from 'ComponentBuilder';
export default class SmartCard extends ComponentBuilder {
    props() {
        return {
            hoverColor: String
        };
    }
    data() {
        return {
            bodyStyle: {
                background: 'blue'
            }
        };
    }
    methods() {
        return {
            handleMouseOver(event) {
                console.log(event);
            },
            handleMouseLeave(event) {
                console.log(event);
            }
        };
    }
    template() {
        return `
    <el-card
      :body-style="bodyStyle"
      @mouseover.native="handleMouseOver"
      @mouseleave.native="handleMouseLeave"
      v-bind="$attrs">
      <slot></slot>
    </el-card>
    `;
    }
}
