package com.gc.common.base.utils;

/**
 * @author shizhongming
 * 2020/1/12 7:13 下午
 */
public final class ExceptionUtils {

    private ExceptionUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static <E extends Exception> void doThrow(Exception e) throws E {
        throw (E)e;
    }
}
