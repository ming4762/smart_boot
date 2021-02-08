package com.gc.common.i18n.cache;

import org.springframework.lang.NonNull;

import java.util.Locale;
import java.util.Map;

/**
 * 资源缓存器接口
 * @author ShiZhongMing
 * 2021/1/31 11:42
 * @since 1.0
 */
public interface ResourceCache {

    /**
     * 清空缓存器
     */
    void clear();

    /**
     * 获取所有资源
     * @param locale 资源信息
     * @return 资源
     */
    Map<String, String> get(@NonNull Locale locale);

    /**
     * 是否包含指定语言
     * @param locale locale
     * @return 是否包含指定语言
     */
    boolean contain(@NonNull Locale locale);

    /**
     * 获取缓存
     * @param locale locale
     * @param key key
     * @return value
     */
    String get(@NonNull Locale locale, @NonNull String key);

    /**
     * 设置缓存
     * @param locale locale
     * @param key key
     * @param value value
     */
    void put(@NonNull Locale locale, @NonNull String key, @NonNull String value);

    /**
     * 添加缓存
     * @param locale locale
     * @param value value
     */
    void putAll(@NonNull Locale locale, @NonNull Map<String, String> value);

    /**
     * 删除缓存
     * @param locale locale
     * @param key key
     * @return 移除的值
     */
    String remove(@NonNull Locale locale, @NonNull String key);

    /**
     * 删除缓存
     * @param locale Locale
     * @return 移除的值
     */
    Map<String, String> remove(@NonNull Locale locale);
}
