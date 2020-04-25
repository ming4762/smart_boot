package com.gc.common.base.exception;

import java.sql.SQLException;

/**
 * @author shizhongming
 * 2020/3/27 2:45 下午
 */
public class SQLRuntimeException extends BaseException {
    private static final long serialVersionUID = -7037034640865975540L;

    public SQLRuntimeException(SQLException e) {
        super(e.getMessage(), e);
    }
}
