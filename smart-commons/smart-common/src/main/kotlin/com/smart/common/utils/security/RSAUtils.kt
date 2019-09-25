package com.smart.common.utils.security

import com.alibaba.fastjson.JSON
import com.smart.common.model.RsaKey
import com.smart.common.utils.Base64Util
import java.io.ByteArrayOutputStream
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher

/**
 * RSA加密工具类
 * @author ming
 * 2019/6/12 上午10:25
 */
object RSAUtils {
    /**
     * 加密算法
     */
    public const val KEY_ALGORITHM = "RSA"

    /**
     * RSA最大加密明文大小
     */
    private const val MAX_ENCRYPT_BLOCK = 117

    /**
     * RSA最大解密密文大小
     */
    private const val MAX_DECRYPT_BLOCK = 128

    /**
     * RSA 位数
     */
    private const val INITIALIZE_LENGTH = 1024

    /**
     * 生产秘钥对
     */
    @JvmStatic
    fun genKeyPair(): RsaKey {
        // 创建秘钥生成器，并初始化
        val keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM)
        keyPairGen.initialize(INITIALIZE_LENGTH)
        val keyPair = keyPairGen.generateKeyPair()
        // 生产公钥
        val pubkey = keyPair.public as RSAPublicKey
        // 生产私钥
        val priKey = keyPair.private as RSAPrivateKey
        return RsaKey(Base64Util.encoder(pubkey.encoded), Base64Util.encoder(priKey.encoded))
    }

    /**
     * 使用私钥解密
     * @param encryptedData 加密数据
     * @param privateKey base64 编码的私钥
     */
    fun decryptByPrivateKey(value: String, privateKey: String): String {
        val encryptedData = org.apache.commons.codec.binary.Base64.decodeBase64(value)
        // 秘钥解码
        val keyBytes = Base64.getDecoder().decode(privateKey)
        val pkcs8KeySpec = PKCS8EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance(KEY_ALGORITHM)
        val privateK = keyFactory.generatePrivate(pkcs8KeySpec)
        val cipher = Cipher.getInstance(keyFactory.algorithm)
        cipher.init(Cipher.DECRYPT_MODE, privateK)
        // 获取数据长度
        val inputLen = encryptedData.size
        val out = ByteArrayOutputStream()
        var offSet = 0;
        var cache: ByteArray
        var i = 0
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK)
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet)
            }
            out.write(cache, 0, cache.size)
            i++
            offSet = i * MAX_DECRYPT_BLOCK
        }
        val decryptedData = out.toByteArray()
        out.close()
        return String(decryptedData)
    }

    /**
     *
     *
     * 公钥解密
     *
     *
     * @param encryptedData
     * 已加密数据
     * @param publicKey
     * 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun decryptByPublicKey(value: String, publicKey: String): String {
        val encryptedData = org.apache.commons.codec.binary.Base64.decodeBase64(value)
        val keyBytes = org.apache.commons.codec.binary.Base64.decodeBase64(publicKey)
        val x509KeySpec = X509EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance(KEY_ALGORITHM)
        val publicK = keyFactory.generatePublic(x509KeySpec)
        val cipher = Cipher.getInstance(keyFactory.algorithm)
        cipher.init(Cipher.DECRYPT_MODE, publicK)
        val inputLen = encryptedData.size
        val out = ByteArrayOutputStream()
        var offSet = 0
        var cache: ByteArray
        var i = 0
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK)
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet)
            }
            out.write(cache, 0, cache.size)
            i++
            offSet = i * MAX_DECRYPT_BLOCK
        }
        val decryptedData = out.toByteArray()
        out.close()
        return String(decryptedData)
    }

    /**
     *
     *
     * 公钥加密
     *
     *
     * @param data
     * 源数据
     * @param publicKey
     * 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun encryptByPublicKey(value: String, publicKey: String): String {
        val data = value.toByteArray()
        val keyBytes = org.apache.commons.codec.binary.Base64.decodeBase64(publicKey)
        val x509KeySpec = X509EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance(KEY_ALGORITHM)
        val publicK = keyFactory.generatePublic(x509KeySpec)
        // 对数据加密
        val cipher = Cipher.getInstance(keyFactory.algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, publicK)
        val inputLen = data.size
        val out = ByteArrayOutputStream()
        var offSet = 0
        var cache: ByteArray
        var i = 0
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK)
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet)
            }
            out.write(cache, 0, cache.size)
            i++
            offSet = i * MAX_ENCRYPT_BLOCK
        }
        val encryptedData = out.toByteArray()
        out.close()
        return org.apache.commons.codec.binary.Base64.encodeBase64String(encryptedData)
    }

    /**
     *
     *
     * 私钥加密
     *
     *
     * @param data
     * 源数据
     * @param privateKey
     * 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun encryptByPrivateKey(value: String, privateKey: String): String {
        val data = value.toByteArray()
        val keyBytes = org.apache.commons.codec.binary.Base64.decodeBase64(privateKey)
        val pkcs8KeySpec = PKCS8EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance(KEY_ALGORITHM)
        val privateK = keyFactory.generatePrivate(pkcs8KeySpec)
        val cipher = Cipher.getInstance(keyFactory.algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, privateK)
        val inputLen = data.size
        val out = ByteArrayOutputStream()
        var offSet = 0
        var cache: ByteArray
        var i = 0
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK)
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet)
            }
            out.write(cache, 0, cache.size)
            i++
            offSet = i * MAX_ENCRYPT_BLOCK
        }
        val encryptedData = out.toByteArray()
        out.close()
        return org.apache.commons.codec.binary.Base64.encodeBase64String(encryptedData)
    }


    @JvmStatic
    fun main(args: Array<String>) {
        val value = JSON.toJSONString(mapOf(
                "name" to "史仲明"
        ))
        val key = this.genKeyPair()
        val text = RSAUtils.encryptByPublicKey(value, key.pubKey)
        println(text)
        println(RSAUtils.decryptByPrivateKey(text, key.priKey))
    }
}