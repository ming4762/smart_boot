package com.gc.monitor.actuate.web.trace.http;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author shizhongming
 * 2020/5/8 10:44 上午
 */
public class AugmentTraceableServletResponse implements AugmentTraceableResponse {

    private final HttpServletResponse response;

    private final String responseBody;

    public AugmentTraceableServletResponse(HttpServletResponse response) {
        this.response = response;
        this.responseBody = this.convertResponseBody();
    }

    @Override
    public String getResponseBody() {
        return this.responseBody;
    }

    @Override
    public String getCharacterEncoding() {
        return this.response.getCharacterEncoding();
    }

    @Override
    public String getContentType() {
        return this.response.getContentType();
    }

    @Override
    public int getStatus() {
        return this.response.getStatus();
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        return this.extractHeaders();
    }

    /**
     * 获取请求体
     * @return
     */
    @SneakyThrows
    private String convertResponseBody() {
        String responseBody = "";
        final ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (!Objects.isNull(wrapper)) {
            responseBody = IOUtils.toString(wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
        }
        return responseBody;
    }

    /**
     * 获取请求头信息
     * @return
     */
    private Map<String, List<String>> extractHeaders() {
        final Map<String, List<String>> headers = Maps.newHashMap();
        this.response.getHeaderNames().forEach(name -> {
            headers.put(name, Lists.newArrayList(this.response.getHeaders(name)));
        });
        return headers;
    }
}
