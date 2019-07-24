define(["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.default = {
        data() {
            return {
                messageFullScreenloading: {}
            };
        },
        methods: {
            errorMessage(message, error, options) {
                console.error(error);
                const defaultParameter = {
                    showClose: true,
                    message: message,
                    type: 'error'
                };
                const parameter = options ? Object.assign(defaultParameter, options) : defaultParameter;
                this.$message(parameter);
            },
            successMessage(message, options) {
                const defaultParameter = { message: message, type: 'success' };
                const parameter = options ? Object.assign(defaultParameter, options) : defaultParameter;
                this.$message(parameter);
            },
            showHideFullScreenloading(loading, options) {
                const defaultOption = {
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                };
                if (loading) {
                    this.messageFullScreenloading = this.$loading(Object.assign({}, defaultOption, options || {}));
                    return this.messageFullScreenloading;
                }
                else {
                    return this.messageFullScreenloading.close();
                }
            }
        }
    };
});
