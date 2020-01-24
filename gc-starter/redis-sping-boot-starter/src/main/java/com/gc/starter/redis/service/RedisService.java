package com.gc.starter.redis.service;

import com.gc.cache.service.CacheService;

/**
 * redis服务层
 * @author zhongming
 */
public interface RedisService extends CacheService {


    void matchDelete(Object prefixKey);
}
