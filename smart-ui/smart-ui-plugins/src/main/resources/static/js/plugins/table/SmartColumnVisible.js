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
    var SmartColumnVisible = (function (_super) {
        __extends(SmartColumnVisible, _super);
        function SmartColumnVisible() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        SmartColumnVisible.prototype.props = function () {
            return {
                columnShow: {
                    type: Object,
                    default: function () { return {}; }
                },
                lineNumber: {
                    type: Number,
                    default: 4
                }
            };
        };
        SmartColumnVisible.prototype.data = function () {
            return {
                result: {}
            };
        };
        SmartColumnVisible.prototype.beforeMount = function () {
            var columnShow = CommonUtils_1.default.clone(this.columnShow);
            for (var column in columnShow) {
                this.$set(this.result, column, !columnShow[column]['hidden']);
            }
        };
        SmartColumnVisible.prototype.watch = function () {
            return {
                result: {
                    deep: true,
                    handler: function (_new) {
                        var listener = 'result-change';
                        if (this.$listeners[listener]) {
                            this.$emit(listener, _new);
                        }
                    }
                }
            };
        };
        SmartColumnVisible.prototype.computed = function () {
            return {
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
            };
        };
        SmartColumnVisible.prototype.template = function () {
            return "\n    <div>\n      <el-row :key=\"index + 'out'\" v-for=\"(columnGroup, index) in computedColumnShow\">\n        <el-col :span=\"computedSpanNumber\" :key=\"index + 'in'\" v-for=\"(column, index) in columnGroup\">\n          <el-checkbox\n              :label=\"column.name\"\n              v-model=\"result[column.key]\"\n          ></el-checkbox>\n        </el-col>\n      </el-row>\n    </div>\n    ";
        };
        return SmartColumnVisible;
    }(ComponentBuilder_1.default));
    exports.default = SmartColumnVisible;
});
