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
    var OrganDetail = (function (_super) {
        __extends(OrganDetail, _super);
        function OrganDetail() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        OrganDetail.prototype.mixins = function () {
            return [
                new MessageMixins_1.default().build()
            ];
        };
        OrganDetail.prototype.props = function () {
            return {
                organId: {
                    type: String
                }
            };
        };
        OrganDetail.prototype.data = function () {
            return {
                organ: {}
            };
        };
        OrganDetail.prototype.mounted = function () {
            var $this = this;
            if ($this.organId) {
                $this.load();
            }
        };
        OrganDetail.prototype.watch = function () {
            return {
                organId: function (_new, old) {
                    if (_new !== old) {
                        this.load();
                    }
                }
            };
        };
        OrganDetail.prototype.methods = function () {
            return {
                load: function () {
                    var _this = this;
                    ApiService_1.default.postAjax('sys/organ/get', { organId: this.organId })
                        .then(function (data) {
                        console.log(data);
                        _this.organ = data;
                    }).catch(function (error) {
                        _this.errorMessage('获取组织详情失败', error);
                    });
                }
            };
        };
        OrganDetail.prototype.template = function () {
            return "\n    <table>\n      <tr>\n        <td>abc</td>\n      </tr>\n    </table>\n    ";
        };
        return OrganDetail;
    }(ComponentBuilder_1.default));
    exports.default = OrganDetail;
});
