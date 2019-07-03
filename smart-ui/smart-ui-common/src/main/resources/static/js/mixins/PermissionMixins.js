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
define(["require", "exports", "ComponentBuilder", "utils/StoreUtil", "Constants"], function (require, exports, ComponentBuilder_1, StoreUtil_1, Constants_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var validateOne = function (permissions, allPermissions) {
        var result = false;
        for (var i in permissions) {
            if (allPermissions.indexOf(permissions[i]) !== -1) {
                result = true;
                break;
            }
        }
        return result;
    };
    var validateAll = function (permissions, allPermissions) {
        var result = true;
        for (var i in permissions) {
            if (allPermissions.indexOf(permissions[i]) === -1) {
                result = false;
                break;
            }
        }
        return result;
    };
    var PermissionMixins = (function (_super) {
        __extends(PermissionMixins, _super);
        function PermissionMixins() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        PermissionMixins.prototype.computed = function () {
            return {
                computedUserPermission: function () {
                    return StoreUtil_1.default.getStore(Constants_1.STORE_KEYS.USER_PREMISSION) || [];
                }
            };
        };
        PermissionMixins.prototype.methods = function () {
            return {
                validatePermission: function (permission) {
                    if (permission === undefined || permission === null || permission === '') {
                        return true;
                    }
                    return this.computedUserPermission.indexOf(permission) !== -1;
                },
                validatePermissions: function (permissions, all) {
                    if (permissions.length === 0) {
                        return true;
                    }
                    else if (all) {
                        return validateAll(permissions, this.computedUserPermission);
                    }
                    else {
                        return validateOne(permissions, this.computedUserPermission);
                    }
                }
            };
        };
        return PermissionMixins;
    }(ComponentBuilder_1.default));
    exports.default = PermissionMixins;
});
