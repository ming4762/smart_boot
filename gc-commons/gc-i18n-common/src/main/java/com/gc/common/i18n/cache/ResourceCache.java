package com.gc.common.i18n.cache;

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
     * 获取缓存
     * @param key key
     * @return value
     */
    String get(String key);

    /**
     * 设置缓存
     * @param key key
     * @param value value
     */
    void put(String key, String value);

    /**
     * 删除缓存
     * @param key key
     */
    void remove(String key);
}
