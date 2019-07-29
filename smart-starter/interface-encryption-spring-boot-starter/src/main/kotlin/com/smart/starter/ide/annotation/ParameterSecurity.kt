package com.smart.starter.ide.annotation

import org.springframework.web.bind.annotation.Mapping

/**
 * 参数加解密
 * @author zhongming
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE)
@Retention()
@Mapping
@MustBeDocumented
annotation class ParameterSecurity(
        /**
         * 入参是否解密，默认解密
         */
        val inDecode: Boolean = true,
        /**
         * 出参是否加密，默认加密
         */
        val outEncode: Boolean = true,

        /**
         * 是否进行MD5认证
         */
        val md5: Boolean = true,

        /**
         * 是否进行时间戳认证
         */
        val timestamp: Boolean = true
) {
}