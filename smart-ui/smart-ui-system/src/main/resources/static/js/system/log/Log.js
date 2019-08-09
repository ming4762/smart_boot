define(["require", "exports", "PageBuilder", "utils/TimeUtils", "plugins/table/SmartTableCRUD", "utils/ApiService", "mixins/LayoutMixins"], function (require, exports, PageBuilder_1, TimeUtils_1, SmartTableCRUD_1, ApiService_1, LayoutMixins_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    class Log extends PageBuilder_1.default {
        build() {
            return exports.LogComponent;
        }
    }
    exports.Log = Log;
    exports.LogComponent = {
        components: {
            'smart-table-crud': SmartTableCRUD_1.default
        },
        mixins: [LayoutMixins_1.default],
        props: {
            userId: String
        },
        data() {
            return {
                apiService: ApiService_1.default,
                columnOptions: [
                    {
                        label: '操作',
                        prop: 'operation',
                        table: {
                            fixed: true,
                            width: 180
                        },
                        form: {}
                    },
                    {
                        label: '用时(ms)',
                        prop: 'useTime',
                        table: {},
                        form: {}
                    },
                    {
                        label: '方法',
                        prop: 'method',
                        table: {
                            width: 260
                        },
                        form: {}
                    },
                    {
                        label: '参数',
                        prop: 'params',
                        table: {
                            minWidth: 240
                        },
                        form: {}
                    },
                    {
                        label: 'ip',
                        prop: 'ip',
                        table: {},
                        form: {}
                    },
                    {
                        label: '时间',
                        prop: 'createTime',
                        table: {
                            sortable: true,
                            width: 165,
                            formatter: (row, column, value) => TimeUtils_1.default.formatTime(value)
                        },
                        form: {}
                    },
                    {
                        label: '请求路径',
                        prop: 'requestPath',
                        table: {
                            width: 220
                        },
                        form: {}
                    },
                    {
                        label: '状态码',
                        prop: 'statusCode',
                        table: {
                            fixed: 'right'
                        },
                        form: {}
                    },
                    {
                        label: '错误信息',
                        prop: 'errorMessage',
                        table: {
                            width: 200
                        },
                        form: {}
                    },
                    {
                        label: '用户',
                        prop: 'userId',
                        table: {
                            width: 100,
                            fixed: 'right',
                            formatter: (row, column, value) => {
                                if (row.user) {
                                    return row.user.name;
                                }
                                else {
                                    return '-';
                                }
                            }
                        },
                        form: {}
                    }
                ]
            };
        },
        methods: {
            formatParams(params) {
                if (params && params.length < 36) {
                    return params;
                }
                if (params.length >= 36) {
                    return params.substring(0, 30) + '...';
                }
            },
            handleShowParamsDetail(params) {
                this.$alert(params ? params : '');
            },
            handleQueryParameterFormatter(parameter) {
                if (this.userId) {
                    return Object.assign({
                        'userId@=': this.userId
                    }, parameter);
                }
                return parameter;
            }
        },
        template: `
  <div style="padding: 15px;">
    <smart-table-crud
      ref="table"
      queryUrl="sys/log/listWithAll"
      deleteUrl="sys/log/batchDelete"
      saveUpdateUrl="sys/log/saveUpdate"
      :keys="['logId']"
      defaultSortColumn="createTime"
      defaultSortOrder="desc"
      :hasTopLeft="false"
      :hasOpreaColumn="false"
      :height="computedTableHeight"
      :columnOptions="columnOptions"
      tableName="日志"
      :queryParameterFormatter="handleQueryParameterFormatter"
      :apiService="apiService">
      <!--参数插槽-->
      <template v-slot:table-params="{row}">
        <el-tooltip effect="dark" content="查看详情" placement="left">
          <a @click="handleShowParamsDetail(row.params)">{{formatParams(row.params)}}</a>
        </el-tooltip>
      </template>
    </smart-table-crud>
  </div>
  `
    };
});
