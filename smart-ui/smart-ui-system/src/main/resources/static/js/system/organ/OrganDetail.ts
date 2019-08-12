// @ts-ignore
import ApiService from '../../utils/ApiService.js'
// @ts-ignore
import MessageMixins from '../../mixins/MessageMixins.js'

export default {
  mixins: [
    MessageMixins
  ],
  props: {
    organId: {
      type: String
    }
  },
  data () {
    return {
      organ: {}
    }
  },
  mounted () {
    const $this: any = this
    if ($this.organId) {
      $this.load()
    }
  },
  watch: {
    organId: function (_new, old) {
      if (_new !== old) {
        this.load()
      }
    }
  },
  methods: {
    load () {
      ApiService.postAjax('sys/organ/get', { organId: this.organId })
          .then(data => {
            console.log(data)
            this.organ = data
          }).catch(error => {
        this.errorMessage('获取组织详情失败', error)
      })
    }
  },
  template: `
  <table>
      <tr>
        <td>abc</td>
      </tr>
    </table>
  `
}
