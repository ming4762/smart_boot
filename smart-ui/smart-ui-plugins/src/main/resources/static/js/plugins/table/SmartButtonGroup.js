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
    /**
     * 顶部按钮组
     */
    var SmartButtonGroup = /** @class */ (function (_super) {
        __extends(SmartButtonGroup, _super);
        function SmartButtonGroup() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        SmartButtonGroup.prototype.props = function () {
            return {
                // 左侧solt是否在group内
                leftInGroup: {
                    type: Boolean,
                    default: true
                },
                // 是否有左侧按钮
                hasLeft: {
                    type: Boolean,
                    default: true
                },
                // 是否有右侧按钮
                hasRight: {
                    type: Boolean,
                    default: true
                },
                addShow: {
                    type: Boolean,
                    default: true
                },
                editShow: {
                    type: Boolean,
                    default: true
                },
                deleteShow: {
                    type: Boolean,
                    default: true
                }
            };
        };
        SmartButtonGroup.prototype.methods = function () {
            return {
                /**
                 * 点击按钮触发事件
                 */
                handleClickButtonGroup: function (ident) {
                    var listener = 'button-click';
                    if (this.$listeners[listener]) {
                        this.$emit(listener, ident);
                    }
                }
            };
        };
        SmartButtonGroup.prototype.template = function () {
            //TODO: 中文使用I18N
            return "\n    <div>\n      <div v-if=\"hasLeft\" class=\"cloud-table-left\">\n        <el-button-group>\n          <el-button\n            v-if=\"addShow\"\n            icon=\"el-icon-plus\"\n            size=\"small\"\n            @click=\"handleClickButtonGroup('add')\"\n            type=\"primary\">\u6DFB\u52A0</el-button>\n          <el-button\n            v-if=\"editShow\"\n            icon=\"el-icon-edit-outline\"\n            @click=\"handleClickButtonGroup('edit')\"\n            size=\"small\"\n            type=\"warning\">\u4FEE\u6539</el-button> \n          <el-button\n            v-if=\"deleteShow\"\n            icon=\"el-icon-delete\"\n            @click=\"handleClickButtonGroup('delete')\"\n            size=\"small\"\n            type=\"danger\">\u5220\u9664</el-button>\n          <template v-if=\"leftInGroup\">\n            <slot name=\"buttonLeft\"></slot>\n          </template>  \n        </el-button-group>\n        <template v-if=\"!leftInGroup\">\n          <slot name=\"buttonLeft\"></slot>\n        </template> \n      </div>\n      <div v-if=\"hasRight\" class=\"cloud-table-right\">\n        <el-tooltip effect=\"dark\" content=\"\u5237\u65B0\" placement=\"top\">\n          <el-button\n            size=\"small\"\n            icon=\"el-icon-refresh\"\n            @click=\"handleClickButtonGroup('refresh')\"\n            circle/>\n        </el-tooltip>\n        <el-tooltip effect=\"dark\" content=\"\u5217\u663E\u793A\u9690\u85CF\" placement=\"top\">\n          <el-button\n            size=\"small\"\n            icon=\"el-icon-menu\"\n            @click=\"handleClickButtonGroup('columnVisible')\"\n            circle/>\n        </el-tooltip>\n        <el-tooltip effect=\"dark\" content=\"\u641C\u7D22\" placement=\"top\">\n          <el-button\n            size=\"small\"\n            icon=\"el-icon-search\"\n            @click=\"handleClickButtonGroup('search')\"\n            circle/>\n        </el-tooltip>\n        <template>\n          <slot name=\"buttonRight\"></slot>\n        </template>\n      </div>\n</div>\n    ";
        };
        return SmartButtonGroup;
    }(ComponentBuilder_1.default));
    exports.default = SmartButtonGroup;
});
