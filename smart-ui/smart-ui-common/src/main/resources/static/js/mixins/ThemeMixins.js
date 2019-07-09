define(["require", "exports"], function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    const sideBarTheme = {
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
    exports.default = {
        computed: {
            getBus() {
                return busVue;
            },
            getTopColor() {
                return this.getBus.theme.topColor;
            },
            getSideBarTheme() {
                return sideBarTheme[this.getBus.theme.menuTheme];
            },
            getTopTextColor() {
                return sideBarTheme.dark['text-color'];
            },
            topActiveTextColor() {
                const isDark = true;
                return isDark ? '#ffd04b' : '#00008B';
            }
        }
    };
});
