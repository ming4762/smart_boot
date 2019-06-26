define(["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var ResizeHandler = (function () {
        function ResizeHandler() {
        }
        ResizeHandler.bind = function (handler) {
            this.handlers.push(handler);
            if (this.listener === null) {
                this.addListener();
            }
        };
        ResizeHandler.addListener = function () {
            window.addEventListener('resize', this.resize);
            this.listener = {};
        };
        ResizeHandler.resize = function () {
            var _this = this;
            ResizeHandler.handlers.forEach(function (handler) {
                handler.apply(_this);
            });
        };
        ResizeHandler.handlers = [];
        ResizeHandler.listener = null;
        return ResizeHandler;
    }());
    exports.default = ResizeHandler;
});
