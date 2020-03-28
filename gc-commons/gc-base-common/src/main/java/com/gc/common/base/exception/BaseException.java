package com.gc.common.base.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author jackson
 * 2020/1/28 3:15 下午
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BaseException extends RuntimeException {
    private static final long serialVersionUID = 5741320275497601133L;

    private Integer code = HttpStatus.INTERNAL_SERVER_ERROR.value();

    private String message = "系统发生未知错误";

    private Throwable e;


    public BaseException(String message) {
        this.message = message;
    }

    public BaseException(String message, Throwable e) {
        this.e = e;
        this.message = message;
    }

    public BaseException(Throwable e) {
        this.e = e;
    }
}
