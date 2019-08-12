import CommonUtils from '../../utils/CommonUtils.js';
export default {
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
    data() {
        return {
            leftFixed: false,
            tableData: []
        };
    },
    methods: {
        useSolt: function (item) {
            return this.$scopedSlots[item.key];
        },
        getColumnValue(column, row, $column, $index) {
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
            const columns = [];
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
            const columns = [];
            this.columnOptions.forEach(item => {
                const column = Object.assign({}, item);
                if (column.visible !== false) {
                    if (!column.align)
                        column.align = 'center';
                    if (!column.key)
                        column.key = column.prop;
                    columns.push(column);
                }
                if (item.fixed === true || item.fixed === 'left') {
                    this.leftFixed = true;
                }
            });
            return columns;
        },
        getRowKey: function () {
            if (this.keys.length === 1) {
                return this.keys[0];
            }
            else {
                return (row) => {
                    return JSON.stringify(CommonUtils.getObjectByKeys(this.keys, [row])[0]);
                };
            }
        }
    },
    template: `
  <el-table
    v-bind="$attrs"
    v-on="$listeners"
    :data="data" 
    :row-key="getRowKey"
    :border="border"
    :stripe="stripe">
    <!--遍历复选框-->
    <el-table-column
      v-for="column in getSelectionIndexColumns"
      :key="column.key"
      :align="column.align"
      :fixed="leftFixed"
      :label="column.label"
      :width="column.width"
      :type="column.type"/>
    <!--遍历其他列-->
    <el-table-column
      v-for="item in getColumns"
      :key="item.key"
      :prop="item.prop"
      :width="item.width"
      :minWidth="item.minWidth"
      :fixed="item.fixed"
      :type="item.type"
      :render-header="item.renderHeader"
      :sortable="item.sortable === true ? 'custom' : false"
      :resizable="item.resizable"
      :formatter="item.formatter"
      :align="item.align"
      :header-align="item.headerAlign"
      :class-name="item.className"
      :label-class-name="item.labelClassName"
      :label="item.label">
      <template slot-scope="{ row, column, $index }">
        <slot
          v-if="useSolt(item)"
          :row="row"
          :name="item.key"
          :column="column"
          :$index="$index"></slot>
        <span v-else>
          {{getColumnValue(item, row, column, $index)}}
        </span>  
      </template>
    </el-table-column>
  </el-table>
  `
};
