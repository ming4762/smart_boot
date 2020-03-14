package com.gc.common.auth.exception;

import com.gc.common.base.exception.BaseException;
import com.gc.common.base.http.HttpStatus;

/**
 * @author jackson
 * 2020/2/15 12:53 下午
 */
public class AuthException extends BaseException {
    private static final long serialVersionUID = -6922142660105351058L;

    public AuthException(HttpStatus status) {
        super(status.getCode(), status.getMessage(), null);
    }

    public AuthException(HttpStatus status, String message) {
        super(status.getCode(), message, null);
    }

    public AuthException(HttpStatus status, String message, Throwable e) {
        super(status.getCode(), message, e);
    }
}
