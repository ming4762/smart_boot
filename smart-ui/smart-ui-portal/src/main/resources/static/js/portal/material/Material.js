define(["require", "exports", "PageBuilder", "portal/material/MaterialGT"], function (require, exports, PageBuilder_1, MaterialGT_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    class Material extends PageBuilder_1.default {
        build() {
            return page;
        }
    }
    exports.Material = Material;
    const page = {
        components: {
            'material-gt': MaterialGT_1.default
        },
        data() {
            return {
                activeName: 'materialGT'
            };
        },
        template: `
  <div style="height: 100%; background-color: #f6f8f9">
    <div style="padding: 15px; ">
      <el-tabs v-model="activeName">
        <el-tab-pane label="图文素材" name="materialGT"><material-gt/></el-tab-pane>
        <el-tab-pane label="图片" name="materialPic">图片</el-tab-pane>
      </el-tabs>
    </div>
  </div>
  `
    };
});
