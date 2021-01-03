package com.gc.auth.extensions.jwt.filter;

import com.gc.auth.core.data.RestUserDetails;
import com.gc.auth.core.handler.AuthLoginSuccessHandler;
import com.gc.auth.core.model.LoginParameter;
import com.gc.auth.extensions.jwt.context.JwtContext;
import com.gc.auth.extensions.jwt.service.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * JWT登录拦截器
 * @author shizhongming
 * 2021/1/1 3:02 上午
 */
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * 默认的登录地址
     */
    private static final String LOGIN_URL = "/auth/login";

    private final JwtService jwtService;
    private final JwtContext jwtContext;

    public JwtLoginFilter(JwtContext jwtContext, JwtService jwtService) {
        super(getLoginUrl(jwtContext));
        this.jwtService = jwtService;
        this.jwtContext = jwtContext;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        // 创建请求参数
        final LoginParameter loginParameter = LoginParameter.create(httpServletRequest);
        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginParameter.getUsername(), loginParameter.getPassword());
        Authentication authentication = this.getAuthenticationManager().authenticate(authenticationToken);
        // 创建JWT
        String jwt = this.jwtService.createJwt(authentication, loginParameter);
        this.setAuthenticationSuccessHandler(new AuthLoginSuccessHandler(this.jwtContext.getAuthProperties()));
        // 设置token
        ((RestUserDetails)authentication.getPrincipal()).setToken(jwt);
        return authentication;
    }

    /**
     * 获取登录地址
     * @return 登录URL
     */
    public static String getLoginUrl(JwtContext context) {
        return Optional.of(context).map(JwtContext :: getLoginUrl).orElse(LOGIN_URL);
    }

}
