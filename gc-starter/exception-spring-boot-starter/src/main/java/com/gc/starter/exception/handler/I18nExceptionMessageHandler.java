package com.gc.starter.exception.handler;

import com.gc.common.base.exception.BaseException;
import com.gc.common.base.http.HttpStatus;
import com.gc.common.base.message.Result;
import com.gc.common.base.utils.JsonUtils;
import com.gc.common.i18n.exception.I18nException;
import com.gc.common.i18n.utils.I18nUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * @author shizhongming
 * 2021/1/24 1:03 下午
 */
@Slf4j
public class I18nExceptionMessageHandler implements ExceptionMessageHandler {

    private static final String MISMATCH_ERROR_MESSAGE = "argument type mismatch\nController";

    @Override
    public Object message(Exception e, HttpServletRequest request) {
        if (e instanceof NoHandlerFoundException) {
            log.error("NoHandlerFoundException: 请求方法 {}, 请求路径 {}", ((NoHandlerFoundException) e).getRequestURL(), ((NoHandlerFoundException) e).getHttpMethod());
            return this.getMessage(HttpStatus.NOT_FOUND);
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            log.error("HttpRequestMethodNotSupportedException: 当前请求方式 {}, 支持请求方式 {}", ((HttpRequestMethodNotSupportedException) e).getMethod(), JsonUtils.toJsonString(((HttpRequestMethodNotSupportedException) e).getSupportedHttpMethods()));
            return this.getMessage(HttpStatus.METHOD_NOT_ALLOWED);
        } else if (e instanceof MethodArgumentNotValidException) {
            log.error("MethodArgumentNotValidException", e);
            // TODO: 未处理
            return Result.failure(((MethodArgumentNotValidException) e).getBindingResult());
        } else if (e instanceof ConstraintViolationException) {
            log.error("ConstraintViolationException", e);
            Set<ConstraintViolation<?>> constraintViolations =  ((ConstraintViolationException) e).getConstraintViolations();
            String message = constraintViolations.isEmpty() ? "未知异常" : constraintViolations.iterator().next().getMessage();
            return this.getMessage(HttpStatus.BAD_REQUEST);
        } else if (e instanceof MethodArgumentTypeMismatchException) {
            log.error("MethodArgumentTypeMismatchException: 参数名 {}, 异常信息 {}", ((MethodArgumentTypeMismatchException) e).getName(), ((MethodArgumentTypeMismatchException) e).getMessage());
            return this.getMessage(HttpStatus.PARAM_NOT_MATCH);
        } else if (e instanceof HttpMessageNotReadableException) {
            log.error("HttpMessageNotReadableException: 错误信息 {}", ((HttpMessageNotReadableException) e).getMessage());
            return this.getMessage(HttpStatus.PARAM_NOT_NULL);
        } else if (e instanceof I18nException) {
            log.error(String.format("DataManagerException: 状态码 %s, 异常信息 %s", ((I18nException) e).getCode(), e.getMessage()), ((I18nException) e).getE());
            return Result.failure(I18nUtils.get(((I18nException) e).getI18nMessage()));
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
        log.error(e.getMessage(), e);
        return Result.ofStatus(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    private Result<String> getMessage(HttpStatus httpStatus) {
        if (I18nUtils.supportI18n()) {
            return Result.failure(I18nUtils.get(httpStatus));
        }
        return Result.ofStatus(httpStatus);
    }

}
