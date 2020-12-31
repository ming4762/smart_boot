package com.gc.auth.extensions.jwt.service;

import com.gc.auth.core.exception.AuthException;
import com.gc.auth.core.model.RestUserDetailsImpl;
import com.gc.auth.core.service.AuthCache;
import com.gc.auth.extensions.jwt.utils.JwtUtil;
import com.gc.common.base.http.HttpStatus;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.lang.NonNull;

/**
 * JWT服务层
 * @author ShiZhongMing
 * 2020/12/31 15:43
 * @since 1.0
 */
public class JwtService {

    private static final String TOKE_KEY_PREFIX = "gc:session:user";

    private static final String DATA_KEY_PREFIX = "gc:session:attribute";

    private final String jwtKey;

    private final AuthCache<String, Object> authCache;

    public JwtService(String jwtKey, AuthCache<String, Object> authCache) {
        this.jwtKey = jwtKey;
        this.authCache = authCache;
    }


    /**
     * 刷新jwt
     * @param jwt jwt
     */
    public RestUserDetailsImpl refreshJwt(String jwt) {
        // 解析jwt
        RestUserDetailsImpl user = JwtUtil.getUser(jwt, this.jwtKey);

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
        return String.format("%s:%s:%s", TOKE_KEY_PREFIX, username, jwt);
    }

    public String getAttributeKey(@NonNull String jwt) {
        return String.format("%s:%s", DATA_KEY_PREFIX, jwt);
    }
}
