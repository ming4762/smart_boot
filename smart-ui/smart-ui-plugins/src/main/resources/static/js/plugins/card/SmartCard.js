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
    var SmartCard = (function (_super) {
        __extends(SmartCard, _super);
        function SmartCard() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        SmartCard.prototype.props = function () {
            return {
                hoverColor: String
            };
        };
        SmartCard.prototype.data = function () {
            return {
                bodyStyle: {
                    background: 'blue'
                }
            };
        };
        SmartCard.prototype.methods = function () {
            return {
                handleMouseOver: function (event) {
                    console.log(event);
                },
                handleMouseLeave: function (event) {
                    console.log(event);
                }
            };
        };
        SmartCard.prototype.template = function () {
            return "\n    <el-card\n      :body-style=\"bodyStyle\"\n      @mouseover.native=\"handleMouseOver\"\n      @mouseleave.native=\"handleMouseLeave\"\n      v-bind=\"$attrs\">\n      <slot></slot>\n    </el-card>\n    ";
        };
        return SmartCard;
    }(ComponentBuilder_1.default));
    exports.default = SmartCard;
});
