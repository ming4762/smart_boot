define(["require", "exports", "utils/ValidateUtils"], function (require, exports, ValidateUtils_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.default = {
        props: {
            to: {
                required: true,
                type: String
            }
        },
        methods: {
            isExternalLink(routePath) {
                return ValidateUtils_1.default.validateURL(routePath);
            }
        },
        template: `
  <a v-if="isExternalLink(to)" :href="to" target="_blank" rel="noopener">
    <slot/>
  </a>
  <a v-else>
    <slot/>
  </a>
  `
    };
});
