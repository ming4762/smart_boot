package com.gc.common.base.exception;

/**
 * @author jackson
 * 2020/3/27 2:43 下午
 */
public class InstantiationRuntimeException extends BaseException {
    private static final long serialVersionUID = 9058795247777023879L;

    public InstantiationRuntimeException(InstantiationException e) {
        super(e.getMessage(), e);
    }
}
