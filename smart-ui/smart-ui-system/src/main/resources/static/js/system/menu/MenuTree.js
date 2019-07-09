define(["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.default = {
        props: {
            data: {
                required: true,
                type: Array
            }
        },
        data() {
            return {
                treeProps: {
                    children: 'children',
                    label: 'menuName'
                }
            };
        },
        methods: {
            getCheckedNodes(leafOnly, includeHalfChecked) {
                return this.$refs['role_menu_tree'].getCheckedNodes(leafOnly, includeHalfChecked);
            },
            setCheckedKeys(keys, leafOnly) {
                this.$refs['role_menu_tree'].setCheckedKeys(keys, leafOnly);
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
    };
});
