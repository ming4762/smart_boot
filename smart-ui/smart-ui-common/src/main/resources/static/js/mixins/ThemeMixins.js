var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
        return extendStatics(d, b);
    }
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
define(["require", "exports", "ComponentBuilder"], function (require, exports, ComponentBuilder_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var sideBarTheme = {
        dark: {
            'background-color': '#304156',
            'button-color': '#ffffff',
            'text-color': '#bfcbd9',
            'active-text-color': '#409EFF'
        },
        light: {
            'background-color': '#DCDCDC',
            'button-color': '#606266',
            'text-color': '#606266',
            'active-text-color': '#4391f4'
        }
    };
    var ThemeMixins = (function (_super) {
        __extends(ThemeMixins, _super);
        function ThemeMixins() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        ThemeMixins.prototype.computed = function () {
            return {
                getBus: function () {
                    return busVue;
                },
                getTopColor: function () {
                    return this.getBus.theme.topColor;
                },
                getSideBarTheme: function () {
                    return sideBarTheme[this.getBus.theme.menuTheme];
                },
                getTopTextColor: function () {
                    return sideBarTheme.dark['text-color'];
                }
            };
        };
        return ThemeMixins;
    }(ComponentBuilder_1.default));
    exports.default = ThemeMixins;
});
