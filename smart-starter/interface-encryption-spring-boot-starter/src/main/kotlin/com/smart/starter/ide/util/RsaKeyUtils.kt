package com.smart.starter.ide.util

import com.smart.common.model.RsaKey
import com.smart.common.utils.security.RSAUtils

/**
 *
 * @author ming
 * 2019/7/25 下午4:00
 */
object RsaKeyUtils {

    private val serverKey = RSAUtils.genKeyPair()

    /**
     * 获取服务端的key
     */
    fun getServerKey(): RsaKey {
        return serverKey
    }
}
