package com.gc.auth.security2.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * 方法级权限配置
 * @author ShiZhongMing
 * 2021/1/5 12:26
 * @since 1.0
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ConditionalOnProperty(prefix = "gc.auth", name = "method", havingValue = "true")
public class AuthMethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    private final PermissionEvaluator permissionEvaluator;

    public AuthMethodSecurityConfig(PermissionEvaluator permissionEvaluator) {
        this.permissionEvaluator = permissionEvaluator;
    }


    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        final DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setPermissionEvaluator(this.permissionEvaluator);
        return super.createExpressionHandler();
    }
}
