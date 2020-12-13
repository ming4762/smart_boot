package com.gc.common.jcraft.exception;

import com.gc.common.base.exception.BaseException;
import com.jcraft.jsch.SftpException;

/**
 * @author shizhongming
 * 2020/12/13 12:10 下午
 */
public class SftpExceptionRuntimeException extends BaseException {
    private static final long serialVersionUID = -8968802972165721512L;

    public SftpExceptionRuntimeException(SftpException e) {
        super(e.getMessage(), e);
    }
}
