package com.smart.common.model

import java.io.Serializable

/**
 *
 * @author ming
 * 2019/6/12 上午10:26
 */
class RsaKey(var pubKey: String, var priKey: String) : Serializable {

    companion object {
        private const val serialVersionUID = 5990939387657237757L
    }
}