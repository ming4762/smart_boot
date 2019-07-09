define(["require", "exports", "ComponentBuilder", "plugins/container/FlexAside", "system/organ/OrganTree", "plugins/form/SmartForm", "utils/ApiService", "mixins/MessageMixins", "utils/CommonUtils", "system/organ/OrganDetail"], function (require, exports, ComponentBuilder_1, FlexAside_1, OrganTree_1, SmartForm_1, ApiService_1, MessageMixins_1, CommonUtils_1, OrganDetail_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    class Organ extends ComponentBuilder_1.default {
        init() {
            this.initVue();
        }
        initVue() {
            this.vue = new Vue({
                el: '#vue-container',
                components: {
                    'organ-main': this.build()
                }
            });
        }
        components() {
            return {
                'flex-aside': FlexAside_1.default,
                'organ-tree': OrganTree_1.default,
                'smart-form': SmartForm_1.default,
                'organ-detail': OrganDetail_1.default
            };
        }
        mixins() {
            return [
                MessageMixins_1.default
            ];
        }
        data() {
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
        }
        methods() {
            return {
                handleShowAddOrgan() {
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
                handleAddOrgan() {
                    ApiService_1.default.postAjax('sys/organ/saveUpdate', this.organModel)
                        .then(data => {
                        console.log(data);
                        this.successMessage('添加部门成功');
                        this.addOrganDialogVisible = false;
                        this.$refs['organTree'].load();
                    }).catch(error => {
                        this.addOrganDialogVisible = false;
                        this.errorMessage('添加部门失败', error);
                    });
                },
                handleClickOrganTree(data) {
                    this.selectedOrgan = data.object;
                }
            };
        }
        template() {
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
            <organ-tree
              ref="organTree"
              @node-click="handleClickOrganTree"/>
          </div>
          <!--添加按钮-->
          <div class="organ-aside-add">
            <el-button 
              type="primary"
              @click="handleShowAddOrgan"
              style="width: 100%"
              icon="el-icon-plus">添加部门</el-button>
          </div>
        </div>
      </template>
      <!--主题部分-->
      <template>
        <div class="full-height">
          <el-tabs v-model="activeTab" style="padding: 15px">
            <el-tab-pane lazy label="组织详情" name="detail">
              <organ-detail :organId="selectedOrgan.organId"/>
            </el-tab-pane>
            <el-tab-pane lazy label="人员信息" name="userList">配置管理</el-tab-pane>
          </el-tabs>
        </div>
      </template>
      <!--添加部门弹窗-->
      <el-dialog
        :visible.sync="addOrganDialogVisible"
        title="添加部门">
        <smart-form
          labelWidth="80px"
          :model="organModel"
          :columnOptions="organAddFormColumn">
          <template v-slot:parentId="{model}">
            <el-form-item label="上级组织">
              <el-input v-model="model['parentId']" style="display: none"/> 
              <el-input disabled :value="(!selectedOrgan.organId || selectedOrgan.organId === '0') ? '根组织' : selectedOrgan.organName"/>
            </el-form-item>
          </template>
        </smart-form>
        <div style="padding-left: 80px">
          <el-button type="primary" @click="handleAddOrgan">添加</el-button>
          <el-button type="warning" @click="addOrganDialogVisible = false">取消</el-button>
        </div>
      </el-dialog>
    </flex-aside>
    `;
        }
    }
    exports.Organ = Organ;
});
