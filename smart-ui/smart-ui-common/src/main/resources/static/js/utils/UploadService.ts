declare const axios

import AuthUtils from './AuthUtils.js'

const baseUrl = localStorage.getItem('API_URL')

const service = axios.create({
  baseURL: baseUrl,
  timeout: 10000
})
/**
 * token请求头key
 * todo: 与apiservice重复
 */
const TOKEN_KEY: string = 'Authorization'

/**
 * 上传服务
 */
export default class UploadService {

  /**
   * 上传文件
   * @param file
   * @param parameter
   */
  public static upload (file: File, parameter?: {[index: string]: any}) {
    // 创建formData
    const formData = new FormData()
    formData.append('file', file)
    return this.doUpload(formData, false, parameter)
  }

  /**
   * 批量上传
   * @param fileList
   * @param parameter
   */
  public static batchUpload (fileList, parameter?: {[index: string]: any}) {
    if (fileList.length > 0) {
      const formData = new FormData()
      fileList.forEach((file) => {
        formData.append('files', file)
      })
      return this.doUpload(formData, true, parameter)
    }
    return Promise.resolve(null)
  }

  private static doUpload (formData, batch: boolean, parameter?: {[index: string]: any}): Promise<any> {
    // 添加其他参数
    if (parameter) {
      Object.keys(parameter)
          .forEach(key => {
            formData.append(key, parameter[key])
          })
    }
    // 设置请求头
    const headers: any = {
      'Content-Type': 'multipart/form-data'
    }
    const token = AuthUtils.getToken()
    if (token) {
      headers[TOKEN_KEY] = token
    }
    const url = batch ? 'public/file/batchUpload' : 'public/file/upload'
    return service.post(url, formData, headers)
        .then(response => {
          if (response.status === 200) {
            if (response.data) {
              if(response.data.ok === true) {
                return response.data.data
              } else {
                return Promise.reject(response)
              }
            }
          } else {
            return Promise.reject(response)
          }
        })
  }

  /**
   * 获取文件显示路径
   * @param fileId
   */
  public static getImageUrl (fileId: string): string {
    return `${baseUrl}/public/image/show/${fileId}`
  }
}