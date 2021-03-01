package com.gc.auth.core.handler;

import com.gc.auth.core.data.RestUserDetails;
import com.gc.auth.core.model.LoginResult;
import com.gc.auth.core.model.Permission;
import com.google.common.collect.Sets;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ShiZhongMing
 * 2021/3/1 9:58
 * @since 1.0
 */
public class DefaultAuthSuccessDataHandler implements AuthSuccessDataHandler {
    @Override
    public LoginResult successData(Authentication authentication, HttpServletRequest request) {
        final RestUserDetails userDetails = (RestUserDetails) authentication.getPrincipal();
        // 处理用户权限信息
        return LoginResult.builder()
                .user(userDetails)
                .token(userDetails.getToken())
                .roles(userDetails.getRoles())
                .permissions(
                        Optional.ofNullable(userDetails.getPermissions())
                                .map(item -> item.stream().map(Permission::getAuthority).collect(Collectors.toSet()))
                                .orElse(Sets.newHashSet())
                ).build();
    }
}
