import CommonUtil from './CommonUtils.js';
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
};
window['smartModuleLoader'] = (moduleName, callback) => {
    const module = moduleMap[moduleName];
    if (!module) {
        console.warn('模块加载失败，未找到模块');
    }
    if (module['css'] && module['css'].length > 0) {
        CommonUtil.loadCSS(module['css']);
    }
    const jsList = module['js'];
    return CommonUtil.loadJS(jsList)
        .then(() => {
        if (callback) {
            callback();
        }
    });
};
