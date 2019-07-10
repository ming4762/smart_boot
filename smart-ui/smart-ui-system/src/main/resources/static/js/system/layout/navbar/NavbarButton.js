define(["require", "exports", "utils/CommonUtils"], function (require, exports, CommonUtils_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.default = {
        data() {
            return {
                buttonList: [],
                isFullScreen: false
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
        methods: {
            handleClickMessage() {
            },
            handleClickFullScreen() {
                this.isFullScreen ? CommonUtils_1.default.exitFullscreen() : CommonUtils_1.default.fullScreen();
                this.isFullScreen = !this.isFullScreen;
            },
            handleClickUser() {
            }
        },
        template: `
  <div>
    <ul class="full-height navbar-button-list">
      <li
        v-for="(button, i) in buttonList"
        @click="button.handler"
        class="navbar-button-li"
        :key="i">
        <el-tooltip
          :content="button.title"
          placement="top">
          <el-dropdown v-if=""></el-dropdown>
          <a :class="button.icon"></a>
        </el-tooltip>
      </li>
    </ul>  
  </div>
  `
    };
});
