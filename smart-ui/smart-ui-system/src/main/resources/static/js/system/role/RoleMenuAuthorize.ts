// @ts-ignore
import ApiService from 'utils/ApiService'
// @ts-ignore
import MessageMixins from 'mixins/MessageMixins'
// @ts-ignore
import TreeUtils from 'utils/TreeUtils'

import MenuTree from 'system/menu/MenuTree'
/**
 * 角色授权操作
 */
export default {

  components: {
    'menu-tree': MenuTree
  },
  mixins: [
    MessageMixins
  ],
  props: {
    roleId: {
      required: true,
      type: String
    }
  },
  data () {
    return {
      // 激活的菜单配置
      activeMenuConfigId: '',
      // 菜单配置信息
      menuConfigData: [],
      // 菜单数据
      menuTreeData: [],
      treeLoading: false
    }
  },
  mounted () {
    this.loadMenuConfigData()
  },
  computed: {
    computedMenuTreeData () {
      if (this.menuTreeData.length === 0) {
        return []
      } else {
        const menuData = []
        this.menuTreeData.forEach(menu => {
          menuData.push({
            menuId: menu.menuId,
            menuName: menu.menuName,
            parentId: menu.parentId,
            type: 'menu'
          })
          // 如果菜单有对应功能
          if (menu.functionId !== null && menu.function !== null && menu.function.children !== null) {
            const children: any[] = menu.function.children
            children.forEach(child => {
              menuData.push({
                menuId: child.functionId,
                menuName: child.functionName,
                parentId: menu.menuId,
                type: 'function'
              })
            })
          }
        })
        return [{
          menuId: '0',
          menuName: '根目录',
          children: TreeUtils.convertList2Tree(menuData, ['menuId', 'parentId'])
        }]
      }
    }
  },
  watch: {
    activeMenuConfigId (_new, old) {
      if (_new !== old) {
        this.reloadData()
      }
    },
    /**
     * 监控角色ID变化
     * @param _new
     * @param old
     */
    roleId (_new, old) {
      if (_new !== old) {
        this.reloadData()
      }
    }
  },
  methods: {
    reloadData () {
      this.loadMenuTreeData().then(() => {
        this.loadAuthentication()
      })
    },
    /**
     * 加载菜单配置数据
     */
    loadMenuConfigData () {
      ApiService.postAjax('sys/menuConfig/list', {})
          .then(data => {
            if (data) {
              for (let i=0; i<data.length; i++) {
                if (data[i].status === true) {
                  this.activeMenuConfigId = data[i].configId
                  break
                }
              }
              this.menuConfigData = data
            }
          }).catch(error => {
            this.errorMessage('加载菜单配置列表失败', error)
          })
    },
    loadMenuTreeData () {
      this.treeLoading = true
      return ApiService.postAjax('sys/menu/listWithFunction', {
        'menuConfigId@=': this.activeMenuConfigId
      }).then((data) => {
        this.treeLoading = false
        this.menuTreeData = data
      }).catch(error => {
        this.treeLoading = false
        this.errorMessage('记载菜单列表失败', error)
      })
    },
    /**
     * 加载角色认证信息
     */
    loadAuthentication () {
      ApiService.postAjax('sys/role/queryAuthentication', {
        menuConfigId: this.activeMenuConfigId,
        roleId: this.roleId
      }).then(result => {
        // 获取当前菜单配置、当前角色对应的权限信息
        const menuFunctionIdList = []
        result.forEach(roleMenu => {
          if (roleMenu.leaf === true) {
            menuFunctionIdList.push(roleMenu.menuFunctionId)
          }
        })
        this.$refs['menuTree'].setCheckedKeys(menuFunctionIdList, true)
      }).catch(error => {
        this.errorMessage('加载角色菜单权限发生错误', error)
      })
    },
    /**
     * 执行保存操作
     */
    handleSave () {
      console.log(this)
      const checkNodeList: any[] = this.$refs['menuTree'].getCheckedNodes(false, true)
      if (checkNodeList !== null) {
        const checkData = []
        // 遍历选中节点,并创建对象
        checkNodeList.forEach(node => {
          if (node.menuId !== '0') {
            checkData.push({
              roleId: this.roleId,
              menuConfigId: this.activeMenuConfigId,
              menuFunctionId: node.menuId,
              type: node.type,
              leaf: node.hasParent !== false && node.hasChild !== true
            })
          }
        })
        // 保存数据
        ApiService.postAjax('sys/role/authorize', checkData)
            .then(result => {
              this.successMessage('角色授权成功')
            }).catch(error => {
              this.errorMessage('角色授权发生错误！', error)
            })
      }
    }
  },
  template: `
  <div class="role-operation-container" style="width: 600px">
    <el-select filterable v-model="activeMenuConfigId">
      <el-option
        :key="item.configId"
        :label="item.configName"
        :value="item.configId"
        v-for="item in menuConfigData">
        <span style="float: left">{{ item.configName }}</span>
        <span class="el-icon-check" style="float: right;line-height: 34px;" v-if="item.status === true"></span>
      </el-option>
    </el-select>
    <br/>
    <menu-tree v-loading="treeLoading" ref="menuTree" :data="computedMenuTreeData"></menu-tree>
    
    <el-button style="margin-top: 15px" type="primary" @click="handleSave">保存</el-button>
  </div>
  `
}