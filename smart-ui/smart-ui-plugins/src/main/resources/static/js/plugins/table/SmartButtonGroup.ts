// @ts-ignore
import ComponentBuilder from 'ComponentBuilder'

/**
 * 顶部按钮组
 */
export default class SmartButtonGroup extends ComponentBuilder {

  protected props () {
    return {
      // 左侧solt是否在group内
      leftInGroup: {
        type: Boolean,
        default: true
      },
      // 是否有左侧按钮
      hasLeft: {
        type: Boolean,
        default: true
      },
      // 是否有右侧按钮
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
    }
  }

  protected methods () {
    return {
      /**
       * 点击按钮触发事件
       */
      handleClickButtonGroup: function (ident) {
        const listener = 'button-click'
        if (this.$listeners[listener]) {
          this.$emit(listener, ident)
        }
      }
    }
  }

  protected template () {
    //TODO: 中文使用I18N
    return `
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
  }
}