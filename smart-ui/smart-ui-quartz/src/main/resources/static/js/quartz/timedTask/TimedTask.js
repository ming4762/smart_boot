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
define(["require", "exports", "PageBuilder", "plugins/table/SmartTableCRUD", "utils/ApiService", "mixins/LayoutMixins", "mixins/MessageMixins"], function (require, exports, PageBuilder_1, SmartTableCRUD_1, ApiService_1, LayoutMixins_1, MessageMixins_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var TimedTask = (function (_super) {
        __extends(TimedTask, _super);
        function TimedTask() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        TimedTask.prototype.build = function () {
            return page;
        };
        return TimedTask;
    }(PageBuilder_1.default));
    exports.TimedTask = TimedTask;
    var page = {
        components: {
            'smart-table-crud': SmartTableCRUD_1.default
        },
        mixins: [LayoutMixins_1.default, MessageMixins_1.default],
        data: function () {
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
            handleOpenClose: function (task) {
                var _this = this;
                var used = !task.used;
                var status = used ? '启用' : '关闭';
                this.$confirm("\u786E\u5B9A\u8981" + status + "\u8BE5\u4EFB\u52A1\u5417?", '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    ApiService_1.default.postAjax('quartz/timeTask/openClose', [
                        {
                            taskId: task.taskId,
                            used: used
                        }
                    ]).then(function () {
                        _this.successMessage(status + "\u4EFB\u52A1\u6210\u529F");
                        _this.$refs['table'].load();
                    }).catch(function (error) {
                        if (error !== 'cancel') {
                            _this.errorMessage(status + "\u4EFB\u52A1\u5931\u8D25", error);
                        }
                    });
                });
            }
        },
        template: "\n  <div style=\"padding: 15px;\">\n    <smart-table-crud\n      ref=\"table\"\n      queryUrl=\"quartz/timeTask/list\"\n      deleteUrl=\"quartz/timeTask/batchDelete\"\n      saveUpdateUrl=\"quartz/timeTask/saveUpdate\"\n      :keys=\"['taskId']\"\n      labelWidth=\"80px\"\n      :height=\"computedTableHeight\"\n      :columnOptions=\"columnOptions\"\n      tableName=\"\u5B9A\u65F6\u4EFB\u52A1\"\n      :defaultButtonConfig=\"defaultButtonConfig\"\n      :apiService=\"apiService\">\n      <template v-slot:table-used=\"{row}\">\n        <el-tag v-if=\"row.used === true\" type=\"success\">\u542F\u7528</el-tag>\n        <el-tag v-else type=\"danger\">\u5173\u95ED</el-tag>\n      </template>\n      <template v-slot:row-operation=\"{row}\">\n        <el-tooltip class=\"item\" effect=\"dark\" :content=\"row.used === true ? '\u5173\u95ED' : '\u542F\u7528'\" placement=\"top\">\n          <el-button @click=\"handleOpenClose(row)\" type=\"success\" :icon=\"row.used === true ? 'el-icon-video-pause' : 'el-icon-video-play'\" size=\"mini\"/>\n        </el-tooltip>\n          </template>\n    </smart-table-crud>\n  </div>\n  "
    };
});
