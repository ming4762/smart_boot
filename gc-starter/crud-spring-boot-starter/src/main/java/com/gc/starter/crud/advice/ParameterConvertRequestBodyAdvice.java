package com.gc.starter.crud.advice;

import com.gc.common.base.exception.IllegalAccessRuntimeException;
import com.gc.common.base.exception.IntrospectionRuntimeException;
import com.gc.common.base.exception.InvocationTargetRuntimeException;
import com.gc.starter.crud.query.PageQueryParameter;
import com.gc.starter.crud.query.QueryParameter;
import com.gc.starter.crud.query.SortQueryParameter;
import lombok.SneakyThrows;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

/**
 * 参数处理类，将map转为queryParameter
 * @author jackson
 * 2020/3/14 6:44 下午
 */
@ControllerAdvice
public class ParameterConvertRequestBodyAdvice implements RequestBodyAdvice {
    @Override
    public boolean supports(@NonNull MethodParameter methodParameter, @NonNull Type type, @NonNull Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @NonNull
    @Override
    public HttpInputMessage beforeBodyRead(@NonNull HttpInputMessage httpInputMessage, @NonNull MethodParameter methodParameter, @NonNull Type type, @NonNull Class<? extends HttpMessageConverter<?>> aClass) {
        return httpInputMessage;
    }

    @SneakyThrows
    @NonNull
    @Override
    public Object afterBodyRead(@NonNull Object body, @NonNull HttpInputMessage httpInputMessage, @NonNull MethodParameter methodParameter, @NonNull Type type, @NonNull Class<? extends HttpMessageConverter<?>> aClass) {
        final Class clazz;
        if (type instanceof Class) {
            clazz = (Class) type;
        } else {
            clazz = Class.forName(type.getTypeName());
        }
        if (body instanceof QueryParameter) {
            this.convertParameter((QueryParameter) body, clazz);
        }
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, @NonNull HttpInputMessage httpInputMessage, @NonNull MethodParameter methodParameter, @NonNull Type type, @NonNull Class<? extends HttpMessageConverter<?>> aClass) {
        return body;
    }

    /**
     * 转换参数
     * @param parameter
     */
    @SuppressWarnings("rawtypes")
    private void convertParameter(QueryParameter parameter, Class clazz) {
        // 处理PageQueryParameter
        if (parameter instanceof PageQueryParameter) {
            this.addParameter(parameter, clazz);
        }
        // 处理SortQueryParameter
        if (parameter instanceof SortQueryParameter) {
            this.addParameter(parameter, clazz);
        }
    }

    /**
     * 添加参数
     * @param parameter 参数
     * @param clazz 实体类类型
     */
    @SuppressWarnings("rawtypes")
    private void addParameter(QueryParameter parameter, Class<? extends QueryParameter> clazz) {
        try {
            for (Field field : clazz.getDeclaredFields()) {
                if (!Modifier.isFinal(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())) {
                    String name = field.getName();
                    if (parameter.containsKey(name)) {
                        Object value = parameter.get(name);
                        parameter.remove(name);
                        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(name, clazz);
                        propertyDescriptor.getWriteMethod().invoke(parameter, value);
                    }
                }
            }
        } catch (IllegalAccessException e) {
           throw new IllegalAccessRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new InvocationTargetRuntimeException(e);
        } catch (IntrospectionException e) {
            throw new IntrospectionRuntimeException(e);
        }
    }

}
