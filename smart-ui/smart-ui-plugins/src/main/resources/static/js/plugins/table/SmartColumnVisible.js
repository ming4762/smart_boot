define(["require", "exports", "utils/CommonUtils"], function (require, exports, CommonUtils_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.default = {
        props: {
            columnShow: {
                type: Object,
                default: function () { return {}; }
            },
            lineNumber: {
                type: Number,
                default: 4
            }
        },
        beforeMount: function () {
            var columnShow = CommonUtils_1.default.clone(this.columnShow);
            for (var column in columnShow) {
                this.$set(this.result, column, !columnShow[column]['hidden']);
            }
        },
        watch: {
            result: {
                deep: true,
                handler: function (_new) {
                    var listener = 'result-change';
                    if (this.$listeners[listener]) {
                        this.$emit(listener, _new);
                    }
                }
            }
        },
        computed: {
            computedSpanNumber: function () {
                return 24 / this.lineNumber;
            },
            computedColumnShow: function () {
                var result = [];
                var i = 0;
                for (var column in this.columnShow) {
                    if (i % this.lineNumber === 0) {
                        result.push([]);
                    }
                    var object = this.columnShow[column];
                    object['key'] = column;
                    result[result.length - 1].push(object);
                    i++;
                }
                return result;
            }
        },
        template: "\n  <div>\n    <el-row :key=\"index + 'out'\" v-for=\"(columnGroup, index) in computedColumnShow\">\n      <el-col :span=\"computedSpanNumber\" :key=\"index + 'in'\" v-for=\"(column, index) in columnGroup\">\n        <el-checkbox\n            :label=\"column.name\"\n            v-model=\"result[column.key]\"\n        ></el-checkbox>\n      </el-col>\n    </el-row>\n  </div>\n  "
    };
});
