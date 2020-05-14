package com.gc.starter.ratelimit.aspect;

import com.gc.starter.ratelimit.RateLimiterProperties;
import com.gc.starter.ratelimit.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.lang.reflect.Method;

/**
 * @author shizhongming
 * 2020/4/16 9:29 下午
 */
@Aspect
@Slf4j
public class RateLimiterAspect {

    private final static String SEPARATOR = ":";
    private final static String REDIS_LIMIT_KEY_PREFIX = "limit:";

    private final StringRedisTemplate stringRedisTemplate;
    private final RedisScript<Long> limitRedisScript;

    private final RateLimiterProperties properties;

    public RateLimiterAspect(StringRedisTemplate stringRedisTemplate, RedisScript<Long> limitRedisScript, RateLimiterProperties properties) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.limitRedisScript = limitRedisScript;
        this.properties = properties;
    }

    /**
     * 限流切面
     */
    @Pointcut("@annotation(com.gc.starter.ratelimit.annotation.RateLimiter)")
    public void rateLimit() {
        // 限流切面
    }

    @Around("rateLimit()")
    public Object pointcut(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        // 通过 AnnotationUtils.findAnnotation 获取 RateLimiter 注解
        RateLimiter rateLimiter = AnnotationUtils.findAnnotation(method, RateLimiter.class);
        return point.proceed();
    }
}
