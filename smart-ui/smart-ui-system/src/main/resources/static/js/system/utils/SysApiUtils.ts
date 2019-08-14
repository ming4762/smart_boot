import URL from '../URL.js'
// @ts-ignore
import ApiService from '../../utils/ApiService.js'
// @ts-ignore
import CollectionUtils from '../../utils/CollectionUtils.js'

/**
 * 系统模块API工具类
 */
export default class SysApiUtils {
  /**
   * 获取字典项
   * @param dictCode 字典编码
   */
  public static async getDictItem (...dictCode: Array<string>): Promise<any> {
    const parameter = {
      'dictCode@in': dictCode
    }
    const itemList: any[] = await ApiService.postAjax(URL.queryDictItem, parameter)
    let result: {[index: string]: any} = {}
    if (itemList && itemList.length > 0) {
      const group = CollectionUtils.group(itemList, 'dictCode')
      if (dictCode.length === 1) {
        result = CollectionUtils.mapToObject(CollectionUtils.listToMap(group.values().next().value, 'itemCode', 'itemValue'))
      } else {
        group.forEach((value: any[], key) => {
          result[key] = CollectionUtils.mapToObject(CollectionUtils.listToMap(value, 'itemCode', 'itemValue'))
        })
      }
    }
    return result
  }
}