define(["require", "exports", "utils/ResizeHandler"], function (require, exports, ResizeHandler_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.default = {
        data: function () {
            return {
                clientHeight: 0
            };
        },
        computed: {
            computedTableHeight: function () {
                return this.clientHeight - 30;
            }
        },
        beforeMount: function () {
            var $this = this;
            $this.clientHeight = document.body.clientHeight;
            ResizeHandler_1.default.bind(function () {
                $this.clientHeight = document.body.clientHeight;
            });
        }
    };
});
