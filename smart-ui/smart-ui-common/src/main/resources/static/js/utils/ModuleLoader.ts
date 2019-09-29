// @ts-ignore
import CommonUtil from './CommonUtils.js'

const moduleMap = {
  'smart-table': {
    name: 'vue-element-table',
    js: [
        'js/plugins/vue-element-table/vue-element-table.umd.min.js'
    ],
    css: [
        'js/plugins/vue-element-table/vue-element-table.css'
    ]
  },
  'vue-echarts': {
    name: 'vue-echarts',
    js: [
        'echarts/4.2.1/echarts.min.js',
        'js/plugins/vue-echarts/vue-echarts.umd.min.js'
    ]
  },
  'vue-arcgis': {
    name: 'vue-arcgis',
    js: ['js/plugins/vue-arcgis/vue-arcgis2.umd.min.js'],
    css: ['js/plugins/vue-arcgis/vue-arcgis2.css']
  },
  'vue-weui': {
    name: 'vue-weui',
    js: ['js/plugins/vue-weui/vue-weui.umd.min.js'],
    css: ['js/plugins/vue-weui/vue-weui.css']
  },
  // 加载弹出层组件
  layer: {
    name: 'layer',
    js: [
        'jquery/3.4.1/jquery-3.4.1.min.js',
        'layer/3.1.1/layer.js'
    ],
  }
}

/**
 * 获取路径
 * @param path
 */
const getPath = CommonUtil.withContextPath

/**
 * 加载模块
 * @param moduleName
 */
const loadModule = (moduleName: string): Promise<any> => {
  // 获取模块信息
  const module = moduleMap[moduleName]
  if (!module) {
    console.warn('模块加载失败，未找到模块')
  }
  if (window[module.name]) {
    // 模块已经加载
    return new Promise<any>(resolve => resolve(null))
  }
  // 加载css
  if (module['css'] && module['css'].length > 0) {
    const cssPaths = module['css'].map(item =>  {
      return getPath(item)
    })
    CommonUtil.loadCSS(...cssPaths)
  }
  // 加载js
  const jsList = module['js'].map(item => getPath(item))
  return CommonUtil.loadJS(...jsList)
}

/**
 * 模块加载器
 * TODO: 判断模块是否已经加载
 * @param moduleNames
 */
window['smartModuleLoader'] = async (...moduleNames: Array<string>): Promise<any> => {
  for (let moduleName of moduleNames) {
    // 判断是否已经加载
    await loadModule(moduleName)
  }
}
