package com.gc.starter.exception.notice.impl;

import com.gc.auth.core.data.RestUserDetails;
import com.gc.starter.exception.notice.ExceptionNotice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 控制台异常通知
 * @author shizhongming
 * 2020/11/15 12:31 上午
 */
@Slf4j
public class ConsoleExceptionNotice implements ExceptionNotice {
    @Override
    public void notice(@NonNull Exception e, RestUserDetails user, HttpServletRequest request) {
        log.info("系统发生异常\n异常信息：{}\n当前用户：{}", e.getMessage(), Optional.ofNullable(user).map(RestUserDetails::getRealname).orElse("未登录"));
        log.error("错误信息", e);
    }
}
