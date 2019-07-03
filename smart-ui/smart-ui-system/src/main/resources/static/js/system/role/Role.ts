// @ts-ignore
import ComponentBuilder from 'ComponentBuilder'
// @ts-ignore
import FlexAside from 'plugins/container/FlexAside'

//组织树
import RoleTreeWithOrgan from 'system/role/RoleTreeWithOrgan'
import RoleDetail from 'system/role/RoleDetail'

// @ts-ignore
import ApiService from 'utils/ApiService'
// @ts-ignore
import TimeUtils from 'utils/TimeUtils'
// @ts-ignore
import PermissionMixins from 'mixins/PermissionMixins'


const roleTypeMap = {
  '1': '业务角色',
  '2': '管理角色',
  '3': '系统内置角色'
}

export class Role extends ComponentBuilder {

  private vue

  /**
   * 初始化函数
   */
  public init () {
    this.initVue()
  }

  private initVue () {
    // @ts-ignore
    this.vue = new Vue({
      el: '#vue-container',
      components: {
        // @ts-ignore
        'home-main': this.build()
      }
    })
  }

  protected mixins () {
    return [
      new PermissionMixins().build()
    ]
  }

  protected components () {
    return {
      'flex-aside': FlexAside,
      'role-tree': RoleTreeWithOrgan,
      'role-detail': RoleDetail
    }
  }

  protected data () {
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
  }

  protected methods () {
    return {
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
    }
  }

  protected template () {
    return `
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

  // protected template1 () {
  //   return `
  //   <div style="padding: 15px">
  //     <smart-table-crud
  //       :defaultButtonConfig="defaultButtonConfig"
  //       queryUrl="sys/role/listWithAll"
  //       deleteUrl="sys/role/batchDelete"
  //       saveUpdateUrl="sys/role/saveUpdate"
  //       :keys="['roleId']"
  //       tableName="角色"
  //       :apiService="apiService"
  //       labelWidth="80px"
  //       :height="computedTableHeight"
  //       :columnOptions="columnOptions">
  //       <!--角色类型插槽-->
  //       <template v-slot:table-roleType="{row}">
  //         <el-tag>{{roleTypeMap[row.roleType]}}</el-tag>
  //       </template>
  //       <!--角色状态插槽-->
  //       <template v-slot:table-enable="{row}">
  //         <el-switch
  //           v-model="row.enable"
  //           disabled
  //           active-color="#13ce66"
  //           inactive-color="#ff4949">
  //         </el-switch>
  //       </template>
  //       <!-- 行按钮插槽 -->
  //       <template slot-scope="scope" slot="row-operation">
  //         <el-tooltip  class="item" effect="dark" content="角色授权" placement="top">
  //           <el-button
  //             type="primary"
  //             @click="handleShowAuthorizeDialog(scope.row)"
  //             icon="el-icon-key"
  //             size="mini"></el-button>
  //         </el-tooltip>
  //         <el-tooltip class="item" effect="dark" content="设置用户" placement="top">
  //           <el-button
  //             type="primary"
  //             @click="handleShowSetUser(scope.row)"
  //             icon="el-icon-user-solid"
  //             size="mini"></el-button>
  //         </el-tooltip>
  //       </template>
  //     </smart-table-crud>
  //   </div>
  //   `
  // }
}