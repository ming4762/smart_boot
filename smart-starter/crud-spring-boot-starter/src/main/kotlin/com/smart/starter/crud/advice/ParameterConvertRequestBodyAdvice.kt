package com.smart.starter.crud.advice

import com.smart.common.utils.DateUtil
import com.smart.common.utils.JsonUtil
import com.smart.starter.crud.model.BaseModel
import com.smart.starter.crud.query.QueryParameter
import org.springframework.core.MethodParameter
import org.springframework.http.HttpInputMessage
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice
import java.lang.reflect.Type
import java.util.*
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

/**
 * 参数转换器
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
            this.convertBody(body, parameter)
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
    private fun convertBody(data: QueryParameter<out BaseModel>, parameter: MethodParameter) {
        // 获取泛型类型
//        val modelClass = (parameter.nestedGenericParameterType as ParameterizedType).actualTypeArguments[0] as Class<out BaseModel>
        // 获取对象的所有树形
        val properties = data :: class.memberProperties
        properties.forEach { property ->
            val name = property.name
            val value = data[name]
            if (!excludeProperties.contains(name) && value != null) {
                val propertyType = property.javaField!!.type
                if (property is KMutableProperty1) {
                    property as KMutableProperty1<QueryParameter<out BaseModel>, Any?>
                    when {
                        value is Map<*, *> -> property.set(data, JsonUtil.toJavaObject(value, propertyType))
                        propertyType == Date :: class.java -> // 处理时间类型
                            property.set(data, DateUtil.convertDate(value.toString()))
                        else -> property.set(data, value)
                    }
                    data.remove(name)
                }
            }
        }
//        data.model = modelClass.newInstance()
//        modelClass.declaredFields.forEach { field ->
//            val name = field.name
//            field.isAccessible = true
//            if (data[name] != null) {
//                field.set(data.model, data[name])
//                data.remove(name)
//            }
//        }
    }
}