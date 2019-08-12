import PageBuilder from 'PageBuilder';
export class Error403 extends PageBuilder {
    build() {
        return page;
    }
}
const page = {
    template: `
  <div style="padding: 100px 0 100px 0">
    <div class="error-container">
      <el-button size="middle" type="primary">返回</el-button>
    </div>
  </div>
  `
};
