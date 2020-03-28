package com.gc.auth.security.service;

import com.gc.cache.service.CacheService;
import com.gc.common.auth.constants.LoginTypeConstants;
import com.gc.common.auth.exception.AuthException;
import com.gc.common.auth.model.RestUserDetails;
import com.gc.common.auth.model.SysUserPO;
import com.gc.common.auth.properties.AuthProperties;
import com.gc.common.auth.utils.AuthUtils;
import com.gc.common.base.http.HttpStatus;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author jackson
 * 2020/2/15 9:49 上午
 */
public class AuthService {

    private AuthProperties authProperties;

    private CacheService cacheService;

    private static final String KEY_PREFIX = "gc:session";

    public AuthService(AuthProperties authProperties, CacheService cacheService) {
        this.authProperties = authProperties;
        this.cacheService = cacheService;
    }

    /**
     * 执行登陆
     * @param authentication
     * @param remeber
     * @param loginType
     * @return
     */
    public String doLogin(Authentication authentication, boolean remeber, LoginTypeConstants loginType) {
        final RestUserDetails userDetails = (RestUserDetails) authentication.getPrincipal();
        // 创建jwt
        final String jwt = JwtUtil.createJwt(authentication, this.authProperties.getJwtKey());
        // 获取有效期
        long timeout = this.authProperties.getSession().getTimeout().getGlobal();
        if (Objects.equals(loginType, LoginTypeConstants.MOBILE)) {
            timeout = this.authProperties.getSession().getTimeout().getMobile();
        } else if (remeber) {
            timeout = this.authProperties.getSession().getTimeout().getRemember();
        }
        // 保存jwt到cache中
        this.cacheService.put(this.getTokenKey(userDetails.getUsername(), jwt), timeout, timeout);
        return jwt;
    }

    /**
     * 刷新jwt
     * @param jwt
     */
    public RestUserDetails refreshJwt(String jwt) {
        // 解析jwt
        RestUserDetails user = JwtUtil.getUser(jwt, this.authProperties.getJwtKey());

        String jwtKey = this.getTokenKey(user.getUsername(), jwt);

        String attributeKey = this.getAttributeKey(jwt);

        // 获取有效期
        @Nullable
        Long timeout = this.cacheService.get(jwtKey, Long.class);
        if (ObjectUtils.isNotEmpty(timeout)) {
            this.cacheService.batchExpire(ImmutableList.of(jwtKey, attributeKey), timeout);
        } else {
            throw new AuthException(HttpStatus.UNAUTHORIZED, "token已过期，请重新登录");
        }
        return user;
    }

    /**
     * 获取token的key
     * @param username
     * @return
     */
    @NotNull
    private String getTokenKey(@NotNull String username, @NotNull String jwt) {
        return String.format("%s:jwt:%s:%s", KEY_PREFIX, username, jwt);
    }

    public String getAttributeKey(@NotNull String jwt) {
        return String.format("%s:attribute:%s", KEY_PREFIX, jwt);
    }

    /**
     * 登出操作
     * @param request
     */
    public void logout(@NotNull HttpServletRequest request) {
        String jwt = JwtUtil.getJwt(request);
        SysUserPO user = AuthUtils.getCurrentUser();
        Assert.notNull(user, "系统发生未知异常：未找到当前用户");
        Assert.notNull(jwt, "系统发生未知异常，未找到token");
        this.cacheService.batchDelete(ImmutableList.of(this.getTokenKey(user.getUsername(), jwt), this.getAttributeKey(jwt)));
    }
}
