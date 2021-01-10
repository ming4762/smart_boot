package com.gc.auth.extensions.jwt.filter;

import com.gc.auth.core.model.LoginParameter;
import com.gc.auth.extensions.jwt.context.JwtContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    public JwtLoginFilter(JwtContext jwtContext) {
        super(getLoginUrl(jwtContext));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        // 创建请求参数
        final LoginParameter loginParameter = LoginParameter.create(httpServletRequest);
        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginParameter.getUsername(), loginParameter.getPassword());
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }

    /**
     * 获取登录地址
     * @return 登录URL
     */
    public static String getLoginUrl(JwtContext context) {
        return Optional.of(context).map(JwtContext :: getLoginUrl).orElse(LOGIN_URL);
    }

}
