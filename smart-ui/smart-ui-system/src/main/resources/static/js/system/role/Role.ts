// @ts-ignore
import PageBuilder from '../../PageBuilder.js'
// @ts-ignore
import FlexAside from '../../plugins/container/FlexAside.js'

//组织树
import RoleTreeWithOrgan from './RoleTreeWithOrgan.js'
import RoleDetail from './RoleDetail.js'

// @ts-ignore
import ApiService from '../../utils/ApiService.js'
// @ts-ignore
import TimeUtils from '../../utils/TimeUtils.js'
// @ts-ignore
import PermissionMixins from '../../mixins/PermissionMixins.js'


const roleTypeMap = {
  '1': '业务角色',
  '2': '管理角色',
  '3': '系统内置角色'
}

declare const ready

ready(function () {
  // @ts-ignore
  new Role().init()
})

class Role extends PageBuilder {
  /**
   * 构建函数
   */
  protected build () {
    return page
  }
}

const page = {
  components: {
    'flex-aside': FlexAside,
    'role-tree': RoleTreeWithOrgan,
    'role-detail': RoleDetail
  },
  mixins: [PermissionMixins],
  data () {
    const roleTypeDic = []
    Object.keys(roleTypeMap).forEach(key => {
      roleTypeDic.push({
        label: key,
        value: roleTypeMap[key]
      })
    })
    return {
      apiService: ApiService,
      roleTypeMap: roleTypeMap,
      columnOptions: [
        {
          prop: 'roleId',
          label: '角色ID',
          form: {
            visible: false
          },
          table: {
            visible: false,
            displayControl: false
          }
        },
        {
          label: '角色名',
          prop: 'roleName',
          table: {
            width: 120,
            fixed: true
          },
          form: {
            rules: true
          }
        },
        {
          label: '备注',
          prop: 'remark',
          table: {
            minWidth: 200
          },
          form: {}
        },
        {
          label: '类型',
          prop: 'roleType',
          type: 'radio',
          table: {
            width: 100
          },
          form: {
            defaultValue: true,
            dicData: roleTypeDic
          }
        },
        {
          label: '启用',
          prop: 'enable',
          type: 'boolean',
          table: {},
          form: {}
        },
        {
          label: '创建时间',
          prop: 'createTime',
          table: {
            formatter: (row, column, value) => TimeUtils.formatTime(value),
            width: 160
          },
          form: {}
        },
        {
          label: '创建用户',
          prop: 'createUserId',
          table: {
            formatter: (row) => {
              const user = row.createUser
              return user ? user.name : '-'
            }
          },
          form: {}
        },
        {
          label: '更新时间',
          prop: 'updateTime',
          table: {
            formatter: (row, column, value) => TimeUtils.formatTime(value),
            width: 160
          },
          form: {}
        },
        {
          label: '更新人员',
          prop: 'updateUserId',
          table: {
            formatter: (row) => {
              const user = row.updateUser
              return user ? user.name : '-'
            }
          },
          form: {}
        },
        {
          label: '序号',
          prop: 'seq',
          type: 'number',
          table: {
            sortable: true
          },
          form: {
            defaultValue: 1
          }
        }
      ],
      searchValue: '',
      activeTab: 'detail',
      // 选中的组织机构/角色
      selectedOrgan: {},
      selectRole: {}
    }
  },
  methods : {
    handleShowSetUser (role) {
      console.log(role)
    },
    handleShowAuthorizeDialog (role) {
      console.log(role)
    },
    /**
     * 点击树节点事件
     * @param roleOrgan
     */
    handleClickRoleTree (roleOrgan) {
      const type = roleOrgan.attributes ? roleOrgan.attributes.type : ''
      if (type === 'role') {
        this.selectRole = roleOrgan
      } else if (type === 'organ') {
        this.selectedOrgan = roleOrgan
      }
    },
    handleAddRole () {
      this.selectRole = {}
    },
    /**
     * 保存结束时间重新加载数据
     */
    handleAfterSave () {
      this.$refs['roleTree'].load()
    }
  },
  template: `
    <flex-aside
      :hasAsideHeader="false">
      <template slot="aside">
        <div class="organ-aside full-height">
          <div class="organ-aside-search">
            <el-input
              v-model="searchValue"
              suffix-icon="el-icon-search"
              placeholder="搜索部门"></el-input>
          </div>
          <!--部门树-->
          <div style="height: calc(100% - 190px); padding-top: 22px">
            <role-tree
              ref="roleTree"
              @node-click="handleClickRoleTree"/>
          </div>
          <!--添加按钮-->
          <!--  TODO: v-if="validatePermission('system:role:add')" -->
          <div 
            class="organ-aside-add">
            <el-button 
              type="primary"
              @click="handleAddRole"
              style="width: 100%"
              icon="el-icon-plus">添加角色</el-button>
          </div>
        </div>
      </template>
      <!--主题部分-->
      <template>
        <div style="padding: 15px">
          <role-detail
            @after-save="handleAfterSave"
            :organId="selectedOrgan.id"
            :organName="selectedOrgan.text"
            :roleId="selectRole.id"/>
        </div>
      </template>
      <!--添加角色弹窗-->
    </flex-aside>
    `
}