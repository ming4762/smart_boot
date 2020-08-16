package com.gc.commons.txt;

import com.gc.common.base.exception.SetMethodNotFountException;
import com.gc.commons.txt.annotation.TxtProperty;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO: 待优化，ConcurrentHashMap不能存储空，导致没有注解的属性被多次读取的问题
 * @author shizhongming
 * 2020/7/8 3:48 下午
 */
public class CacheUtils {

    private static final Map<Field, Method> WRITE_METHOD_MAP = new ConcurrentHashMap<>();

    private static final Map<Field, TxtProperty> TXT_PROPERTY_MAP = new ConcurrentHashMap<>();

    private CacheUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 获取属性的set函数
     * @param field 属性
     * @return set函数
     */
    @NonNull
    public static Method getWriteMethod(Field field) {
        Method method = WRITE_METHOD_MAP.get(field);
        if (Objects.nonNull(method)) {
            return method;
        }
        method = Optional.ofNullable(BeanUtils.getPropertyDescriptor(field.getDeclaringClass(), field.getName()))
                .map(PropertyDescriptor::getWriteMethod)
                .orElse(null);
        if (Objects.isNull(method)) {
            throw new SetMethodNotFountException("未找到set方法,field:" + field.toString());
        }
        WRITE_METHOD_MAP.put(field, method);
        return method;
    }

    /**
     * 获取属性注解
     * @param field 属性
     * @return TxtProperty
     */
    @Nullable
    public static TxtProperty getTxtProperty(Field field) {
        if (!TXT_PROPERTY_MAP.containsKey(field)) {
            TxtProperty txtProperty = AnnotationUtils.findAnnotation(field, TxtProperty.class);
            if (Objects.nonNull(txtProperty)) {
                TXT_PROPERTY_MAP.put(field, txtProperty);
            }
        }
        return TXT_PROPERTY_MAP.get(field);
    }
}
