package com.smart.starter.crud.advice

import com.smart.starter.crud.model.BaseModel
import com.smart.starter.crud.query.QueryParameter
import org.springframework.core.MethodParameter
import org.springframework.http.HttpInputMessage
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice
import java.lang.reflect.Type
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.memberProperties

/**
 *
 * @author ming
 * 2019/9/13 下午10:12
 */
@ControllerAdvice
class ParameterConvertRequestBodyAdvice : RequestBodyAdvice {

    companion object {
        private val excludeProperties = listOf("entries", "keys", "size", "values")
    }

    override fun afterBodyRead(body: Any, inputMessage: HttpInputMessage, parameter: MethodParameter, targetType: Type, converterType: Class<out HttpMessageConverter<*>>): Any {
        if (body is QueryParameter<out BaseModel>) {
            this.convertBody(body)
        }
        return body
    }

    override fun beforeBodyRead(inputMessage: HttpInputMessage, parameter: MethodParameter, targetType: Type, converterType: Class<out HttpMessageConverter<*>>): HttpInputMessage {
        return inputMessage
    }

    override fun handleEmptyBody(body: Any?, inputMessage: HttpInputMessage, parameter: MethodParameter, targetType: Type, converterType: Class<out HttpMessageConverter<*>>): Any? {
        return body
    }

    override fun supports(methodParameter: MethodParameter, targetType: Type, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        return true
    }

    /**
     * 转换数据
     * TODO:过滤非@参数
     */
    @Suppress("UNCHECKED_CAST")
    private fun convertBody(data: QueryParameter<out BaseModel>) {
        // 获取对象的所有树形
        val properties = data :: class.memberProperties
        properties.forEach { property ->
            val name = property.name
            if (!excludeProperties.contains(name) && data[name] != null) {
                if (property is KMutableProperty1) {
                    property as KMutableProperty1<QueryParameter<out BaseModel>, Any?>
                    property.set(data, data[name])
                    data.remove(name)
                }
            }
        }
    }
}