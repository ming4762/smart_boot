define(["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.default = {
        data() {
            return {
                menuPath: {}
            };
        },
        computed: {
            getBus() {
                return busVue;
            },
            computedOpenMenuList() {
                return this.getBus.openMenuList;
            },
            computedActiveMenu() {
                return this.getBus.activeMenu;
            }
        },
        methods: {
            getMenuPath(menu) {
                if (menu.id === this.computedActiveMenu.id) {
                    const path = menu.path;
                    if (!this.menuPath[menu.id]) {
                        this.menuPath[menu.id] = path;
                    }
                    return path;
                }
                else {
                    return this.menuPath[menu.id];
                }
            }
        },
        template: `
  <div class="page-container">
    <iframe
      :class="menu.id === computedActiveMenu.id ? 'active' : ''"
      class="animation-fade page-frame"
      v-for="menu in computedOpenMenuList"
      :src="getMenuPath(menu)"
      :key="menu.id"/>
  </div>
  `
    };
});