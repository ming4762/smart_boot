package com.gc.auth.extensions.jwt.utils;

import com.gc.auth.core.data.GcGrantedAuthority;
import com.gc.auth.core.data.PermissionGrantedAuthority;
import com.gc.auth.core.data.RoleGrantedAuthority;
import com.gc.auth.core.model.Permission;
import com.gc.auth.core.model.RestUserDetailsImpl;
import com.gc.common.base.exception.IllegalAccessRuntimeException;
import com.gc.common.base.exception.InvocationTargetRuntimeException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * jwt工具类
 * @author jackson
 * 2020/2/14 10:34 下午
 */
@Slf4j
public class JwtUtil {

    private JwtUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static final String USER_KEY = "user";

    private static final String ROLE_KEY = "role";

    private static final String PERMISSION_KEY = "permission";

    static {
        ConvertUtils.register(new DateConverter(null), Date.class);
    }

    /**
     * 创建jwt
     * @param authentication 认证信息
     * @return jwt
     */
    public static String createJwt(Authentication authentication, String key) {
        final RestUserDetailsImpl userDetails = (RestUserDetailsImpl) authentication.getPrincipal();
        final Date now = new Date();
        final JwtBuilder builder = Jwts.builder()
                .setId(userDetails.getUserId().toString())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, key)
                .claim(ROLE_KEY, userDetails.getRoles())
                .claim(PERMISSION_KEY, userDetails.getPermissions())
                .claim(USER_KEY, userDetails);
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
     * @param jwt JWT
     * @param key key
     * @return 用户名
     */
    public static String getUsername(String jwt, String key) {
        return parseJwt(jwt, key).getSubject();
    }

    /**
     * 获取用户信息
     * @param jwt jwt
     * @param key key
     * @return 用户信息
     */
    @SuppressWarnings("unchecked")
    public static RestUserDetailsImpl getUser(String jwt, String key) {
        try {
            final Claims claims = parseJwt(jwt, key);
            final RestUserDetailsImpl restUserDetails = new RestUserDetailsImpl();
            BeanUtils.populate(restUserDetails, claims.get(USER_KEY, Map.class));
            // 设置权限
            final List<Map<String, String>> permissionMap = claims.get(PERMISSION_KEY, List.class);
            final List<Permission> permissions = permissionMap.stream()
                    .map(item -> {
                        try {
                            Permission permission = new Permission();
                            BeanUtils.populate(permission, item);
                            return permission;
                        } catch (IllegalAccessException e) {
                            throw new IllegalAccessRuntimeException(e);
                        } catch (InvocationTargetException e) {
                            throw new InvocationTargetRuntimeException(e);
                        }
                    }).collect(Collectors.toList());
            // 设置角色
            final List<String> roles = claims.get(ROLE_KEY, List.class);
            final Set<GcGrantedAuthority> authorities = new HashSet<>(permissions.size() + roles.size());
            permissions.forEach(permission -> authorities.add(new PermissionGrantedAuthority(permission)));
            roles.forEach(item -> authorities.add(new RoleGrantedAuthority(item)));
            restUserDetails.setAuthorities(authorities);
            return restUserDetails;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new InternalAuthenticationServiceException( "JWT解析失败", e);
        }
    }

    /**
     * 获取jwt
     * @param request 请求体
     * @return JWT
     */
    @Nullable
    public static String getJwt(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }
}
