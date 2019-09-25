import ScrollPane from './ScrollPane.js';
export default {
    components: {
        'scroll-pane': ScrollPane
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
        v-for="(menu, index) in computedOpenMenuList"
        :class="isActive(menu)?'active':''"
        class="tags-view-item"
        @contextmenu.prevent.native="handleOpenOperate(menu, $event)"
        :key="menu.path">
        {{menu.name}}
        <!--第一菜单不显示关闭按钮-->
        <span v-if="index !== 0" class="el-icon-close" @click.prevent.stop="handleCloseSelectedMenu(menu)" />
      </a>
    </scroll-pane>
  </div>
  `
};
