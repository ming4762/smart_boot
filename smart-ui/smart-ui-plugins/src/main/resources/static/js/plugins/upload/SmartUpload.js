export default {
    props: {
        action: String,
        type: { type: String, required: true },
        data: { type: Object, default: () => { return {}; } },
        acceptExtension: String,
        beforeUpload: Function
    },
    data() {
        return {
            uploadParameter: {}
        };
    },
    created() {
        this.uploadParameter = Object.assign(this.data, { type: this.type });
    },
    computed: {
        computedUploadUrl() {
            if (this.action) {
                return this.action;
            }
            else {
                return `${localStorage.getItem('API_URL')}/public/file/upload`;
            }
        }
    },
    methods: {
        handleBeforeUpload(file) {
            if (this.acceptExtension) {
                if (!this.fileInAcceptExtension(file.name, this.acceptExtension)) {
                    this.$message.error(`只能上传${this.acceptExtension}文件`);
                    return false;
                }
            }
            return this.beforeUpload ? this.beforeUpload(file) : true;
        },
        fileInAcceptExtension(filename, acceptExtension) {
            const acceptList = acceptExtension.split(',');
            const filenameS = filename.split('.');
            const extension = filenameS[filenameS.length - 1];
            return acceptList.indexOf(extension) !== -1;
        },
        submit() {
            this.$refs['upload'].submit();
        },
        getFileList() {
            return this.$refs['upload'].uploadFiles;
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
};
