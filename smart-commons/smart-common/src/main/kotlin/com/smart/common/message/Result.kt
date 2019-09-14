package com.smart.common.message

/**
 *
 * @author ming
 * 2019/6/12 上午10:11
 */
class Result<T: Any?> {

    var code: Int = 200

    var message: String? = null

    var data: T? = null

    var ok: Boolean = true

    companion object {

        private fun <T: Any> newInstance(): Result<T?> {
            return Result()
        }

        @JvmStatic
        fun <T: Any> success(data: T?, message: String = ResultCodeEnum.SUCCESS.msg): Result<T?> {
            val result = newInstance<T>()
            result.data = data
            result.code = ResultCodeEnum.SUCCESS.code
            result.message = message
            result.ok = true

            if (data == null) {
                result.data = null
            } else {
                // 遇到没有属性的空类,防止JSON转换的时候异常
                val fields = data.javaClass.declaredFields
                if (fields.isEmpty()) {
                    result.data = null
                }
            }
            return result
        }

        @JvmStatic fun <T: Any> failure(message: String?): Result<T?> {
            val result = newInstance<T>()
            result.code = ResultCodeEnum.FAILURE.code
            result.message = message
            result.ok = false
            return result
        }

        @JvmStatic fun <T: Any> failure(errorCode: Int, message: String?, data: T?): Result<T?> {
            val result = newInstance<T>()
            result.code = errorCode
            result.message = message
            result.data = data

            if (data == null) {
                result.data = null
            }
            result.ok = false
            return result
        }

        @JvmStatic fun <T: Any> failure(errorCode: Int, message: String?): Result<T?> {
            val result = newInstance<T>()
            result.code = errorCode
            result.message = message
            result.data = null
            result.ok = false
            return result
        }

        /**
         * 获取失败对象
         * @author ming
         * @param message 返回信息
         * @param data 返回数据
         * @return 失败对象
         */
        @JvmStatic fun <T: Any> failure(message: String?, data: T?): Result<T?> {
            return failure(ResultCodeEnum.FAILURE.code, message, data)
        }
    }
}