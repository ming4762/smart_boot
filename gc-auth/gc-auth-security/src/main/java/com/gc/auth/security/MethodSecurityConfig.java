package com.gc.auth.security;

import com.gc.auth.security.authentication.RestPermissionEvaluatorImpl;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * @author jackson
 * 2020/1/24 11:21 上午
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    private final RestPermissionEvaluatorImpl restPermissionEvaluator;

    public MethodSecurityConfig(RestPermissionEvaluatorImpl restPermissionEvaluator) {
        this.restPermissionEvaluator = restPermissionEvaluator;
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setPermissionEvaluator(restPermissionEvaluator);
        return handler;
    }
}
