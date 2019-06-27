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
define(["require", "exports", "ComponentBuilder", "utils/ApiService", "mixins/MessageMixins"], function (require, exports, ComponentBuilder_1, ApiService_1, MessageMixins_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var OrganTree = (function (_super) {
        __extends(OrganTree, _super);
        function OrganTree() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        OrganTree.prototype.mixins = function () {
            return [
                new MessageMixins_1.default().build()
            ];
        };
        OrganTree.prototype.data = function () {
            return {
                organTreeData: []
            };
        };
        OrganTree.prototype.mounted = function () {
            var $this = this;
            $this.load();
        };
        OrganTree.prototype.computed = function () {
            return {
                computedOrganTreeData: function () {
                    return [
                        {
                            id: '0',
                            text: '组织根',
                            object: {
                                organId: '0',
                            },
                            children: this.organTreeData
                        }
                    ];
                }
            };
        };
        OrganTree.prototype.methods = function () {
            return {
                load: function () {
                    var _this = this;
                    ApiService_1.default.postAjax('sys/organ/listTree', [])
                        .then(function (data) {
                        _this.organTreeData = data['0'];
                    }).catch(function (error) {
                        _this.errorMessage('加载部门数据失败', error);
                    });
                }
            };
        };
        OrganTree.prototype.template = function () {
            return "\n    <el-tree\n      v-on=\"$listeners\"\n      :expand-on-click-node=\"false\"\n      node-key=\"organId\"\n      :default-expanded-keys=\"['0']\"\n      :data=\"computedOrganTreeData\">\n      <template v-slot=\"{data}\">\n        <div>\n          <i class=\"el-icon-school\"></i>\n          <span class=\"el-tree-node__label\">{{data.text}}</span>\n        </div>\n      </template>\n    </el-tree>\n    ";
        };
        return OrganTree;
    }(ComponentBuilder_1.default));
    exports.default = OrganTree;
});
