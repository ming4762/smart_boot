package com.gc.common.base.utils.security;


import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.io.InputStream;

/**
 * MD5工具类
 * @author shizhongming
 * 2020/1/8 8:49 下午
 */
public final class Md5Utils {

    private Md5Utils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * MD5加密
     * @param value 加密前的字符串
     * @param hashIterations 加密次数
     * @return 加密后的字符串
     */
    @NonNull
    public static String md5(@NonNull String value, @NonNull Integer hashIterations) {
        String hash = DigestUtils.md5Hex(value);
        if (hashIterations > 1) {
            for(int i = 1; i<hashIterations; i++) {
                hash = DigestUtils.md5Hex(hash);
            }
        }
        return hash;
    }

    /**
     * MD5加密
     * @param value 加密前的字符串
     * @return 加密后的字符串
     */
    @NonNull
    public static String md5(@NonNull String value) {
        return md5(value, 1);
    }

    /**
     * MD5加密
     * @param inputStream 加密流
     * @return 加密后的字符串
     */
    @NonNull
    public static String md5(@NonNull InputStream inputStream) throws IOException {
        return DigestUtils.md5Hex(inputStream);
    }
}
