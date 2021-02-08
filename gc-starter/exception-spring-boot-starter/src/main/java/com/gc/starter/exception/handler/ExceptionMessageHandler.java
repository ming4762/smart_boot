package com.gc.starter.exception.handler;

import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;

/**
 * 异常信息处理类
 * @author shizhongming
 * 2021/1/24 12:55 下午
 */
public interface ExceptionMessageHandler {

    /**
     * 异常返回信息
     * @param e Exception
     * @param request HttpServletRequest
     * @return 信息
     */
    Object message(Exception e, @Nullable HttpServletRequest request);
}
