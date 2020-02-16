package com.gc.common.auth.exception;

import com.gc.common.base.exception.BaseException;
import org.springframework.http.HttpStatus;

/**
 * @author jackson
 * 2020/2/15 12:53 下午
 */
public class AuthException extends BaseException {
    private static final long serialVersionUID = -6922142660105351058L;

    public AuthException(HttpStatus status) {
        super(status.value(), status.getReasonPhrase(), null);
    }

    public AuthException(HttpStatus status, String message) {
        super(status.value(), message, null);
    }
}
