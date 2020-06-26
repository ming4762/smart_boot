package com.gc.common.base.utils;

import com.google.common.collect.Maps;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.lang.NonNull;

import java.util.Map;

/**
 * 切面工具类
 * @author shizhongming
 * 2020/6/8 5:02 下午
 */
public class AopUtils {

    private AopUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 获取目标方法的参数
     * @param point 切面
     * @return 参数
     */
    @NonNull
    public static Map<String, Object> getParameterMap(ProceedingJoinPoint point) {
        final Signature signature = point.getSignature();
        Map<String, Object> parameterMap = Maps.newHashMap();
        if (signature instanceof MethodSignature) {
            // 获取参数
            Object[] objects = point.getArgs();
            // 获取参数名
            String[] names = ((MethodSignature) signature).getParameterNames();
            for (int i = 0; i < names.length; i++) {
                parameterMap.put(names[i], objects[i]);
            }
        }

        return parameterMap;
    }

}
