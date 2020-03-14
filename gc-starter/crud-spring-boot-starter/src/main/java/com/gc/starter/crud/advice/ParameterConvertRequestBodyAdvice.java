package com.gc.starter.crud.advice;

import com.gc.starter.crud.query.PageQueryParameter;
import com.gc.starter.crud.query.QueryParameter;
import com.gc.starter.crud.query.SortQueryParameter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
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
    public boolean supports(@NotNull MethodParameter methodParameter, @NotNull Type type, @NotNull Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @NotNull
    @Override
    public HttpInputMessage beforeBodyRead(@NotNull HttpInputMessage httpInputMessage, @NotNull MethodParameter methodParameter, @NotNull Type type, @NotNull Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        return httpInputMessage;
    }

    @SneakyThrows
    @NotNull
    @Override
    public Object afterBodyRead(@NotNull Object body, @NotNull HttpInputMessage httpInputMessage, @NotNull MethodParameter methodParameter, @NotNull Type type, @NotNull Class<? extends HttpMessageConverter<?>> aClass) {
        if (body instanceof QueryParameter) {
            this.convertParameter((QueryParameter) body);
        }
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, @NotNull HttpInputMessage httpInputMessage, @NotNull MethodParameter methodParameter, @NotNull Type type, @NotNull Class<? extends HttpMessageConverter<?>> aClass) {
        return body;
    }

    /**
     * 转换参数
     * @param parameter
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws IntrospectionException
     */
    @SuppressWarnings("rawtypes")
    private void convertParameter(QueryParameter parameter) throws IllegalAccessException, InvocationTargetException, IntrospectionException {
        // 处理PageQueryParameter
        if (parameter instanceof PageQueryParameter) {
            this.addParameter(parameter, PageQueryParameter.class);
        }
        if (parameter instanceof SortQueryParameter) {
            this.addParameter(parameter, SortQueryParameter.class);
        }
    }

    /**
     * 添加参数
     * @param parameter
     * @param clazz
     * @throws IntrospectionException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @SuppressWarnings("rawtypes")
    private void addParameter(QueryParameter parameter, Class<? extends QueryParameter> clazz) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
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
    }

}
