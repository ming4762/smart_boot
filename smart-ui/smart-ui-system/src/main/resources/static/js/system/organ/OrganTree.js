define(["require", "exports", "utils/ApiService", "mixins/MessageMixins"], function (require, exports, ApiService_1, MessageMixins_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.default = {
        mixins: [
            MessageMixins_1.default
        ],
        data: function () {
            return {
                organTreeData: []
            };
        },
        mounted: function () {
            this.load();
        },
        computed: {
            computedOrganTreeData: function () {
                return [
                    {
                        id: '0',
                        text: '组织根',
                        object: {
                            organId: '0',
                        },
                        children: this.organTreeData
                    }
                ];
            }
        },
        methods: {
            load: function () {
                var _this = this;
                ApiService_1.default.postAjax('sys/organ/listTree', [])
                    .then(function (data) {
                    _this.organTreeData = data['0'];
                }).catch(function (error) {
                    _this.errorMessage('加载部门数据失败', error);
                });
            }
        },
        template: "\n  <el-tree\n    v-on=\"$listeners\"\n    :expand-on-click-node=\"false\"\n    node-key=\"id\"\n    :default-expanded-keys=\"['0']\"\n    :data=\"computedOrganTreeData\">\n    <template v-slot=\"{data}\">\n      <div>\n        <i class=\"el-icon-school\"></i>\n        <span class=\"el-tree-node__label\">{{data.text}}</span>\n      </div>\n    </template>\n  </el-tree>\n  "
    };
});
