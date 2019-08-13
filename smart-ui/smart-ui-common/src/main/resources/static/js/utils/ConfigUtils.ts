import { STORE_KEYS } from '../Constants.js'
import ApiService from './ApiService.js'
// @ts-ignore
import StoreUtil from './StoreUtil.js'

export default class ConfigUtils {

  /**
   * 设置配置
   * @param config
   */
  public static setConfig(config: any) {
    StoreUtil.setStore(STORE_KEYS.LOCAL_CONFIG_KEY, config)
  }

  /**
   * 获取配置
   * @param key
   */
  public static getConfig(key: string): any | null {
    const configs = this.getConfigs()
    if (configs) {
      return configs[key] || null
    }
    return null
  }

  /**
   * 获取全部配置
   */
  public static getConfigs(): any | null {
    return StoreUtil.getStore(STORE_KEYS.LOCAL_CONFIG_KEY)
  }

  /**
   * 加载配置信息
   */
  public static loadConfig (): Promise<any> {
    return ApiService.postAjax('public/sys/readConfig', {})
        .then(result => {
          ConfigUtils.setConfig(result)
        })
  }
}