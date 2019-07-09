define(["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.default = {
        props: {
            leftInGroup: {
                type: Boolean,
                default: true
            },
            hasLeft: {
                type: Boolean,
                default: true
            },
            hasRight: {
                type: Boolean,
                default: true
            },
            addShow: {
                type: Boolean,
                default: true
            },
            editShow: {
                type: Boolean,
                default: true
            },
            deleteShow: {
                type: Boolean,
                default: true
            }
        },
        methods: {
            handleClickButtonGroup: function (ident) {
                const listener = 'button-click';
                if (this.$listeners[listener]) {
                    this.$emit(listener, ident);
                }
            }
        },
        template: `
  <div>
      <div v-if="hasLeft" class="cloud-table-left">
        <el-button-group>
          <el-button
            v-if="addShow"
            icon="el-icon-plus"
            size="small"
            @click="handleClickButtonGroup('add')"
            type="primary">添加</el-button>
          <el-button
            v-if="editShow"
            icon="el-icon-edit-outline"
            @click="handleClickButtonGroup('edit')"
            size="small"
            type="warning">修改</el-button> 
          <el-button
            v-if="deleteShow"
            icon="el-icon-delete"
            @click="handleClickButtonGroup('delete')"
            size="small"
            type="danger">删除</el-button>
          <template v-if="leftInGroup">
            <slot name="buttonLeft"></slot>
          </template>  
        </el-button-group>
        <template v-if="!leftInGroup">
          <slot name="buttonLeft"></slot>
        </template> 
      </div>
      <div v-if="hasRight" class="cloud-table-right">
        <el-tooltip effect="dark" content="刷新" placement="top">
          <el-button
            size="small"
            icon="el-icon-refresh"
            @click="handleClickButtonGroup('refresh')"
            circle/>
        </el-tooltip>
        <el-tooltip effect="dark" content="列显示隐藏" placement="top">
          <el-button
            size="small"
            icon="el-icon-menu"
            @click="handleClickButtonGroup('columnVisible')"
            circle/>
        </el-tooltip>
        <el-tooltip effect="dark" content="搜索" placement="top">
          <el-button
            size="small"
            icon="el-icon-search"
            @click="handleClickButtonGroup('search')"
            circle/>
        </el-tooltip>
        <template>
          <slot name="buttonRight"></slot>
        </template>
      </div>
    </div>
  `
    };
});
