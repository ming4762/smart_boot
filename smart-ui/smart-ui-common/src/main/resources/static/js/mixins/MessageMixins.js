define(["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.default = {
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
            }
        }
    };
});
