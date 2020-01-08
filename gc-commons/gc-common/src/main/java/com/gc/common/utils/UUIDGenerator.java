package com.gc.common.utils;

import java.util.UUID;

/**
 * UUID工具类
 * @author shizhongming
 * 2020/1/8 7:54 下午
 */
public class UUIDGenerator {

    /**
     * 生成UUID
     * @author shizhongming
     * @return
     */
    public static String getUUID() {
        var s = UUID.randomUUID().toString();
        s = s.replace("-", "");
        return s;
    }

    /**
     *
     * @param a
     */
    public void abc(String a) {

    }
}
