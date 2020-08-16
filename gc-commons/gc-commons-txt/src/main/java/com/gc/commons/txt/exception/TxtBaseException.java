package com.gc.commons.txt.exception;

/**
 * @author shizhongming
 * 2020/8/16 4:57 下午
 */
public class TxtBaseException extends RuntimeException {
    private static final long serialVersionUID = 2739993781434925615L;

    public TxtBaseException(Throwable throwable) {
        super(throwable);
    }
}
