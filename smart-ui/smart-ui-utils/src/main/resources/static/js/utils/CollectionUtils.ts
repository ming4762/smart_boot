/**
 * 集合工具类
 */
export default class CollectionUtils {

  /**
   * 分组操作
   * @param list 要分组的数据
   * @param groupHandle 分组的字段或方法
   */
  // @ts-ignore
  public static group<T> (list: Array<T>, groupHandle: string | Function): Map<string, Array<T>> {
    // @ts-ignore
    const result: Map<string, Array<T>> = new Map()
    list.forEach(item => {
      let value: string = this.getValue(item, groupHandle)
      const getValue: Array<T>| undefined = result.get(value)
      if (getValue) {
        getValue.push(item)
      } else {
        result.set(value, [item])
      }
    })
    return result
  }

  /**
   * list转map
   * @param keyHandle key
   * @param valueHandle value
   */
  // @ts-ignore
  public static listToMap(list: Array<any>, keyHandle: string | Function, valueHandle: string | Function): Map<any, any> {
    // @ts-ignore
    const result = new Map()
    list.forEach(item => {
      const key = this.getValue(item, keyHandle)
      const value = this.getValue(item, valueHandle)
      result.set(key, value)
    })
    return result
  }

  /**
   * map转object
   * @param map map
   */
  // @ts-ignore
  public static mapToObject(map: Map<string, any>): any {
    const result: any = {}
    map.forEach((value, key) => {
      result[key] = value
    })
    return result
  }

  /**
   * 获取值
   * @param item 对象
   * @param handle
   */
  private static getValue (item: any, handle: string | Function): any {
    if (typeof handle === 'function') {
      return handle(item)
    } else {
      return item[handle]
    }
  }

  /**
   * 拆分集合
   * @param list 原有集合
   * @param number 拆分后的每个集合最大数
   */
  private static splitArray<T> (list: Array<T>, number: number): Array<Array<T>> {
    if (!list) return []
    const resultList = []
    let num = 0
    list.forEach(item => {
      if (num === 0) {
        resultList.push([])
      }
      resultList[resultList.length - 1].push(item)
      num ++
      if (num === number) {
        num = 0
      }
    })
    return resultList
  }
}
