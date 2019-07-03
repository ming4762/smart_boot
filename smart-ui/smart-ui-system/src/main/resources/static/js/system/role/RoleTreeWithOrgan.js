define(["require", "exports", "utils/ApiService", "mixins/MessageMixins"], function (require, exports, ApiService_1, MessageMixins_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.default = {
        mixins: [
            MessageMixins_1.default
        ],
        data: function () {
            return {
                treeData: []
            };
        },
        mounted: function () {
            this.load();
        },
        methods: {
            getIconClass: function (type) {
                if (type === 'role') {
                    return 'el-icon-user';
                }
                else {
                    return 'el-icon-school';
                }
            },
            load: function () {
                var _this = this;
                ApiService_1.default.postAjax('sys/role/listAllTreeWithOrgan', {})
                    .then(function (data) {
                    _this.treeData = [data];
                }).catch(function (error) {
                    _this.errorMessage('加载角色树失败', error);
                });
            }
        },
        template: "\n  <el-tree\n    v-on=\"$listeners\"\n    :expand-on-click-node=\"false\"\n    node-key=\"id\"\n    :default-expanded-keys=\"['0']\"\n    :data=\"treeData\">\n    <template v-slot=\"{data}\">\n      <div>\n        <i :class=\"getIconClass(data.attributes ? data.attributes.type : '')\"></i>\n        <span class=\"el-tree-node__label\">{{data.text}}</span>\n      </div>\n    </template>\n  </el-tree>\n  "
    };
});
