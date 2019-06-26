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
define(["require", "exports", "ComponentBuilder", "utils/ResizeHandler"], function (require, exports, ComponentBuilder_1, ResizeHandler_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var LayoutMixins = (function (_super) {
        __extends(LayoutMixins, _super);
        function LayoutMixins() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        LayoutMixins.prototype.data = function () {
            return {
                clientHeight: 0
            };
        };
        LayoutMixins.prototype.beforeMount = function () {
            var $this = this;
            $this.clientHeight = document.body.clientHeight;
            ResizeHandler_1.default.bind(function () {
                $this.clientHeight = document.body.clientHeight;
            });
        };
        return LayoutMixins;
    }(ComponentBuilder_1.default));
    exports.default = LayoutMixins;
});
