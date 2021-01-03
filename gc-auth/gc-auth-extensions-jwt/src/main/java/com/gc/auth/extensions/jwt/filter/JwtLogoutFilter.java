package com.gc.auth.extensions.jwt.filter;

import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * 登出执行器
 * @author shizhongming
 * 2021/1/3 8:00 下午
 */
public class JwtLogoutFilter extends LogoutFilter {

    public static final String LOGOUT_URL = "/auth/login";

    public JwtLogoutFilter(LogoutSuccessHandler logoutSuccessHandler, LogoutHandler... handlers) {
        super(logoutSuccessHandler, handlers);
    }
}
