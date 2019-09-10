import PageBuilder from '../PageBuilder.js';
import ApiService from '../utils/ApiService.js';
import LayoutMixins from '../mixins/LayoutMixins.js';
import TimeUtils from '../utils/TimeUtils.js';
import FileService from '../utils/FileService.js';
import SmartUpload from '../plugins/upload/SmartUpload.js';
ApiService.validateLogin();
ready(function () {
    smartModuleLoader('smart-table').then(() => {
        new FileList().init();
    });
});
class FileList extends PageBuilder {
    build() {
        return page;
    }
}
const page = {
    components: {
        SmartUpload
    },
    mixins: [
        LayoutMixins
    ],
    data() {
        return {
            apiService: ApiService,
            defaultButtonConfig: {
                add: {
                    rowShow: false
                },
                edit: {
                    rowShow: false,
                    topShow: false
                }
            },
            columnOptions: [
                {
                    prop: 'fileId',
                    label: '文件ID',
                    table: {
                        visible: false,
                        displayControl: false
                    },
                    form: {
                        visible: false
                    }
                },
                {
                    prop: 'fileName',
                    label: '文件名',
                    search: {
                        symbol: 'like'
                    },
                    table: {
                        width: 220,
                        fixed: true
                    },
                    form: {
                        visible: false
                    }
                },
                {
                    prop: 'createTime',
                    label: '创建时间',
                    table: {
                        width: 170,
                        sortable: true,
                        formatter: row => TimeUtils.formatTime(row.createTime)
                    },
                    form: {
                        visible: false
                    }
                },
                {
                    prop: 'createUserId',
                    label: '创建人员ID',
                    table: {
                        visible: false,
                        displayControl: false
                    },
                    form: {
                        visible: false
                    }
                },
                {
                    prop: 'type',
                    label: '类型',
                    search: {
                        symbol: 'like'
                    },
                    table: {
                        width: 150
                    },
                    form: {
                        span: 12,
                        rules: true,
                        defaultValue: 'TEMP'
                    }
                },
                {
                    prop: 'contentType',
                    label: '文件类型',
                    table: {
                        minWidth: 180
                    },
                    form: {
                        visible: false
                    }
                },
                {
                    prop: 'size',
                    label: '文件大小',
                    table: {
                        width: 160
                    },
                    form: {
                        visible: false
                    }
                },
                {
                    prop: 'md5',
                    label: 'md5',
                    table: {
                        visible: false
                    },
                    form: {
                        visible: false
                    }
                },
                {
                    prop: 'seq',
                    label: '序号',
                    type: 'number',
                    table: {
                        width: 100,
                        sortable: true
                    },
                    form: {
                        defaultValue: 1,
                        span: 12
                    }
                },
                {
                    prop: 'dbId',
                    label: '数据ID',
                    table: {
                        visible: false
                    },
                    form: {}
                }
            ],
            file: null
        };
    },
    methods: {
        handleDownload(file) {
            window.open(FileService.getDownloadURL(file.fileId));
        },
        handleBeforeUpload(file) {
            this.file = file;
            return false;
        },
        saveUpdateHandler(url, model) {
            return ApiService.postWithFile('file/batchUpload', [this.file], model);
        }
    },
    template: `
  <div style="padding: 15px">
    <smart-table-crud
      ref="table"
      defaultSortColumn="seq, createTime"
      defaultSortOrder="asc, desc"
      :saveUpdateHandler="saveUpdateHandler"
      :defaultButtonConfig="defaultButtonConfig"
      queryUrl="public/file/list"
      deleteUrl="file/batchDelete"
      saveUpdateUrl="public/file/saveUpdate"
      :keys="['fileId']"
      tableName="文件"
      :columnOptions ="columnOptions"
      :apiService="apiService"
      :height="computedTableHeight">
      <template v-slot:row-operation="{row}">
        <el-tooltip content="下载">
          <el-button
            type="primary"
            size="mini"
            @click="handleDownload(row)"
            icon="el-icon-download"/>
        </el-tooltip>
      </template>
      
      <!--文件上传-->
      <template v-slot:form-dbId="{model}">
        <el-form-item label="上传文件">
          <span>{{file ? file.name : ''}}</span>
          <SmartUpload
            :limit="1"
            :type="model.type"
            :beforeUpload="handleBeforeUpload"
            :multiple="false">
            <el-button size="small" type="primary">点击上传</el-button>
          </SmartUpload>
        </el-form-item>
      </template>
    </smart-table-crud>
  </div>
  `
};
