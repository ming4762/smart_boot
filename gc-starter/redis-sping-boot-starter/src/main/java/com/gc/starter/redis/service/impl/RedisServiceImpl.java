package com.gc.starter.redis.service.impl;

import com.gc.starter.redis.service.RedisService;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis服务层
 * @author shizhongming
 * 2020/1/17 8:47 下午
 */
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<Object, Object> redisTemplate;

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


    /**
     * 匹配key
     * @param patternKey 所有key
     * @return 所有key
     */
    @Override
    public List<Object> matchKeys(@NonNull String patternKey) {
        List<Object> result = new LinkedList<>();

        Cursor<byte[]> scan = this.scan(patternKey, null);
        while (scan.hasNext()) {
            String key = new String(scan.next());
            result.add(key);
        }
        return result;
    }

    /**
     * 获取扫描对象
     * @param patternKey 所有key
     * @param count 获取数量
     * @return 扫描对象
     */
    private Cursor<byte[]> scan(@NonNull String patternKey, Integer count) {
        // 创建扫描参数
        ScanOptions.ScanOptionsBuilder builder = ScanOptions.scanOptions()
                .match(patternKey);
        if (Objects.nonNull(count)) {
            builder.count(count);
        }
        ScanOptions scanOptions = builder.build();
        // 获取连接信息
        final RedisConnectionFactory factory = this.redisTemplate.getConnectionFactory();
        Assert.notNull(factory, "获取redis连接失败");
        final RedisConnection redisConnection = factory.getConnection();
        return redisConnection.scan(scanOptions);
    }
}
