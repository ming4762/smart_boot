define(["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    /**
     * 组件构造器
     */
    var ComponentBuilder = /** @class */ (function () {
        function ComponentBuilder() {
        }
        /**
         * 数据构造方法
         */
        ComponentBuilder.prototype.data = function () {
            return {};
        };
        /**
         * 模板构造器
         */
        ComponentBuilder.prototype.template = function () {
            return '';
        };
        /**
         * 方法
         */
        ComponentBuilder.prototype.methods = function () {
            return {};
        };
        /**
         * 组件
         */
        ComponentBuilder.prototype.components = function () {
            return {};
        };
        /**
         * 计算属性
         */
        ComponentBuilder.prototype.computed = function () {
            return {};
        };
        /**
         * 混入
         */
        ComponentBuilder.prototype.mixins = function () {
            return [];
        };
        /**
         * 生命周期钩子创建完毕状态
         */
        ComponentBuilder.prototype.created = function () { };
        /**
         * 创建组件
         */
        ComponentBuilder.prototype.build = function () {
            return {
                template: this.template(),
                data: this.data,
                methods: this.methods(),
                created: this.created,
                components: this.components(),
                computed: this.computed(),
                mixins: this.mixins()
            };
        };
        return ComponentBuilder;
    }());
    exports.default = ComponentBuilder;
});
