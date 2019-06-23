package com.smart.shiro.redis.cache

import com.smart.starter.redis.service.RedisService
import org.apache.shiro.cache.Cache
import org.apache.shiro.cache.CacheException
import java.util.*




/**
 * redis shiro缓存
 * @author ming
 * 2019/6/22 下午3:51
 */
class RedisCache<K: Any, V: Any> : Cache<K, V> {

    private var redisService: RedisService

    private var keyPrefix = "shiro_redis:"

    constructor(redisService: RedisService) {
        this.redisService = redisService
    }

    constructor(redisService: RedisService, prefix: String): this(redisService) {
        this.keyPrefix = prefix
    }

    override fun values(): MutableCollection<V> {
        val keys = redisService.keys(this.keyPrefix) ?: return Collections.emptyList()
        val values = redisService.batchGet(keys)
        if (values == null || values.isEmpty()) {
            return Collections.emptyList()
        } else {
            val newValues = ArrayList<V>()
            for (value in values) {
//                if (value != null) {
//                    newValues.add(value as V)
//                }
                newValues.add(value as V)
            }
            return newValues
        }
    }

    override fun clear() {
        this.redisService.matchDelete(this.keyPrefix);
    }

    override fun put(key: K, value: V): V {
        try {
            redisService.put(prefixKey(key), value)
            return value
        } catch (t: Throwable) {
            throw CacheException(t)
        }
    }

    override fun remove(key: K): V? {
        try {
            val previous = get(key)
            this.redisService.delete(prefixKey(key))
            return previous
        } catch (t: Throwable) {
            throw CacheException(t)
        }
    }

    override fun size(): Int {
        val key = this.redisService.keys(this.keyPrefix)
        return key?.size ?: 0
    }

    override fun get(key: K): V? {
        try {
            return redisService.get(prefixKey(key)) as V?
        } catch (t: Throwable) {
            throw CacheException(t)
        }
    }

    override fun keys(): MutableSet<K> {
        val keys = this.redisService.keys(this.keyPrefix)
        if (keys == null || keys.isEmpty()) {
            return Collections.emptySet()
        } else {
            val keysNew = HashSet<K>(keys.size)
            for (key in keys) {
                keysNew.add(key as K)
            }
            return keysNew
        }
    }

    /**
     * 获取key_re
     */
    private fun prefixKey(key: Any): String {
        return this.keyPrefix + key
    }

}