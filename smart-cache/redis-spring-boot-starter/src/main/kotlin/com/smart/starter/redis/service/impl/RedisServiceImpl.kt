package com.smart.starter.redis.service.impl

import com.smart.starter.redis.service.RedisService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.connection.RedisStringCommands
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.types.Expiration
import org.springframework.data.redis.serializer.RedisSerializer
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * redis缓存服务
 * @author ming
 * 2019/6/22 下午3:28
 */
class RedisServiceImpl : RedisService {

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, Any>

    /**
     * 写入缓存
     * @param key 缓存的key
     * @param value 缓存的value
     */
    override fun put(key: String, value: Any) {
        val valueOperations = redisTemplate.opsForValue()
        valueOperations.set(key, value)
    }


    /**
     * 写入缓存，并设置有效时间
     * @param key 缓存的key
     * @param value 缓存的value
     * @param timeout 缓存的有效时间（单位：秒）
     */
    override fun put(key: String, value: Any, timeout: Long) {
        val valueOperations = redisTemplate.opsForValue()
        valueOperations.set(key, value, timeout, TimeUnit.SECONDS)
    }

    /**
     * 写入缓存，并设置过期时间
     * @param key 缓存的key
     * @param value 缓存的value
     * @param expireTime 缓存过期时间
     */
    override fun put(key: String, value: Any, expireTime: Date) {
        this.put(key, value)
        redisTemplate.expireAt(key, expireTime)
    }

    /**
     * 批量写入缓存
     * @param keyValues 缓存键值对
     */
    override fun batchPut(keyValues: Map<String, Any>) {
        this.redisTemplate.opsForValue().multiSet(keyValues)
    }

    /**
     * 批量写入缓存,并设置有效时间
     * @param keyValues 缓存键值对
     * @param timeout 缓存有效时间（单位：秒）
     */
    override fun batchPut(keyValues: Map<String, Any>, timeout: Long) {
        redisTemplate.executePipelined { connection ->
            val keySerializer = redisTemplate.keySerializer as RedisSerializer<String>
            val valueSerializer = redisTemplate.valueSerializer as RedisSerializer<Any>
            for ((key, value) in keyValues) {
                connection.set(keySerializer.serialize(key)!!, valueSerializer.serialize(value)!!,
                        Expiration.seconds(timeout), RedisStringCommands.SetOption.UPSERT)

            }
        }
    }

    /**
     * 批量写入缓存,并设置过期时间
     * @param keyValues 缓存键值对
     * @param expireTime 缓存过期时间
     */
    override fun batchPut(keyValues: Map<String, Any>, expireTime: Date) {
        redisTemplate.executePipelined { connection ->
            val keySerializer = redisTemplate.keySerializer as RedisSerializer<String>
            val valueSerializer = redisTemplate.valueSerializer as RedisSerializer<Any>
            for ((key, value) in keyValues) {
                connection.set(keySerializer.serialize(key)!!, valueSerializer.serialize(value)!!,
                        Expiration.milliseconds(expireTime.getTime() - System.currentTimeMillis()),
                        RedisStringCommands.SetOption.UPSERT)
            }
        }
    }

    /**
     * 读取缓存
     * @param key 键
     * @return 值
     */
    override fun get(key: String): Any? {
        return this.redisTemplate.opsForValue().get(key)
    }

    /**
     * 读取缓存
     * @param key 键
     * @param clazz 值类型
     * @param <T> 值类型
     * @return 值
    </T> */
    override fun <T> get(key: String, clazz: Class<T>): T? {
        return this.redisTemplate.opsForValue().get(key) as T?
    }

    /**
     * 批量读取缓存
     * @param keys 键集合
     * @return 值集合
     */
    override fun batchGet(keys: Collection<String>): List<Any>? {
        return this.redisTemplate.opsForValue().multiGet(keys)
    }

    /**
     * 批量量读取缓存
     * @param keys 键集合
     * @param cls 值的类型
     * @param <T> 值的类型
     * @return 值
    </T> */
    override fun <T> batchGet(keys: Collection<String>, cls: Class<T>): List<T>? {
        return this.redisTemplate.opsForValue().multiGet(keys) as List<T>?
    }

    /**
     * 删除缓存
     * @param key 键
     */
    override fun delete(key: String) {
        this.redisTemplate.delete(key)
    }

    /**
     * 批量删除
     * @param keys 键集合
     */
    override fun batchDelete(keys: Collection<String>) {
        this.redisTemplate.delete(keys)
    }


}