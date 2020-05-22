package com.gc.auth.security.filter;

import com.gc.auth.security.handler.RestJsonWriter;
import com.gc.auth.security.service.AuthService;
import com.gc.auth.security.service.JwtUtil;
import com.gc.common.auth.exception.AuthException;
import com.gc.common.auth.model.RestUserDetailsImpl;
import com.gc.common.auth.properties.AuthProperties;
import com.gc.common.base.http.HttpStatus;
import com.gc.common.base.message.Result;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
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
 * @author jackson
 * 2020/2/15 10:58 上午
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final AuthService authService;

    private final AuthProperties authProperties;

    public JwtAuthenticationFilter(AuthService authService, AuthProperties authProperties) {
        this.authService = authService;
        this.authProperties = authProperties;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (Objects.equals(this.authProperties.getDevelopment(), Boolean.TRUE) || checkIgnores(request)) {
            filterChain.doFilter(request, response);
        } else {
            try {
                String jwt = JwtUtil.getJwt(request);
                if (StringUtils.isNotEmpty(jwt)) {
                    // 刷新jwt的过期时间
                    RestUserDetailsImpl user = this.authService.refreshJwt(jwt);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                    filterChain.doFilter(request, response);
                } else {
                    RestJsonWriter.writeJson(response, Result.failure(HttpStatus.UNAUTHORIZED.getCode(), "未登录"));
                }
            } catch (AuthException e) {
                log.error(e.getMessage(), e.getE());
                RestJsonWriter.writeJson(response, Result.failure(e.getCode(), e.getMessage()));
            } catch (InternalAuthenticationServiceException e) {
                log.error(e.getMessage(), e);
                RestJsonWriter.writeJson(response, Result.failure(HttpStatus.UNAUTHORIZED.getCode(), e.getMessage()));
            }

        }
    }

    /**
     * 请求是否不需要进行权限拦截
     *
     * @param request 当前请求
     * @return true - 忽略，false - 不忽略
     */
    private boolean checkIgnores(HttpServletRequest request) {
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
                AntPathRequestMatcher matcher = new AntPathRequestMatcher(ignore, method);
                if (matcher.matches(request)) {
                    return true;
                }
            }
        }

        return false;
    }
}
