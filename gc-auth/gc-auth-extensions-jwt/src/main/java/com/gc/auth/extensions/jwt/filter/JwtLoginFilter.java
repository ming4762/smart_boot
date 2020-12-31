package com.gc.auth.extensions.jwt.filter;

import com.gc.auth.extensions.jwt.context.JwtContext;
import com.gc.auth.extensions.jwt.model.JwtLoginParameter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * JWT登录过滤器
 * @author ShiZhongMing
 * 2020/12/31 16:11
 * @since 1.0
 */
public class JwtLoginFilter extends OncePerRequestFilter {

    /**
     * 默认的登录地址
     */
    private static final String LOGIN_URL = "jwt/login";

    private final JwtContext jwtContext;

    public JwtLoginFilter(JwtContext jwtContext) {
        this.jwtContext = jwtContext;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) {
        // 创建请求参数
        final JwtLoginParameter loginParameter = this.createLoginParameter(request);
        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginParameter.getUsername(), loginParameter.getPassword());
        this.get
    }

    /**
     * 判断是否需要拦截
     * @param request 请求信息
     * @return boolean
     */
    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return request.getRequestURI().contains(this.getLoginUrl());
    }

    /**
     * 创建登录参数
     * @param request 请求
     * @return 登录参数
     */
    protected JwtLoginParameter createLoginParameter(@NonNull HttpServletRequest request) {
        boolean remember = false;
        String rememberStr = request.getParameter("remember");
        if (StringUtils.isNotBlank(rememberStr)) {
            remember = Boolean.parseBoolean(rememberStr);
        }
        return JwtLoginParameter.builder()
                .username(request.getParameter("username"))
                .password(request.getParameter("password"))
                .remember(remember)
                .build();
    }

    /**
     * 获取登录地址
     * @return 登录URL
     */
    private String getLoginUrl() {
        return Optional.of(jwtContext).map(JwtContext :: getLoginUrl).orElse(LOGIN_URL);
    }
}
