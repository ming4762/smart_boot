package com.gc.auth.security.handler;

import com.gc.common.base.message.Result;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 无权限执行器
 * @author shizhongming
 * 2020/1/16 9:18 下午
 */
@Component
public class RestAuthAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        RestJsonWriter.writeJson(response, Result.failure(HttpStatus.FORBIDDEN.value(), e.getMessage()));
    }
}
