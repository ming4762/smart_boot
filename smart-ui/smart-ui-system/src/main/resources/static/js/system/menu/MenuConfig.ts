// @ts-ignore
import PageBuilder from 'PageBuilder'
// @ts-ignore
import ApiService from 'utils/ApiService'
// @ts-ignore
import MessageMixins from 'mixins/MessageMixins'
// @ts-ignore
import CollectionUtils from 'utils/CollectionUtils'
// @ts-ignore
import SmartForm from 'plugins/form/SmartForm'


/**
 * 菜单配置页面
 */
export class MenuConfig extends PageBuilder {


  protected components () {
    return {
      'smart-form': SmartForm
    }
  }

  protected mixins () {
    return [
        MessageMixins
    ]
  }

  protected data () {
    return {
      // 添加card演示
      addCardBodyStyle: {
        'font-size': '18px',
        'color': '#737373'
      },
      data: [],
      // 菜单选中状态
      menuConfigSelect: {},
      selectConfigId: '',
      // 添加弹窗显示装填
      addDialogVisible: false,
      addEditFormColumns: [
        {
          label: '菜单分类ID',
          prop: 'configId',
          visible: false
        },
        {
          label: '菜单分类名称',
          prop: 'configName'
        },
        {
          label: '序号',
          prop: 'seq',
          type: 'number',
          defaultValue: 1
        },
        {
          label: '是否启用',
          prop: 'status',
          type: 'boolean'
        }
      ],
      addEditFormModel: {}
    }
  }

  protected computed () {
    return {
      // 数据计算属性
      computedData () {
        return CollectionUtils.splitArray(this.data, 3)
      }
    }
  }

  protected mounted () {
    const $this: any = this
    // 加载数据
    $this.load()
  }

  protected methods () {
    return {
      /**
       * 点击添加按钮
       */
      handleShowAddMenuConfig (menuConfig) {
        this.addDialogVisible = true
        if (menuConfig) {
          this.addEditFormModel = {
            configId: menuConfig.configId,
            configName: menuConfig.configName,
            seq: menuConfig.seq,
            status: menuConfig.status
          }
        } else {
          this.addEditFormModel = {}
        }
      },
      handleMouseOver (menuConfig) {
        Object.keys(this.menuConfigSelect).forEach(key => {
          this.menuConfigSelect[key] = false
        })
        this.$set(this.menuConfigSelect, menuConfig.configId, true)
      },
      handleMouseLeave (menuConfig) {
        Object.keys(this.menuConfigSelect).forEach(key => {
          this.menuConfigSelect[key] = false
        })
      },
      getCardBodyStyle (menuConfig) {
        const configId = menuConfig.configId
        const activeStyle = {
          color: 'white',
          'background-color': '#1fbceb'
        }
        const noActiveStyle = {
          color: '#737373'
        }
        if (configId === this.selectConfigId) {
          return activeStyle
        }
        if (this.menuConfigSelect[configId] === true) {
          return activeStyle
        } else {
          return noActiveStyle
        }
      },
      /**
       * 编辑DIV是否显示
       * @param menuConfig
       */
      getEditShow (menuConfig) {
        return this.menuConfigSelect[menuConfig.configId] === true
      },
      /**
       * 执行保存操作
       */
      handleSaveUpdate () {
        ApiService.postAjax('sys/menuConfig/saveUpdate', this.addEditFormModel)
            .then(data => {
              this.load()
              this.addDialogVisible = false
            }).catch(error => {
              this.errorMessage('保存菜单分类发生错误', error)
            })
      },
      /**
       * 加载数据
       */
      load () {
        const $this: any = this
        // 加载数据
        ApiService.postAjax('sys/menuConfig/list', {
          defaultSortColumn: 'seq'
        }).then(data => {
          this.data = data
          // const menuConfigSelect = {}
          data.forEach(item => {
            // menuConfigSelect[item.configId] = item.status
            if (item.status === true) {
              $this.selectConfigId = item.configId
            }
          })
          // $this.menuConfigSelect = menuConfigSelect
        }).catch(error => {
          // @ts-ignore
          this.errorMessage('加载菜单分类失败', error)
        })
      },
      /**
       * 执行删除操作
       * @param menuConfig
       */
      handleDeleteMenuConfig (menuConfig) {
        this.$confirm('确定要删除该分类吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return ApiService.postAjax('sys/menuConfig/delete', {
            configId: menuConfig.configId
          })
        }).then(() => {
          this.load()
          this.successMessage('删除成功')
        }).catch(error => {
          if (error !== 'cancel') {
            this.errorMessage('删除菜单分类发生错误', error)
          }
        })
      },
      /**
       * 显示菜单分类详情
       * @param menuConfig
       */
      handleShowConfigDetail (menuConfig) {
        console.log(menuConfig)
      }
    }
  }

  protected template () {
    return `
    <div class="menu-config-container">
      <!--遍历菜单-->
      <el-row
        class="menu-config-row"
        :gutter="20"
        :key="index + 'row'"
        v-for="(items, index) in computedData">
        <el-col
          :span="8"
          v-for="(item, index) in items"
          :key="index + 'col'"
          class="menu-confg-col">
          ${this.createMenuConfigTemplate()}
        </el-col>
      </el-row>
      <!--添加菜单-->
      <el-row class="menu-config-row" :gutter="20">
        <el-col class="menu-confg-col" :span="8">
          ${this.createAddMenuConfigTemplate()}
        </el-col>
      </el-row>
      <!--添加修改弹窗-->
      ${this.createAddDialogTemplate()}
    </div>
    `
  }

  private createMenuConfigTemplate () {
    return `
    <el-card
      @click.native="handleShowConfigDetail(item)"
      class="menu-config-card menu-config-show-card"
      :style="getCardBodyStyle(item)"
      @mouseover.native="handleMouseOver(item)"
      @mouseleave.native="handleMouseLeave(item)"
      shadow="hover">
      <div class="menu-config-edit-container">
        <div v-show="getEditShow(item)" class="edit-button">
          <span @click="handleShowAddMenuConfig(item)">修改</span>
          <span v-if="item.configId !== '0'" @click="handleDeleteMenuConfig(item)">删除</span>
        </div>
      </div>
      <div style="padding-top: 13px">
        <span>{{item.configName}}</span>
      </div>
    </el-card>
    `
  }

  /**
   * 创建添加模板
   */
  private createAddMenuConfigTemplate () {
    return `
    <el-card @click.native="handleShowAddMenuConfig" class="menu-config-card menu-config-add-card" :body-style="addCardBodyStyle" shadow="hover">
      <i class="el-icon-plus"></i>
      <span>创建菜单类别</span>
    </el-card>
    `
  }

  private createAddDialogTemplate () {
    return `
    <el-dialog
      width="400px"
      title="添加菜单分类"
      :visible.sync="addDialogVisible">
      <smart-form
        :model="addEditFormModel"
        labelWidth="100px"
        :columnOptions="addEditFormColumns"/>
      <div style="padding-left: 100px">
        <el-button @click="handleSaveUpdate" type="primary">保存</el-button>
        <el-button @click="addDialogVisible = false" type="warning">取消</el-button>
      </div>  
    </el-dialog>
    `
  }
}