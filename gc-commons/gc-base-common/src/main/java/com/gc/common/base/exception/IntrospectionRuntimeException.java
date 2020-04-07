package com.gc.common.base.exception;

import java.beans.IntrospectionException;

/**
 * @author shizhongming
 * 2020/3/30 9:45 下午
 */
public class IntrospectionRuntimeException extends BaseException {
    private static final long serialVersionUID = 6953556563155199343L;

    public IntrospectionRuntimeException(IntrospectionException e) {
        super(e);
    }
}
