// @ts-ignore
import ComponentBuilder from 'ComponentBuilder'
// @ts-ignore
import FlexAside from 'plugins/container/FlexAside'
import OrganTree from 'system/organ/OrganTree'
// @ts-ignore
import SmartForm from 'plugins/form/SmartForm'
// @ts-ignore
import ApiService from 'utils/ApiService'
// @ts-ignore
import MessageMixins from 'mixins/MessageMixins'
// @ts-ignore
import CommonUtils from 'utils/CommonUtils'
import OrganDetail from 'system/organ/OrganDetail'

export class Organ extends ComponentBuilder {

  private vue: any

  public init() {
    this.initVue()
  }

  private initVue () {
    // @ts-ignore
    this.vue = new Vue({
      el: '#vue-container',
      components: {
        // @ts-ignore
        'organ-main': this.build()
      }
    })
  }

  /**
   * 构建组件
   */
  protected components () {
    return {
      'flex-aside': FlexAside,
      // @ts-ignore
      'organ-tree': OrganTree,
      'smart-form':SmartForm,
      // @ts-ignore
      'organ-detail': OrganDetail
    }
  }

  protected mixins () {
    return [
      MessageMixins
    ]
  }


  protected data () {
    return {
      // 部门搜索值
      searchValue: '',
      addOrganDialogVisible: false,
      organAddFormColumn: [
        {
          label: '组织ID',
          prop: 'organId',
          visible: false
        },
        {
          label: '上级Id',
          prop: 'parentId'
        },
        {
          label: '组织编码',
          prop: 'organCode',
          rules: true
        },
        {
          label: '组织名称',
          prop: 'organName',
          rules: true
        },
        {
          label: '组织简称',
          prop: 'shortName'
        },
        {
          label: '负责人',
          prop: 'leaderId'
        },
        {
          label: '描述',
          prop: 'description',
          type: 'textarea'
        }

      ],
      organModel: {
        organCode: ''
      },
      // 选择的组织
      selectedOrgan: {
      },
      // 激活的tab
      activeTab: 'detail'
    }
  }

  protected methods () {
    return {
      /**
       * 添加部门
       */
      handleShowAddOrgan () {
        this.addOrganDialogVisible = true
        this.organModel.organCode = CommonUtils.createUUID()
        this.organModel.organId = CommonUtils.createUUID()
        this.organModel.parentId = this.selectedOrgan.organId ? this.selectedOrgan.organId : '0'
        if (this.organModel.parentId === '0') {
          this.organModel.topParentId = this.organModel.organId
        } else  {
          this.organModel.topParentId = this.selectedOrgan.topParentId
        }
      },
      /**
       * 添加部门
       */
      handleAddOrgan () {
        ApiService.postAjax('sys/organ/saveUpdate', this.organModel)
            .then(data => {
              console.log(data)
              this.successMessage('添加部门成功')
              this.addOrganDialogVisible = false
              // 重新加载数据
              this.$refs['organTree'].load()
            }).catch(error => {
              this.addOrganDialogVisible = false
              this.errorMessage('添加部门失败', error)
            })
      },
      /**
       * 点击节点时触发
       * @param data
       */
      handleClickOrganTree (data) {
        this.selectedOrgan = data.object
      }
    }
  }


  protected template () {
    // TODO I18N
    return `
    <flex-aside
      :hasAsideHeader="false">
      <template slot="aside">
        <div class="organ-aside full-height">
          <div class="organ-aside-search">
            <el-input
              v-model="searchValue"
              suffix-icon="el-icon-search"
              placeholder="搜索部门"></el-input>
          </div>
          <!--部门树-->
          <div style="height: calc(100% - 190px); padding-top: 22px">
            <organ-tree
              ref="organTree"
              @node-click="handleClickOrganTree"/>
          </div>
          <!--添加按钮-->
          <div class="organ-aside-add">
            <el-button 
              type="primary"
              @click="handleShowAddOrgan"
              style="width: 100%"
              icon="el-icon-plus">添加部门</el-button>
          </div>
        </div>
      </template>
      <!--主题部分-->
      <template>
        <div class="full-height">
          <el-tabs v-model="activeTab" style="padding: 15px">
            <el-tab-pane lazy label="组织详情" name="detail">
              <organ-detail :organId="selectedOrgan.organId"/>
            </el-tab-pane>
            <el-tab-pane lazy label="人员信息" name="userList">配置管理</el-tab-pane>
          </el-tabs>
        </div>
      </template>
      <!--添加部门弹窗-->
      <el-dialog
        :visible.sync="addOrganDialogVisible"
        title="添加部门">
        <smart-form
          labelWidth="80px"
          :model="organModel"
          :columnOptions="organAddFormColumn">
          <template v-slot:parentId="{model}">
            <el-form-item label="上级组织">
              <el-input v-model="model['parentId']" style="display: none"/> 
              <el-input disabled :value="(!selectedOrgan.organId || selectedOrgan.organId === '0') ? '根组织' : selectedOrgan.organName"/>
            </el-form-item>
          </template>
        </smart-form>
        <div style="padding-left: 80px">
          <el-button type="primary" @click="handleAddOrgan">添加</el-button>
          <el-button type="warning" @click="addOrganDialogVisible = false">取消</el-button>
        </div>
      </el-dialog>
    </flex-aside>
    `
  }
}