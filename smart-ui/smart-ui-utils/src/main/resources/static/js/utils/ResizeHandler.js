define(["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    class ResizeHandler {
        static bind(handler) {
            this.handlers.push(handler);
            if (this.listener === null) {
                this.addListener();
            }
        }
        static addListener() {
            window.addEventListener('resize', this.resize);
            this.listener = {};
        }
        static resize() {
            ResizeHandler.handlers.forEach((handler) => {
                handler.apply(this);
            });
        }
    }
    ResizeHandler.handlers = [];
    ResizeHandler.listener = null;
    exports.default = ResizeHandler;
});
