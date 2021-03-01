package com.gc.auth.extensions.saml2.utils;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * SAML 重试次数记录
 * @author ShiZhongMing
 * 2021/3/1 10:06
 * @since 1.0
 */
public class SamlRetryTimerHolder {

    private static final Map<String, Integer> RETRY_TIMER = Maps.newConcurrentMap();

    public static int add(String key) {
        int num = 1;
        if (RETRY_TIMER.containsKey(key)) {
            num = RETRY_TIMER.get(key) + 1;
        }
        RETRY_TIMER.put(key, num);
        return num;
    }


    public static int get(String key) {
        if (!RETRY_TIMER.containsKey(key)) {
            return 0;
        }
        return RETRY_TIMER.get(key);
    }

    public static void reset(String key) {
        RETRY_TIMER.remove(key);
    }
}
