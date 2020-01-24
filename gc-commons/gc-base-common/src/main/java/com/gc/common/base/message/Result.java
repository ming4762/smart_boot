package com.gc.common.base.message;


import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;

import java.lang.reflect.Field;
import java.util.Optional;

/**
 * 后台传输工具类
 * @author jackson
 */
@Getter
@Setter
public class Result<T> {

    private Integer code = ResultCodeEnum.SUCCESS.getCode();

    private String message = null;

    private Boolean ok = Boolean.TRUE;

    private T data = null;

    private static <T> Result<T> newInstance() {
        return new Result<>();
    }

    /**
     * 成功消息
     * @param code
     * @param message
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(Integer code, String message, T data) {
        final Result<T> result = newInstance();
        result.setCode(code);
        result.setMessage(message);
        if (data == null) {
            result.setData(null);
        } else {
            Field[] fields = data.getClass().getDeclaredFields();
            if (fields.length == 0) {
                result.setData(null);
            }
        }
        result.setData(data);
        return result;
    }

    /**
     * 成功消息
     * @param message
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T data,String message) {
        return Result.success(ResultCodeEnum.SUCCESS.getCode(), message, data);
    }

    /**
     * 成功消息
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T data) {
        return Result.success(data, ResultCodeEnum.SUCCESS.getMsg());
    }

    /**
     * 失败消息
     * @param code
     * @param message
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> failure(Integer code, String message, T data) {
        final Result<T> result = newInstance();
        result.setOk(Boolean.FALSE);
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 失败消息
     * @param code
     * @param message
     * @param <T>
     * @return
     */
    public static <T> Result<T> failure(Integer code, String message) {
        return Result.failure(code, message, null);
    }

    /**
     * 失败消息
     * @param message
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> failure(String message, T data) {
        return Result.failure(ResultCodeEnum.FAILURE.getCode(), message, data);
    }

    /**
     * 失败消息
     * @param message
     * @param <T>
     * @return
     */
    @org.jetbrains.annotations.NotNull
    public static <T> Result<T> failure(String message) {
        return Result.failure(ResultCodeEnum.FAILURE.getCode(), message);
    }

    /**
     * 失败消息
     * @param bindingResult
     * @param <T>
     * @return
     */
    @org.jetbrains.annotations.NotNull
    public static <T> Result<T> failure(@NotNull BindingResult bindingResult) {
        String errorMessage = Optional.ofNullable(bindingResult.getFieldError())
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("参数校验失败");
        return failure(errorMessage);
    }
}
