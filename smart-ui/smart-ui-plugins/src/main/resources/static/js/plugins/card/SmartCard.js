define(["require", "exports", "ComponentBuilder"], function (require, exports, ComponentBuilder_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    class SmartCard extends ComponentBuilder_1.default {
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
    exports.default = SmartCard;
});
