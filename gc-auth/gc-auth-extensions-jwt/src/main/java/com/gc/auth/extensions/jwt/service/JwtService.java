package com.gc.auth.extensions.jwt.service;

import com.gc.auth.core.constants.LoginTypeConstants;
import com.gc.auth.core.data.RestUserDetails;
import com.gc.auth.core.exception.AuthException;
import com.gc.auth.core.properties.AuthProperties;
import com.gc.auth.core.service.AuthCache;
import com.gc.auth.extensions.jwt.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * JWT服务层
 * @author ShiZhongMing
 * 2020/12/31 15:43
 * @since 1.0
 */
@Slf4j
public class JwtService implements LogoutHandler {

    private static final String TOKE_KEY_PREFIX = "gc:session:user";

    private static final String DATA_KEY_PREFIX = "gc:session:attribute";


    private final AuthCache<String, Object> authCache;

    private final AuthProperties authProperties;

    public JwtService(AuthProperties authProperties, AuthCache<String, Object> authCache) {
        this.authCache = authCache;
        this.authProperties = authProperties;
    }

    public String createJwt(RestUserDetails userDetails, LoginTypeConstants loginType) {
        // 创建jwt
        final String jwt = JwtUtil.createJwt(userDetails, this.authProperties.getJwtKey());
        // 获取有效期
        long timeout = authProperties.getSession().getTimeout().getGlobal();
        if (Objects.equals(loginType, LoginTypeConstants.MOBILE)) {
            timeout = authProperties.getSession().getTimeout().getMobile();
        } else if (Objects.equals(loginType, LoginTypeConstants.REMEMBER)) {
            timeout = authProperties.getSession().getTimeout().getRemember();
        }
        // 保存jwt到cache中
        this.authCache.put(this.getTokenKey(userDetails.getUsername(), jwt), timeout, timeout);
        return jwt;
    }


    /**
     * 刷新jwt
     * @param jwt jwt
     */
    public RestUserDetails refreshJwt(String jwt) {
        // 解析jwt
        RestUserDetails user = JwtUtil.getUser(jwt, this.authProperties.getJwtKey());

        String jwtKey = this.getTokenKey(user.getUsername(), jwt);

        String attributeKey = this.getAttributeKey(jwt);

        // 获取有效期
        Long timeout = (Long) this.authCache.get(jwtKey);
        if (ObjectUtils.isNotEmpty(timeout)) {
            this.authCache.expire(jwtKey, timeout);
            this.authCache.expire(attributeKey, timeout);
        } else {
            throw new InternalAuthenticationServiceException("token已过期，请重新登录");
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
        return String.format("%s:%s:%s", TOKE_KEY_PREFIX, username, jwt);
    }

    public String getAttributeKey(@NonNull String jwt) {
        return String.format("%s:%s", DATA_KEY_PREFIX, jwt);
    }

    /**
     * 执行登出操作
     * @param request request
     * @param response response
     * @param authentication authentication
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String jwt = JwtUtil.getJwt(request);
        if (StringUtils.isBlank(jwt)) {
            throw new AuthException("JWT为null，无法登录");
        }
        final RestUserDetails user = JwtUtil.getUser(jwt, this.authProperties.getJwtKey());
        if (Objects.nonNull(this.authCache.get(this.getTokenKey(user.getUsername(), jwt)))) {
            Assert.notNull(user, "系统发生未知异常：未找到当前用户");
            Assert.notNull(jwt, "系统发生未知异常，未找到token");
            this.authCache.remove(this.getTokenKey(user.getUsername(), jwt));
            this.authCache.remove(this.getAttributeKey(jwt));
        } else {
            log.debug("用户已登出");
        }

    }
}
