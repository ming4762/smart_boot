define(["require", "exports", "system/layout/TagsView/ScrollPane"], function (require, exports, ScrollPane_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.default = {
        components: {
            'scroll-pane': ScrollPane_1.default
        },
        computed: {
            getBus() {
                return busVue;
            },
            computedOpenMenuList() {
                return this.getBus.openMenuList;
            }
        },
        methods: {
            isActive(menu) {
                return menu.path === this.getBus.activeMenu.path;
            },
            handleOpenOperate(menu, event) {
                console.log(menu);
            },
            handleCloseSelectedMenu(menu) {
                this.getBus.deleteMenu(menu.path);
            },
            handleActiveMenu(menu) {
                this.getBus.addMenu(menu);
            }
        },
        template: `
  <div class="tags-view-container">
    <scroll-pane class="tags-view-wrapper" ref="scrollPane">
      <a
        @click="handleActiveMenu(menu)"
        v-for="menu in computedOpenMenuList"
        :class="isActive(menu)?'active':''"
        class="tags-view-item"
        @contextmenu.prevent.native="handleOpenOperate(menu, $event)"
        :key="menu.path">
        {{menu.name}}
        <span class="el-icon-close" @click.prevent.stop="handleCloseSelectedMenu(menu)" />
      </a>
    </scroll-pane>
  </div>
  `
    };
});