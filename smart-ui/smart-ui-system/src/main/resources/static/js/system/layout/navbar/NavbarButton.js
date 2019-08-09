define(["require", "exports", "utils/CommonUtils", "mixins/ThemeMixins", "mixins/MessageMixins", "utils/ApiService"], function (require, exports, CommonUtils_1, ThemeMixins_1, MessageMixins_1, ApiService_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.default = {
        mixins: [ThemeMixins_1.default, MessageMixins_1.default],
        data() {
            return {
                buttonList: [],
                isFullScreen: false,
                activeIndex: null
            };
        },
        created() {
            this.buttonList = [
                {
                    id: 'message',
                    title: '通知',
                    icon: 'el-icon-message-solid',
                    handler: this.handleClickMessage
                },
                {
                    title: '全屏',
                    id: 'fullScreen',
                    icon: 'el-icon-full-screen',
                    handler: this.handleClickFullScreen
                },
                {
                    id: 'user',
                    title: '用户',
                    icon: 'el-icon-user-solid',
                    handler: this.handleClickUser
                }
            ];
        },
        computed: {
            getBus() {
                return busVue;
            }
        },
        methods: {
            handleClickMessage() {
            },
            handleClickFullScreen() {
                this.isFullScreen ? CommonUtils_1.default.exitFullscreen() : CommonUtils_1.default.fullScreen();
                this.isFullScreen = !this.isFullScreen;
            },
            handleClickUser() {
            },
            handleSelect(index) {
                console.log(index);
                switch (index) {
                    case 'fullScreen':
                        this.handleClickFullScreen();
                        break;
                    case 'userlogout':
                        this.logout();
                        break;
                    case 'accountMessage':
                        this.handleShowAccountMessage();
                        break;
                }
            },
            logout() {
                this.$confirm('您确定要退出系统吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    return ApiService_1.default.logout();
                }).then(() => {
                    ApiService_1.default.goToLogin();
                }).catch(error => {
                    if (error !== 'cancel') {
                        this.errorMessage('退出时发生错误，请稍后重试', error);
                    }
                });
            },
            handleShowAccountMessage() {
                this.getBus.addMenu({
                    name: '账户信息',
                    path: '/ui/system/accountMessage'
                });
            }
        },
        template: `
  <div>
    <el-menu 
      :default-active="activeIndex"
      :background-color="getTopColor"
      :active-text-color="topActiveTextColor"
      :text-color="getTopTextColor"
      @select="handleSelect"
      mode="horizontal">
      <el-submenu index="user">
        <template slot="title">
          <i class="el-icon-user-solid"></i>
        </template>
        <el-menu-item index="accountMessage">账户信息</el-menu-item>
        <el-divider></el-divider>
        <el-menu-item index="userlogout">
          <i class="el-icon-switch-button"></i>
          退出
        </el-menu-item>
      </el-submenu>
      <el-tooltip placement="top" content="通知">
        <el-menu-item index="notice">
          <i class="el-icon-message-solid"></i>
        </el-menu-item>
      </el-tooltip>
      <el-tooltip placement="top" content="全屏">
        <el-menu-item index="fullScreen">
          <i class="el-icon-full-screen"></i>
        </el-menu-item>
      </el-tooltip>
    </el-menu>
    <!--<ul class="full-height navbar-button-list">-->
      <!--<li-->
        <!--v-for="(button, i) in buttonList"-->
        <!--@click="button.handler"-->
        <!--class="navbar-button-li"-->
        <!--:key="i">-->
        <!--<el-tooltip-->
          <!--:content="button.title"-->
          <!--placement="top">-->
          <!--<el-dropdown v-if=""></el-dropdown>-->
          <!--<a :class="button.icon"></a>-->
        <!--</el-tooltip>-->
      <!--</li>-->
    <!--</ul>  -->
  </div>
  `
    };
});
