var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
        return extendStatics(d, b);
    }
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
define(["require", "exports", "PageBuilder", "utils/ApiService", "mixins/MessageMixins", "utils/CollectionUtils", "plugins/form/SmartForm"], function (require, exports, PageBuilder_1, ApiService_1, MessageMixins_1, CollectionUtils_1, SmartForm_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var MenuConfig = (function (_super) {
        __extends(MenuConfig, _super);
        function MenuConfig() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        MenuConfig.prototype.components = function () {
            return {
                'smart-form': SmartForm_1.default
            };
        };
        MenuConfig.prototype.mixins = function () {
            return [
                MessageMixins_1.default
            ];
        };
        MenuConfig.prototype.data = function () {
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
        };
        MenuConfig.prototype.computed = function () {
            return {
                computedData: function () {
                    return CollectionUtils_1.default.splitArray(this.data, 3);
                }
            };
        };
        MenuConfig.prototype.mounted = function () {
            var $this = this;
            $this.load();
        };
        MenuConfig.prototype.methods = function () {
            return {
                handleShowAddMenuConfig: function (menuConfig) {
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
                handleMouseOver: function (menuConfig) {
                    var _this = this;
                    Object.keys(this.menuConfigSelect).forEach(function (key) {
                        _this.menuConfigSelect[key] = false;
                    });
                    this.$set(this.menuConfigSelect, menuConfig.configId, true);
                },
                handleMouseLeave: function (menuConfig) {
                    var _this = this;
                    Object.keys(this.menuConfigSelect).forEach(function (key) {
                        _this.menuConfigSelect[key] = false;
                    });
                },
                getCardBodyStyle: function (menuConfig) {
                    var configId = menuConfig.configId;
                    var activeStyle = {
                        color: 'white',
                        'background-color': '#1fbceb'
                    };
                    var noActiveStyle = {
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
                getEditShow: function (menuConfig) {
                    return this.menuConfigSelect[menuConfig.configId] === true;
                },
                handleSaveUpdate: function () {
                    var _this = this;
                    ApiService_1.default.postAjax('sys/menuConfig/saveUpdate', this.addEditFormModel)
                        .then(function (data) {
                        _this.load();
                        _this.addDialogVisible = false;
                    }).catch(function (error) {
                        _this.errorMessage('保存菜单分类发生错误', error);
                    });
                },
                load: function () {
                    var _this = this;
                    var $this = this;
                    ApiService_1.default.postAjax('sys/menuConfig/list', {
                        defaultSortColumn: 'seq'
                    }).then(function (data) {
                        _this.data = data;
                        data.forEach(function (item) {
                            if (item.status === true) {
                                $this.selectConfigId = item.configId;
                            }
                        });
                    }).catch(function (error) {
                        _this.errorMessage('加载菜单分类失败', error);
                    });
                },
                handleDeleteMenuConfig: function (menuConfig) {
                    var _this = this;
                    this.$confirm('确定要删除该分类吗?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(function () {
                        return ApiService_1.default.postAjax('sys/menuConfig/delete', {
                            configId: menuConfig.configId
                        });
                    }).then(function () {
                        _this.load();
                        _this.successMessage('删除成功');
                    }).catch(function (error) {
                        if (error !== 'cancel') {
                            _this.errorMessage('删除菜单分类发生错误', error);
                        }
                    });
                },
                handleShowConfigDetail: function (menuConfig) {
                    console.log(menuConfig);
                }
            };
        };
        MenuConfig.prototype.template = function () {
            return "\n    <div class=\"menu-config-container\">\n      <!--\u904D\u5386\u83DC\u5355-->\n      <el-row\n        class=\"menu-config-row\"\n        :gutter=\"20\"\n        :key=\"index + 'row'\"\n        v-for=\"(items, index) in computedData\">\n        <el-col\n          :span=\"8\"\n          v-for=\"(item, index) in items\"\n          :key=\"index + 'col'\"\n          class=\"menu-confg-col\">\n          " + this.createMenuConfigTemplate() + "\n        </el-col>\n      </el-row>\n      <!--\u6DFB\u52A0\u83DC\u5355-->\n      <el-row class=\"menu-config-row\" :gutter=\"20\">\n        <el-col class=\"menu-confg-col\" :span=\"8\">\n          " + this.createAddMenuConfigTemplate() + "\n        </el-col>\n      </el-row>\n      <!--\u6DFB\u52A0\u4FEE\u6539\u5F39\u7A97-->\n      " + this.createAddDialogTemplate() + "\n    </div>\n    ";
        };
        MenuConfig.prototype.createMenuConfigTemplate = function () {
            return "\n    <el-card\n      @click.native=\"handleShowConfigDetail(item)\"\n      class=\"menu-config-card menu-config-show-card\"\n      :style=\"getCardBodyStyle(item)\"\n      @mouseover.native=\"handleMouseOver(item)\"\n      @mouseleave.native=\"handleMouseLeave(item)\"\n      shadow=\"hover\">\n      <div class=\"menu-config-edit-container\">\n        <div v-show=\"getEditShow(item)\" class=\"edit-button\">\n          <span @click=\"handleShowAddMenuConfig(item)\">\u4FEE\u6539</span>\n          <span v-if=\"item.configId !== '0'\" @click=\"handleDeleteMenuConfig(item)\">\u5220\u9664</span>\n        </div>\n      </div>\n      <div style=\"padding-top: 13px\">\n        <span>{{item.configName}}</span>\n      </div>\n    </el-card>\n    ";
        };
        MenuConfig.prototype.createAddMenuConfigTemplate = function () {
            return "\n    <el-card @click.native=\"handleShowAddMenuConfig\" class=\"menu-config-card menu-config-add-card\" :body-style=\"addCardBodyStyle\" shadow=\"hover\">\n      <i class=\"el-icon-plus\"></i>\n      <span>\u521B\u5EFA\u83DC\u5355\u7C7B\u522B</span>\n    </el-card>\n    ";
        };
        MenuConfig.prototype.createAddDialogTemplate = function () {
            return "\n    <el-dialog\n      width=\"400px\"\n      title=\"\u6DFB\u52A0\u83DC\u5355\u5206\u7C7B\"\n      :visible.sync=\"addDialogVisible\">\n      <smart-form\n        :model=\"addEditFormModel\"\n        labelWidth=\"100px\"\n        :columnOptions=\"addEditFormColumns\"/>\n      <div style=\"padding-left: 100px\">\n        <el-button @click=\"handleSaveUpdate\" type=\"primary\">\u4FDD\u5B58</el-button>\n        <el-button @click=\"addDialogVisible = false\" type=\"warning\">\u53D6\u6D88</el-button>\n      </div>  \n    </el-dialog>\n    ";
        };
        return MenuConfig;
    }(PageBuilder_1.default));
    exports.MenuConfig = MenuConfig;
});
