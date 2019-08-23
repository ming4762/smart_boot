import ApiService from '../../utils/ApiService.js';
import MessageMixins from '../../mixins/MessageMixins.js';
import TimeUtils from '../../utils/TimeUtils.js';
import CommonUtils from '../../utils/CommonUtils.js';
import FileService from '../../utils/FileService.js';
const getPath = CommonUtils.withContextPath;
export default {
    mixins: [
        MessageMixins
    ],
    props: {
        newsId: {
            type: String,
            required: true
        }
    },
    data() {
        return {
            news: {},
            cssId: null,
            attachmentFileList: []
        };
    },
    watch: {
        newsId() {
            this.queryNewsDetail();
        }
    },
    created() {
        this.cssId = CommonUtils.addCSS(cssText);
    },
    beforeDestory() {
        if (this.cssId) {
            const cssNode = document.getElementById(this.cssId);
            cssNode.parentNode.removeChild(cssNode);
        }
    },
    mounted() {
        this.queryNewsDetail();
    },
    methods: {
        queryNewsDetail() {
            ApiService.postAjax('public/portal/news/queryDetail', this.newsId)
                .then(data => {
                this.news = data;
                this.queryAttachmentFile();
            }).catch(error => {
                this.errorMessage('加载新闻失败，请稍后重试', error);
            });
        },
        queryAttachmentFile() {
            if (this.news.attachmentFileIdList && this.news.attachmentFileIdList.length > 0) {
                ApiService.postAjax('public/file/list', {
                    'fileId@in': this.news.attachmentFileIdList
                }).then(data => {
                    this.attachmentFileList = data;
                }).catch(error => {
                    this.errorMessage('加载附件列表失败，请稍后重试', error);
                });
            }
        },
        formatTime(time) {
            return TimeUtils.formatDate(time);
        },
        handleDownload(fileId) {
            window.open(FileService.getDownloadURL(fileId));
        }
    },
    template: `
  <div class="news_body">
    <div class="news_content">
      <div class="news_oneColumn">
        <h1>{{news.title}}</h1>
        <div class="news_pages-date">
          {{formatTime(news.releaseTime)}}
          <span class="font">作者：{{news.author}}</span>
          <span class="font">{{news.commentNum}} 人评论</span>
          <span class="font">{{news.praiseNum}} 人点赞</span>
          <span class="font">浏览次数： {{news.readNum}}</span>
        </div>
        <div class="news_pages_content" v-html=news.content>
        </div>
        <div class="news_editor">责任编辑：{{news.author}}</div>
      </div>
      <div style="margin-top: 10px" v-if="attachmentFileList.length > 0">
        <span>附件：</span>
        <div
          style="padding-top: 5px"
          :key="file.fileId"
          v-for="file in attachmentFileList">
          <el-tooltip placement="right" content="点击下载">
            <el-link
              target="_blank"
              @click="handleDownload(file.fileId)"
              type="primary">{{file.fileName}}</el-link>
          </el-tooltip>
        </div>
      </div>
    </div>
  </div>
  `
};
const cssText = `
.news_content {
  margin: 0 auto;
  padding: 36px;
  background: #fff;
}
.news_body {
  width: 100%;
  height: auto;
  color: #333;
  font-family: "宋体",serif;
  background: #f2f2f2;
}
.news_oneColumn {
  padding: 10px 66px 38px 66px;
  border: 1px #dddddd solid;
}
.news_body h1 {
  line-height: 56px;
  font-family: "微软雅黑", "宋体";
  font-size: 38px;
  text-align: center;
  padding: 26px 0 26px 0;
  font-weight: normal;
}
.news_pages-date {
  position: relative;
  border-bottom: 1px #dcdcdc solid;
  padding-top: 7px;
  text-align: center;
  color: #666666;
  font-size: 14px;
  height: 35px;
  overflow: hidden;
}
.news_pages-date span.font,
.pages_print span.font {
  margin: 0 10px;
}
.news_pages_content {
  line-height: 30px;
  margin: 0;
  padding: 40px 0 40px 0;
  font-size: 16px;
}
.news_editor {
  padding-bottom: 30px;
  text-align: right;
  color: #888888;
  font-size: 14px;
}
`;
