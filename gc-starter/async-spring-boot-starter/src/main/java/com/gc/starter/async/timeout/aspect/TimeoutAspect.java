package com.gc.starter.async.timeout.aspect;

import com.gc.starter.async.timeout.annotation.Timeout;
import com.gc.starter.async.timeout.handler.CompleteHandler;
import com.gc.starter.async.timeout.handler.DefaultTimeoutHandler;
import com.gc.starter.async.timeout.handler.TimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.async.WebAsyncTask;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 请求超时切面
 * @author shizhongming
 * 2020/5/28 1:57 下午
 */
@Aspect
@Slf4j
public class TimeoutAspect {


    private final ApplicationContext applicationContext;

    private CompleteHandler completeHandler;

    public TimeoutAspect(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 切面
     */
    @Pointcut("@annotation(com.gc.starter.async.timeout.annotation.Timeout)")
    public void timeoutPointCut() {
        // 切点
    }

    /**
     * 超时执行逻辑
     * @param point 切点
     * @return 执行结果
     * @throws Throwable
     */
    @Around("timeoutPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        final Signature signature = point.getSignature();
        if (signature instanceof MethodSignature) {
            // 获取目标方法对应类
            final Method method = ((MethodSignature) signature).getMethod();
            final Class<?> controllClass = method.getDeclaringClass();
            // 验证是否是controller
            final Controller controllerAnnotation = AnnotationUtils.findAnnotation(controllClass, Controller.class);
            if (!Objects.isNull(controllerAnnotation)) {
                return this.doAsyncTimeout(method, point);
            }
        }
        return point.proceed();
    }

    /**
     * 执行timeout检查
     * @param method 目标方法
     * @param point 切点
     * @return 异步执行任务
     */
    private Object doAsyncTimeout(@NonNull Method method, @NonNull ProceedingJoinPoint point) {
        final Timeout timeoutAnnotation = AnnotationUtils.findAnnotation(method, Timeout.class);
        Assert.notNull(timeoutAnnotation, "系统发生未知错误");
        // 获取超时时间
        final long timeout = timeoutAnnotation.value();
        log.debug("外部线程：{}", Thread.currentThread().getName());
        final Object[] result = new Object[1];
        WebAsyncTask<Object> webAsyncTask = new WebAsyncTask<>(timeout, () -> {
            log.debug("内部线程：{}", Thread.currentThread().getName());
            try {
                result[0] = point.proceed();
                return result[0];
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        });
        // 绑定超时操作
        webAsyncTask.onTimeout(() -> this.getTimeoutHandler(timeoutAnnotation).handler(point, webAsyncTask));
        // 绑定执行结束操作
        if (!Objects.isNull(this.completeHandler)) {
            webAsyncTask.onCompletion(() -> this.completeHandler.handler(point, webAsyncTask, result[0]));
        }
        return webAsyncTask;
    }

    /**
     * 获取超时执行器
     * @param timeoutAnnotation 超时注解
     * @return 超时执行器
     */
    @NonNull
    private TimeoutHandler getTimeoutHandler(@NonNull Timeout timeoutAnnotation) {
        // 获取类型/名字
        final Class<? extends TimeoutHandler> clazz = timeoutAnnotation.handlerClass();
        final String beanName = timeoutAnnotation.handlerName();
        boolean userName = false;
        if (Objects.equals(clazz, DefaultTimeoutHandler.class) && !StringUtils.isEmpty(beanName)) {
            userName = true;
        }
        if (userName) {
            Object bean = this.applicationContext.getBean(beanName);
            // 类型不匹配抛出异常
            if (!(bean instanceof TimeoutHandler)) {
                throw new BeanNotOfRequiredTypeException(beanName, TimeoutHandler.class, bean.getClass());
            }
            return (TimeoutHandler) bean;
        } else {
            return this.applicationContext.getBean(clazz);
        }
    }

    /**
     * 注入结束执行器
     * @param completeHandler 结束执行器
     */
    @Autowired(required = false)
    public void setCompleteHandler(CompleteHandler completeHandler) {
        this.completeHandler = completeHandler;
    }
}
