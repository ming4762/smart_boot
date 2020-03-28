package com.gc.common.base.utils;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

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
    public static Object getFieldValue(@NotNull Object value, @NotNull String fieldName) {
        try {
            final Field field = value.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
