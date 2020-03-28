package com.gc.common.base.exception;

/**
 * @author jackson
 * 2020/3/27 2:26 下午
 */
public class NoSuchMethodRuntimeException extends BaseException {
    private static final long serialVersionUID = -2132525949065845345L;

    public NoSuchMethodRuntimeException(NoSuchMethodException e) {
        super(e.getMessage(), e);
    }
}
