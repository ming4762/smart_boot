package com.smart.starter.fluorite.model

import java.io.Serializable

/**
 * token实体类
 */
class AccessToken(var accessToken: String, var expireTime: Long) : Serializable {

    companion object {
        private const val serialVersionUID = 5990939387657237756L
    }

}