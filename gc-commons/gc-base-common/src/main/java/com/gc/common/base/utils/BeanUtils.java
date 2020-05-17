package com.gc.common.base.utils;

import com.gc.common.base.annotation.MapKey;
import com.gc.common.base.exception.IllegalAccessRuntimeException;
import com.gc.common.base.exception.InstantiationRuntimeException;
import com.gc.common.base.exception.InvocationTargetRuntimeException;
import com.gc.common.base.exception.NoSuchMethodRuntimeException;
import com.google.common.collect.Maps;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * bean 工具类
 * @author shizhongming
 * 2020/3/30 8:27 下午
 */
public class BeanUtils {

    private BeanUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 复制集合
     * @param resource 源
     * @param targetClass 目标类型
     * @param <T> 目标类型
     * @return 复制后的集合
     */
    @NonNull
    public static <T> List<T> copyCollection(@NonNull Collection<?> resource, @NonNull Class<T> targetClass) {
        return resource.stream()
                .map(item -> {
                    try {
                        T target = targetClass.getDeclaredConstructor().newInstance();
                        org.springframework.beans.BeanUtils.copyProperties(item, target);
                        return target;
                    } catch (InstantiationException e) {
                        throw new InstantiationRuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new IllegalAccessRuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new InvocationTargetRuntimeException(e);
                    } catch (NoSuchMethodException e) {
                        throw new NoSuchMethodRuntimeException(e);
                    }
                }).collect(Collectors.toList());
    }

    /**
     * 实体类转为map
     * @param bean
     * @return
     */
    @NonNull
    public static Map<String, Object> beanToMap(@NonNull Object bean) {
        Map<String, Object> mapKeys = Maps.newHashMap();
        ReflectionUtils.doWithFields(bean.getClass(), field -> {
            if (!Modifier.isFinal(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())) {
                MapKey mapKey = AnnotationUtils.findAnnotation(field, MapKey.class);
                String key = field.getName();
                if (!Objects.isNull(mapKey)) {
                    key = mapKey.value();
                }
                if (mapKeys.containsKey(key)) {
                    throw new IllegalAccessRuntimeException(new IllegalAccessException(String.format("mapKey冲突:%s", key)));
                }
                try {
                    mapKeys.put(key, PropertyUtils.getProperty(bean, field.getName()));
                } catch (InvocationTargetException e) {
                    throw new InvocationTargetRuntimeException(e);
                } catch (NoSuchMethodException e) {
                    throw new NoSuchMethodRuntimeException(e);
                }
            }
        });
        return mapKeys;
    }
}
