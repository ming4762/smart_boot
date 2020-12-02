package com.gc.file.common.exception;

import com.gc.common.base.exception.BaseException;

/**
 * @author shizhongming
 * 2020/11/5 10:58 下午
 */
public class SmartFileException extends BaseException {
    private static final long serialVersionUID = 8738840688421208415L;

    public SmartFileException(String message) {
        super(message);
    }

    public SmartFileException(String message, Exception e) {
        super(message,e);
    }
}
