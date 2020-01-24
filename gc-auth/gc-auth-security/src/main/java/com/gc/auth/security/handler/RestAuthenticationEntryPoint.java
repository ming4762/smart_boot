package com.gc.auth.security.handler;

import com.gc.common.base.message.Result;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 未登录
 * @author shizhongming
 * 2020/1/16 9:22 下午
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        RestJsonWriter.writeJson(httpServletResponse, Result.failure(HttpStatus.UNAUTHORIZED.value(), "未登录，无权限访问"));
    }
}
