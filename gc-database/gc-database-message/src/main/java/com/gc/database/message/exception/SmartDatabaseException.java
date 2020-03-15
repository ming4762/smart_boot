package com.gc.database.message.exception;

import com.gc.database.message.constants.ExceptionConstant;

/**
 * @author shizhongming
 * 2020/1/19 8:21 下午
 */
public class SmartDatabaseException extends RuntimeException {
    private static final long serialVersionUID = 2339582348409153072L;
    public SmartDatabaseException(ExceptionConstant exceptionConstant, String ...args) {
        super(String.format(exceptionConstant.getValue(), args));
    }
}
