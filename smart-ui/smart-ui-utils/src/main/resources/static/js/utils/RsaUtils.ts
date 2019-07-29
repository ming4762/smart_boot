// @ts-ignore
import JSEncrypt from 'jsencrypt/3.0.0/jsencrypt.min'
// import JSEncrypt from 'jsrsasign/8.0.0/jsrsasign-rsa-min'
/**
 * 秘钥工具类
 */
export default class RsaUtils {
  // 全局的key
  private static GLOBAL_KEY = new JSEncrypt()

  /**
   * 生产秘钥
   */
  public static createKey(): RsaKey {
    const jSEncrypt = new JSEncrypt()
    return new RsaKey(jSEncrypt.getPublicKeyB64(), jSEncrypt.getPrivateKeyB64())
  }

  /**
   * 加密
   * @param publicKey
   * @param data
   */
  public static rsaEncrypt(publicKey: string, data: string): string {
    this.GLOBAL_KEY.setPublicKey(publicKey)
    return this.GLOBAL_KEY.encrypt(data)
  }

  /**
   * 解密
   * @param privateKey 私钥
   * @param ciphertext 密文
   */
  public static rsaDecrypt(privateKey: string, ciphertext: string): string {
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