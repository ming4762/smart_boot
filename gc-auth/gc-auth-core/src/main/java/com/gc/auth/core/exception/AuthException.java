package com.gc.auth.core.exception;

import com.gc.common.base.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

/**
 * @author jackson
 * 2020/2/15 12:53 下午
 */
public class AuthException extends AuthenticationException {
    private static final long serialVersionUID = -6922142660105351058L;

    public AuthException(HttpStatus status) {
        super(status.getMessage());
    }

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, Throwable e) {
        super(message, e);
    }
}
