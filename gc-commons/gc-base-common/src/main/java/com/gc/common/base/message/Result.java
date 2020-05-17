package com.gc.common.base.message;


import com.gc.common.base.exception.BaseException;
import com.gc.common.base.http.HttpStatus;
import com.gc.common.base.http.IHttpStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindingResult;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

/**
 * 后台传输工具类
 * @author jackson
 */
@Getter
@Setter
@ApiModel(value = "通用api接口", description = "通用api接口返回")
public class Result<T> {

    @ApiModelProperty(value = "状态码", example = "200", required = true)
    private Integer code = ResultCodeEnum.SUCCESS.getCode();

    @ApiModelProperty(value = "返回信息", example = "成功")
    private String message = null;

    @ApiModelProperty(value = "接口返回状态", example = "true", required = true)
    private Boolean ok = Boolean.TRUE;

    @ApiModelProperty(value = "接口返回数据")
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
    @NonNull
    public static <T> Result<T> failure(String message) {
        return Result.failure(ResultCodeEnum.FAILURE.getCode(), message);
    }

    /**
     * 失败消息
     * @param bindingResult
     * @param <T>
     * @return
     */
    @NonNull
    public static <T> Result<T> failure(@NonNull BindingResult bindingResult) {
        String errorMessage = Optional.ofNullable(bindingResult.getFieldError())
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("参数校验失败");
        return failure(HttpStatus.BAD_REQUEST.getCode(), errorMessage);
    }

    /**
     * 错误信息
     * @param e
     * @return
     */
    public static Result<String> failure(@NonNull BaseException e) {
        return failure(e.getCode(), e.getMessage());
    }

    /**
     *
     * @param status
     * @param <T>
     * @return
     */
    public static <T> Result<T> ofStatus(IHttpStatus status) {
        return ofStatus(status, null);
    }

    public static <T> Result<T> ofStatus(IHttpStatus status, String message) {
        return ofStatus(status, message, null);
    }

    public static Result<String> ofExceptionStatus(IHttpStatus status, Exception e) {
        return ofStatus(status, e.getMessage());
    }

    public static <T> Result<T> ofStatus(IHttpStatus status, String message, T data) {
        String returnMessage = StringUtils.isEmpty(message) ? status.getMessage() : message;
        if (Objects.equals(status.getCode(), HttpStatus.OK.getCode())) {
            return success(status.getCode(), returnMessage, data);
        } else {
            return failure(status.getCode(), returnMessage, data);
        }
    }
}
