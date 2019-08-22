declare const axios

import AuthUtils from './AuthUtils.js'

const service = axios.create({
  // baseURL: StoreUtil.getStore('API_URL'),
  baseURL: localStorage.getItem('API_URL'),
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
   * @param filename
   * @param parameter
   */
  public static upload (file: File, filename: string, parameter?: {[index: string]: any}) {
    // 创建formData
    const formData = new FormData()
    formData.append('file', file, filename)
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
    return service.post('file/upload', formData, headers)
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
}