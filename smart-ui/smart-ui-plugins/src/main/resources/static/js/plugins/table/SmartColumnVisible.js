define(["require", "exports", "utils/CommonUtils"], function (require, exports, CommonUtils_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.default = {
        props: {
            columnShow: {
                type: Object,
                default: () => { return {}; }
            },
            lineNumber: {
                type: Number,
                default: 4
            }
        },
        data() {
            return {
                result: {}
            };
        },
        beforeMount() {
            let columnShow = CommonUtils_1.default.clone(this.columnShow);
            for (let column in columnShow) {
                this.$set(this.result, column, !columnShow[column]['hidden']);
            }
        },
        watch: {
            result: {
                deep: true,
                handler: function (_new) {
                    const listener = 'result-change';
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
                let result = [];
                let i = 0;
                for (let column in this.columnShow) {
                    if (i % this.lineNumber === 0) {
                        result.push([]);
                    }
                    let object = this.columnShow[column];
                    object['key'] = column;
                    result[result.length - 1].push(object);
                    i++;
                }
                return result;
            }
        },
        template: `
  <div>
    <el-row :key="index + 'out'" v-for="(columnGroup, index) in computedColumnShow">
      <el-col :span="computedSpanNumber" :key="index + 'in'" v-for="(column, index) in columnGroup">
        <el-checkbox
            :label="column.name"
            v-model="result[column.key]"
        ></el-checkbox>
      </el-col>
    </el-row>
  </div>
  `
    };
});
