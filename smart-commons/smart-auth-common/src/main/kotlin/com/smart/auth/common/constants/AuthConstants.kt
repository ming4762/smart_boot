package com.smart.auth.common.constants

/**
 *
 * @author ming
 * 2019/7/2 下午8:43
 */
enum class AuthConstants(value: String) {

    PERMISSION("session_permission"),
    // 登录类型
    LOGIN_WEB_TYPE("LOGIN_WEB_TYPE"),
    LOGIN_MOBILE_TYPE("LOGIN_MOBILE_TYPE"),

    // IP
    LOGIN_IP("LOGIN_IP"),
    // session 中存储公钥
    CLIENT_PUBLIC_KEY("CLIENT_PUBLIC_KEY")
}