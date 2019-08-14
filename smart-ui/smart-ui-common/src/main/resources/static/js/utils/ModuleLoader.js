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
        js: [
            '/js/plugins/vue-element-table/vue-element-table.umd.min.js'
        ],
        css: [
            '/js/plugins/vue-element-table/vue-element-table.css'
        ]
    },
    'vue-echarts': {
        js: ['/js/plugins/vue-echarts/vue-echarts.umd.min.js']
    },
    'vue-arcgis': {
        js: ['/js/plugins/vue-arcgis/vue-arcgis2.umd.min.js'],
        css: ['/js/plugins/vue-arcgis/vue-arcgis2.css']
    }
};
const loadModule = (moduleName) => {
    const module = moduleMap[moduleName];
    if (!module) {
        console.warn('模块加载失败，未找到模块');
    }
    if (module['css'] && module['css'].length > 0) {
        CommonUtil.loadCSS(module['css']);
    }
    const jsList = module['js'];
    return CommonUtil.loadJS(jsList);
};
window['smartModuleLoader'] = (...moduleNames) => __awaiter(this, void 0, void 0, function* () {
    for (let moduleName of moduleNames) {
        yield loadModule(moduleName);
    }
});
