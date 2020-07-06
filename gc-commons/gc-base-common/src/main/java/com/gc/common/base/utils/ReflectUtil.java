package com.gc.common.base.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 反射工具类
 * @author shizhongming
 * 2020/1/12 5:10 下午
 */
@Slf4j
public final class ReflectUtil {

    private ReflectUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 通过对象属性名获取属性值
     * @param value 对象
     * @param fieldName 属性名
     * @return 属性值
     */
    @Nullable
    public static Object getFieldValue(@NonNull Object value, @NonNull String fieldName) {
        try {
            final Field field = value.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 判断field是否是静态变量
     * @param field field
     * @return 是否是静态变量
     */
    public static boolean isStatic(@NonNull Field field) {
        return Modifier.isStatic(field.getModifiers());
    }

    /**
     * 判断field是否是静态变量
     * @param field field
     * @return 是否是静态变量
     */
    public static boolean isNotStatic(@NonNull Field field) {
        return !Modifier.isStatic(field.getModifiers());
    }

    /**
     * 获取所有Filed 包括上级的
     * @param clazz Class
     * @param fields field集合
     */
    public static void getAllFields(@NonNull Class<?> clazz, Set<Field> fields) {
        final Field[] fieldArray = clazz.getDeclaredFields();
        fields.addAll(Arrays.stream(fieldArray)
                .filter(ReflectUtil :: isNotStatic)
                .collect(Collectors.toSet()));
        // 获取上级
        Class<?> superClass = clazz.getSuperclass();
        if (Objects.nonNull(superClass)) {
            getAllFields(superClass, fields);
        }
    }
}
