// @ts-ignore
import ApiService from 'utils/ApiService'
// @ts-ignore
import MessageMixins from 'mixins/MessageMixins'

/**
 * 角色添加用户
 */
export default {
  mixins: [
      MessageMixins
  ],
  props: {
    // 角色ID
    roleId: {
      required: true,
      type: String
    }
  },
  data () {
    return {
      // 人员穿梭框数据
      userTransferData: [],
      // 选中的人员信息
      userTransferModel: [],
      loading: false
    }
  },
  mounted () {
    this.loading = true
    ApiService.postAjax('sys/user/list', {})
        .then(userList => {
          this.userTransferData = userList.map(user => {
            return {
              key: user.userId,
              label: user.name
            }
          })
          this.loadRoleUser()
        }).catch(error => {
          this.loading = false
          this.errorMessage('加载用户角色信息发生错误', error)
        })
  },
  watch: {
    /**
     * 监控角色ID变化
     */
    roleId (_new, old) {
      if (_new !== old) {
        this.loadRoleUser()
      }
    }
  },
  methods: {
    /**
     * 设置角色用户
     */
    handleSetUser () {
      const parameter = {}
      parameter[this.roleId] = this.userTransferModel
      ApiService.postAjax('sys/role/updateUser', parameter)
          .then(() => {
            this.successMessage('保存成功')
          }).catch(error => {
            this.errorMessage('设置人员失败', error)
          })
    },
    loadRoleUser () {
      this.loading = true
      ApiService.postAjax('sys/role/getRoleUserId', { roleId: this.roleId })
          .then(data => {
            this.loading = false
            this.userTransferModel = data
          }).catch(error => {
            this.loading = false
            this.errorMessage('加载用户角色信息发生错误', error)
          })
    }
  },
  template:  `
  <div v-loading="loading" style="width: 496px; padding: 20px; border:1px dashed #A9A9A9;">
    <el-transfer class="user-transfer"
      :data="userTransferData"
      v-model="userTransferModel"
      filter-placeholder="请输入关键字"
      :titles="['用户', '用户']"
      filterable></el-transfer>
    <div style="padding-top: 15px">
      <el-button type="primary" @click="handleSetUser">保存</el-button> 
    </div> 
  </div>
  `
}