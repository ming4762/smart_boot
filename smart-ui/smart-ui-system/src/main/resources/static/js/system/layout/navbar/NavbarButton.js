import CommonUtils from '../../../utils/CommonUtils.js';
import ThemeMixins from '../../../mixins/ThemeMixins.js';
import MessageMixins from '../../../mixins/MessageMixins.js';
import ApiService from '../../../utils/ApiService.js';
export default {
    mixins: [ThemeMixins, MessageMixins],
    data() {
        return {
            buttonList: [],
            isFullScreen: false,
            activeIndex: null,
            accountMessageUrl: '/ui/system/accountMessage'
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
            this.isFullScreen ? CommonUtils.exitFullscreen() : CommonUtils.fullScreen();
            this.isFullScreen = !this.isFullScreen;
        },
        handleClickUser() {
        },
        handleSelect(index) {
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
                case 'updatePassword':
                    this.handleUpdatePassword();
                    break;
            }
        },
        logout() {
            this.$confirm('您确定要退出系统吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                return ApiService.logout();
            }).then(() => {
                ApiService.toLoginPage();
            }).catch(error => {
                if (error !== 'cancel') {
                    this.errorMessage('退出时发生错误，请稍后重试', error);
                }
            });
        },
        handleShowAccountMessage() {
            this.goToAccountMessagePage(this.accountMessageUrl);
        },
        handleUpdatePassword() {
            this.goToAccountMessagePage(`${this.accountMessageUrl}?activeTab=password`);
        },
        goToAccountMessagePage(menuPath) {
            const menuId = 'accountMessage';
            let accountMessageMenu = null;
            for (let menu of this.getBus.openMenuList) {
                if (menu.id === menuId) {
                    accountMessageMenu = menu;
                    break;
                }
            }
            if (accountMessageMenu !== null) {
                accountMessageMenu.path = menuPath;
            }
            else {
                this.getBus.addMenu({
                    id: menuId,
                    name: '账户信息',
                    path: menuPath
                });
            }
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
        <el-menu-item index="updatePassword">修改密码</el-menu-item>
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
