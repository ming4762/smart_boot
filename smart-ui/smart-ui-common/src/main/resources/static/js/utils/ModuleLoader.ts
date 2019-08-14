// @ts-ignore
import CommonUtil from './CommonUtils.js'

const moduleMap = {
  'smart-table': {
    js: [
        '/js/plugins/vue-element-table/vue-element-table.umd.min.js'
    ],
    css: [
        '/js/plugins/vue-element-table/vue-element-table.css'
    ]
  },
  'vue-echarts': {
    js: ['/js/plugins/vue-echarts/vue-echarts.umd.min.js']
  }
}

/**
 * 模块加载器
 * @param moduleName
 */
window['smartModuleLoader'] = (moduleName: string, callback: Function): Promise<any> => {
  // 获取模块信息
  const module = moduleMap[moduleName]
  if (!module) {
    console.warn('模块加载失败，未找到模块')
  }
  // 加载css
  if (module['css'] && module['css'].length > 0) {
    CommonUtil.loadCSS(module['css'])
  }
  // 记载js
  const jsList = module['js']
  return CommonUtil.loadJS(jsList)
      .then(() => {
        if (callback) {
          callback()
        }
      })
}
