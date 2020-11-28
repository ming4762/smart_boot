package com.gc.common.base.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author jackson
 * 2020/1/28 3:15 下午
 */
@Getter
@NoArgsConstructor
public class BaseException extends RuntimeException {
    private static final long serialVersionUID = 5741320275497601133L;

    private Integer code;

    private String message;

    private Throwable e;

    public BaseException(String message) {
        this(message, null);
    }

    public BaseException(Integer code, String message, Throwable e) {
        super(message, e);
        this.code = code;
        this.message = message;
        this.e = e;
    }

    public BaseException(String message, Throwable e) {
        this(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, e);
    }

    public BaseException(Throwable e) {
        this(500, e.getMessage(), e);
    }
}
