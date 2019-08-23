// @ts-ignore
import PageBuilder from '../../PageBuilder.js'
// @ts-ignore
import CommonUtils from '../../utils/CommonUtils.js'
// @ts-ignore
import ApiService from '../../utils/ApiService.js'
// @ts-ignore
import MessageMixins from '../../mixins/MessageMixins.js'
// @ts-ignore
import TreeUtils from '../../utils/TreeUtils.js'
// @ts-ignore
import SmartUpload from '../../plugins/upload/SmartUpload.js'
// 引入上传工具类
// @ts-ignore
import UploadService from '../../utils/UploadService.js'

const getPath = CommonUtils.withContextPath

declare const ready, smartModuleLoader

ready(function () {
  smartModuleLoader('smart-table').then(() => {
    return CommonUtils.loadJS(
        getPath('js/plugins/vue-tinymce/tinymce/4.8.3/tinymce.min.js'),
        getPath('js/plugins/vue-tinymce/vue-tinymce.umd.min.js')
    )
  }).then(() => {
    // @ts-ignore
    new NewsAdd().init()
  })
})

const NEW_FILE_TYPE = 'NEWS_FILE'
const ATTACHMENT_FILE_TYPE = "NEWS_ATTACHMENT_FILE"

class NewsAdd extends PageBuilder {
  /**
   * 构建函数
   */
  protected build () {
    return page
  }
}

const page = {
  mixins: [
    MessageMixins
  ],
  components: {
    SmartUpload
  },
  data () {
    return {
      content: '',
      formModel: {
        releaseTime: new Date()
      },
      formColumns: [
        {
          prop: 'title',
          label: '文章标题',
          rules: true,
          span: 12
        },
        {
          prop: 'subtitle',
          label: '文章副标题',
          span: 12
        },
        {
          prop: 'author',
          label: '作者',
          rules: true,
          span: 12
        },
        {
          prop: 'releaseTime',
          label: '发布日期',
          span: 12
        },
        {
          prop: 'moduleId',
          label: '模块',
          rules: [
            { required: true, message: '请选择模块', trigger: 'change' }
          ],
          span: 12
        },
        {
          prop: 'top',
          label: '是否首页显示',
          type: 'boolean',
          defaultValue: false,
          span: 12
        },
        {
          prop: 'upload',
          label: '上传附件'
        }
      ],
      pickerOptions: {
        shortcuts: [{
          text: '今天',
          onClick(picker) {
            picker.$emit('pick', new Date())
          }
        }, {
          text: '昨天',
          onClick(picker) {
            const date = new Date()
            date.setTime(date.getTime() - 3600 * 1000 * 24)
            picker.$emit('pick', date)
          }
        }, {
          text: '一周前',
          onClick(picker) {
            const date = new Date()
            date.setTime(date.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', date)
          }
        }]
      },
      props: {
        value: 'moduleId',
        children: 'children',
        label: 'moduleName'
      },
      // 模块列表
      moduleList: [],
      attachmentList: [],
      attachmentFileList: []
    }
  },
  created () {
    this.loadModuleTree()
  },
  methods: {
    getPath: getPath,
    /**
     * 加载模块列表
     */
    loadModuleTree () {
      ApiService.postAjax('portal/module/list', {})
          .then((data) => {
            this.moduleList = TreeUtils.convertList2Tree(data, ['moduleId', 'parentId'], '0')
          }).catch(error => {
            this.errorMessage('加载模块列表失败', error)
      })
    },
    handleUpload (blob, success, fail) {
      UploadService.upload(blob.blob(), {
        type: NEW_FILE_TYPE
      }).then((data) => {
        success(UploadService.getImageUrl(data.fileId))
      }).catch(error => {
        fail('上传失败')
        console.error(error)
      })
    },
    /**
     * 保存新闻
     */
    saveNews () {
      const newId = new Date().getTime() + ''
      this.$refs['newsForm'].validate().then((valid) => {
        if (valid) {
          // 保存新闻
          const news = Object.assign({
            newsId: newId,
            content: this.content
          }, this.formModel)
          news.moduleId = news.moduleId[0]
          // 保存新闻信息
          return ApiService.postAjax('portal/news/saveUpdate', news)
        }
      }).then(() => {
        this.uploadAttachment(newId)
      })
    },
    /**
     * 上传附件
     * @param newsId
     */
    uploadAttachment (newsId) {
      // 批量上传附件
      const attachmentList = this.$refs['fileUpload'].getFileList()
      if (attachmentList.length > 0) {
        const files = UploadService.batchUpload(attachmentList.map(item => item.raw), {
          type: ATTACHMENT_FILE_TYPE
        }).then(data => {
          // 保存附件
          this.saveAttachment(newsId, data.map((file) => file.fileId))
          // 保存附件信息
        }).catch(error => {
          this.errorMessage('上传附近发生错误', error)
          this.deleteNews(newsId)
        })
      }
    },
    /**
     * 保存附件
     * @param newsId
     * @param fileIdList
     */
    saveAttachment (newsId, fileIdList) {
      const data = fileIdList.map((fileId, index) => {
        return {
          newsId: newsId,
          fileId: fileId,
          seq: index
        }
      })
      return ApiService.postAjax('portal/newsAttachment/batchSave', data)
    },
    /**
     * 删除新闻
     * @param newsId
     */
    deleteNews (newsId) {
      ApiService.postAjax('portal/news/batchDelete', [
        {
          newsId: newsId
        }
      ])
    }
  },
  template: `
  <div style="padding: 20px">
    <smart-form
      ref="newsForm"
      style="width: 70%;margin:0 auto"
      :model="formModel"
      labelWidth="100px"
      :columnOptions="formColumns">
      <!-- 发布日期插槽 -->
      <template v-slot:releaseTime="{model}">
        <el-form-item label="发布日期">
          <el-date-picker v-model="model.releaseTime" type="date" placeholder="选择日期时间" align="right" :picker-options="pickerOptions"></el-date-picker>
        </el-form-item>
      </template>
      <!-- 模块插槽 -->
      <template v-slot:moduleId="{model}">
        <el-form-item prop="moduleId" label="模块">
          <el-cascader
            placeholder="请选择模块"
            :options="moduleList"
            filterable
            :props="props"
            v-model="model.moduleId"
            change-on-select></el-cascader>
        </el-form-item>
      </template>
      <!--上传附近-->
      <template v-slot:upload="{}">
        <el-form-item label="上传附件">
          <SmartUpload
            :file-list="attachmentFileList"
            ref="fileUpload"
            multiple
            :auto-upload="false"
            type="news_attachments"
            list-type="text">
            <el-button size="small" type="primary">选择文件</el-button>
          </SmartUpload>
        </el-form-item>  
      </template>
    </smart-form>
    <vue-tinymce-edit
      :uploadHandler="handleUpload"
      :js-path="getPath('js/plugins/vue-tinymce/tinymce/4.8.3')" 
      v-model="content"/>
    <div style="width:80%;margin-left:10%;text-align: center;">
      <br>
      <el-row>
        <el-button type="primary" size="medium" @click="saveNews">保存</el-button>
        <el-button size="medium">取消</el-button>
      </el-row>
    </div>
  </div>
  `
}