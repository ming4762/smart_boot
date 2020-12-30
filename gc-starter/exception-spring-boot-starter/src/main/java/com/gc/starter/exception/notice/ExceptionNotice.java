package com.gc.starter.exception.notice;


import com.gc.auth.core.data.RestUserDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * 异常通知接口
 * @author shizhongming
 * 2020/11/15 12:08 上午
 */
public interface ExceptionNotice {

    /**
     * 异常通知
     * @param e 异常信息
     * @param user 用户信息
     * @param request 请求信息
     */
    void notice(Exception e, RestUserDetails user, HttpServletRequest request);
}
