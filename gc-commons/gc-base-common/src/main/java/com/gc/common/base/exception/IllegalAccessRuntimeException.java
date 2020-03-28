package com.gc.common.base.exception;

/**
 * @author jackson
 * 2020/3/27 2:23 下午
 */
public class IllegalAccessRuntimeException extends BaseException {
    private static final long serialVersionUID = 251129850919866312L;

    public IllegalAccessRuntimeException(IllegalAccessException e) {
        super(e.getMessage(), e);
    }
}
