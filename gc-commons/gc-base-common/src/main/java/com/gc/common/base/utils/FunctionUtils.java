package com.gc.common.base.utils;

import com.gc.common.base.function.ExecuteFunction;

/**
 * @author shizhongming
 * 2020/5/18 9:23 下午
 */
public class FunctionUtils {

    /**
     * 根据表达式是否执行方法
     * @param result 表达式
     * @param function 函数
     */
    public static void trueExecute(boolean result, ExecuteFunction function) {
        if (result) {
            function.execute();
        }
    }
}
