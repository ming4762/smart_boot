package com.gc.auth.core.authentication;

import com.gc.auth.core.constants.RoleConstants;
import com.gc.auth.core.data.RestUserDetails;
import com.gc.auth.core.model.Permission;
import com.gc.auth.core.properties.AuthProperties;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 自定义的方法权限认证其
 * @author jackson
 * 2020/1/24 10:53 上午
 */
public class MethodPermissionEvaluatorImpl implements PermissionEvaluator {


    private final AuthProperties authProperties;

    public MethodPermissionEvaluatorImpl(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        // 开发模式不拦截
        if (BooleanUtils.isTrue(this.authProperties.getDevelopment())) {
            return true;
        }
        // 验证角色，超级管理员角色不拦截
        final RestUserDetails user = (RestUserDetails) authentication.getPrincipal();
        if (user.getRoles().contains(RoleConstants.ROLE_SUPERADMIN.getRole())) {
            return true;
        }
        // 验证权限
        final String permissionStr = String.join(":", targetDomainObject.toString(), permission.toString());
        Set<String> permissions = user.getPermissions().stream()
                .map(Permission::getAuthority)
                .collect(Collectors.toSet());
        return permissions.contains(permissionStr);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}
