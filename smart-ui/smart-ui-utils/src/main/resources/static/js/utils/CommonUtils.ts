declare const contextPath
/**
 * common工具类
 * @author zhongming
 */
export default class CommonUtil {
  /**
   * 生成UUID
   */
  public static createUUID (): string {
    let s: Array<any> = []
    let hexDigits = '0123456789abcdef'
    for (let i = 0; i < 36; i++) {
      s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1)
    }
    s[14] = '4'  // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1)  // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23] = '-'
    const uuid = s.join('')
    return uuid
  }

  /**
   * 深拷贝对象
   * @param object 被拷贝对象
   */
  public static clone<T> (object: T): T | null {
    if (object == null) {
      return null
    }
    let objectStr: string = JSON.stringify(object)
    return JSON.parse(objectStr)
  }

  /**
   * 从对象列表中获取指定key并构成一个新的对象数组
   * @param keys key数组
   * @param objectList 原始对象数组
   */
  public static getObjectByKeys (keys: string[], objectList: any[]): any[] {
    if (objectList && keys) {
      let resultList: any[] = []
      objectList.forEach(object => {
        let result: any = {}
        keys.forEach(key => {
          result[key] = object[key]
        })
        resultList.push(result)
      })
      return resultList
    }
    return []
  }

  /**
   * 全屏操作
   */
  public static fullScreen () {
    const element: any = document.documentElement;
    if (element.requestFullscreen) {
      element.requestFullscreen()
    } else if (element.msRequestFullscreen) {
      element.msRequestFullscreen()
    } else if (element.mozRequestFullScreen) {
      element.mozRequestFullScreen()
    } else if (element.webkitRequestFullscreen) {
      element.webkitRequestFullscreen()
    }
  }

  /**
   * 退出全屏
   */
  public static exitFullscreen () {
    const doc: any = document
    if (doc.exitFullscreen) {
      doc.exitFullscreen()
    } else if (doc.msExitFullscreen) {
      doc.msExitFullscreen()
    } else if (doc.mozCancelFullScreen) {
      doc.mozCancelFullScreen()
    } else if (doc.webkitExitFullscreen) {
      doc.webkitExitFullscreen()
    }
  }

  /**
   * 加载js文件
   * @param urls
   */
  public static async loadJS(...urls: Array<string>): Promise<any> {
    for (let url of urls) {
      await this.doLoadJS(url)
    }
    // return new Promise<any>((resolve) => {
    //   resolve()
    // })
  }

  /**
   * 异步加载js
   * @param urls
   */
  public static loadJSAsync(...urls: Array<string>) {
    for (let url of urls) {
      this.doLoadJS(url)
    }
  }


  /**
   * 加载js
   * @param url
   */
  private static doLoadJS(url: string): Promise<any> {
    return new Promise<any>((resolve) => {
      const script = document.createElement('script')
      script.type = 'text/javascript'
      script.onload = () => {
        resolve()
      }
      script.src = url
      document.body.appendChild(script)
    })
  }

  /**
   * 加载css文件
   * @param hrefs
   */
  public static async loadCSS(...hrefs: Array<string>): Promise<any> {
    for (let href of hrefs) {
      await this.doLaodCSS(href)
    }
  }

  /**
   * 加载css文件
   * @param hrefs
   */
  public static loadCSSAsync(...hrefs: Array<string>) {
    for (let href of hrefs) {
      this.doLaodCSS(href)
    }
  }

  /**
   * 加载css
   * @param href
   */
  private static doLaodCSS(href: string): Promise<any> {
    return new Promise<any>((resolve) => {
      const link = document.createElement('link')
      link.setAttribute("rel","stylesheet")
      link.setAttribute("type","text/css")
      link.setAttribute("href", href)
      link.onload = () => {
        resolve()
      }
      document.getElementsByTagName("head")[0].appendChild(link)
    })
  }

  /**
   * 添加css代码
   * @param cssText
   */
  public static addCSS (cssText) {
    // 创建一个style元素
    const style = document.createElement('style')
    // 获取head元素
    const head  = document.head || document.getElementsByTagName('head')[0]
    const id = '' + new Date().getTime()
    style.id = id
    style.type = 'text/css'
    const textNode = document.createTextNode(cssText)
    style.appendChild(textNode)
    head.appendChild(style)
    return id
  }

  static withContextPath (path: string): string {
    return contextPath + path
  }
}
