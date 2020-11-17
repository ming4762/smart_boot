package com.gc.auth.security.service;

import com.gc.common.auth.constants.LoginTypeConstants;
import com.gc.common.auth.core.RestUserDetails;
import com.gc.common.auth.exception.AuthException;
import com.gc.common.auth.model.RestUserDetailsImpl;
import com.gc.common.auth.properties.AuthProperties;
import com.gc.common.auth.service.AuthCache;
import com.gc.common.auth.utils.AuthUtils;
import com.gc.common.base.http.HttpStatus;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 认证服务层
 * @author jackson
 * 2020/2/15 9:49 上午
 */
public class AuthService {

    private final AuthProperties authProperties;

    private final AuthCache<String, Object> authCache;


    private static final String KEY_PREFIX = "gc:session";

    public AuthService(AuthProperties authProperties, AuthCache<String, Object> authCache) {
        this.authProperties = authProperties;
        this.authCache = authCache;
    }

    /**
     * 执行登陆
     * @param authentication 登录信息
     * @param remeber 是否记住用户
     * @param loginType 登录类型
     * @return jwt
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
        this.authCache.put(this.getTokenKey(userDetails.getUsername(), jwt), timeout, timeout);
        return jwt;
    }

    /**
     * 刷新jwt
     * @param jwt jwt
     */
    public RestUserDetailsImpl refreshJwt(String jwt) {
        // 解析jwt
        RestUserDetailsImpl user = JwtUtil.getUser(jwt, this.authProperties.getJwtKey());

        String jwtKey = this.getTokenKey(user.getUsername(), jwt);

        String attributeKey = this.getAttributeKey(jwt);

        // 获取有效期
        Long timeout = (Long) this.authCache.get(jwtKey);
        if (ObjectUtils.isNotEmpty(timeout)) {
            this.authCache.expire(jwtKey, timeout);
            this.authCache.expire(attributeKey, timeout);
        } else {
            throw new AuthException(HttpStatus.UNAUTHORIZED, "token已过期，请重新登录");
        }
        return user;
    }

    /**
     * 获取token的key
     * @param username 用户名
     * @return jst
     */
    @NonNull
    private String getTokenKey(@NonNull String username, @NonNull String jwt) {
        return String.format("%s:jwt:%s:%s", KEY_PREFIX, username, jwt);
    }

    public String getAttributeKey(@NonNull String jwt) {
        return String.format("%s:attribute:%s", KEY_PREFIX, jwt);
    }

    /**
     * 登出操作
     * @param request 请求体
     */
    public void logout(@NonNull HttpServletRequest request) {
        String jwt = JwtUtil.getJwt(request);
        RestUserDetails user = AuthUtils.getCurrentUser();
        Assert.notNull(user, "系统发生未知异常：未找到当前用户");
        Assert.notNull(jwt, "系统发生未知异常，未找到token");
        this.authCache.remove(this.getTokenKey(user.getUsername(), jwt));
        this.authCache.remove(this.getAttributeKey(jwt));
    }
}
