var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
import CommonUtil from './CommonUtils.js';
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
        js: ['js/plugins/mobile/vue-weui/vue-weui.umd.min.js'],
        css: ['js/plugins/mobile/vue-weui/vue-weui.css']
    },
    'vue-amap': {
        name: 'vue-amap',
        js: ['js/plugins/mobile/vue-amap/vue-amap.umd.min.js'],
        css: ['js/plugins/mobile/vue-amap/vue-amap.css']
    },
    'swiper': {
        name: 'vue-weui-swiper',
        css: ['swiper/swiper.min.css'],
        js: ['swiper/swiper.min.js']
    },
    layer: {
        name: 'layer',
        js: [
            'jquery/3.4.1/jquery-3.4.1.min.js',
            'layer/3.1.1/layer.js'
        ],
    }
};
const getPath = CommonUtil.withContextPath;
const loadModule = (moduleName) => {
    const module = moduleMap[moduleName];
    if (!module) {
        console.warn('模块加载失败，未找到模块');
    }
    if (window[module.name]) {
        return new Promise(resolve => resolve(null));
    }
    if (module['css'] && module['css'].length > 0) {
        const cssPaths = module['css'].map(item => {
            return getPath(item);
        });
        CommonUtil.loadCSS(...cssPaths);
    }
    const jsList = module['js'].map(item => getPath(item));
    return CommonUtil.loadJS(...jsList);
};
window['loadMoules'] = [];
window['smartModuleLoader'] = (...moduleNames) => __awaiter(this, void 0, void 0, function* () {
    for (let moduleName of moduleNames) {
        if (window['loadMoules'].indexOf(moduleName) === -1) {
            yield loadModule(moduleName);
            window['loadMoules'].push(moduleName);
        }
        else {
            console.warn(`${moduleName}已加载，请勿重复加载`);
        }
    }
});
