define(["require", "exports", "PageBuilder", "plugins/table/SmartTableCRUD", "utils/ApiService", "mixins/LayoutMixins", "utils/TimeUtils", "utils/TreeUtils"], function (require, exports, PageBuilder_1, SmartTableCRUD_1, ApiService_1, LayoutMixins_1, TimeUtils_1, TreeUtils_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    class Module extends PageBuilder_1.default {
        build() {
            return page;
        }
    }
    exports.Module = Module;
    const page = {
        components: {
            SmartTableCRUD: SmartTableCRUD_1.default
        },
        mixins: [
            LayoutMixins_1.default
        ],
        data() {
            return {
                apiService: ApiService_1.default,
                defaultButtonConfig: {},
                columnOptions: [
                    {
                        label: '模块ID',
                        prop: 'moduleId',
                        form: {
                            visible: false
                        },
                        table: {
                            visible: false,
                            displayControl: false
                        }
                    },
                    {
                        label: '上级ID',
                        prop: 'parentId',
                        form: {},
                        table: {
                            visible: false,
                            displayControl: false
                        }
                    },
                    {
                        label: '模块名',
                        prop: 'moduleName',
                        form: {
                            rules: true
                        },
                        table: {
                            align: 'left',
                            width: 120,
                            fixed: true
                        }
                    },
                    {
                        label: '模块图标',
                        prop: 'moduleIcon',
                        form: {
                            visible: false
                        },
                        table: {
                            visible: false,
                            displayControl: false
                        }
                    },
                    {
                        label: '封面图片',
                        prop: 'coverPicId',
                        form: {},
                        table: {
                            visible: false
                        }
                    },
                    {
                        label: '备注',
                        prop: 'remark',
                        form: {},
                        table: {
                            minWidth: 200
                        }
                    },
                    {
                        label: '顶级ID',
                        prop: 'topParentId',
                        form: {
                            visible: false
                        },
                        table: {
                            visible: false,
                            displayControl: false
                        }
                    },
                    {
                        label: '创建时间',
                        prop: 'createTime',
                        type: 'datetime',
                        table: {
                            width: 170,
                            formatter: (row, column, cellValue, index) => {
                                return TimeUtils_1.default.formatTime(cellValue);
                            },
                            sortable: true
                        },
                        form: {
                            visible: false
                        }
                    },
                    {
                        label: '创建人员',
                        prop: 'createUserId',
                        table: {
                            visible: false
                        },
                        form: {
                            visible: false
                        }
                    },
                    {
                        label: '更新时间',
                        prop: 'updateTime',
                        table: {
                            visible: false
                        },
                        form: {
                            visible: false
                        }
                    },
                    {
                        label: '更新人员',
                        prop: 'updateUserId',
                        table: {
                            visible: false
                        },
                        form: {
                            visible: false
                        }
                    },
                    {
                        label: '序号',
                        prop: 'seq',
                        type: 'number',
                        table: {
                            sortable: true
                        },
                        form: {
                            defaultValue: 0
                        }
                    }
                ]
            };
        },
        methods: {
            handleAddEditDialogShow(ident, model, callBack, row) {
                if (ident === 'add') {
                    if (!row) {
                        this.parentName = '根目录';
                        model.parentId = '0';
                    }
                    else {
                        this.parentName = row.moduleName;
                        model.parentId = row.moduleId;
                        model.topParentId = row.topParentId;
                    }
                    callBack(model);
                }
                else {
                    callBack();
                }
            },
            handleDataFormatter(data) {
                return TreeUtils_1.default.convertList2Tree(data, ['moduleId', 'parentId'], '0');
            }
        },
        template: `
  <div style="padding: 15px;">
    <SmartTableCRUD
      :defaultButtonConfig="defaultButtonConfig"
      queryUrl="portal/module/list"
      deleteUrl="portal/module/batchDelete"
      saveUpdateUrl="portal/module/saveUpdate"
      :keys="['module_id']"
      defaultSortColumn="seq"
      tableName="模块" 
      :apiService="apiService"
      labelWidth="80px"
      :height="computedTableHeight"
      @add-edit-dialog-show="handleAddEditDialogShow"
      :tableDataFormatter="handleDataFormatter"
      :columnOptions="columnOptions">
      <!-- form上级插槽 -->
      <template slot="form-parentId" slot-scope="{}">
        <el-form-item label="上级模块">
          <el-input disabled v-model="parentName"></el-input>
        </el-form-item>
      </template>
    </SmartTableCRUD>
  </div>
  `
    };
});
