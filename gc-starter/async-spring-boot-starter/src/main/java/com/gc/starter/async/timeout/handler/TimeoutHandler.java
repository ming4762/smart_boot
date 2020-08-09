package com.gc.starter.async.timeout.handler;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.lang.NonNull;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.context.request.async.WebAsyncTask;

/**
 * 超时执行器
 * @author shizhongming
 * 2020/5/28 3:37 下午
 */
public interface TimeoutHandler {

    /**
     * 超时执行器
     * @param point 切点
     * @param task 异步任务
     * @param <T> 泛型
     * @return 超时结果
     */
    default <T> Object handler(@NonNull ProceedingJoinPoint point, WebAsyncTask<T> task) {
        return new AsyncRequestTimeoutException();
    }
}
