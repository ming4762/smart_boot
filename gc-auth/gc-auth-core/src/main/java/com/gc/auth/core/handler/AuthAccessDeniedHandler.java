package com.gc.auth.core.handler;

import com.gc.auth.core.utils.RestJsonWriter;
import com.gc.common.base.http.HttpStatus;
import com.gc.common.base.message.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 无权限执行器
 * @author shizhongming
 * 2020/1/16 9:18 下午
 */
public class AuthAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse response, AccessDeniedException e) throws IOException {
        RestJsonWriter.writeJson(response, Result.ofStatus(HttpStatus.FORBIDDEN));
    }
}
