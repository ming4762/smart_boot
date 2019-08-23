import PageBuilder from '../../PageBuilder.js';
import CommonUtils from '../../utils/CommonUtils.js';
import NewsDetailComponennt from './NewsDetailComponennt.js';
const getPath = CommonUtils.withContextPath;
ready(function () {
    new NewsDetailPage().init();
});
class NewsDetailPage extends PageBuilder {
    build() {
        return page;
    }
}
const page = {
    components: {
        NewsDetailComponennt
    },
    data() {
        return {
            newsId: newsId
        };
    },
    template: `
  <NewsDetailComponennt :newsId="newsId"/>
  `
};
