define(["require", "exports", "mixins/ThemeMixins", "system/layout/sidebar/Item", "system/layout/navbar/NavbarButton"], function (require, exports, ThemeMixins_1, Item_1, NavbarButton_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.default = {
        components: {
            'menu-item': Item_1.default,
            'navbar-button': NavbarButton_1.default
        },
        data() {
            return {
                activeIndex: '',
                sidebar: {}
            };
        },
        mixins: [
            ThemeMixins_1.default
        ],
        computed: {
            getIsActive() {
                return this.getBus.sidebar.opened === true;
            },
            getBus() {
                return busVue;
            },
            computedStyle() {
                return `fill: ${this.getTopTextColor}`;
            },
            getTopDivStyle() {
                return 'background-color:' + this.getBus.theme.topColor;
            },
            computedUserMenuList() {
                return this.getBus.userMenuList;
            },
            computedActiveMenu() {
                return this.getBus.activeMenu;
            }
        },
        watch: {
            'computedActiveMenu': function (_new, old) {
                if (_new && _new.topId !== old.topId) {
                    this.setActiveTopMenu();
                }
            }
        },
        mounted() {
        },
        methods: {
            handleMenuSelectEvent(index) {
                this.activeIndex = index;
            },
            handleToggleClick() {
                this.getBus.sidebar.opened = !this.getBus.sidebar.opened;
            },
            handleOpenMenu(menu) {
                this.getBus.setActiveTopMenu(menu);
            },
            getDefaultTopMenuId() {
                if (this.computedUserMenuList.length > 0) {
                    const menu = this.computedUserMenuList[0];
                    if (menu) {
                        return menu.topId;
                    }
                }
                return '';
            },
            setActiveTopMenu() {
                const activeTopMenu = this.getBus.activeTopMenu;
                const activeTopId = this.computedActiveMenu.topId;
                if (activeTopMenu.id !== activeTopId) {
                    const userMenuList = this.getBus.userMenuList;
                    for (let i in userMenuList) {
                        if (activeTopId === userMenuList[i].topId) {
                            this.getBus.setActiveTopMenu(userMenuList[i]);
                            break;
                        }
                    }
                }
            }
        },
        template: `
  <div :style="getTopDivStyle" class="navbar-outer-a">
    <el-menu
      ref="topMenu"
      :default-active="computedActiveMenu.topId"
      :background-color="getTopColor"
      :text-color="getTopTextColor"
      :active-text-color="topActiveTextColor"
      class="navbar"
      @select="handleMenuSelectEvent"
      mode="horizontal">
      <div class="hamburger-container">
        <svg
            :class="{'is-active':getIsActive}"
            t="1492500959545"
            class="hamburger"
            :style="computedStyle"
            viewBox="0 0 1024 1024"
            version="1.1"
            xmlns="http://www.w3.org/2000/svg"
            p-id="1691"
            xmlns:xlink="http://www.w3.org/1999/xlink"
            width="64"
            height="64"
            @click="handleToggleClick">
          <path
              d="M966.8023 568.849776 57.196677 568.849776c-31.397081 0-56.850799-25.452695-56.850799-56.850799l0 0c0-31.397081 25.452695-56.849776 56.850799-56.849776l909.605623 0c31.397081 0 56.849776 25.452695 56.849776 56.849776l0 0C1023.653099 543.397081 998.200404 568.849776 966.8023 568.849776z"
              p-id="1692" />
          <path
              d="M966.8023 881.527125 57.196677 881.527125c-31.397081 0-56.850799-25.452695-56.850799-56.849776l0 0c0-31.397081 25.452695-56.849776 56.850799-56.849776l909.605623 0c31.397081 0 56.849776 25.452695 56.849776 56.849776l0 0C1023.653099 856.07443 998.200404 881.527125 966.8023 881.527125z"
              p-id="1693" />
          <path
              d="M966.8023 256.17345 57.196677 256.17345c-31.397081 0-56.850799-25.452695-56.850799-56.849776l0 0c0-31.397081 25.452695-56.850799 56.850799-56.850799l909.605623 0c31.397081 0 56.849776 25.452695 56.849776 56.850799l0 0C1023.653099 230.720755 998.200404 256.17345 966.8023 256.17345z"
              p-id="1694" />
        </svg>
      </div>
      <!--遍历菜单-->
      <el-menu-item
        :key="menu.id"
        :index="menu.id"
        @click="handleOpenMenu(menu)"
        v-for="menu in computedUserMenuList">
        <menu-item
          :color="getTopTextColor"
          :active="computedActiveMenu.topId === menu.id"
          :icon="menu.icon"
          :activeColor="topActiveTextColor"
          :title="menu.name"/>
      </el-menu-item>
    </el-menu>
    <!--顶部按钮列-->
    <navbar-button class="navbar-button-container"/>
  </div>
  `
    };
});
