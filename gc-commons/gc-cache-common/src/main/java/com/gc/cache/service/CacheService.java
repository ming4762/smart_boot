package com.gc.cache.service;

import org.jetbrains.annotations.Nullable;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 缓存服务
 * @author shizhongming
 * 2020/1/17 8:32 下午
 */
public interface CacheService {
    /**
     * 写入缓存
     * @param key 缓存的key
     * @param value 缓存的value
     */
    void put(@NonNull Object key, @NonNull Object value);

    /**
     * 写入缓存，并设置有效时间
     * @param key 缓存的key
     * @param value 缓存的value
     * @param timeout 缓存的有效时间（单位：秒）
     */
    void put(@NonNull Object key, @NonNull Object value, long timeout);

    /**
     * 写入缓存，并设置过期时间
     * @param key 缓存的key
     * @param value 缓存的value
     * @param expireTime 缓存过期时间
     */
    void put(@NonNull Object key, @NonNull Object value, @NonNull Date expireTime);

    /**
     * 批量写入缓存
     * @param keyValues 缓存键值对
     */
    void batchPut(@NonNull Map<Object, Object> keyValues);

    /**
     * 批量写入缓存,并设置有效时间
     * @param keyValues 缓存键值对
     * @param timeout 缓存有效时间（单位：秒）
     */
    void batchPut(@NonNull Map<Object, Object> keyValues, long timeout);

    /**
     * 设置key的过期时间
     * @param key key
     * @param timeout 过期时间
     */
    void expire(@NonNull Object key, long timeout);

    /**
     * 批量设置key的过期时间
     * @param keys key
     * @param timeout 过期时间
     */
    void batchExpire(@NonNull Collection<Object> keys, long timeout);

    /**
     * 批量写入缓存,并设置过期时间
     * @param keyValues 缓存键值对
     * @param expireTime 缓存过期时间
     */
    void batchPut(@NonNull Map<Object, Object> keyValues, @NonNull Date expireTime);

    /**
     * 读取缓存
     * @param key 键
     * @return 值
     */
    @Nullable
    Object get(@NonNull Object key);

    /**
     * 读取缓存
     * @param key 键
     * @param clazz 值类型
     * @param <T> 值类型
     * @return 值
    </T> */
    @Nullable
    <T> T get(@NonNull Object key, @NonNull Class<T> clazz);

    /**
     * 批量读取缓存
     * @param keys 键集合
     * @return 值集合
     */
    @Nullable
    List<Object> batchGet(@NonNull Collection<Object> keys);

    /**
     * 批量量读取缓存
     * @param keys 键集合
     * @param clazz 值的类型
     * @param <T> 值的类型
     * @return 值
    </T> */
    @Nullable
    <T> List<T> batchGet(@NonNull Collection<Object> keys, @NonNull Class<T> clazz);

    /**
     * 删除缓存
     * @param key 键
     */
    void delete(@NonNull Object key);

    /**
     * 批量删除
     * @param keys 键集合
     */
    void batchDelete(@NonNull List<Object> keys);
}
