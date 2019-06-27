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
define(["require", "exports", "ComponentBuilder", "plugins/container/FlexAside", "system/organ/OrganTree", "plugins/form/SmartForm", "utils/ApiService", "mixins/MessageMixins", "utils/CommonUtils", "system/organ/OrganDetail"], function (require, exports, ComponentBuilder_1, FlexAside_1, OrganTree_1, SmartForm_1, ApiService_1, MessageMixins_1, CommonUtils_1, OrganDetail_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var Organ = (function (_super) {
        __extends(Organ, _super);
        function Organ() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        Organ.prototype.init = function () {
            this.initVue();
        };
        Organ.prototype.initVue = function () {
            this.vue = new Vue({
                el: '#vue-container',
                components: {
                    'organ-main': this.build()
                }
            });
        };
        Organ.prototype.components = function () {
            return {
                'flex-aside': new FlexAside_1.default().build(),
                'organ-tree': new OrganTree_1.default().build(),
                'smart-form': new SmartForm_1.default().build(),
                'organ-detail': new OrganDetail_1.default().build()
            };
        };
        Organ.prototype.mixins = function () {
            return [
                new MessageMixins_1.default().build()
            ];
        };
        Organ.prototype.data = function () {
            return {
                searchValue: '',
                addOrganDialogVisible: false,
                organAddFormColumn: [
                    {
                        label: '组织ID',
                        prop: 'organId',
                        visible: false
                    },
                    {
                        label: '上级Id',
                        prop: 'parentId'
                    },
                    {
                        label: '组织编码',
                        prop: 'organCode',
                        rules: true
                    },
                    {
                        label: '组织名称',
                        prop: 'organName',
                        rules: true
                    },
                    {
                        label: '组织简称',
                        prop: 'shortName'
                    },
                    {
                        label: '负责人',
                        prop: 'leaderId'
                    },
                    {
                        label: '描述',
                        prop: 'description',
                        type: 'textarea'
                    }
                ],
                organModel: {
                    organCode: ''
                },
                selectedOrgan: {},
                activeTab: 'detail'
            };
        };
        Organ.prototype.methods = function () {
            return {
                handleShowAddOrgan: function () {
                    this.addOrganDialogVisible = true;
                    this.organModel.organCode = CommonUtils_1.default.createUUID();
                    this.organModel.organId = CommonUtils_1.default.createUUID();
                    this.organModel.parentId = this.selectedOrgan.organId ? this.selectedOrgan.organId : '0';
                    if (this.organModel.parentId === '0') {
                        this.organModel.topParentId = this.organModel.organId;
                    }
                    else {
                        this.organModel.topParentId = this.selectedOrgan.topParentId;
                    }
                },
                handleAddOrgan: function () {
                    var _this = this;
                    ApiService_1.default.postAjax('sys/organ/saveUpdate', this.organModel)
                        .then(function (data) {
                        console.log(data);
                        _this.successMessage('添加部门成功');
                        _this.addOrganDialogVisible = false;
                        _this.$refs['organTree'].load();
                    }).catch(function (error) {
                        _this.addOrganDialogVisible = false;
                        _this.errorMessage('添加部门失败', error);
                    });
                },
                handleClickOrganTree: function (data) {
                    this.selectedOrgan = data.object;
                }
            };
        };
        Organ.prototype.template = function () {
            return "\n    <flex-aside\n      :hasAsideHeader=\"false\">\n      <template slot=\"aside\">\n        <div class=\"organ-aside full-height\">\n          <div class=\"organ-aside-search\">\n            <el-input\n              v-model=\"searchValue\"\n              suffix-icon=\"el-icon-search\"\n              placeholder=\"\u641C\u7D22\u90E8\u95E8\"></el-input>\n          </div>\n          <!--\u90E8\u95E8\u6811-->\n          <div style=\"height: calc(100% - 190px); padding-top: 22px\">\n            <organ-tree\n              ref=\"organTree\"\n              @node-click=\"handleClickOrganTree\"/>\n          </div>\n          <!--\u6DFB\u52A0\u6309\u94AE-->\n          <div class=\"organ-aside-add\">\n            <el-button \n              type=\"primary\"\n              @click=\"handleShowAddOrgan\"\n              style=\"width: 100%\"\n              icon=\"el-icon-plus\">\u6DFB\u52A0\u90E8\u95E8</el-button>\n          </div>\n        </div>\n      </template>\n      <!--\u4E3B\u9898\u90E8\u5206-->\n      <template>\n        <div class=\"full-height\">\n          <el-tabs v-model=\"activeTab\" style=\"padding: 15px\">\n            <el-tab-pane lazy label=\"\u7EC4\u7EC7\u8BE6\u60C5\" name=\"detail\">\n              <organ-detail :organId=\"selectedOrgan.organId\"/>\n            </el-tab-pane>\n            <el-tab-pane lazy label=\"\u4EBA\u5458\u4FE1\u606F\" name=\"userList\">\u914D\u7F6E\u7BA1\u7406</el-tab-pane>\n          </el-tabs>\n        </div>\n      </template>\n      <!--\u6DFB\u52A0\u90E8\u95E8\u5F39\u7A97-->\n      <el-dialog\n        :visible.sync=\"addOrganDialogVisible\"\n        title=\"\u6DFB\u52A0\u90E8\u95E8\">\n        <smart-form\n          labelWidth=\"80px\"\n          :model=\"organModel\"\n          :columnOptions=\"organAddFormColumn\">\n          <template v-slot:parentId=\"{model}\">\n            <el-form-item label=\"\u4E0A\u7EA7\u7EC4\u7EC7\">\n              <el-input v-model=\"model['parentId']\" style=\"display: none\"/> \n              <el-input disabled :value=\"(!selectedOrgan.organId || selectedOrgan.organId === '0') ? '\u6839\u7EC4\u7EC7' : selectedOrgan.organName\"/>\n            </el-form-item>\n          </template>\n        </smart-form>\n        <div style=\"padding-left: 80px\">\n          <el-button type=\"primary\" @click=\"handleAddOrgan\">\u6DFB\u52A0</el-button>\n          <el-button type=\"warning\" @click=\"addOrganDialogVisible = false\">\u53D6\u6D88</el-button>\n        </div>\n      </el-dialog>\n    </flex-aside>\n    ";
        };
        return Organ;
    }(ComponentBuilder_1.default));
    exports.Organ = Organ;
});
