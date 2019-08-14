import PageBuilder from '../../PageBuilder.js';
import ApiService from '../../utils/ApiService.js';
import LayoutMixins from '../../mixins/LayoutMixins.js';
import ValidateUtils from '../../utils/ValidateUtils.js';
ready(function () {
    smartModuleLoader('smart-table').then(() => {
        new User().init();
    });
});
export class User extends PageBuilder {
    build() {
        return page;
    }
}
const validateMobile = (rule, value, callback) => {
    if (value && value.trim() !== '') {
        let result = ValidateUtils.validateMobile(value.trim());
        if (result[0]) {
            callback();
        }
        else {
            callback(new Error(result[1]));
        }
    }
    else {
        callback();
    }
};
const page = {
    components: {},
    mixins: [LayoutMixins],
    data() {
        return {
            apiService: ApiService,
            columnOptions: [
                {
                    prop: 'userId',
                    label: '用户Id',
                    form: {
                        visible: false
                    },
                    table: {
                        visible: false,
                        displayControl: false
                    }
                },
                {
                    prop: 'username',
                    label: '用户名',
                    form: {
                        span: 12,
                        rules: true
                    },
                    table: {
                        fixed: true
                    }
                },
                {
                    prop: 'name',
                    label: '姓名',
                    form: {
                        span: 12,
                        rules: true
                    },
                    table: {
                        width: 150,
                        fixed: true
                    }
                },
                {
                    prop: 'status',
                    label: '状态',
                    form: {
                        visible: true
                    },
                    table: {
                        slot: true
                    }
                }, {
                    prop: 'mobile',
                    label: '电话',
                    table: {
                        minWidth: 150
                    },
                    form: {
                        span: 12,
                        rules: [{
                                required: false,
                                tigger: 'blur',
                                validator: validateMobile
                            }]
                    }
                },
                {
                    prop: 'email',
                    label: '邮箱',
                    table: {
                        minWidth: 200,
                        slot: true
                    },
                    form: {
                        span: 12
                    }
                },
                {
                    prop: 'sex',
                    label: '性别',
                    type: 'radio',
                    table: {
                        formatter: (row, column, cellValue) => {
                            if (cellValue === 0) {
                                return '男';
                            }
                            if (cellValue === 1) {
                                return '女';
                            }
                        }
                    },
                    form: {
                        span: 12,
                        defaultValue: '0',
                        dicData: [{
                                label: '0',
                                value: '男'
                            }, {
                                label: '1',
                                value: '女'
                            }]
                    }
                },
                {
                    label: '序号',
                    prop: 'seq',
                    type: 'number',
                    table: {
                        sortable: true
                    },
                    form: {
                        span: 12,
                        defaultValue: 1
                    }
                }
            ],
            defaultButtonConfig: {
                add: {
                    rowShow: false,
                    permission: 'system:user:save'
                },
                edit: {
                    permission: 'system:user:update'
                },
                delete: {
                    permission: 'system:user:delete'
                }
            }
        };
    },
    methods: {
        formatStatusType(row) {
            let result = '';
            switch (row.status) {
                case '1':
                    result = 'success';
                    break;
                case '0':
                    result = 'danger';
                    break;
                case '2':
                    result = 'warning';
                    break;
            }
            return result;
        },
        formatStateValue(row) {
            let result = '';
            switch (row.status) {
                case '1':
                    result = '正常';
                    break;
                case '0':
                    result = '禁用';
                    break;
                case '2':
                    result = '锁定';
                    break;
            }
            return result;
        }
    },
    computed: {
        computedTableHeight() {
            return this.clientHeight - 30;
        }
    },
    template: `
    <div style="padding: 15px;">
      <smart-table-crud
        :defaultButtonConfig="defaultButtonConfig"
        queryUrl="sys/user/list"
        deleteUrl="sys/user/batchDelete"
        saveUpdateUrl="sys/user/saveUpdate"
        :keys="['userId']"
        tableName="系统用户" 
        :apiService="apiService"
        labelWidth="80px"
        :height="computedTableHeight"
        :columnOptions="columnOptions">
        <!-- 状态status插槽 -->
        <template slot="table-status" slot-scope="{ row }">
          <el-tag
            :type="formatStatusType(row)">{{formatStateValue(row)}}</el-tag>
        </template>
      </smart-table-crud>
    </div>
    `
};
