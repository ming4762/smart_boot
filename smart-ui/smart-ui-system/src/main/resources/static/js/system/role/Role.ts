// @ts-ignore
import ComponentBuilder from 'ComponentBuilder'
// @ts-ignore
import LayoutMixins from 'mixins/LayoutMixins'
// @ts-ignore
import SmartTableCRUD from 'plugins/table/SmartTableCRUD'
// @ts-ignore
import ApiService from 'utils/ApiService'
// @ts-ignore
import TimeUtils from 'utils/TimeUtils'


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
      new LayoutMixins().build()
    ]
  }

  protected components () {
    return {
      'smart-table-crud': new SmartTableCRUD().build()
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
      // 默认按钮配置 todo: 设置权限
      defaultButtonConfig: {
        add: {
          rowShow: false,
          permission: 'system:role:save'
        },
        delete: {
          rowShow: false,
          permission: 'system:role:delete'
        },
        edit: {
          permission: 'system:role:update'
        }
      }
    }
  }

  protected template () {
    return `
    <div style="padding: 15px">
      <smart-table-crud
        :defaultButtonConfig="defaultButtonConfig"
        queryUrl="sys/role/listWithAll"
        deleteUrl="sys/role/batchDelete"
        saveUpdateUrl="sys/role/saveUpdate"
        :keys="['roleId']"
        tableName="角色" 
        :apiService="apiService"
        labelWidth="80px"
        :height="computedTableHeight"
        :columnOptions="columnOptions">
        <!--角色类型插槽-->
        <template v-slot:table-roleType="{row}">
          <el-tag>{{roleTypeMap[row.roleType]}}</el-tag>
        </template>
        <!--角色状态插槽-->
        <template v-slot:table-enable="{row}">
          <el-switch
            v-model="row.enable"
            disabled
            active-color="#13ce66"
            inactive-color="#ff4949">
          </el-switch>
        </template>
      </smart-table-crud>
    </div>
    `
  }
}