package com.gc.common.auth.utils;

import com.gc.common.auth.model.RestUserDetails;
import com.gc.common.auth.model.SysUserPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

/**
 * 认证工具类
 * @author jackson
 * 2020/1/22 7:07 下午
 */
@Slf4j
public final class AuthUtils {

    private static final String NOT_LOGIN_USER = "anonymousUser";

    private AuthUtils () {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 获取当前用户
     * @return 当前用户
     */
    @org.springframework.lang.Nullable
    public static SysUserPO getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (Objects.isNull(principal) || StringUtils.equals(principal.toString(), NOT_LOGIN_USER)) {
            log.warn("用户未登录");
            return null;
        }
        return ((RestUserDetails)principal).getUser();
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
