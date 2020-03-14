package com.gc.auth.security.authentication;

import com.gc.common.auth.model.RestUserDetails;
import com.gc.common.auth.properties.AuthProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 自定义的权限认证器
 * @author jackson
 * 2020/1/24 10:53 上午
 */
@Component
public class RestPermissionEvaluatorImpl implements PermissionEvaluator {

    private static final String ROLE_SUPERADMIN = "SUPERADMIN";

    @Autowired
    private AuthProperties authProperties;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        // 开发模式不拦截
        if (this.authProperties.getDevelopment()) {
            return true;
        }
        // 验证角色，超级管理员角色不拦截
        final RestUserDetails user = (RestUserDetails) authentication.getPrincipal();
        if (user.getRoles().contains(ROLE_SUPERADMIN)) {
            return true;
        }
        // 验证权限
        final String permissionStr = String.join(":", targetDomainObject.toString(), permission.toString());
        return user.getPermissions().contains(permissionStr);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}
