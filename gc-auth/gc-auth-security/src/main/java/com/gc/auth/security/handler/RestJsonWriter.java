package com.gc.auth.security.handler;

import com.alibaba.fastjson.JSON;
import com.gc.common.base.message.Result;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author shizhongming
 * 2020/1/16 9:25 下午
 */
public class RestJsonWriter {

    public static void writeJson(HttpServletResponse response, Result<?> result) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(result));
    }
}
