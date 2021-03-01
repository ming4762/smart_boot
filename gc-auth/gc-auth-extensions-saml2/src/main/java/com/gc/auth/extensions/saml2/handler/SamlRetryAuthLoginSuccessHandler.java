package com.gc.auth.extensions.saml2.handler;

import com.gc.auth.core.handler.AuthLoginSuccessHandler;
import com.gc.auth.extensions.saml2.utils.SamlRetryTimerHolder;
import com.gc.common.base.utils.IpUtils;
import org.springframework.security.core.Authentication;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ShiZhongMing
 * 2021/3/1 10:03
 * @since 1.0
 */
public class SamlRetryAuthLoginSuccessHandler extends AuthLoginSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        // 登录成功后重置重试
        SamlRetryTimerHolder.reset(IpUtils.getIpAddr(httpServletRequest));
        super.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
    }
}
