define(["require", "exports", "PageBuilder", "plugins/table/SmartTableCRUD", "utils/ApiService", "mixins/LayoutMixins", "utils/TimeUtils"], function (require, exports, PageBuilder_1, SmartTableCRUD_1, ApiService_1, LayoutMixins_1, TimeUtils_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    class Dict extends PageBuilder_1.default {
        build() {
            return page;
        }
    }
    exports.Dict = Dict;
    const DictItem = {
        components: {
            SmartTableCRUD: SmartTableCRUD_1.default
        },
        mixins: [
            LayoutMixins_1.default
        ],
        props: {
            dictCode: {
                type: String,
                required: true
            },
            show: Boolean
        },
        data() {
            return {
                apiService: ApiService_1.default,
                defaultButtonConfig: {
                    add: {
                        rowShow: false
                    }
                },
                columnOptions: [
                    {
                        prop: 'id',
                        label: 'id',
                        table: {
                            visible: false,
                            displayControl: false
                        },
                        form: {
                            visible: false
                        }
                    },
                    {
                        prop: 'dictCode',
                        label: '字典编码',
                        table: {
                            fixed: true,
                            visible: false
                        },
                        form: {}
                    },
                    {
                        prop: 'itemCode',
                        label: '字典项编码',
                        table: {
                            width: 150,
                            fixed: true
                        },
                        form: {
                            rules: true
                        }
                    },
                    {
                        prop: 'itemValue',
                        label: '字典项值',
                        table: {
                            fixed: true,
                            width: 180
                        },
                        form: {
                            rules: true
                        }
                    },
                    {
                        prop: 'remark',
                        label: '描述',
                        table: {
                            minWidth: 200
                        },
                        form: {}
                    },
                    {
                        prop: 'createUserId',
                        label: '创建人员',
                        table: {
                            width: 120
                        },
                        form: {
                            visible: false
                        }
                    },
                    {
                        prop: 'createTime',
                        label: '创建时间',
                        table: {
                            width: 170,
                            formatter: (row, column, cellValue) => {
                                return TimeUtils_1.default.formatTime(cellValue);
                            }
                        },
                        form: {
                            visible: false
                        }
                    },
                    {
                        prop: 'updateUserId',
                        label: '更新人员',
                        table: {
                            width: 120
                        },
                        form: {
                            visible: false
                        }
                    },
                    {
                        prop: 'updateTime',
                        label: '创建人员',
                        table: {
                            width: 160
                        },
                        form: {
                            visible: false
                        }
                    },
                    {
                        prop: 'inUse',
                        type: 'boolean',
                        label: '是否启用',
                        table: {
                            slot: true,
                            width: 120,
                            sortable: true
                        },
                        form: {
                            defaultValue: true
                        }
                    }, {
                        prop: 'seq',
                        label: '序号',
                        type: 'number',
                        table: {
                            sortable: true
                        },
                        form: {
                            defaultValue: 1
                        }
                    }
                ]
            };
        },
        watch: {
            dictCode() {
                this.$refs['itemTable'].load();
            }
        },
        methods: {
            handleQueryParameterFormatter(parameter) {
                parameter['dictCode@='] = this.dictCode;
                return parameter;
            },
            handleAddEditDialogShow(ident, model, callBack, row) {
                if (ident === 'edit') {
                    ApiService_1.default.postAjax('sys/dictItem/get', { id: row.id })
                        .then(result => {
                        callBack(result);
                    }).catch(error => {
                        this.errorMessage('获取字典项信息失败', error);
                    });
                }
                else {
                    model.dictCode = this.dictCode;
                    callBack(model);
                }
            },
            handleGoBack() {
                this.$emit('update:show', false);
            }
        },
        template: `
  <SmartTableCRUD
    ref="itemTable"
    :height="computedTableHeight"
    queryUrl="sys/dictItem/list"
    deleteUrl="sys/dictItem/batchDelete"
    saveUpdateUrl="sys/dictItem/saveUpdate"
    defaultSortColumn="seq,inUse"
    labelWidth="100px"
    :defaultButtonConfig="defaultButtonConfig"
    tableName="字典项"
    :apiService="apiService"
    :opreaColumnWidth="150"
    :keys="['id']"
    @add-edit-dialog-show="handleAddEditDialogShow"
    :queryParameterFormatter="handleQueryParameterFormatter"
    :columnOptions="columnOptions">
    <!-- 字典编码form -->
    <template slot="form-dictCode" slot-scope="{model}">
      <el-form-item prop="dictCode" label="字典项">
        <el-input disabled v-model="model.dictCode"></el-input>
      </el-form-item>
    </template>
    <!-- 是否启用插槽 -->
    <template slot="table-inUse" slot-scope="{row}">
      <el-switch disabled v-model="row.inUse"></el-switch>
    </template>
    <!-- 上方右侧侧按钮 -->
    <template slot="top-right">
      <el-tooltip class="item" effect="dark" content="返回" placement="top">
        <el-button
          circle
          @click="handleGoBack()"
          size="small"
          type="primary"
          icon="el-icon-back"></el-button>
      </el-tooltip>
    </template>
  </SmartTableCRUD>
  `
    };
    const page = {
        components: {
            SmartTableCRUD: SmartTableCRUD_1.default,
            DictItem
        },
        mixins: [
            LayoutMixins_1.default
        ],
        data() {
            return {
                apiService: ApiService_1.default,
                defaultButtonConfig: {
                    add: {
                        rowShow: false
                    }
                },
                currentDict: {
                    dictCode: 'xx'
                },
                showItem: false,
                columnOptions: [
                    {
                        prop: 'dictCode',
                        label: '字典编码',
                        table: {
                            width: 150,
                            fixed: true,
                            sortable: true
                        },
                        form: {}
                    },
                    {
                        prop: 'dictName',
                        label: '字典名称',
                        table: {
                            width: 120,
                            fixed: true,
                            sortable: true
                        },
                        form: {}
                    },
                    {
                        prop: 'createUserId',
                        label: '创建人员',
                        table: {
                            width: 120
                        },
                        form: {
                            visible: false
                        }
                    },
                    {
                        prop: 'createTime',
                        label: '创建时间',
                        table: {
                            sortable: true,
                            width: 170,
                            formatter: (row, column, cellValue) => {
                                return TimeUtils_1.default.formatTime(cellValue);
                            }
                        },
                        form: {
                            visible: false
                        }
                    },
                    {
                        prop: 'remark',
                        label: '描述',
                        table: {
                            minWidth: 200
                        },
                        form: {}
                    },
                    {
                        prop: 'updateUserId',
                        label: '更新人员',
                        table: {
                            width: 120,
                            visible: false
                        },
                        form: {
                            visible: false
                        }
                    },
                    {
                        prop: 'inUse',
                        type: 'boolean',
                        label: '是否启用',
                        table: {
                            slot: true,
                            width: 120,
                            sortable: true
                        },
                        form: {
                            defaultValue: true
                        }
                    },
                    {
                        prop: 'seq',
                        label: '序号',
                        type: 'number',
                        table: {
                            sortable: true
                        },
                        form: {
                            defaultValue: 1
                        }
                    }
                ]
            };
        },
        methods: {
            handleShowDictItem(dict) {
                this.currentDict = dict;
                this.showItem = true;
            }
        },
        template: `
  <div style="padding: 15px;">
    <DictItem
      :dictCode="currentDict.dictCode"
      :show.sync="showItem"
      v-show="showItem"/>
    <SmartTableCRUD
      v-show="!showItem"
      :height="computedTableHeight"
      queryUrl="sys/dict/list"
      deleteUrl="sys/dict/batchDelete"
      saveUpdateUrl="sys/dict/saveUpdate"
      defaultSortColumn="seq,inUse"
      :defaultButtonConfig="defaultButtonConfig"
      tableName="字典管理"
      :keys="['dictCode']"
      :apiService="apiService"
      :columnOptions="columnOptions">
      <!-- 行按钮 -->
      <template slot="row-operation" slot-scope="{row}">
        <el-tooltip class="item" effect="dark" content="字典项" placement="top">
          <el-button
            type="primary"
            @click="handleShowDictItem(row)"
            icon="el-icon-tickets"
            size="mini"></el-button>
        </el-tooltip>
      </template>
      <!-- 是否启用插槽 -->
      <template slot="table-inUse" slot-scope="{row}">
        <el-switch disabled v-model="row.inUse"></el-switch>
      </template>
    </SmartTableCRUD>
  </div>
  `
    };
});
