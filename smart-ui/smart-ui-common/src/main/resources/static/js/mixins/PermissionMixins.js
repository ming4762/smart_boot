import StoreUtil from '../utils/StoreUtil.js';
import { STORE_KEYS } from '../Constants.js';
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
export default {
    computed: {
        computedUserPermission() {
            return StoreUtil.getStore(STORE_KEYS.USER_PREMISSION) || [];
        }
    },
    methods: {
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
    }
};
