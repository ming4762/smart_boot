package com.gc.common.auth.utils;

import com.gc.common.auth.model.SysUserPO;
import org.jetbrains.annotations.Nullable;

/**
 * 认证工具类
 * @author jackson
 * 2020/1/22 7:07 下午
 */
public final class AuthUtils {

    /**
     * 获取当前用户
     * @return 当前用户
     */
    @Nullable
    public static SysUserPO getCurrentUser() {
        return null;
    }

    /**
     * 获取当前用户ID
     * @return 当前用户ID
     */
    @Nullable
    public static Long getCurrentUserId() {
        SysUserPO user = getCurrentUser();
        if (user == null) {
            return null;
        }
        return user.getUserId();
    }
}
