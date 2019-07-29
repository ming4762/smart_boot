package com.smart.starter.ide.util

import com.smart.auth.common.constants.AuthConstants
import com.smart.auth.common.utils.AuthUtils
import com.smart.common.model.RsaKey
import com.smart.starter.ide.model.EncrypParameter

object IDEUtils {

    /**
     * 线程存储
     */
    private val threadLocal: ThreadLocal<EncrypParameter> = ThreadLocal()

    // 公共秘钥
    private var rsaKey: RsaKey? = null

    @JvmStatic
    fun get(): EncrypParameter? {
        return threadLocal.get()
    }


    @JvmStatic
    fun set(parameter: EncrypParameter) {
        this.threadLocal.set(parameter)
    }

    /**
     * 获取客户端公钥
     */
    fun getClientPublicKey(): String? {
        return AuthUtils.getSession()?.getAttribute(AuthConstants.CLIENT_PUBLIC_KEY) as String?
    }
}