package com.gc.auth.core.authentication;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * URl验证接口
 * @author ShiZhongMing
 * 2021/1/4 16:59
 * @since 1.0
 */
public interface UrlAuthenticationProvider {

    /**
     * 是否拥有权限
     * @param request request
     * @param authentication authentication
     * @return 是否拥有权限
     */
    boolean hasPermission(HttpServletRequest request, Authentication authentication);
}
