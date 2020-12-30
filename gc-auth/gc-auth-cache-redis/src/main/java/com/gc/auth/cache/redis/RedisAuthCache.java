package com.gc.auth.cache.redis;

import com.gc.auth.core.service.AuthCache;
import com.gc.cache.service.CacheService;
import com.google.common.collect.Sets;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shizhongming
 * 2020/7/1 3:54 下午
 */
public class RedisAuthCache implements AuthCache<String, Object> {

    private final CacheService cacheService;

    public RedisAuthCache(CacheService cacheService) {
        this.cacheService = cacheService;
    }
    /**
     * 添加缓存
     * @param key key
     * @param value value
     * @param timeout 超时时间
     */
    @Override
    public void put(@NonNull String key, @NonNull Object value, long timeout) {
        this.cacheService.put(key, value, timeout);
    }

    /**
     * 设置超时时间
     * @param key key
     * @param timeout 超时时间
     */
    @Override
    public void expire(@NonNull String key, long timeout) {
        this.cacheService.expire(key, timeout);
    }

    /**
     * 获取缓存内容
     * @param key key
     * @return value
     */
    @Override
    @Nullable
    public Object get(@NonNull String key) {
        return this.cacheService.get(key);
    }

    /**
     * 删除缓存
     * @param key key
     */
    @Override
    public void remove(@NonNull String key) {
        this.cacheService.delete(key);
    }

    /**
     * 获取所有key的集合
     * @return key的集合
     */
    @Override
    public Set<String> keys() {
        // TODO: 待开发
        return Sets.newHashSet();
    }

    /**
     * 匹配获取
     * @param patternKey 匹配的key
     * @return 所有对象
     */
    @Override
    @NonNull
    public Set<Object> matchGet(String patternKey) {
        final List<String> keys = this.cacheService.matchKeys(patternKey)
                .stream().map(Object::toString)
                .collect(Collectors.toList());
        return this.batchGet(keys);
    }

    /**
     * 批量获取
     * @param keys keys
     * @return 获取的缓存
     */
    @Override
    @NonNull
    public Set<Object> batchGet(@NonNull Collection<String> keys) {
        List<Object> data = this.cacheService.batchGet(Collections.singleton(keys));
        return Objects.isNull(data) ? Sets.newHashSet() : Sets.newHashSet(data);
    }
}
