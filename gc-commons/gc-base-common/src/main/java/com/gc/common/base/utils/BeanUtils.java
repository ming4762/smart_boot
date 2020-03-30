package com.gc.common.base.utils;

import com.gc.common.base.exception.IllegalAccessRuntimeException;
import com.gc.common.base.exception.InstantiationRuntimeException;
import com.gc.common.base.exception.InvocationTargetRuntimeException;
import com.gc.common.base.exception.NoSuchMethodRuntimeException;
import org.springframework.lang.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
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

}
