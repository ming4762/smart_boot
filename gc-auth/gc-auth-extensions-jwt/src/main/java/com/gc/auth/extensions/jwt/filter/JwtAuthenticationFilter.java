package com.gc.auth.extensions.jwt.filter;

import com.gc.auth.core.data.RestUserDetails;
import com.gc.auth.core.matcher.ExtensionPathMatcher;
import com.gc.auth.core.properties.AuthProperties;
import com.gc.auth.extensions.jwt.context.JwtContext;
import com.gc.auth.extensions.jwt.service.JwtService;
import com.gc.auth.extensions.jwt.utils.JwtUtil;
import com.gc.common.base.http.HttpStatus;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

/**
 * jwt拦截器
 * @author shizhongming
 * 2020/2/15 10:58 上午
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final JwtContext jwtContext;

    public JwtAuthenticationFilter(JwtService authService, JwtContext jwtContext) {
        this.jwtService = authService;
        this.jwtContext = jwtContext;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String jwt = JwtUtil.getJwt(request);
        if (StringUtils.isBlank(jwt)) {
            throw new InternalAuthenticationServiceException(HttpStatus.UNAUTHORIZED.getMessage());
        }
        // 刷新jwt的过期时间
        RestUserDetails user = this.jwtService.refreshJwt(jwt);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return Objects.equals(this.jwtContext.getAuthProperties().getDevelopment(), Boolean.TRUE) || checkIgnores(request);
    }

    /**
     * 请求是否不需要进行权限拦截
     *
     * @param request 当前请求
     * @return true - 忽略，false - 不忽略
     */
    private boolean checkIgnores(HttpServletRequest request) {
        final AuthProperties authProperties = this.jwtContext.getAuthProperties();
        String method = request.getMethod();
        HttpMethod httpMethod = HttpMethod.resolve(method);
        if (Objects.isNull(httpMethod)) {
            httpMethod = HttpMethod.GET;
        }
        Set<String> ignores = Sets.newHashSet();
        switch (httpMethod) {
            case GET:
                ignores.addAll(authProperties.getIgnores()
                        .getGet());
                break;
            case PUT:
                ignores.addAll(authProperties.getIgnores()
                        .getPut());
                break;
            case HEAD:
                ignores.addAll(authProperties.getIgnores()
                        .getHead());
                break;
            case POST:
                ignores.addAll(authProperties.getIgnores()
                        .getPost());
                break;
            case PATCH:
                ignores.addAll(authProperties.getIgnores()
                        .getPatch());
                break;
            case TRACE:
                ignores.addAll(authProperties.getIgnores()
                        .getTrace());
                break;
            case DELETE:
                ignores.addAll(authProperties.getIgnores()
                        .getDelete());
                break;
            case OPTIONS:
                ignores.addAll(authProperties.getIgnores()
                        .getOptions());
                break;
            default:
                break;
        }

        ignores.addAll(authProperties.getIgnores()
                .getPattern());
        if (!ignores.isEmpty()) {
            for (String ignore : ignores) {
                ExtensionPathMatcher extensionPathMatcher = new ExtensionPathMatcher(httpMethod, ignore);
                if (extensionPathMatcher.matches(request)) {
                    return true;
                }
            }
        }
        return false;
    }
}
