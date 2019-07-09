define(["require", "exports", "system/layout/sidebar/Link", "system/layout/sidebar/Item", "mixins/ThemeMixins"], function (require, exports, Link_1, Item_1, ThemeMixins_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    exports.default = {
        components: {
            'app-link': Link_1.default,
            'menu-item': Item_1.default
        },
        mixins: [
            ThemeMixins_1.default
        ],
        props: {
            item: {
                required: true
            },
            isNest: {
                type: Boolean,
                default: false
            }
        },
        computed: {
            computedActiveIndex() {
                return this.$parent.activeIndex;
            }
        },
        methods: {
            handleOpenMenu(menu) {
                this.getBus.addMenu(menu);
            }
        },
        template: `
  <div class="menu-wrapper">
    <template v-if="!item.isCatalog">
      <app-link
        @click.native="handleOpenMenu(item)"
        :to="item.path">
        <el-tooltip :ref="item.id + 'tip'" manual class="item" effect="dark" :content="item.name" placement="right">
          <el-menu-item :index="item.id" :class="{'submenu-title-noDropdown': !isNest}">
            <menu-item
              :color="getTopTextColor"
              :activeColor="topActiveTextColor"
              :icon="item.icon"
              :active="computedActiveIndex === item.id"
              :title="item.name"/>
          </el-menu-item>
        </el-tooltip>
      </app-link>
    </template>
    <el-submenu v-else :index="item.id">
      <template slot="title">
        <menu-item
          :color="getTopTextColor"
          :activeColor="topActiveTextColor"
          :icon="item.icon"
          :active="computedActiveIndex === item.id"
          :title="item.name"/>
      </template>
      <template
        v-for="child in item.children">
        <sidebar-item
          class="nest-menu"
          :is-nest="true"
          :item="child"
          :key="child.id"></sidebar-item>
      </template>
    </el-submenu>
  </div>
  `
    };
});
