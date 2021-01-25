package com.gc.starter.exception.handler;

import com.gc.auth.core.utils.AuthUtils;
import com.gc.common.base.message.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常管理
 * @author shizhongming
 * 2020/2/15 7:12 下午
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    private final AsyncNoticeHandler asyncNoticeHandler;

    private final ExceptionMessageHandler exceptionMessageHandler;

    public GlobalExceptionHandler(AsyncNoticeHandler asyncNoticeHandler, ExceptionMessageHandler exceptionMessageHandler) {
        this.asyncNoticeHandler = asyncNoticeHandler;
        this.exceptionMessageHandler = exceptionMessageHandler;
    }

    /**
     * 处理异常
     * @param e 异常信息
     * @return 处理后的信息
     */
    public static Result<String> doException(Exception e) {
        return Result.failure(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Object handlerException(Exception e, HttpServletRequest request) {
        // 处理异常通知
        this.asyncNoticeHandler.noticeException(e, AuthUtils.getCurrentUser(), request);
        return this.exceptionMessageHandler.message(e, request);
    }
}
