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
        data: function () {
            return {
                treeProps: {
                    children: 'children',
                    label: 'menuName'
                }
            };
        },
        methods: {
            getCheckedNodes: function (leafOnly, includeHalfChecked) {
                return this.$refs['role_menu_tree'].getCheckedNodes(leafOnly, includeHalfChecked);
            },
            setCheckedKeys: function (keys, leafOnly) {
                this.$refs['role_menu_tree'].setCheckedKeys(keys, leafOnly);
            }
        },
        template: "\n  <el-tree\n    ref=\"role_menu_tree\"\n    :data=\"data\"\n    :props=\"treeProps\"\n    :default-expanded-keys=\"['0']\"\n    show-checkbox\n    node-key=\"menuId\">\n  </el-tree>\n  "
    };
});
