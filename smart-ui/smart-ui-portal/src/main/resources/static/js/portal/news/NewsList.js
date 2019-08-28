import PageBuilder from '../../PageBuilder.js';
import CommonUtils from '../../utils/CommonUtils.js';
import ApiService from '../../utils/ApiService.js';
import LayoutMixins from '../../mixins/LayoutMixins.js';
import MessageMixins from '../../mixins/MessageMixins.js';
import TimeUtils from '../../utils/TimeUtils.js';
const getPath = CommonUtils.withContextPath;
ready(function () {
    smartModuleLoader('smart-table').then(() => {
        new NewsList().init();
    });
});
class NewsList extends PageBuilder {
    build() {
        return page;
    }
}
const page = {
    mixins: [
        LayoutMixins,
        MessageMixins
    ],
    props: {
        moduleId: String
    },
    data() {
        return {
            apiService: ApiService,
            moduleList: [],
            defaultButtonConfig: {
                add: {
                    rowShow: false,
                    topShow: false
                },
                edit: {
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
                    search: {
                        symbol: 'like'
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
                    search: {},
                    table: {
                        formatter: (row) => {
                            if (row.module) {
                                return row.module.moduleName;
                            }
                            return '-';
                        }
                    },
                    form: {}
                },
                {
                    label: '点赞数',
                    prop: 'praiseNum',
                    table: {},
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
        };
    },
    created() {
        this.loadModule();
    },
    methods: {
        titleTooltip() {
            return;
        },
        formatTitle(title) {
            if (title && title.length > 9) {
                return title.substring(0, 8) + '...';
            }
            return title;
        },
        handleQueryParameterFormatter(parameter) {
            const para = {
                withContent: false
            };
            if (this.moduleId) {
                para['moduleId@='] = this.moduleId;
            }
            return Object.assign(para, parameter);
        },
        handleShowNewsAdd() {
            window.open(getPath('ui/common?title=添加新闻&page=portal/news/NewsAddEdit'));
        },
        handleGetDetailHref(newsId) {
            return getPath(`ui/portal/newsDetail/${newsId}`);
        },
        handleEdit(newsId) {
            window.open(getPath(`ui/common?title=添加新闻&page=portal/news/NewsAddEdit&pageParameter=${newsId}`));
        },
        loadModule() {
            ApiService.postAjax('portal/module/list', {})
                .then(data => {
                this.moduleList = data;
            }).catch(error => {
                this.errorMessage('加载模块数据失败', error);
            });
        }
    },
    template: `
  <div style="padding: 15px;">
    <smart-table-crud
      ref="table"
      queryUrl="public/portal/news/listWthAll"
      deleteUrl="portal/news/batchDelete"
      saveUpdateUrl="portal/news/saveUpdate"
      :height="computedTableHeight"
      tableName="新闻"
      :apiService="apiService"
      :columnOptions="columnOptions"
      :queryParameterFormatter="handleQueryParameterFormatter"
      :defaultButtonConfig="defaultButtonConfig"
      :keys="['newsId']">
      <!-- 标题插槽 -->
      <template slot="table-title" slot-scope="{ row }">
        <el-tooltip v-if="row.newsId" :enterable="false" effect="dark" :content="\`查看详情\`" placement="left">
          <a target="_blank" :href="handleGetDetailHref(row.newsId)" :title="row.title">{{formatTitle(row.title)}}</a>
          <!--<router-link :to="\`/portal/newsDetail/{row.newsId}\`" :title="row.title" target="_blank">{{formatTitle(row.title)}}</router-link>-->
        </el-tooltip>
        <span v-else>—</span>
      </template>
      <template v-slot:table-subtitle="{row}">
        <span :title="row.subtitle">{{ formatTitle(row.subtitle) }}</span>
      </template>
      <template v-slot:row-operation="{row}">
        <el-tooltip placement="top" content="修改">
          <el-button
            size="mini"
            icon="el-icon-edit-outline"
            @click="handleEdit(row.newsId)"
            type="warning"></el-button>
        </el-tooltip>
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
      <!--模块搜索插槽-->
      <template v-slot:search-moduleId="{model}">
        <el-form-item label="模块">
          <el-select v-model="model.moduleId">
            <el-option
              :key="item.moduleId"
              :value="item.moduleId"
              :label="item.moduleName"
              v-for="item in moduleList"/>
          </el-select>
        </el-form-item>
      </template>
    </smart-table-crud>
  </div>
  `
};
