/**
 * 菜单树
 */
export default {

  props: {
    data: {
      required: true,
      type: Array
    }
  },
  data () {
    return {
      treeProps: {
        children: 'children',
        label: 'menuName'
      }
    }
  },
  methods: {
    /**
     * 获取选中的节点
     */
    getCheckedNodes (leafOnly?: boolean, includeHalfChecked?: boolean): any[] {
      return this.$refs['role_menu_tree'].getCheckedNodes(leafOnly, includeHalfChecked)
    },
    /**
     * 设置选中节点的key
     */
    setCheckedKeys (keys: any[], leafOnly?: boolean) {
      this.$refs['role_menu_tree'].setCheckedKeys(keys, leafOnly)
    }
  },
  template: `
  <el-tree
    ref="role_menu_tree"
    :data="data"
    :props="treeProps"
    :default-expanded-keys="['0']"
    show-checkbox
    node-key="menuId">
  </el-tree>
  `
}