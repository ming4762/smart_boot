/**
 * 上传组件
 */
export default {
  props: {
    action: String,
    // 文件标识
    type: { type: String, required: true },
    data: { type: Object, default: () => { return {} } },
    // 允许的扩展名
    acceptExtension: String
  },
  data () {
    return {
      uploadParameter: {}
    }
  },
  created () {
    this.uploadParameter = Object.assign(this.data, { type: this.type })
  },
  computed: {
    computedUploadUrl () {
      if (this.action) {
        return this.action
      } else {
        // TODO: 改为动态的
        return `${localStorage.getItem('API_URL')}/public/file/upload`
      }
    }
  },
  methods: {
    /**
     * 上传前操作
     */
    handleBeforeUpload(file: any): boolean {
      this.$emit('before-upload', file)
      if (this.acceptExtension) {
        if (!this.fileInAcceptExtension(file.name, this.acceptExtension)) {
          this.$message.error(`只能上传${this.acceptExtension}文件`)
          return false
        }
      }
      return true
    },
    /**
     * 判断文件名是否在允许的文件扩展名内
     */
    fileInAcceptExtension(filename: string, acceptExtension: string): boolean {
      const acceptList = acceptExtension.split(',')
      // 获取文件扩展名
      const filenameS = filename.split('.')
      const extension = filenameS[filenameS.length - 1]
      return acceptList.indexOf(extension) !== -1
    },
    submit () {
      this.$refs['upload'].submit()
    },
    getFileList () {
      return this.$refs['upload'].uploadFiles
    }
  },
  template: `
  <el-upload
    ref="upload"
    v-bind="$attrs"
    :action="computedUploadUrl"
    :data="uploadParameter"
    :before-upload="handleBeforeUpload"
    v-on="$listeners">
    <slot></slot>
    <slot name="tip"></slot>
  </el-upload>
  `
}