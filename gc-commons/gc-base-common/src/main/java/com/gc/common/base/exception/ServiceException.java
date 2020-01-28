package com.gc.common.base.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author jackson
 * 2020/1/28 3:15 下午
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 5741320275497601133L;

    private Integer code = HttpStatus.INTERNAL_SERVER_ERROR.value();

    private String message = "系统发生未知错误";


    public ServiceException(String message) {
        this.message = message;
    }
}
