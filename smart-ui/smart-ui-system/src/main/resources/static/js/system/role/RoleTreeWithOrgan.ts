// @ts-ignore
import ApiService from 'utils/ApiService'
// @ts-ignore
import MessageMixins from 'mixins/MessageMixins'

export default {
  mixins: [
    MessageMixins
  ],
  data () {
    return {
      treeData: []
    }
  },
  mounted () {
    this.load()
  },
  methods: {
    // 获取节点的样式
    getIconClass (type) {
      if (type === 'role') {
        return 'el-icon-user'
      } else {
        return 'el-icon-school'
      }
    },
    load () {
      // 加载数据
      ApiService.postAjax('sys/role/listAllTreeWithOrgan', {})
          .then(data => {
            this.treeData = [data]
          }).catch(error => {
            this.errorMessage('加载角色树失败', error)
          })
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
}