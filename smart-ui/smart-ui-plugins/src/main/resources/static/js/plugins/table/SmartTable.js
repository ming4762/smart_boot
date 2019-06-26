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
define(["require", "exports", "ComponentBuilder", "utils/CommonUtils"], function (require, exports, ComponentBuilder_1, CommonUtils_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    /**
     * 表格组件
     * @author zhongming
     */
    var SmartTable = /** @class */ (function (_super) {
        __extends(SmartTable, _super);
        function SmartTable() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        SmartTable.prototype.props = function () {
            return {
                // 表格列配置
                columnOptions: {
                    required: true
                },
                // 是否显示复选框
                selection: {
                    type: Boolean,
                    default: true
                },
                // 是否显示序号
                showIndex: {
                    type: Boolean,
                    default: true
                },
                // 是否为斑马纹
                stripe: {
                    type: Boolean,
                    default: true
                },
                // 是否显示边框
                border: {
                    type: Boolean,
                    default: true
                },
                // 表格类型
                type: {
                    type: String,
                    default: 'normal'
                },
                // 表格数据
                data: {
                    type: Array
                },
                keys: {
                    type: Array,
                    required: true
                }
            };
        };
        SmartTable.prototype.data = function () {
            return {
                // 标识是否有左侧列冻结
                leftFixed: false,
                // 表格数据
                tableData: []
            };
        };
        SmartTable.prototype.methods = function () {
            return {
                useSolt: function (item) {
                    return this.$scopedSlots[item.key];
                },
                getColumnValue: function (column, row, $column, $index) {
                    if (column.formatter) {
                        return column.formatter(row, $column, row[column.key], $index);
                    }
                    else {
                        return row[column.key];
                    }
                }
            };
        };
        SmartTable.prototype.computed = function () {
            return {
                // 复选框/序号列计算属性
                getSelectionIndexColumns: function () {
                    var columns = [];
                    if (this.selection === true) {
                        columns.push({
                            key: 'selection',
                            type: 'selection',
                            width: 40,
                            align: 'center'
                        });
                    }
                    if (this.showIndex === true) {
                        columns.push({
                            key: 'index',
                            type: 'index',
                            width: 60,
                            align: 'center',
                            label: '序号'
                        });
                    }
                    return columns;
                },
                // 表格列计算属性
                getColumns: function () {
                    var _this = this;
                    var columns = [];
                    this.columnOptions.forEach(function (item) {
                        // @ts-ignore
                        var column = Object.assign({}, item);
                        // 显示的列才加入
                        if (column.visible !== false) {
                            if (!column.align)
                                column.align = 'center';
                            if (!column.key)
                                column.key = column.prop;
                            columns.push(column);
                        }
                        if (item.fixed === true || item.fixed === 'left') {
                            _this.leftFixed = true;
                        }
                    });
                    return columns;
                },
                // 获取表格的key
                getRowKey: function () {
                    var _this = this;
                    if (this.keys.length === 1) {
                        return this.keys[0];
                    }
                    else {
                        return function (row) {
                            return JSON.stringify(CommonUtils_1.default.getObjectByKeys(_this.keys, [row])[0]);
                        };
                    }
                }
            };
        };
        SmartTable.prototype.template = function () {
            return "\n    <el-table\n      v-bind=\"$attrs\"\n      v-on=\"$listeners\"\n      :data=\"data\" \n      :row-key=\"getRowKey\"\n      :border=\"border\"\n      :stripe=\"stripe\">\n      <!--\u904D\u5386\u590D\u9009\u6846-->\n      <el-table-column\n        v-for=\"column in getSelectionIndexColumns\"\n        :key=\"column.key\"\n        :align=\"column.align\"\n        :fixed=\"leftFixed\"\n        :label=\"column.label\"\n        :width=\"column.width\"\n        :type=\"column.type\"/>\n      <!--\u904D\u5386\u5176\u4ED6\u5217-->\n      <el-table-column\n        v-for=\"item in getColumns\"\n        :key=\"item.key\"\n        :prop=\"item.prop\"\n        :width=\"item.width\"\n        :minWidth=\"item.minWidth\"\n        :fixed=\"item.fixed\"\n        :render-header=\"item.renderHeader\"\n        :sortable=\"item.sortable === true ? 'custom' : false\"\n        :resizable=\"item.resizable\"\n        :formatter=\"item.formatter\"\n        :align=\"item.align\"\n        :header-align=\"item.headerAlign\"\n        :class-name=\"item.className\"\n        :label-class-name=\"item.labelClassName\"\n        :label=\"item.label\">\n        <template slot-scope=\"{ row, column, $index }\">\n          <slot\n            v-if=\"useSolt(item)\"\n            :row=\"row\"\n            :name=\"item.key\"\n            :column=\"column\"\n            :$index=\"$index\"></slot>\n          <span v-else>\n            {{getColumnValue(item, row, column, $index)}}\n          </span>  \n        </template>\n      </el-table-column>\n    </el-table>\n    ";
        };
        return SmartTable;
    }(ComponentBuilder_1.default));
    exports.default = SmartTable;
});