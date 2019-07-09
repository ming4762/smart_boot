define(["require", "exports", "ComponentBuilder", "utils/StoreUtil", "Constants"], function (require, exports, ComponentBuilder_1, StoreUtil_1, Constants_1) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    const validateOne = (permissions, allPermissions) => {
        let result = false;
        for (let i in permissions) {
            if (allPermissions.indexOf(permissions[i]) !== -1) {
                result = true;
                break;
            }
        }
        return result;
    };
    const validateAll = (permissions, allPermissions) => {
        let result = true;
        for (let i in permissions) {
            if (allPermissions.indexOf(permissions[i]) === -1) {
                result = false;
                break;
            }
        }
        return result;
    };
    class PermissionMixins extends ComponentBuilder_1.default {
        computed() {
            return {
                computedUserPermission() {
                    return StoreUtil_1.default.getStore(Constants_1.STORE_KEYS.USER_PREMISSION) || [];
                }
            };
        }
        methods() {
            return {
                validatePermission(permission) {
                    if (permission === undefined || permission === null || permission === '') {
                        return true;
                    }
                    return this.computedUserPermission.indexOf(permission) !== -1;
                },
                validatePermissions(permissions, all) {
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
        }
    }
    exports.default = PermissionMixins;
});
