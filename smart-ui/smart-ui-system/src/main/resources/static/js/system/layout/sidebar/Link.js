import ValidateUtils from '../../../utils/ValidateUtils.js';
export default {
    props: {
        to: {
            required: true,
            type: String
        }
    },
    methods: {
        isExternalLink(routePath) {
            return ValidateUtils.validateURL(routePath);
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
