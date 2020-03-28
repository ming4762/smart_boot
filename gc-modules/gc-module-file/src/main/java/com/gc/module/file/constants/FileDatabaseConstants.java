package com.gc.module.file.constants;

import com.gc.common.base.constants.TransactionManagerConstants;

/**
 * @author jackson
 * 2020/1/27 7:42 下午
 */
public final class FileDatabaseConstants {

    private FileDatabaseConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String TRANSACTION_MANAGER = TransactionManagerConstants.SYSTEM_MANAGER;
}
