package com.gc.auth.security.service;

import com.gc.common.auth.core.GcGrantedAuthority;
import com.gc.common.auth.core.PermissionGrantedAuthority;
import com.gc.common.auth.core.RoleGrantedAuthority;
import com.gc.common.auth.model.AuthUser;
import com.gc.common.auth.model.RestUserDetailsImpl;
import com.gc.common.auth.service.AuthUserService;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author jackson
 * 2020/1/23 7:34 下午
 */
public class RestUserDetailsServiceImpl implements UserDetailsService{

    private final AuthUserService userService;

    public RestUserDetailsServiceImpl(AuthUserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        if (StringUtils.isEmpty(username)) {
            return null;
        }
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
        restUserDetails.setRealname(user.getRealname());
        restUserDetails.setUsername(user.getUsername());
        restUserDetails.setPassword(user.getPassword());
        return restUserDetails;
    }
}
