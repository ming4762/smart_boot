define(["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.default = {
        methods: {
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
        }
    };
});
