package com.gc.starter.log.handler;

import com.gc.starter.log.annotation.Log;
import com.gc.starter.log.model.SysLogPO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 日志执行器
 * @author shizhongming
 * 2020/6/24 8:06 下午
 */
public interface LogHandler {

    /**
     * 保存次日在
     * @param sysLog 日志信息
     * @param point 切点
     * @param logAnnotation 日志注解
     * @param time 用户
     * @param code 返回编码
     * @param result 返回值
     * @param errorMessage 错误信息
     * @return 是否保存成功
     */
    boolean save(@NonNull SysLogPO sysLog, @NonNull ProceedingJoinPoint point, @NonNull Log logAnnotation, long time, int code, @Nullable Object result, @Nullable String errorMessage);
}
