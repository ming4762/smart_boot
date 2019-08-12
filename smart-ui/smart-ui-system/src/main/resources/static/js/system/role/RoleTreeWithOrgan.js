import ApiService from '../../utils/ApiService.js';
import MessageMixins from '../../mixins/MessageMixins.js';
export default {
    mixins: [
        MessageMixins
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
            ApiService.postAjax('sys/role/listAllTreeWithOrgan', {})
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
