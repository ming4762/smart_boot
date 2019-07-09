define(["require", "exports", "PageBuilder", "plugins/table/SmartTableCRUD", "utils/ApiService", "mixins/LayoutMixins", "mixins/MessageMixins"], function (require, exports, PageBuilder_1, SmartTableCRUD_1, ApiService_1, LayoutMixins_1, MessageMixins_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    class TimedTask extends PageBuilder_1.default {
        build() {
            return page;
        }
    }
    exports.TimedTask = TimedTask;
    const page = {
        components: {
            'smart-table-crud': SmartTableCRUD_1.default
        },
        mixins: [LayoutMixins_1.default, MessageMixins_1.default],
        data() {
            return {
                apiService: ApiService_1.default,
                columnOptions: [
                    {
                        label: '任务id',
                        prop: 'taskId',
                        table: {
                            visible: false,
                            displayControl: false
                        },
                        form: {}
                    },
                    {
                        label: '任务名称',
                        prop: 'taskName',
                        table: {
                            width: 100,
                            fixed: true
                        },
                        form: {}
                    },
                    {
                        label: '任务类',
                        prop: 'clazz',
                        table: {
                            width: 200
                        },
                        form: {}
                    },
                    {
                        label: '表达式',
                        prop: 'cron',
                        table: {
                            width: 100
                        },
                        form: {}
                    },
                    {
                        label: '启用',
                        prop: 'used',
                        table: {
                            sortable: true
                        },
                        form: {}
                    },
                    {
                        label: '备注',
                        prop: 'remark',
                        table: {
                            minWidth: 220
                        },
                        form: {}
                    },
                    {
                        label: '序号',
                        prop: 'seq',
                        type: 'number',
                        table: {
                            sortable: true
                        },
                        form: {
                            defaultValue: 1
                        }
                    }
                ],
                defaultButtonConfig: {
                    add: {
                        rowShow: false
                    }
                }
            };
        },
        methods: {
            handleOpenClose(task) {
                const used = !task.used;
                const status = used ? '启用' : '关闭';
                this.$confirm(`确定要${status}该任务吗?`, '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    ApiService_1.default.postAjax('quartz/timeTask/openClose', [
                        {
                            taskId: task.taskId,
                            used: used
                        }
                    ]).then(() => {
                        this.successMessage(`${status}任务成功`);
                        this.$refs['table'].load();
                    }).catch(error => {
                        if (error !== 'cancel') {
                            this.errorMessage(`${status}任务失败`, error);
                        }
                    });
                });
            }
        },
        template: `
  <div style="padding: 15px;">
    <smart-table-crud
      ref="table"
      queryUrl="quartz/timeTask/list"
      deleteUrl="quartz/timeTask/batchDelete"
      saveUpdateUrl="quartz/timeTask/saveUpdate"
      :keys="['taskId']"
      labelWidth="80px"
      :height="computedTableHeight"
      :columnOptions="columnOptions"
      tableName="定时任务"
      :defaultButtonConfig="defaultButtonConfig"
      :apiService="apiService">
      <template v-slot:table-used="{row}">
        <el-tag v-if="row.used === true" type="success">启用</el-tag>
        <el-tag v-else type="danger">关闭</el-tag>
      </template>
      <template v-slot:row-operation="{row}">
        <el-tooltip class="item" effect="dark" :content="row.used === true ? '关闭' : '启用'" placement="top">
          <el-button @click="handleOpenClose(row)" type="success" :icon="row.used === true ? 'el-icon-video-pause' : 'el-icon-video-play'" size="mini"/>
        </el-tooltip>
          </template>
    </smart-table-crud>
  </div>
  `
    };
});
