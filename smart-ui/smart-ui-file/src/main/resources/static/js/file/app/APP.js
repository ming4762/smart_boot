import PageBuilder from '../../PageBuilder.js';
import ApiService from '../../utils/ApiService.js';
import LayoutMixins from '../../mixins/LayoutMixins.js';
import TimeUtils from '../../utils/TimeUtils.js';
import SmartUpload from '../../plugins/upload/SmartUpload.js';
import FileService from '../../utils/FileService.js';
ready(function () {
    smartModuleLoader('smart-table').then(() => {
        new APP().init();
    });
});
class APP extends PageBuilder {
    build() {
        return page;
    }
}
const APPVersion = {
    components: {
        SmartUpload
    },
    mixins: [
        LayoutMixins
    ],
    props: {
        appId: {
            type: String
        }
    },
    data() {
        return {
            apiService: ApiService,
            columnOptions: [
                {
                    prop: 'versionId',
                    label: '版本ID',
                    table: {
                        visible: false,
                        displayControl: false,
                        fixed: true
                    },
                    form: {
                        visible: false
                    }
                },
                {
                    prop: 'appId',
                    label: 'APPid',
                    table: {
                        visible: false,
                        displayControl: false
                    },
                    form: {
                        visible: false
                    }
                },
                {
                    prop: 'versionNumber',
                    label: '版本号',
                    table: {},
                    form: {
                        rules: true
                    }
                },
                {
                    prop: 'createTime',
                    label: '发布日期',
                    type: 'date',
                    table: {
                        formatter: (row) => TimeUtils.formatTime(row.createTime),
                        width: 180
                    },
                    form: {
                        visible: false
                    }
                },
                {
                    prop: 'description',
                    label: '描述',
                    table: {
                        minWidth: 220
                    },
                    form: {}
                },
                {
                    prop: 'fileId',
                    label: '文件ID',
                    table: {
                        visible: false,
                        displayControl: false
                    },
                    form: {}
                }
            ],
            defaultButtonConfig: {
                add: {
                    rowShow: false,
                },
                edit: {},
                delete: {}
            },
            number1: 0,
            number2: 0,
            number3: 1,
            apkFile: null
        };
    },
    watch: {
        appId() {
            this.$refs['table'].load();
        }
    },
    methods: {
        handleGoBack() {
            this.$emit('update:show', false);
        },
        handleQueryParameterFormatter(parameter) {
            if (this.appId) {
                parameter['appId@='] = this.appId;
            }
            return parameter;
        },
        handleBeforeUpload(file) {
            this.apkFile = file;
            return false;
        },
        saveUpdateHandler(url, addEditModel) {
            if (!this.appId)
                throw Error('app id为null，无法保存');
            const model = {
                versionNumber: `${this.number1}.${this.number2}.${this.number3}`,
                appId: this.appId
            };
            return ApiService.postWithFile('app/version/saveWithFile', [this.apkFile], Object.assign(model, addEditModel));
        },
        handleDownAPK(row) {
            const downloadUrl = FileService.getDownloadURL(row.fileId);
            window.open(downloadUrl);
        }
    },
    template: `
  <smart-table-crud
    ref="table"
    :saveUpdateHandler="saveUpdateHandler"
    :defaultButtonConfig="defaultButtonConfig"
    queryUrl="app/version/list"
    deleteUrl="app/version/batchDelete"
    saveUpdateUrl="app/version/saveUpdate"
    :keys="['versionId']"
    tableName="app版本"
    :height="computedTableHeight"
    :queryParameterFormatter="handleQueryParameterFormatter"
    :columnOptions="columnOptions"
    :apiService="apiService">
    <template v-slot:form-description="{model}">
      <el-form-item label="描述">
        <el-input
          type="textarea"
          placeholder="请输入内容"
          v-model="model.description"/>
      </el-form-item>
    </template>
    <!--版本号插槽-->
    <template v-slot:form-versionNumber="{model}">
      <el-form-item label="版本号">
        <el-input-number size="small" v-model="number1"></el-input-number>
        .
        <el-input-number size="small" v-model="number2"></el-input-number>
        .
        <el-input-number size="small" v-model="number3"></el-input-number>
      </el-form-item>
    </template>
    <template v-slot:form-fileId="{model}">
      <el-form-item label="上传文件">
        <span>{{apkFile ? apkFile.name : ''}}</span>
        <SmartUpload
          :limit="1"
          type="APP_APK"
          :beforeUpload="handleBeforeUpload"
          :multiple="false"
          acceptExtension="apk">
          <el-button size="small" type="primary">点击上传</el-button>
        </SmartUpload>
      </el-form-item>
    </template>
    <!-- 上方右侧侧按钮 -->
    <template slot="top-right">
      <el-tooltip class="item" effect="dark" content="返回" placement="top">
        <el-button
          circle
          @click="handleGoBack()"
          size="small"
          type="primary"
          icon="el-icon-back"></el-button>
      </el-tooltip>
    </template>
    <template v-slot:row-operation="{row}">
        <el-tooltip content="下载">
          <el-button
            type="primary"
            size="mini"
            @click="handleDownAPK(row)"
            icon="el-icon-download"/>
        </el-tooltip>
      </template>
  </smart-table-crud>
  `
};
const page = {
    components: {
        APPVersion
    },
    mixins: [
        LayoutMixins
    ],
    data() {
        return {
            showVersion: false,
            apiService: ApiService,
            currentApp: {},
            columnOptions: [
                {
                    prop: 'appId',
                    label: 'app id',
                    table: {
                        displayControl: false,
                        visible: false
                    },
                    form: {
                        visible: false
                    }
                },
                {
                    prop: 'appCode',
                    label: 'app编码',
                    table: {
                        fixed: true
                    },
                    form: {
                        rules: true
                    }
                },
                {
                    prop: 'appName',
                    label: '名称',
                    table: {
                        width: 220,
                        fixed: true
                    },
                    form: {
                        rules: true
                    }
                },
                {
                    prop: 'developer',
                    label: '开发者',
                    table: {},
                    form: {
                        rules: true
                    }
                },
                {
                    prop: 'createTime',
                    label: '发布时间',
                    table: {
                        formatter: (row) => TimeUtils.formatTime(row.createTime),
                        width: 180
                    },
                    form: {
                        visible: false
                    }
                },
                {
                    prop: 'type',
                    label: '类别',
                    table: {},
                    form: {
                        rules: true
                    }
                },
                {
                    prop: 'description',
                    label: '描述',
                    table: {
                        minWidth: 220
                    },
                    form: {}
                }
            ],
            defaultButtonConfig: {
                add: {
                    rowShow: false,
                },
                edit: {},
                delete: {}
            }
        };
    },
    methods: {
        handleShowVersion(row) {
            this.currentApp = row;
            this.showVersion = true;
        }
    },
    template: `
  <div style="padding: 15px">
    <APPVersion
      :appId="currentApp.appId"
      :show.sync="showVersion"
      v-show="showVersion"/>
    <smart-table-crud
      v-show="!showVersion"
      :defaultButtonConfig="defaultButtonConfig"
      queryUrl="app/list"
      deleteUrl="app/batchDelete"
      saveUpdateUrl="app/saveUpdate"
      :keys="['appId']"
      tableName="app"
      :height="computedTableHeight"
      :columnOptions="columnOptions"
      :apiService="apiService">
      <!--描述form插槽-->
      <template v-slot:form-description="{model}">
        <el-form-item label="描述">
          <el-input
            type="textarea"
            placeholder="请输入内容"
            v-model="model.description"/>
        </el-form-item>
      </template>
      
      <template v-slot:row-operation="{row}">
        <el-tooltip content="查看版本信息">
          <el-button
            type="primary"
            size="mini"
            @click="handleShowVersion(row)"
            icon="el-icon-s-grid"/>
        </el-tooltip>
      </template>
    </smart-table-crud>
  </div>
  `
};
