package com.gc.auth.extensions.jwt.handler;

import com.gc.auth.core.data.RestUserDetails;
import com.gc.auth.core.handler.DefaultAuthSuccessDataHandler;
import com.gc.auth.core.model.LoginResult;
import com.gc.auth.extensions.jwt.service.JwtService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ShiZhongMing
 * 2021/3/1 10:00
 * @since 1.0
 */
public class JwtAuthSuccessDataHandler extends DefaultAuthSuccessDataHandler implements InitializingBean {

    private JwtService jwtService;

    @Override
    public LoginResult successData(Authentication authentication, HttpServletRequest request) {
        String jwt = this.jwtService.createJwt((RestUserDetails)authentication.getPrincipal(), null);
        // 设置token
        ((RestUserDetails)authentication.getPrincipal()).setToken(jwt);
        return super.successData(authentication, request);
    }

    @Autowired
    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.jwtService, "jwtService is null, please init it");
    }
}
