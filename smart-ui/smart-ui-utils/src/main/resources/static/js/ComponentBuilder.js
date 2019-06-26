define(["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var ComponentBuilder = (function () {
        function ComponentBuilder() {
        }
        ComponentBuilder.prototype.data = function () {
            return {};
        };
        ComponentBuilder.prototype.props = function () {
            return {};
        };
        ComponentBuilder.prototype.template = function () {
            return '';
        };
        ComponentBuilder.prototype.methods = function () {
            return {};
        };
        ComponentBuilder.prototype.components = function () {
            return {};
        };
        ComponentBuilder.prototype.computed = function () {
            return {};
        };
        ComponentBuilder.prototype.mixins = function () {
            return [];
        };
        ComponentBuilder.prototype.watch = function () { };
        ComponentBuilder.prototype.created = function () { };
        ComponentBuilder.prototype.build = function () {
            var component = {
                template: this.template(),
                data: this.data,
                methods: this.methods(),
                components: this.components(),
                computed: this.computed(),
                mixins: this.mixins(),
                props: this.props(),
                watch: this.watch()
            };
            if (this['beforeMount']) {
                component['beforeMount'] = this['beforeMount'];
            }
            if (this['created']) {
                component['created'] = this['created'];
            }
            if (this['mounted']) {
                component['mounted'] = this['mounted'];
            }
            if (this['beforeUpdate']) {
                component['beforeUpdate'] = this['beforeUpdate'];
            }
            return component;
        };
        return ComponentBuilder;
    }());
    exports.default = ComponentBuilder;
});
