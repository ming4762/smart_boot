package com.gc.auth.cache.guava.cache;


import com.gc.auth.core.properties.AuthProperties;
import com.gc.auth.core.service.AuthCache;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * authCache Guava实现
 * @author shizhongming
 * 2020/9/11 9:40 下午
 */
public class GuavaAuthCache implements AuthCache<String, Object> {

    private final Cache<String, CacheObject> cache;

    public GuavaAuthCache(AuthProperties authProperties) {
        // 求最大超时时间
        final AuthProperties.Timeout sessionTimeout = authProperties.getSession().getTimeout();
        final Long maxTime = ImmutableList.of(sessionTimeout.getMobile(), sessionTimeout.getGlobal(), sessionTimeout.getRemember()).stream()
                .max(Long::compareTo).orElse(Long.MAX_VALUE);
        // 创建缓存起
        cache = CacheBuilder.newBuilder()
                .expireAfterAccess(maxTime, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public void put(@NonNull String key, @NonNull Object value, long timeout) {
        final CacheObject cacheObject = CacheObject.builder()
                .operationTime(LocalDateTime.now())
                .timeout(timeout)
                .data(value)
                .build();
        cache.put(key, cacheObject);
    }

    @Override
    public void expire(@NonNull String key, long timeout) {
        final CacheObject cacheObject = cache.getIfPresent(key);
        if (Objects.nonNull(cacheObject)) {
            cacheObject.setOperationTime(LocalDateTime.now());
            cacheObject.setTimeout(timeout);
            cache.put(key, cacheObject);
        }
    }

    @Override
    public Object get(@NonNull String key) {
        final CacheObject cacheObject = cache.getIfPresent(key);
        if (Objects.nonNull(cacheObject)) {
            if (Objects.nonNull(cacheObject.getTimeout()) && cacheObject.getOperationTime().plusSeconds(cacheObject.getTimeout()).isBefore(LocalDateTime.now())) {
                // 判断是否超时
                cache.invalidate(key);
                return null;
            }
            return cacheObject.getData();
        }
        return null;
    }

    @Override
    public void remove(@NonNull String key) {
        cache.invalidate(key);
    }

    @Override
    public Set<String> keys() {
        return cache.asMap().keySet();
    }

    /**
     * 匹配获取
     * @param patternKey 匹配的key
     * @return 所有对象
     */
    @Override
    @NonNull
    public Set<Object> matchGet(@NonNull String patternKey) {
        final Set<Object> result = Sets.newHashSet();
        this.cache.asMap().forEach((key, value) -> {
            if (key.startsWith(patternKey)) {
                result.add(this.get(key));
            }
        });
        return result;
    }

    /**
     * 批量获取
     * @param keys keys
     * @return 获取的缓存
     */
    @Override
    @NonNull
    public Set<Object> batchGet(@NonNull Collection<String> keys) {
        final Set<Object> result = Sets.newHashSet();
        this.cache.asMap().forEach((key, value) -> {
            if (keys.contains(key)) {
                result.add(this.get(key));
            }
        });
        return result;
    }
}
