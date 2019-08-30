package com.smart.shiro.redis.cache

import com.smart.starter.redis.service.RedisService
import org.apache.shiro.cache.Cache
import org.apache.shiro.cache.CacheManager
import java.util.concurrent.ConcurrentHashMap

/**
 * shiro redis缓存管理器
 * @author ming
 * 2019/6/22 下午6:29
 */
class RedisCacheManager : CacheManager {

    private val caches = ConcurrentHashMap<String, Cache<Any, Any>>()

    lateinit var redisService: RedisService

    var ident = "normal"

    var keyPrefix = "shiro_redis_cache:"

    override fun <K : Any, V : Any> getCache(name: String): Cache<K, V> {
        var cache = caches[name]
        if (cache == null) {
            cache = RedisCache(redisService, "${keyPrefix}${ident}_$name")
            caches[name] = cache
        }
        return cache as Cache<K, V>
    }
}