package com.gc.auth.extensions.jwt.handler;

import com.gc.auth.core.data.RestUserDetails;
import com.gc.auth.core.handler.AuthLoginSuccessHandler;
import com.gc.auth.core.model.LoginResult;
import com.gc.auth.extensions.jwt.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ShiZhongMing
 * 2021/1/8 15:36
 * @since 1.0
 */
public class JwtAuthLoginSuccessHandler extends AuthLoginSuccessHandler {

    private JwtService jwtService;

    @Override
    protected LoginResult successData(Authentication authentication, HttpServletRequest httpServletRequest) {
        String jwt = this.jwtService.createJwt((RestUserDetails)authentication.getPrincipal(), null);
        // 设置token
        ((RestUserDetails)authentication.getPrincipal()).setToken(jwt);
        return super.successData(authentication, httpServletRequest);
    }

    @Autowired
    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        Assert.notNull(this.jwtService, "jwtService is null, please init it");
    }
}
