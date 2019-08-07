define(["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    const MaterialList = {
        template: `
  <div>
    <el-card></el-card>
  </div>
  `
    };
    exports.default = {
        components: {
            'material-list': MaterialList
        },
        methods: {
            handleSearch() {
                console.log(this);
            },
            handleAdd() {
                console.log(this);
            }
        },
        template: `
  <div>
    <div style="text-align: right">
      <div style="float: left;" class="material-top-div">
        <h3 style="font-weight: 400; font-size: 20px;">图文素材（共0条）</h3>
      </div>
      <div class="material-top-div">
        <el-input style="width: 240px" placeholder="搜索图文素材">
          <el-button slot="append" icon="el-icon-search" @click="handleSearch"></el-button>
        </el-input>
        <el-button type="primary" @click="handleAdd">新建图文素材</el-button>
      </div>
    </div>
    <div><material-list/></div>
  </div>
  
  `
    };
});
