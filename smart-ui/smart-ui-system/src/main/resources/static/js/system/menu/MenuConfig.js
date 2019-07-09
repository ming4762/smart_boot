define(["require", "exports", "PageBuilder", "utils/ApiService", "mixins/MessageMixins", "utils/CollectionUtils", "plugins/form/SmartForm"], function (require, exports, PageBuilder_1, ApiService_1, MessageMixins_1, CollectionUtils_1, SmartForm_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    class MenuConfig extends PageBuilder_1.default {
        build() {
            return page;
        }
    }
    exports.MenuConfig = MenuConfig;
    const menuConfigTemplate = `
<el-card
  @click.native="handleShowConfigDetail(item)"
  class="menu-config-card menu-config-show-card"
  :style="getCardBodyStyle(item)"
  @mouseover.native="handleMouseOver(item)"
  @mouseleave.native="handleMouseLeave(item)"
  shadow="hover">
  <div class="menu-config-edit-container">
    <div v-show="getEditShow(item)" class="edit-button">
      <span @click="handleShowAddMenuConfig(item)">修改</span>
      <span v-if="item.configId !== '0'" @click="handleDeleteMenuConfig(item)">删除</span>
    </div>
  </div>
  <div style="padding-top: 13px">
    <span>{{item.configName}}</span>
  </div>
</el-card>
`;
    const addMenuConfigTemplate = `
<el-card @click.native="handleShowAddMenuConfig" class="menu-config-card menu-config-add-card" :body-style="addCardBodyStyle" shadow="hover">
  <i class="el-icon-plus"></i>
  <span>创建菜单类别</span>
</el-card>
`;
    const addDialogTemplate = `
<el-dialog
  width="400px"
  title="添加菜单分类"
  :visible.sync="addDialogVisible">
  <smart-form
    :model="addEditFormModel"
    labelWidth="100px"
    :columnOptions="addEditFormColumns"/>
  <div style="padding-left: 100px">
    <el-button @click="handleSaveUpdate" type="primary">保存</el-button>
    <el-button @click="addDialogVisible = false" type="warning">取消</el-button>
  </div>  
</el-dialog>
`;
    const page = {
        components: {
            'smart-form': SmartForm_1.default
        },
        mixins: [MessageMixins_1.default],
        data() {
            return {
                addCardBodyStyle: {
                    'font-size': '18px',
                    'color': '#737373'
                },
                data: [],
                menuConfigSelect: {},
                selectConfigId: '',
                addDialogVisible: false,
                addEditFormColumns: [
                    {
                        label: '菜单分类ID',
                        prop: 'configId',
                        visible: false
                    },
                    {
                        label: '菜单分类名称',
                        prop: 'configName'
                    },
                    {
                        label: '序号',
                        prop: 'seq',
                        type: 'number',
                        defaultValue: 1
                    },
                    {
                        label: '是否启用',
                        prop: 'status',
                        type: 'boolean'
                    }
                ],
                addEditFormModel: {}
            };
        },
        computed: {
            computedData() {
                return CollectionUtils_1.default.splitArray(this.data, 3);
            }
        },
        mounted() {
            this.load();
        },
        methods: {
            handleShowAddMenuConfig(menuConfig) {
                this.addDialogVisible = true;
                if (menuConfig) {
                    this.addEditFormModel = {
                        configId: menuConfig.configId,
                        configName: menuConfig.configName,
                        seq: menuConfig.seq,
                        status: menuConfig.status
                    };
                }
                else {
                    this.addEditFormModel = {};
                }
            },
            handleMouseOver(menuConfig) {
                Object.keys(this.menuConfigSelect).forEach(key => {
                    this.menuConfigSelect[key] = false;
                });
                this.$set(this.menuConfigSelect, menuConfig.configId, true);
            },
            handleMouseLeave(menuConfig) {
                Object.keys(this.menuConfigSelect).forEach(key => {
                    this.menuConfigSelect[key] = false;
                });
            },
            getCardBodyStyle(menuConfig) {
                const configId = menuConfig.configId;
                const activeStyle = {
                    color: 'white',
                    'background-color': '#1fbceb'
                };
                const noActiveStyle = {
                    color: '#737373'
                };
                if (configId === this.selectConfigId) {
                    return activeStyle;
                }
                if (this.menuConfigSelect[configId] === true) {
                    return activeStyle;
                }
                else {
                    return noActiveStyle;
                }
            },
            getEditShow(menuConfig) {
                return this.menuConfigSelect[menuConfig.configId] === true;
            },
            handleSaveUpdate() {
                ApiService_1.default.postAjax('sys/menuConfig/saveUpdate', this.addEditFormModel)
                    .then(data => {
                    this.load();
                    this.addDialogVisible = false;
                }).catch(error => {
                    this.errorMessage('保存菜单分类发生错误', error);
                });
            },
            load() {
                const $this = this;
                ApiService_1.default.postAjax('sys/menuConfig/list', {
                    defaultSortColumn: 'seq'
                }).then(data => {
                    this.data = data;
                    data.forEach(item => {
                        if (item.status === true) {
                            $this.selectConfigId = item.configId;
                        }
                    });
                }).catch(error => {
                    this.errorMessage('加载菜单分类失败', error);
                });
            },
            handleDeleteMenuConfig(menuConfig) {
                this.$confirm('确定要删除该分类吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    return ApiService_1.default.postAjax('sys/menuConfig/delete', {
                        configId: menuConfig.configId
                    });
                }).then(() => {
                    this.load();
                    this.successMessage('删除成功');
                }).catch(error => {
                    if (error !== 'cancel') {
                        this.errorMessage('删除菜单分类发生错误', error);
                    }
                });
            },
            handleShowConfigDetail(menuConfig) {
                console.log(menuConfig);
            }
        },
        template: `
    <div class="menu-config-container">
      <!--遍历菜单-->
      <el-row
        class="menu-config-row"
        :gutter="20"
        :key="index + 'row'"
        v-for="(items, index) in computedData">
        <el-col
          :span="8"
          v-for="(item, index) in items"
          :key="index + 'col'"
          class="menu-confg-col">
          ${menuConfigTemplate}
        </el-col>
      </el-row>
      <!--添加菜单-->
      <el-row class="menu-config-row" :gutter="20">
        <el-col class="menu-confg-col" :span="8">
          ${addMenuConfigTemplate}
        </el-col>
      </el-row>
      <!--添加修改弹窗-->
      ${addDialogTemplate}
    </div>
    `
    };
});
