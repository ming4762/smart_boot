'use strict';
// @ts-ignore
// import JSEncrypt from '../jsencrypt/3.0.0/jsencrypt.min.js'
declare const JSEncrypt
/**
 * 秘钥工具类
 */
export default class RsaUtils {
  private GLOBAL_KEY
  // 全局的key
  constructor () {
    try {
      if (JSEncrypt) {
        this.GLOBAL_KEY = new JSEncrypt()
      }
    } catch (e) {
      // TODO: 待处理问题
    }
  }

  /**
   * 生产秘钥
   */
  public createKey(): RsaKey {
    const jSEncrypt = new JSEncrypt()
    return new RsaKey(jSEncrypt.getPublicKeyB64(), jSEncrypt.getPrivateKeyB64())
  }

  /**
   * 加密
   * @param publicKey
   * @param data
   */
  public rsaEncrypt(publicKey: string, data: string): string {
    this.GLOBAL_KEY.setPublicKey(publicKey)
    return this.GLOBAL_KEY.encrypt(data)
  }

  /**
   * 解密
   * @param privateKey 私钥
   * @param ciphertext 密文
   */
  public rsaDecrypt(privateKey: string, ciphertext: string): string {
    this.GLOBAL_KEY.setPrivateKey(privateKey)
    return this.GLOBAL_KEY.decrypt(ciphertext)
  }
}


/**
 * 秘钥实体类
 */
class RsaKey {

  constructor(pubKey: String, priKey: String) {
    this.pubKey = pubKey
    this.priKey = priKey
  }

  public pubKey: String

  public priKey: String
}