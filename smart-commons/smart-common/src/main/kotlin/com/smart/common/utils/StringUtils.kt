package com.smart.common.utils

import org.slf4j.LoggerFactory
import org.springframework.util.StringUtils
import java.util.regex.Pattern

/**
 * 字符串工具类
 * TODO: 无法继承静态方法
 * @author ming
 * 2019/6/12 上午9:48
 */
object StringUtils : StringUtils() {

    private val LOGGER = LoggerFactory.getLogger(StringUtils ::class.java)

    /**
     * 批量替换字符串
     * @param oldValue 旧字符串
     * @param newValues 替换字符串的key， value
     * @param ignoreCase 是否忽略大小写，默认false
     * @param surround 旧字符串被替换部分包围
     * example https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=#{appid}&secret=#{appsecret}
     * 参数{"appid": "abc123", "appsecret": "abc345"}
     * 结果 https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=abc123&secret=abc345
     */
    @JvmStatic
    fun replaceAll(oldValue: String, newValues: Map<String, String>, ignoreCase: Boolean = false, surround: String = "#{key}"): String {
        var newValue = oldValue
        val noMatchs = mutableListOf<String>()
        newValues.forEach { key, value ->
            // 判断旧string是否包含值
            val replaceValue = surround.replace("key", key)
            if (newValue.contains(replaceValue, ignoreCase)) {
                newValue = newValue.replace(replaceValue, value, ignoreCase)
            } else {
                noMatchs.add(key)
            }
        }
        if (noMatchs.isNotEmpty()) LOGGER.warn("以下字符匹配失败：$noMatchs")
        return newValue
    }

    /**
     * 驼峰改为下划线
     */
    @JvmStatic
    fun underscoreName(camelCaseName: String): String {
        val pattern = Pattern.compile("[A-Z]")
        val matcher = pattern.matcher(camelCaseName)
        var stringBuffer = StringBuffer(camelCaseName)
        if (matcher.find()) {
            stringBuffer = StringBuffer()
            matcher.appendReplacement(stringBuffer,"_"+matcher.group(0).toLowerCase())
            matcher.appendTail(stringBuffer)
        } else {
            return stringBuffer.toString()
        }
        return underscoreName(stringBuffer.toString())
    }
}