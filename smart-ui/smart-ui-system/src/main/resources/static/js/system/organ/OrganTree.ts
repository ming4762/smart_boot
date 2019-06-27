// @ts-ignore
import ComponentBuilder from 'ComponentBuilder'
// @ts-ignore
import ApiService from 'utils/ApiService'
// @ts-ignore
import MessageMixins from 'mixins/MessageMixins'

/**
 * 部门树组件
 */
export default class OrganTree extends ComponentBuilder {

  protected mixins () {
    return [
        new MessageMixins().build()
    ]
  }

  protected data () {
    return {
      organTreeData: []
    }
  }

  protected mounted () {
    const $this: any = this
    $this.load()
  }

  protected computed () {
    return {
      computedOrganTreeData () {
        return [
          {
            id: '0',
            text: '组织根',
            object: {
              organId: '0',
            },
            children: this.organTreeData
          }
        ]
      }
    }
  }

  protected methods () {
    return {
      /**
       * 加载数据
       */
      load () {
        // 加载数据
        ApiService.postAjax('sys/organ/listTree', [])
            .then(data => {
              // @ts-ignore
              this.organTreeData = data['0']
            }).catch(error => {
          // @ts-ignore
          this.errorMessage('加载部门数据失败', error)
        })
      }
    }
  }

  /**
   * 模板
   */
  protected template () {
    return `
    <el-tree
      v-on="$listeners"
      :expand-on-click-node="false"
      node-key="organId"
      :default-expanded-keys="['0']"
      :data="computedOrganTreeData">
      <template v-slot="{data}">
        <div>
          <i class="el-icon-school"></i>
          <span class="el-tree-node__label">{{data.text}}</span>
        </div>
      </template>
    </el-tree>
    `
  }
}