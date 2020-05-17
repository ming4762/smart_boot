package com.gc.starter.redis.service.impl;

import com.gc.starter.redis.service.RedisService;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * redis服务层
 * @author shizhongming
 * 2020/1/17 8:47 下午
 */
public class RedisServiceImpl implements RedisService {

    private RedisTemplate<Object, Object> redisTemplate;

    public RedisServiceImpl(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void matchDelete(@NonNull Object prefixKey) {
        // todo: 待开发
    }

    @Override
    public void put(@NonNull Object key, @NonNull Object value) {
        redisTemplate.opsForValue().set(key, value);

    }

    @Override
    public void put(@NonNull Object key, @NonNull Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    @Override
    public void put(@NonNull Object key, @NonNull Object value, @NonNull Date expireTime) {
        this.put(key, value);
        redisTemplate.expireAt(key, expireTime);
    }

    @Override
    public void batchPut(@NonNull Map<Object, Object> keyValues) {
        this.redisTemplate.opsForValue().multiSet(keyValues);
    }

    @Override
    public void batchPut(@NonNull Map<Object, Object> keyValues, long timeout) {
        this.redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            RedisSerializer keySerializer = this.redisTemplate.getKeySerializer();
            RedisSerializer valueSerializer = this.redisTemplate.getValueSerializer();
            keyValues.forEach((key, value) -> connection.set(keySerializer.serialize(key), valueSerializer.serialize(value), Expiration.seconds(timeout),  RedisStringCommands.SetOption.UPSERT));
            return null;
        });
    }

    /**
     * 设置key的过期时间
     * @param key key
     * @param timeout 过期时间
     */
    @Override
    public void expire(@NonNull Object key, long timeout) {
        this.redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 批量设置key的过期时间
     * @param keys key
     * @param timeout 过期时间
     */
    @Override
    public void batchExpire(@NonNull Collection<Object> keys, long timeout) {
        keys.forEach(key -> this.expire(key, timeout));
    }

    @Override
    public void batchPut(@NonNull Map<Object, Object> keyValues, @NonNull Date expireTime) {
        this.redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            RedisSerializer keySerializer = this.redisTemplate.getKeySerializer();
            RedisSerializer valueSerializer = this.redisTemplate.getValueSerializer();
            keyValues.forEach((key, value) -> connection.set(keySerializer.serialize(key), valueSerializer.serialize(value), Expiration.milliseconds(expireTime.getTime() - System.currentTimeMillis()),  RedisStringCommands.SetOption.UPSERT));
            return null;
        });
    }

    @Override
    public @Nullable Object get(@NonNull Object key) {
        return this.redisTemplate.opsForValue().get(key);
    }

    @Override
    public <T> T get(@NonNull Object key, @NonNull Class<T> clazz) {
        return (T) this.get(key);
    }

    @Override
    public @Nullable List<Object> batchGet(@NonNull Collection<Object> keys) {
        return this.redisTemplate.opsForValue().multiGet(keys);
    }

    @Override
    public @Nullable <T> List<T> batchGet(@NonNull Collection<Object> keys, @NonNull Class<T> clazz) {
        return (List<T>) this.redisTemplate.opsForValue().multiGet(keys);
    }

    @Override
    public void delete(@NonNull Object key) {
        this.redisTemplate.delete(key);
    }

    @Override
    public void batchDelete(@NonNull List<Object> keys) {
        this.redisTemplate.delete(keys);
    }
}
