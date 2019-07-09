define(["require", "exports", "plugins/form/SmartForm", "system/role/RoleUser", "system/role/RoleMenuAuthorize", "utils/ApiService", "mixins/MessageMixins"], function (require, exports, SmartForm_1, RoleUser_1, RoleMenuAuthorize_1, ApiService_1, MessageMixins_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    const roleTypeMap = {
        '1': '业务角色',
        '2': '管理角色',
        '3': '系统内置角色'
    };
    exports.default = {
        props: {
            organId: String,
            organName: String,
            roleId: String
        },
        components: {
            'smart-form': SmartForm_1.default,
            'role-user': RoleUser_1.default,
            'role-menu-authorize': RoleMenuAuthorize_1.default
        },
        mixins: [
            MessageMixins_1.default
        ],
        computed: {
            computedIsAdd() {
                return this.roleId === undefined || this.roleId === null;
            }
        },
        data() {
            const roleTypeDic = [];
            Object.keys(roleTypeMap).forEach(key => {
                roleTypeDic.push({
                    label: key,
                    value: roleTypeMap[key]
                });
            });
            return {
                activeName: 'first',
                roleModel: {},
                roleFormColumns: [
                    {
                        prop: 'roleId',
                        label: '角色ID',
                        disabled: true,
                        visible: false,
                        span: 12
                    },
                    {
                        prop: 'organId',
                        label: '所属组织',
                        span: 12
                    },
                    {
                        label: '角色名',
                        prop: 'roleName',
                        rules: true,
                        span: 12
                    },
                    {
                        label: '类型',
                        prop: 'roleType',
                        type: 'radio',
                        defaultValue: '1',
                        dicData: roleTypeDic
                    },
                    {
                        label: '备注',
                        prop: 'remark',
                    },
                    {
                        label: '启用',
                        prop: 'enable',
                        type: 'boolean',
                        defaultValue: true,
                        span: 12
                    },
                    {
                        label: '序号',
                        prop: 'seq',
                        type: 'number',
                        defaultValue: 1,
                        span: 12
                    }
                ],
                detailLoading: false
            };
        },
        watch: {
            roleId(_new, old) {
                if (_new !== old && _new) {
                    this.loadRoleDetal();
                }
                else if (this.computedIsAdd) {
                    this.roleModel = {
                        organName: this.organName
                    };
                }
            }
        },
        methods: {
            loadRoleDetal() {
                this.detailLoading = true;
                ApiService_1.default.postAjax('sys/role/queryDetail', {
                    roleId: this.roleId
                }).then(data => {
                    this.detailLoading = false;
                    this.roleModel = data;
                }).catch(error => {
                    this.detailLoading = false;
                    this.errorMessage('加载角色详情失败', error);
                });
            },
            handleSave() {
                const model = {};
                if (this.computedIsAdd) {
                    model['organId'] = this.organId;
                }
                ApiService_1.default.postAjax('sys/role/saveUpdate', Object.assign(model, this.roleModel))
                    .then(() => {
                    if (this.computedIsAdd && this.$listeners['after-save']) {
                        this.$emit('after-save');
                    }
                }).catch(error => {
                    this.errorMessage('保存角色时发生错误', error);
                });
            },
            handleDelete() {
                this.$confirm('您确定要删除该角色吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    return ApiService_1.default.postAjax('sys/role/delete', {
                        roleId: this.roleId
                    });
                }).then(() => {
                    this.successMessage('删除成功');
                    this.$emit('after-save');
                    this.roleModel = {};
                }).catch(error => {
                    if (error !== 'cancel') {
                        this.errorMessage('删除角色发生错误', error);
                    }
                });
            }
        },
        template: `
  <el-tabs v-model="activeName">
    <el-tab-pane label="角色基本信息" name="first">
      <div v-loading="detailLoading" class="role-operation-container" style="width: 600px;">
        <smart-form
          :model="roleModel"
          labelWidth="80px"
          :columnOptions="roleFormColumns">
          <template v-slot:organId="{model}">
            <el-form-item label="所属组织">
              <el-input v-if="computedIsAdd" disabled :value="organName"></el-input>
              <el-input v-else disabled :value="model.organ ? model.organ.organName : '-'"></el-input>
              <el-input style="display: none" v-model="model.organId"></el-input>
            </el-form-item>
          </template>
        </smart-form>
        <el-button @click="handleSave" style="margin: 10px 0 0 80px" :type="computedIsAdd ? 'primary' : 'warning'">
          {{computedIsAdd ? '新增' : '修改'}}
        </el-button> 
        <el-button v-if="!computedIsAdd" @click="handleDelete" type="danger">删除</el-button>
      </div>
    </el-tab-pane>
    <el-tab-pane lazy label="菜单授权" name="second">
      <div v-if="!computedIsAdd">
        <role-menu-authorize
          :roleId="roleId"/>
      </div>
    </el-tab-pane>
    <el-tab-pane lazy label="非菜单授权" name="third">
      <div v-if="!computedIsAdd">
        
      </div>
    </el-tab-pane>
    <el-tab-pane lazy label="人员授权" name="forth">
      <div v-if="!computedIsAdd">
        <role-user
          :roleId="roleId"/>
      </div>
    </el-tab-pane>
  </el-tabs>
  `
    };
});
