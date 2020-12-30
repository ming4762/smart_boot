package com.gc.starter.log.handler;

import com.gc.auth.core.utils.AuthUtils;
import com.gc.starter.log.annotation.Log;
import com.gc.starter.log.model.SysLogPO;
import com.gc.starter.log.service.SysLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.lang.NonNull;

/**
 * 默认的日志执行器
 * @author shizhongming
 * 2020/6/24 8:24 下午
 */
public class DefaultLogHandler implements LogHandler {

    private final SysLogService sysLogService;

    public DefaultLogHandler(SysLogService sysLogService) {
        this.sysLogService = sysLogService;
    }

    /**
     * 保存次日在
     * @param sysLog 日志信息
     * @param point 切点
     * @param logAnnotation 日志注解
     * @param time 用户
     * @param code 返回编码
     * @param errorMessage 错误信息
     * @return 是否保存成功
     */
    @Override
    public boolean save(@NonNull SysLogPO sysLog, @NonNull ProceedingJoinPoint point, @NonNull Log logAnnotation, long time, int code, Object result, String errorMessage) {
        return this.sysLogService.saveWithUser(sysLog, AuthUtils.getCurrentUserId());
    }
}
