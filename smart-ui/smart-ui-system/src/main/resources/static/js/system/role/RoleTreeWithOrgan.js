define(["require", "exports", "utils/ApiService", "mixins/MessageMixins"], function (require, exports, ApiService_1, MessageMixins_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.default = {
        mixins: [
            MessageMixins_1.default
        ],
        data() {
            return {
                treeData: []
            };
        },
        mounted() {
            this.load();
        },
        methods: {
            getIconClass(type) {
                if (type === 'role') {
                    return 'el-icon-user';
                }
                else {
                    return 'el-icon-school';
                }
            },
            load() {
                ApiService_1.default.postAjax('sys/role/listAllTreeWithOrgan', {})
                    .then(data => {
                    this.treeData = [data];
                }).catch(error => {
                    this.errorMessage('加载角色树失败', error);
                });
            }
        },
        template: `
  <el-tree
    v-on="$listeners"
    :expand-on-click-node="false"
    node-key="id"
    :default-expanded-keys="['0']"
    :data="treeData">
    <template v-slot="{data}">
      <div>
        <i :class="getIconClass(data.attributes ? data.attributes.type : '')"></i>
        <span class="el-tree-node__label">{{data.text}}</span>
      </div>
    </template>
  </el-tree>
  `
    };
});
