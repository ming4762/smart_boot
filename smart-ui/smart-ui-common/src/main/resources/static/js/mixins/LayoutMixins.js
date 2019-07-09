define(["require", "exports", "utils/ResizeHandler"], function (require, exports, ResizeHandler_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.default = {
        data() {
            return {
                clientHeight: 0
            };
        },
        computed: {
            computedTableHeight() {
                return this.clientHeight - 30;
            }
        },
        beforeMount() {
            const $this = this;
            $this.clientHeight = document.body.clientHeight;
            ResizeHandler_1.default.bind(() => {
                $this.clientHeight = document.body.clientHeight;
            });
        }
    };
});
