// @ts-ignore
import PageBuilder from '../../PageBuilder.js'
// @ts-ignore
import CommonUtils from '../../utils/CommonUtils.js'
// @ts-ignore
import ApiService from '../../utils/ApiService.js'
// @ts-ignore
import LayoutMixins from '../../mixins/LayoutMixins.js'
// @ts-ignore
import TimeUtils from '../../utils/TimeUtils.js'

const getPath = CommonUtils.withContextPath

declare const ready, smartModuleLoader

ready(function () {
  smartModuleLoader('smart-table').then(() => {
    // @ts-ignore
    new NewsList().init()
  })
})

class NewsList extends PageBuilder {
  /**
   * 构建函数
   */
  protected build () {
    return page
  }
}

const page = {
  mixins: [
    LayoutMixins
  ],
  props: {
    moduleId: String
  },
  data () {
    return {
      apiService: ApiService,
      moduleMap: {},
      defaultButtonConfig: {
        add: {
          rowShow: false,
          topShow: false
        }
      },
      columnOptions: [
        {
          label: '新闻ID',
          prop: 'newsId',
          table: {
            visible: false,
            displayControl: false
          },
          form: {
            visible: false
          }
        },
        {
          label: '标题',
          prop: 'title',
          table: {
            fixed: true,
            width: 160
          },
          form: {
            span: 12,
            rules: true
          }
        },
        {
          label: '副标题',
          prop: 'subtitle',
          table: {
            width: 160
          },
          form: {
            span: 12
          }
        },
        {
          label: '摘要',
          prop: 'summary',
          table: {},
          form: {}
        },
        {
          label: '标题图片',
          prop: 'coverPicId',
          table: {
            visible: false
          },
          form: {}
        },
        {
          label: '作者',
          prop: 'author',
          table: {
            width: 140
          },
          form: {}
        },
        {
          label: '创建时间',
          prop: 'createTime',
          table: {
            width: 170,
            formatter: (row, column, value) => TimeUtils.formatTime(value),
            sortable: true
          },
          form: {}
        },
        {
          label: '内容',
          prop: 'content',
          form: {
            visible: false
          },
          table: {
            visible: false,
            displayControl: false
          }
        },
        {
          label: '模块',
          prop: 'moduleId',
          table: {
            formatter: (row) => {
              if (row.module) {
                return row.module.moduleName
              }
              return '-'
            }
          },
          form: {}
        },
        {
          label: '点赞数',
          prop: 'praiseNum',
          table: {
          },
          form: {}
        },
        {
          label: '阅读数',
          prop: 'readNum',
          table: {},
          form: {}
        },
        {
          label: '评论数',
          prop: 'commentNum',
          table: {},
          form: {}
        },
        {
          label: '是否置顶',
          prop: 'top',
          type: 'boolean',
          table: {},
          form: {}
        },
        {
          label: '来源',
          prop: 'sources',
          table: {},
          form: {}
        }
      ]
    }
  },
  methods: {
    titleTooltip () {
      return
    },
    /**
     * 格式化标题
     */
    formatTitle (title: string) {
      if (title && title.length > 9) {
        return title.substring(0, 8) + '...'
      }
      return title
    },
    /**
     * 搜索条件格式化
     * @param parameter
     */
    handleQueryParameterFormatter (parameter): any {
      // 不查询内容
      const para = {
        withContent: false
      }
      if (this.moduleId) {
        para['moduleId@='] = this.moduleId
      }
      return Object.assign(para, parameter)
    },
    /**
     * 跳转到发布新闻页面
     */
    handleShowNewsAdd (): void {
      console.log(this)
    },
    /**
     * 获取详情路径
     */
    handleGetDetailHref () {
      //TODO:待开发
      return getPath(``)
    }
  },
  template: `
  <div style="padding: 15px;">
    <smart-table-crud
      ref="table"
      queryUrl="portal/news/listWthAll"
      deleteUrl="portal/news/batchDelete"
      saveUpdateUrl="portal/news/saveUpdate"
      :height="computedTableHeight"
      tableName="新闻"
      :apiService="apiService"
      :columnOptions="columnOptions"
      :queryParameterFormatter="handleQueryParameterFormatter"
      :defaultButtonConfig="defaultButtonConfig"
      :keys="['news_id']">
      <!-- 标题插槽 -->
      <template slot="table-title" slot-scope="{ row }">
        <el-tooltip v-if="row.newsId" :enterable="false" effect="dark" :content="\`查看详情\`" placement="left">
          <a target="_blank" :href="handleGetDetailHref(row)" :title="row.title">{{formatTitle(row.title)}}</a>
          <!--<router-link :to="\`/portal/newsDetail/{row.newsId}\`" :title="row.title" target="_blank">{{formatTitle(row.title)}}</router-link>-->
        </el-tooltip>
        <span v-else>—</span>
      </template>
      <template v-slot:table-subtitle="{row}">
        <span :title="row.subtitle">{{ formatTitle(row.subtitle) }}</span>
      </template>
      <!--是否置顶插槽-->
      <template v-slot:table-top="{row}">
        <el-switch
          v-model="row.top"
          active-color="#13ce66"
          disabled
          inactive-color="#ff4949"/>
      </template>
      <template slot="top-left">
        <el-button
          icon="el-icon-plus"
          size="small"
          @click="handleShowNewsAdd"
          type="primary">添加</el-button>
      </template>
    </smart-table-crud>
  </div>
  `
}