define(["require", "exports", "utils/ApiService", "mixins/MessageMixins", "utils/TreeUtils", "system/menu/MenuTree"], function (require, exports, ApiService_1, MessageMixins_1, TreeUtils_1, MenuTree_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.default = {
        components: {
            'menu-tree': MenuTree_1.default
        },
        mixins: [
            MessageMixins_1.default
        ],
        props: {
            roleId: {
                required: true,
                type: String
            }
        },
        data: function () {
            return {
                activeMenuConfigId: '',
                menuConfigData: [],
                menuTreeData: [],
                treeLoading: false
            };
        },
        mounted: function () {
            this.loadMenuConfigData();
        },
        computed: {
            computedMenuTreeData: function () {
                if (this.menuTreeData.length === 0) {
                    return [];
                }
                else {
                    var menuData_1 = [];
                    this.menuTreeData.forEach(function (menu) {
                        menuData_1.push({
                            menuId: menu.menuId,
                            menuName: menu.menuName,
                            parentId: menu.parentId,
                            type: 'menu'
                        });
                        if (menu.functionId !== null && menu.function !== null && menu.function.children !== null) {
                            var children = menu.function.children;
                            children.forEach(function (child) {
                                menuData_1.push({
                                    menuId: child.functionId,
                                    menuName: child.functionName,
                                    parentId: menu.menuId,
                                    type: 'function'
                                });
                            });
                        }
                    });
                    return [{
                            menuId: '0',
                            menuName: '根目录',
                            children: TreeUtils_1.default.convertList2Tree(menuData_1, ['menuId', 'parentId'])
                        }];
                }
            }
        },
        watch: {
            activeMenuConfigId: function (_new, old) {
                if (_new !== old) {
                    this.reloadData();
                }
            },
            roleId: function (_new, old) {
                if (_new !== old) {
                    this.reloadData();
                }
            }
        },
        methods: {
            reloadData: function () {
                var _this = this;
                this.loadMenuTreeData().then(function () {
                    _this.loadAuthentication();
                });
            },
            loadMenuConfigData: function () {
                var _this = this;
                ApiService_1.default.postAjax('sys/menuConfig/list', {})
                    .then(function (data) {
                    if (data) {
                        for (var i = 0; i < data.length; i++) {
                            if (data[i].status === true) {
                                _this.activeMenuConfigId = data[i].configId;
                                break;
                            }
                        }
                        _this.menuConfigData = data;
                    }
                }).catch(function (error) {
                    _this.errorMessage('加载菜单配置列表失败', error);
                });
            },
            loadMenuTreeData: function () {
                var _this = this;
                this.treeLoading = true;
                return ApiService_1.default.postAjax('sys/menu/listWithFunction', {
                    'menuConfigId@=': this.activeMenuConfigId
                }).then(function (data) {
                    _this.treeLoading = false;
                    _this.menuTreeData = data;
                }).catch(function (error) {
                    _this.treeLoading = false;
                    _this.errorMessage('记载菜单列表失败', error);
                });
            },
            loadAuthentication: function () {
                var _this = this;
                ApiService_1.default.postAjax('sys/role/queryAuthentication', {
                    menuConfigId: this.activeMenuConfigId,
                    roleId: this.roleId
                }).then(function (result) {
                    var menuFunctionIdList = [];
                    result.forEach(function (roleMenu) {
                        if (roleMenu.leaf === true) {
                            menuFunctionIdList.push(roleMenu.menuFunctionId);
                        }
                    });
                    _this.$refs['menuTree'].setCheckedKeys(menuFunctionIdList, true);
                }).catch(function (error) {
                    _this.errorMessage('加载角色菜单权限发生错误', error);
                });
            },
            handleSave: function () {
                var _this = this;
                console.log(this);
                var checkNodeList = this.$refs['menuTree'].getCheckedNodes(false, true);
                if (checkNodeList !== null) {
                    var checkData_1 = [];
                    checkNodeList.forEach(function (node) {
                        if (node.menuId !== '0') {
                            checkData_1.push({
                                roleId: _this.roleId,
                                menuConfigId: _this.activeMenuConfigId,
                                menuFunctionId: node.menuId,
                                type: node.type,
                                leaf: node.hasParent !== false && node.hasChild !== true
                            });
                        }
                    });
                    ApiService_1.default.postAjax('sys/role/authorize', checkData_1)
                        .then(function (result) {
                        _this.successMessage('角色授权成功');
                    }).catch(function (error) {
                        _this.errorMessage('角色授权发生错误！', error);
                    });
                }
            }
        },
        template: "\n  <div class=\"role-operation-container\" style=\"width: 600px\">\n    <el-select filterable v-model=\"activeMenuConfigId\">\n      <el-option\n        :key=\"item.configId\"\n        :label=\"item.configName\"\n        :value=\"item.configId\"\n        v-for=\"item in menuConfigData\">\n        <span style=\"float: left\">{{ item.configName }}</span>\n        <span class=\"el-icon-check\" style=\"float: right;line-height: 34px;\" v-if=\"item.status === true\"></span>\n      </el-option>\n    </el-select>\n    <br/>\n    <menu-tree v-loading=\"treeLoading\" ref=\"menuTree\" :data=\"computedMenuTreeData\"></menu-tree>\n    \n    <el-button style=\"margin-top: 15px\" type=\"primary\" @click=\"handleSave\">\u4FDD\u5B58</el-button>\n  </div>\n  "
    };
});
