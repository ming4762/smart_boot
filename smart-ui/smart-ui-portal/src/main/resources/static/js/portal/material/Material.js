import PageBuilder from 'PageBuilder';
import MaterialGT from 'portal/material/MaterialGT';
export class Material extends PageBuilder {
    build() {
        return page;
    }
}
const page = {
    components: {
        'material-gt': MaterialGT
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
