define(["require", "exports", "ComponentBuilder"], function (require, exports, ComponentBuilder_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    class SmartContainer extends ComponentBuilder_1.default {
        template() {
            return `
    <div class="common-container">
      <slot></slot>
    </div>
    `;
        }
    }
    exports.default = SmartContainer;
});
