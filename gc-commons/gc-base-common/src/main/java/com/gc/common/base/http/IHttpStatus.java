package com.gc.common.base.http;

/**
 * http 代码
 * @author jackson
 * 2020/2/15 7:17 下午
 */
public interface IHttpStatus {

    /**
     * 获取状态码
     * @return 状态码
     */
    Integer getCode();

    /**
     * 获取状态信息
     * @return 状态信息
     */
    String getMessage();
}
