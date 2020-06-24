package com.gc.monitor.actuate.web.trace.http;

import org.springframework.boot.actuate.trace.http.TraceableRequest;

import java.util.Map;

/**
 * trace 请求头
 * @author shizhongming
 * 2020/5/7 5:23 下午
 */
public interface AugmentTraceableRequest extends TraceableRequest {

    Map<String, String[]> getParameterMap();

    String getRequestBody();
}
