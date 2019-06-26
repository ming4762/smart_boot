define(["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    /**
     * 页面大小变化时调整
     */
    var ResizeHandler = /** @class */ (function () {
        function ResizeHandler() {
        }
        /**
         * 绑定操作
         * @param handler
         */
        ResizeHandler.bind = function (handler) {
            this.handlers.push(handler);
            if (this.listener === null) {
                this.addListener();
            }
        };
        /**
         * 添加时间
         */
        ResizeHandler.addListener = function () {
            window.addEventListener('resize', this.resize);
            this.listener = {};
        };
        /**
         * 执行resize
         */
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
