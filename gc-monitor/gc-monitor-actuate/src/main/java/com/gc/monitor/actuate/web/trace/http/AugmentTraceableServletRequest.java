package com.gc.monitor.actuate.web.trace.http;

import org.apache.commons.io.IOUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author shizhongming
 * 2020/5/7 5:53 下午
 */
public class AugmentTraceableServletRequest implements AugmentTraceableRequest {

    private final HttpServletRequest request;

    private final String requestBody;

    public AugmentTraceableServletRequest(@NonNull HttpServletRequest request) throws IOException {
        this.request = request;
        this.requestBody = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return this.request.getParameterMap();
    }

    @Override
    public String getRequestBody() {
        return this.requestBody;
    }

    @Override
    public String getMethod() {
        return this.request.getMethod();
    }

    @Override
    public URI getUri() {
        String queryString = this.request.getQueryString();
        if (!StringUtils.hasText(queryString)) {
            return URI.create(this.request.getRequestURL().toString());
        }
        try {
            StringBuffer urlBuffer = appendQueryString(queryString);
            return new URI(urlBuffer.toString());
        }
        catch (URISyntaxException ex) {
            String encoded = UriUtils.encodeQuery(queryString, StandardCharsets.UTF_8);
            StringBuffer urlBuffer = appendQueryString(encoded);
            return URI.create(urlBuffer.toString());
        }
    }

    private StringBuffer appendQueryString(String queryString) {
        return this.request.getRequestURL().append("?").append(queryString);
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        return extractHeaders();
    }

    @Override
    public String getRemoteAddress() {
        return this.request.getRemoteAddr();
    }

    private Map<String, List<String>> extractHeaders() {
        Map<String, List<String>> headers = new LinkedHashMap<>();
        Enumeration<String> names = this.request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            headers.put(name, Collections.list(this.request.getHeaders(name)));
        }
        return headers;
    }
}
