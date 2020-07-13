package com.gc.auth.security.handler;

import com.gc.common.base.message.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证失败处理类
 * @author jackson
 * 2020/1/23 7:51 下午
 */
@Slf4j
@Component
public class RestAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error("登录时发生错误: {}", exception.getMessage());
        if (exception instanceof InternalAuthenticationServiceException) {
            RestJsonWriter.writeJson(response, Result.failure(HttpStatus.UNAUTHORIZED.value(), String.format("登录失败：%s", exception.getMessage())));
        } else if (exception instanceof BadCredentialsException) {
            RestJsonWriter.writeJson(response, Result.failure(HttpStatus.UNAUTHORIZED.value(), exception.getMessage()));
        } else {
            RestJsonWriter.writeJson(response, Result.failure(HttpStatus.INTERNAL_SERVER_ERROR.value(), "登录时发生未知错误"));
        }

    }
}
