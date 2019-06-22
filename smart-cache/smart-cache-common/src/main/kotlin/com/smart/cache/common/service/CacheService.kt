package com.smart.cache.common.service

import java.util.*

/**
 * 缓存服务
 * @author ming
 * 2019/6/22 下午3:22
 */
interface CacheService {

    /**
     * 写入缓存
     * @param key 缓存的key
     * @param value 缓存的value
     */
    fun put(key: String, value: Any)

    /**
     * 写入缓存，并设置有效时间
     * @param key 缓存的key
     * @param value 缓存的value
     * @param timeout 缓存的有效时间（单位：秒）
     */
    fun put(key: String, value: Any, timeout: Long)

    /**
     * 写入缓存，并设置过期时间
     * @param key 缓存的key
     * @param value 缓存的value
     * @param expireTime 缓存过期时间
     */
    fun put(key: String, value: Any, expireTime: Date)

    /**
     * 批量写入缓存
     * @param keyValues 缓存键值对
     */
    fun batchPut(keyValues: Map<String, Any>)

    /**
     * 批量写入缓存,并设置有效时间
     * @param keyValues 缓存键值对
     * @param timeout 缓存有效时间（单位：秒）
     */
    fun batchPut(keyValues: Map<String, Any>, timeout: Long)

    /**
     * 批量写入缓存,并设置过期时间
     * @param keyValues 缓存键值对
     * @param expireTime 缓存过期时间
     */
    fun batchPut(keyValues: Map<String, Any>, expireTime: Date)

    /**
     * 读取缓存
     * @param key 键
     * @return 值
     */
    fun get(key: String): Any?

    /**
     * 读取缓存
     * @param key 键
     * @param clazz 值类型
     * @param <T> 值类型
     * @return 值
    </T> */
    fun <T> get(key: String, clazz: Class<T>): T?

    /**
     * 批量读取缓存
     * @param keys 键集合
     * @return 值集合
     */
    fun batchGet(keys: Collection<String>): List<Any>?

    /**
     * 批量量读取缓存
     * @param keys 键集合
     * @param cls 值的类型
     * @param <T> 值的类型
     * @return 值
    </T> */
    fun <T> batchGet(keys: Collection<String>, clazz: Class<T>): List<T>?


    /**
     * 删除缓存
     * @param key 键
     */
    fun delete(key: String)

    /**
     * 批量删除
     * @param keys 键集合
     */
    fun batchDelete(keys: Collection<String>)
}