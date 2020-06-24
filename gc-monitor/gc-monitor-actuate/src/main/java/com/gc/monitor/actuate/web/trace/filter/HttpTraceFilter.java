package com.gc.monitor.actuate.web.trace.filter;

import com.gc.monitor.actuate.web.trace.http.AugmentTraceableServletRequest;
import com.gc.monitor.actuate.web.trace.http.AugmentTraceableServletResponse;
import com.gc.monitor.actuate.web.trace.http.HttpTrace;
import com.gc.monitor.actuate.web.trace.http.HttpTraceManager;
import lombok.Setter;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author shizhongming
 * 2020/5/7 4:47 下午
 */
public class HttpTraceFilter extends OncePerRequestFilter implements Ordered {

    @Setter
    private int order = Ordered.LOWEST_PRECEDENCE - 10;

    private final HttpTraceManager traceManager;

    /**
     * 构造方法，初始化
     */
    public HttpTraceFilter(HttpTraceManager traceManager) {
        this.traceManager = traceManager;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        if (!isRequestValid(request)) {
            filterChain.doFilter(request, response);
        } else {
            // 转换请求头、请求体，防止数据流数据读取后丢失
            if (!(request instanceof ContentCachingRequestWrapper)) {
                request = new ContentCachingRequestWrapper(request);
            }
            if (!(response instanceof ContentCachingResponseWrapper)) {
                response = new ContentCachingResponseWrapper(response);
            }
            // 创建请求头
            AugmentTraceableServletRequest traceableServletRequest = new AugmentTraceableServletRequest(request);
            HttpTrace httpTrace = new HttpTrace();
            httpTrace.setRequest(traceableServletRequest);

            long startTime = System.currentTimeMillis();
            try {
                filterChain.doFilter(request, response);
            } finally {
                AugmentTraceableServletResponse augmentTraceableServletResponse = new AugmentTraceableServletResponse(response);
                httpTrace.setResponse(augmentTraceableServletResponse);
                httpTrace.setUserTime(System.currentTimeMillis() - startTime);
                this.traceManager.add(httpTrace);
            }
        }
    }

    private boolean isRequestValid(HttpServletRequest request) {
        try {
            new URI(request.getRequestURL().toString());
            return true;
        }
        catch (URISyntaxException ex) {
            return false;
        }
    }
}
