package com.gc.common.base.exception.handler;

import com.gc.common.base.message.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 参数校验异常
 * @author jackson
 * 2020/3/12 9:52 下午
 */
@ControllerAdvice
public class ValidatorExceptionHandler {
    /**
     * 拦截参数校验异常
     * @author jackson
     * @return 校验结果
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<Object> parameterValidateException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        return Result.failure(result);
    }

    /**
     * 拦截参数校验异常
     * @author jackson
     * @return 校验结果
     */
    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    public Result<Object> parameterBindException(BindException e) {
        BindingResult result = e.getBindingResult();
        return Result.failure(result);
    }
}
