package com.gc.starter.exception.handler;

import com.gc.common.auth.utils.AuthUtils;
import com.gc.common.base.exception.BaseException;
import com.gc.common.base.http.HttpStatus;
import com.gc.common.base.message.Result;
import com.gc.common.base.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * 全局异常管理
 * @author shizhongming
 * 2020/2/15 7:12 下午
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String MISMATCH_ERROR_MESSAGE = "argument type mismatch\nController";

    private final AsyncNoticeHandler asyncNoticeHandler;

    public GlobalExceptionHandler(AsyncNoticeHandler asyncNoticeHandler) {
        this.asyncNoticeHandler = asyncNoticeHandler;
    }

    /**
     * 处理异常
     * @param e 异常信息
     * @return 处理后的信息
     */
    public static Result<String> doException(Exception e) {
        if (e instanceof NoHandlerFoundException) {
            log.error("NoHandlerFoundException: 请求方法 {}, 请求路径 {}", ((NoHandlerFoundException) e).getRequestURL(), ((NoHandlerFoundException) e).getHttpMethod());
            return Result.ofStatus(HttpStatus.NOT_FOUND);
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            log.error("HttpRequestMethodNotSupportedException: 当前请求方式 {}, 支持请求方式 {}", ((HttpRequestMethodNotSupportedException) e).getMethod(), JsonUtils.toJsonString(((HttpRequestMethodNotSupportedException) e).getSupportedHttpMethods()));
            return Result.ofStatus(HttpStatus.METHOD_NOT_ALLOWED);
        } else if (e instanceof MethodArgumentNotValidException) {
            log.error("MethodArgumentNotValidException", e);
            return Result.failure(((MethodArgumentNotValidException) e).getBindingResult());
        } else if (e instanceof ConstraintViolationException) {
            log.error("ConstraintViolationException", e);
            Set<ConstraintViolation<?>> constraintViolations =  ((ConstraintViolationException) e).getConstraintViolations();
            String message = constraintViolations.isEmpty() ? "未知异常" : constraintViolations.iterator().next().getMessage();
            return Result.failure(HttpStatus.BAD_REQUEST.getCode(), message, null);
        } else if (e instanceof MethodArgumentTypeMismatchException) {
            log.error("MethodArgumentTypeMismatchException: 参数名 {}, 异常信息 {}", ((MethodArgumentTypeMismatchException) e).getName(), ((MethodArgumentTypeMismatchException) e).getMessage());
            return Result.ofExceptionStatus(HttpStatus.PARAM_NOT_MATCH, e);
        } else if (e instanceof HttpMessageNotReadableException) {
            log.error("HttpMessageNotReadableException: 错误信息 {}", ((HttpMessageNotReadableException) e).getMessage());
            return Result.ofExceptionStatus(HttpStatus.PARAM_NOT_NULL, e);
        } else if (e instanceof BaseException) {
            log.error(String.format("DataManagerException: 状态码 %s, 异常信息 %s", ((BaseException) e).getCode(), e.getMessage()), ((BaseException) e).getE());
            return Result.failure((BaseException)e);
        } else if (e instanceof AccessDeniedException) {
            // security 认证错误不拦截，交给security过滤器处理
            throw (AccessDeniedException)e;
        } else if (e instanceof InternalAuthenticationServiceException) {
            log.error("登录异常");
            return Result.failure(HttpStatus.UNAUTHORIZED.getCode(), e.getMessage());
        } else if (e instanceof DuplicateKeyException) {
            log.error("key冲突异常", e);
            return Result.failure("key冲突错误", e.getMessage());
        } else if (e instanceof IllegalStateException && StringUtils.startsWith(e.getMessage(), MISMATCH_ERROR_MESSAGE)) {
            log.error("参数类型不支持", e);
            return Result.failure("参数类型不支持", e.getMessage());
        } else if (e instanceof HttpMediaTypeNotSupportedException) {
            log.error(HttpStatus.UNSUPPORTED_MEDIA_TYPE.getMessage(), e);
            return Result.failure(HttpStatus.UNSUPPORTED_MEDIA_TYPE.getCode(), e.getMessage());
        }
        log.error("系统发生未知异常", e);
        return Result.ofStatus(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result<String> handlerException(Exception e, HttpServletRequest request) {
        // 处理异常通知
        this.asyncNoticeHandler.noticeException(e, AuthUtils.getCurrentUser(), request);
        return GlobalExceptionHandler.doException(e);
    }
}
