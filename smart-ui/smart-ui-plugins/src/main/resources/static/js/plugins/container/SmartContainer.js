var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
        return extendStatics(d, b);
    }
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
define(["require", "exports", "ComponentBuilder"], function (require, exports, ComponentBuilder_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var SmartContainer = (function (_super) {
        __extends(SmartContainer, _super);
        function SmartContainer() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        SmartContainer.prototype.template = function () {
            return "\n    <div class=\"common-container\">\n      <slot></slot>\n    </div>\n    ";
        };
        return SmartContainer;
    }(ComponentBuilder_1.default));
    exports.default = SmartContainer;
});
