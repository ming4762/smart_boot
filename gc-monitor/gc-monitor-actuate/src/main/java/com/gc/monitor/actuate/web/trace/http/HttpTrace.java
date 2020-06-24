package com.gc.monitor.actuate.web.trace.http;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * 存储http 请求trace信息
 * @author shizhongming
 * 2020/5/7 4:53 下午
 */

public class HttpTrace {

    @Getter
    @Setter
    private AugmentTraceableRequest request;

    @Getter
    @Setter
    private AugmentTraceableResponse response;

    @Getter
    private final Instant timestamp;

    @Getter @Setter
    private Long userTime;

    public HttpTrace() {
        this.timestamp = Instant.now();
    }
}
