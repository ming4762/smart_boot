package com.gc.starter.exception.handler;

import com.gc.common.auth.core.RestUserDetails;
import com.gc.starter.exception.notice.ExceptionNotice;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 异步通知
 * @author shizhongming
 * 2020/11/15 12:21 上午
 */
@Async
public class AsyncNoticeHandler {

    /**
     * spring 上下文
     */
    private final ApplicationContext applicationContext;

    public AsyncNoticeHandler(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 进行异常通知
     * @param e 异常信息
     * @param user 用户信息
     * @param request 请求信息
     */
    public void noticeException(Exception e, RestUserDetails user, HttpServletRequest request) {
        // 获取所有通知器
        Map<String, ExceptionNotice> noticeMap = applicationContext.getBeansOfType(ExceptionNotice.class);
        // 执行通知
        noticeMap.forEach((key, notice) -> {
            notice.notice(e, user, request);
        });
    }
}
