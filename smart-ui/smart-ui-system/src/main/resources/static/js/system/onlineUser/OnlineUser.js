import PageBuilder from '../../PageBuilder.js';
import SmartTableCRUD from '../../plugins/table/SmartTableCRUD.js';
import ApiService from '../../utils/ApiService.js';
import LayoutMixins from '../../mixins/LayoutMixins.js';
import MessageMixins from '../../mixins/MessageMixins.js';
ready(function () {
    new OnlineUser().init();
});
export class OnlineUser extends PageBuilder {
    build() {
        return page;
    }
}
const page = {
    components: {
        'smart-table-crud': SmartTableCRUD
    },
    mixins: [LayoutMixins, MessageMixins],
    data() {
        return {
            apiService: ApiService,
            columnOptions: [
                {
                    prop: 'username',
                    label: '用户名',
                    form: {},
                    table: {
                        formatter: (row) => row.user.username,
                        fixed: true
                    }
                },
                {
                    prop: 'name',
                    label: '姓名',
                    form: {},
                    table: {
                        fixed: true,
                        formatter: (row) => row.user.name,
                    }
                },
                {
                    prop: 'onlineNum',
                    label: '在线数',
                    form: {},
                    table: {
                        width: 80
                    }
                }
            ],
            defaultButtonConfig: {
                add: {
                    rowShow: false
                },
                edit: {
                    rowShow: false
                },
                delete: {
                    rowShow: false
                }
            }
        };
    },
    methods: {
        handleRemoveUser(userId) {
            this.$confirm('您确定要将该用户强制下线吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.showHideFullScreenloading(true);
                return ApiService.postAjax('auth/removeUser', [userId]);
            }).then(() => {
                this.showHideFullScreenloading(false);
                this.$refs['table'].load();
            }).catch(error => {
                this.showHideFullScreenloading(false);
                if (error !== 'cancel') {
                    this.errorMessage('强制下线失败，请稍后重置', error);
                }
            });
        }
    },
    template: `
  <div style="padding: 15px;">
    <smart-table-crud
      ref="table"
      :defaultButtonConfig="defaultButtonConfig"
      :hasTopLeft="false"
      queryUrl="auth/listOnlineUser"
      :paging="false"
      :keys="['userId']"
      :height="computedTableHeight"
      :columnOptions="columnOptions"
      tableName="在线用户" 
      :apiService="apiService"
      :opreaColumnWidth="100"
      labelWidth="80px">
      <template v-slot:row-operation="{row}">
        <el-tooltip effect="dark" content="强制下线" placement="top">
          <el-button @click="handleRemoveUser(row.user.userId)" icon="el-icon-scissors" type="danger" size="mini"/>
        </el-tooltip>
      </template>
      <!--<template v-slot:table-expand="{row}">-->
        <!--<el-table border :data="row.sessionList">-->
          <!--<el-table-column-->
            <!--type="selection"-->
            <!--width="40">-->
          <!--<el-table-column label="登录地址" prop="host"/>-->
          <!--<el-table-column label="登录时间" prop="startTime"/>-->
          <!--<el-table-column label="过期时间" prop="lastAccessTime"/>-->
          <!--<el-table-column label="上次访问时间" prop="lastUseTime"/>-->
          <!--<el-table-column label="有效期（毫秒）" prop="timeout"/>-->
        <!--</el-table>-->
      <!--</template>-->
    </smart-table-crud>
  </div>
  `
};
