package com.gc.monitor.actuate.web.trace.http;

import org.springframework.boot.actuate.trace.http.TraceableResponse;

/**
 * @author shizhongming
 * 2020/5/8 10:22 上午
 */
public interface AugmentTraceableResponse extends TraceableResponse {

    /**
     * 获取返回内容
     * @return 返回内容
     */
    String getResponseBody();

    /**
     * 获取编码格式
     * @return
     */
    String getCharacterEncoding();

    String getContentType();
}
