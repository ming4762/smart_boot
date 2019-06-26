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
    var MessageMixins = (function (_super) {
        __extends(MessageMixins, _super);
        function MessageMixins() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        MessageMixins.prototype.methods = function () {
            return {
                errorMessage: function (message, error, options) {
                    console.error(error);
                    var defaultParameter = {
                        showClose: true,
                        message: message,
                        type: 'error'
                    };
                    var parameter = options ? Object.assign(defaultParameter, options) : defaultParameter;
                    this.$message(parameter);
                },
                successMessage: function (message, options) {
                    var defaultParameter = { message: message, type: 'success' };
                    var parameter = options ? Object.assign(defaultParameter, options) : defaultParameter;
                    this.$message(parameter);
                }
            };
        };
        return MessageMixins;
    }(ComponentBuilder_1.default));
    exports.default = MessageMixins;
});
