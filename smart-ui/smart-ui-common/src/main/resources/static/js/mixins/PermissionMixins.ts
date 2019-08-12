// @ts-ignore
import StoreUtil from '../utils/StoreUtil.js'
// @ts-ignore
import { STORE_KEYS } from '../Constants.js'
/**
 * 验证是否拥有一项权限
 * @param permissions 权限
 * @param allPermissions 所有权限
 */
const validateOne = (permissions: string[], allPermissions: string[]): boolean => {
  let result: boolean = false
  for (let i in permissions) {
    if (allPermissions.indexOf(permissions[i]) !== -1) {
      result = true
      break
    }
  }
  return result
}

/**
 * 验证是否拥有所有权限
 * @param permissions 权限
 * @param allPermissions 所有权限
 */
const validateAll = (permissions: string[], allPermissions: string[]): boolean => {
  let result: boolean = true
  for (let i in permissions) {
    if (allPermissions.indexOf(permissions[i]) === -1) {
      result = false
      break
    }
  }
  return result
}

export default {
  computed: {
    /**
     * 用户权限计算属性
     */
    computedUserPermission () {
      return StoreUtil.getStore(STORE_KEYS.USER_PREMISSION) || []
    }
  },
  methods: {
    /**
     * 验证是否拥有权限
     * @param permission
     */
    validatePermission (permission: string): boolean {
      if (permission === undefined || permission === null || permission === '') {
        return true
      }
      return this.computedUserPermission.indexOf(permission) !== -1
    },
    /**
     * 验证是否拥有权限
     * @param permissions 权限集合
     * @param all true 需要所有权限满足，false 一项权限满足
     */
    validatePermissions (permissions: Array<string>, all: boolean): boolean {
      if (permissions.length === 0) {
        return true
      } else if (all) {
        return validateAll(permissions, this.computedUserPermission)
      } else {
        return validateOne(permissions, this.computedUserPermission)
      }
    }
  }
}