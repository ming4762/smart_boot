package com.gc.auth.core.service;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.Set;

/**
 * 认证缓存管理器
 * @author shizhongming
 * 2020/7/1 1:59 下午
 */
public interface AuthCache<K, V> {

    /**
     * 添加缓存
     * @param key key
     * @param value value
     * @param timeout 超时时间
     */
    void put(@NonNull K key, @NonNull V value, long timeout);

    /**
     * 设置超时时间
     * @param key key
     * @param timeout 超时时间
     */
    void expire(@NonNull K key, long timeout);

    /**
     * 获取缓存内容
     * @param key key
     * @return value
     */
    @Nullable
    V get(@NonNull K key);

    /**
     * 删除缓存
     * @param key key
     */
    void remove(@NonNull K key);

    /**
     * 获取所有key的集合
     * @return key的集合
     */
    Set<K> keys();

    /**
     * 匹配获取
     * @param patternKey 匹配的key
     * @return 所有对象
     */
    @NonNull
    Set<V> matchGet(String patternKey);

    /**
     * 批量获取
     * @param keys keys
     * @return 获取的缓存
     */
    @NonNull
    Set<V> batchGet(@NonNull Collection<K> keys);
}
