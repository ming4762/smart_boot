package com.gc.auth.extensions.saml2.service;

import com.gc.auth.core.data.GcGrantedAuthority;
import com.gc.auth.core.data.PermissionGrantedAuthority;
import com.gc.auth.core.data.RoleGrantedAuthority;
import com.gc.auth.core.model.AuthUser;
import com.gc.auth.core.model.RestUserDetailsImpl;
import com.gc.auth.core.service.AuthUserService;
import com.google.common.collect.Sets;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ShiZhongMing
 * 2021/1/7 17:14
 * @since 1.0
 */
public class DefaultSamlUserDetailsServiceImpl implements SAMLUserDetailsService {

    private final AuthUserService userService;

    public DefaultSamlUserDetailsServiceImpl(AuthUserService authUserService) {
        this.userService = authUserService;
    }

    @Override
    public Object loadUserBySAML(SAMLCredential credential) throws UsernameNotFoundException {
        // 获取用户名
        String username = credential.getNameID().getValue();
        // 查询用户
        final AuthUser user = this.userService.getByUsername(username);
        if (Objects.isNull(user)) {
            return null;
        }
        Set<GcGrantedAuthority> grantedAuthoritySet = Sets.newHashSet();
        // 添加角色
        grantedAuthoritySet.addAll(
                this.userService.listRoleCode(user).stream()
                        .map(RoleGrantedAuthority::new)
                        .collect(Collectors.toList())
        );
        // 添加权限
        grantedAuthoritySet.addAll(
                this.userService.listPermission(user)
                        .stream()
                        .map(PermissionGrantedAuthority::new).collect(Collectors.toList())
        );
        // 查询用户角色信息
        final RestUserDetailsImpl restUserDetails = createByUser(user);
        restUserDetails.setAuthorities(grantedAuthoritySet);
        return restUserDetails;
    }

    @NonNull
    protected static RestUserDetailsImpl createByUser(@NonNull AuthUser user) {
        final RestUserDetailsImpl restUserDetails = new RestUserDetailsImpl();
        restUserDetails.setUserId(user.getUserId());
        restUserDetails.setRealName(user.getRealname());
        restUserDetails.setUsername(user.getUsername());
        restUserDetails.setPassword(user.getPassword());
        return restUserDetails;
    }
}
