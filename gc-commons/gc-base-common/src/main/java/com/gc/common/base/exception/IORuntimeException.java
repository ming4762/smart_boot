package com.gc.common.base.exception;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author shizhongming
 * 2020/11/29 4:05 上午
 */
public class IORuntimeException extends BaseException {
    private static final long serialVersionUID = -5740445789722500726L;

    public IORuntimeException(IOException e) {
        super(e.getMessage(), e);
    }
}
