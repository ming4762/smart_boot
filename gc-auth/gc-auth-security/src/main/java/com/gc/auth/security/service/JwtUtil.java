package com.gc.auth.security.service;

import com.gc.common.auth.core.GcGrantedAuthority;
import com.gc.common.auth.core.PermissionGrantedAuthority;
import com.gc.common.auth.core.RoleGrantedAuthority;
import com.gc.common.auth.exception.AuthException;
import com.gc.common.auth.model.RestUserDetails;
import com.gc.common.auth.model.SysUserPO;
import com.gc.common.base.http.HttpStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * jwt工具类
 * @author jackson
 * 2020/2/14 10:34 下午
 */
@Slf4j
public class JwtUtil {


    private static final String USER_KEY = "user";

    private static final String ROLE_KEY = "role";

    private static final String PERMISSION_KEY = "permission";

    static {
        ConvertUtils.register(new DateConverter(null), java.util.Date.class);
    }

    /**
     * 创建jwt
     * @param authentication 认证信息
     * @return jwt
     */
    public static String createJwt(Authentication authentication, String key) {
        final RestUserDetails userDetails = (RestUserDetails) authentication.getPrincipal();
        final Date now = new Date();
        final JwtBuilder builder = Jwts.builder()
                .setId(userDetails.getUserId().toString())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, key)
                .claim(ROLE_KEY, userDetails.getRoles())
                .claim(PERMISSION_KEY, userDetails.getPermissions())
                .claim(USER_KEY, userDetails.getUser());
        return builder.compact();
    }


    /**
     * 解析JWT
     *
     * @param jwt JWT
     * @return {@link Claims}
     */
    public static Claims parseJwt(String jwt, String key) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwt)
                .getBody();
    }

    /**
     * 获取用户名
     * @param jwt
     * @param key
     * @return
     */
    public static String getUsername(String jwt, String key) {
        return parseJwt(jwt, key).getSubject();
    }

    /**
     * 获取用户信息
     * @param jwt
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public static RestUserDetails getUser(String jwt, String key) {
        try {
            final Claims claims = parseJwt(jwt, key);
            final SysUserPO user = new SysUserPO();
            BeanUtils.populate(user, claims.get(USER_KEY, Map.class));
            final RestUserDetails restUserDetails = RestUserDetails.createByUser(user);
            // 设置权限
            final List<String> permissions = claims.get(PERMISSION_KEY, List.class);
            // 设置角色
            final List<String> roles = claims.get(ROLE_KEY, List.class);
            final Set<GcGrantedAuthority> authorities = new HashSet<>(permissions.size() + roles.size());
            permissions.forEach(permission -> authorities.add(new PermissionGrantedAuthority(permission)));
            roles.forEach(item -> authorities.add(new RoleGrantedAuthority(item)));
            restUserDetails.setAuthorities(authorities);
            return restUserDetails;
        } catch (Exception e) {
            throw new AuthException(HttpStatus.UNAUTHORIZED, "JWT解析失败", e);
        }
    }

    /**
     * 获取jwt
     * @param request
     * @return
     */
    @Nullable
    public static String getJwt(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }
}
