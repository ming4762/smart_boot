define(["require", "exports", "utils/ApiService", "mixins/MessageMixins"], function (require, exports, ApiService_1, MessageMixins_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.default = {
        mixins: [
            MessageMixins_1.default
        ],
        props: {
            organId: {
                type: String
            }
        },
        data() {
            return {
                organ: {}
            };
        },
        mounted() {
            const $this = this;
            if ($this.organId) {
                $this.load();
            }
        },
        watch: {
            organId: function (_new, old) {
                if (_new !== old) {
                    this.load();
                }
            }
        },
        methods: {
            load() {
                ApiService_1.default.postAjax('sys/organ/get', { organId: this.organId })
                    .then(data => {
                    console.log(data);
                    this.organ = data;
                }).catch(error => {
                    this.errorMessage('获取组织详情失败', error);
                });
            }
        },
        template: `
  <table>
      <tr>
        <td>abc</td>
      </tr>
    </table>
  `
    };
});
