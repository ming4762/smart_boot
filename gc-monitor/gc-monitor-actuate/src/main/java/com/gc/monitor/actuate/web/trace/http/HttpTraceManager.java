package com.gc.monitor.actuate.web.trace.http;

import java.util.List;
import java.util.Map;

/**
 * {@link HttpTrace} 管理器
 * @author shizhongming
 * 2020/5/7 4:53 下午
 */
public interface HttpTraceManager {


    /**
     * 添加trace
     * @param trace 要添加的trace
     */
    void add(HttpTrace trace);

    /**
     * 查询trace
     * @param parameter
     * @return
     */
    List<HttpTrace> list(Map<String, Object> parameter);
}
