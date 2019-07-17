define(["require", "exports", "PageBuilder"], function (require, exports, PageBuilder_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    class Error403 extends PageBuilder_1.default {
        build() {
            return page;
        }
    }
    exports.Error403 = Error403;
    const page = {
        template: `
  <div style="padding: 100px 0 100px 0">
    <div class="error-container">
      <el-button size="middle" type="primary">返回</el-button>
    </div>
  </div>
  `
    };
});
