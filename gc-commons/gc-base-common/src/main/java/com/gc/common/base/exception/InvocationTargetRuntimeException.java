package com.gc.common.base.exception;

import java.lang.reflect.InvocationTargetException;

/**
 * @author jackson
 * 2020/3/27 2:25 下午
 */
public class InvocationTargetRuntimeException extends BaseException {
    private static final long serialVersionUID = -690574407276935714L;

    public InvocationTargetRuntimeException(InvocationTargetException e) {
        super(e.getMessage(), e);
    }
}
