// @ts-ignore
import ComponentBuilder from 'ComponentBuilder'
// @ts-ignore
import ApiService from 'utils/ApiService'
// @ts-ignore
import MessageMixins from 'mixins/MessageMixins'

export default class OrganDetail extends ComponentBuilder {

  protected mixins () {
    return [
      MessageMixins
    ]
  }

  protected props () {
    return {
      organId: {
        type: String
      }
    }
  }

  protected data () {
    return {
      organ: {}
    }
  }

  mounted () {
    const $this: any = this
    if ($this.organId) {
      $this.load()
    }
  }

  protected watch () {
    return {
      organId: function (_new, old) {
        if (_new !== old) {
          this.load()
        }
      }
    }
  }

  protected methods () {
    return {
      load () {
        ApiService.postAjax('sys/organ/get', { organId: this.organId })
            .then(data => {
              console.log(data)
              this.organ = data
            }).catch(error => {
              this.errorMessage('获取组织详情失败', error)
            })
      }
    }
  }


  protected template () {
    return `
    <table>
      <tr>
        <td>abc</td>
      </tr>
    </table>
    `
  }

}