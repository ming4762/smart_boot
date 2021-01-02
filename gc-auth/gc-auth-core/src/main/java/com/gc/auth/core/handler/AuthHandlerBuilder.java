package com.gc.auth.core.handler;

import com.gc.auth.core.properties.AuthProperties;
import lombok.Getter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * handler 构建器
 * @author shizhongming
 * 2021/1/2 8:58 下午
 */
public class AuthHandlerBuilder {

    private final AuthProperties authProperties;

    public AuthHandlerBuilder(AuthProperties authProperties) {
        this.authProperties = authProperties;
        this.init();
    }

    @Getter
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    private void init() {
        this.authenticationSuccessHandler = new AuthLoginSuccessHandler(this.authProperties);
    }

    /**
     * 设置登录成功执行器
     * @param authenticationSuccessHandler 登录成功
     * @return this
     */
    public AuthHandlerBuilder authenticationSuccessHandler(AuthenticationSuccessHandler authenticationSuccessHandler) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        return this;
    }
}
