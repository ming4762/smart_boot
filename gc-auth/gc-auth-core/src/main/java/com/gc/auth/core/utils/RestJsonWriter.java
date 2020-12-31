package com.gc.auth.core.utils;

import com.gc.common.base.message.Result;
import com.gc.common.base.utils.JsonUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author shizhongming
 * 2020/1/16 9:25 下午
 */
public class RestJsonWriter {

    private RestJsonWriter() {
        throw new IllegalStateException("Utility class");
    }

    public static void writeJson(HttpServletResponse response, Result<?> result) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JsonUtils.toJsonString(result));
    }
}
