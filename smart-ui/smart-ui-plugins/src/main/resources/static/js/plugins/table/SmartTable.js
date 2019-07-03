define(["require", "exports", "utils/CommonUtils"], function (require, exports, CommonUtils_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.default = {
        props: {
            columnOptions: {
                required: true
            },
            selection: {
                type: Boolean,
                default: true
            },
            showIndex: {
                type: Boolean,
                default: true
            },
            stripe: {
                type: Boolean,
                default: true
            },
            border: {
                type: Boolean,
                default: true
            },
            type: {
                type: String,
                default: 'normal'
            },
            data: {
                type: Array
            },
            keys: {
                type: Array,
                required: true
            }
        },
        data: function () {
            return {
                leftFixed: false,
                tableData: []
            };
        },
        methods: {
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
        },
        computed: {
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
            getColumns: function () {
                var _this = this;
                var columns = [];
                this.columnOptions.forEach(function (item) {
                    var column = Object.assign({}, item);
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
        },
        template: "\n  <el-table\n    v-bind=\"$attrs\"\n    v-on=\"$listeners\"\n    :data=\"data\" \n    :row-key=\"getRowKey\"\n    :border=\"border\"\n    :stripe=\"stripe\">\n    <!--\u904D\u5386\u590D\u9009\u6846-->\n    <el-table-column\n      v-for=\"column in getSelectionIndexColumns\"\n      :key=\"column.key\"\n      :align=\"column.align\"\n      :fixed=\"leftFixed\"\n      :label=\"column.label\"\n      :width=\"column.width\"\n      :type=\"column.type\"/>\n    <!--\u904D\u5386\u5176\u4ED6\u5217-->\n    <el-table-column\n      v-for=\"item in getColumns\"\n      :key=\"item.key\"\n      :prop=\"item.prop\"\n      :width=\"item.width\"\n      :minWidth=\"item.minWidth\"\n      :fixed=\"item.fixed\"\n      :render-header=\"item.renderHeader\"\n      :sortable=\"item.sortable === true ? 'custom' : false\"\n      :resizable=\"item.resizable\"\n      :formatter=\"item.formatter\"\n      :align=\"item.align\"\n      :header-align=\"item.headerAlign\"\n      :class-name=\"item.className\"\n      :label-class-name=\"item.labelClassName\"\n      :label=\"item.label\">\n      <template slot-scope=\"{ row, column, $index }\">\n        <slot\n          v-if=\"useSolt(item)\"\n          :row=\"row\"\n          :name=\"item.key\"\n          :column=\"column\"\n          :$index=\"$index\"></slot>\n        <span v-else>\n          {{getColumnValue(item, row, column, $index)}}\n        </span>  \n      </template>\n    </el-table-column>\n  </el-table>\n  "
    };
});
