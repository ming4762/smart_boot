// @ts-ignore
import PageBuilder from '../../PageBuilder.js'
// @ts-ignore
import CommonUtils from '../../utils/CommonUtils.js'

import NewsDetailComponennt from './NewsDetailComponennt.js'

const getPath = CommonUtils.withContextPath

declare const ready, newsId

ready(function () {
  // @ts-ignore
  new NewsDetailPage().init()
})

class NewsDetailPage extends PageBuilder {
  /**
   * 构建函数
   */
  protected build () {
    return page
  }
}

const page = {
  components: {
    NewsDetailComponennt
  },
  data () {
    return {
      newsId: newsId
    }
  },
  template: `
  <NewsDetailComponennt :newsId="newsId"/>
  `
}