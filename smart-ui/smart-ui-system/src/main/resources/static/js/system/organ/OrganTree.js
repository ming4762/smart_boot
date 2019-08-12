import ApiService from '../../utils/ApiService.js';
import MessageMixins from '../../mixins/MessageMixins.js';
export default {
    mixins: [
        MessageMixins
    ],
    data() {
        return {
            organTreeData: []
        };
    },
    mounted() {
        this.load();
    },
    computed: {
        computedOrganTreeData() {
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
        load() {
            ApiService.postAjax('sys/organ/listTree', [])
                .then(data => {
                this.organTreeData = data['0'];
            }).catch(error => {
                this.errorMessage('加载部门数据失败', error);
            });
        }
    },
    template: `
  <el-tree
    v-on="$listeners"
    :expand-on-click-node="false"
    node-key="id"
    :default-expanded-keys="['0']"
    :data="computedOrganTreeData">
    <template v-slot="{data}">
      <div>
        <i class="el-icon-school"></i>
        <span class="el-tree-node__label">{{data.text}}</span>
      </div>
    </template>
  </el-tree>
  `
};
