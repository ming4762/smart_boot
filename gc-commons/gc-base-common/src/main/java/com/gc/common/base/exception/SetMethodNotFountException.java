package com.gc.common.base.exception;

/**
 * 未找到set方法异常
 * @author shizhongming
 * 2020/8/16 4:44 下午
 */
public class SetMethodNotFountException extends RuntimeException {
    private static final long serialVersionUID = -7117214476508034605L;

    public SetMethodNotFountException(String message) {
        super(message);
    }
}
