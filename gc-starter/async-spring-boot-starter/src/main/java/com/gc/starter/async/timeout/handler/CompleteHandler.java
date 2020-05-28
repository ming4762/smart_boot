package com.gc.starter.async.timeout.handler;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.async.WebAsyncTask;

/**
 * 结束执行器
 * @author shizhongming
 * 2020/5/28 4:04 下午
 */
public interface CompleteHandler {

    /**
     * 超时执行器
     * @param point 切点
     * @param task 异步任务
     * @param result 执行结果
     * @param <T>
     */
    <T> void handler(@NonNull ProceedingJoinPoint point, WebAsyncTask<T> task, @Nullable Object result);
}
