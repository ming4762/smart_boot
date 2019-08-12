import ThemeMixins from '../../../mixins/ThemeMixins.js';
import ValidateUtils from '../../../utils/ValidateUtils.js';
export default {
    mixins: [
        ThemeMixins
    ],
    data() {
        return {
            activeIndex: ''
        };
    },
    computed: {
        getBus() {
            return busVue;
        },
        getSidebarHeaderStyle() {
            return `background-color:${this.getTopColor}`;
        },
        computedMenuList() {
            const activeTopMenu = this.getBus.activeTopMenu;
            if (ValidateUtils.validateNull(activeTopMenu)) {
                return [];
            }
            else {
                return activeTopMenu.children;
            }
        },
        computedIsCollapse() {
            return !this.getBus.sidebar.opened;
        },
        computedActiveMenu() {
            return this.getBus.activeMenu;
        },
        computedActiveIndex() {
            const activeMenu = this.getBus.activeMenu;
            return activeMenu ? activeMenu.id : '';
        }
    },
    template: `
  <el-scrollbar wrap-class="scrollbar-wrapper">
    <div class="left-outer">
      <div :style="getSidebarHeaderStyle" class="left-header">
      </div>
      <el-menu
        class="left-menu"
        :collapse="computedIsCollapse"
        :show-timeout="200"
        :background-color="getSideBarTheme['background-color']"
        :text-color="getSideBarTheme['text-color']"
        :active-text-color="getSideBarTheme['active-text-color']"
        mode="vertical"
        :default-active="computedActiveIndex">
        <sidebar-item
          :key="menu.id"
          :item="menu"
          v-for="menu in computedMenuList"/>
      </el-menu>
    </div>
  </el-scrollbar>
  `
};
