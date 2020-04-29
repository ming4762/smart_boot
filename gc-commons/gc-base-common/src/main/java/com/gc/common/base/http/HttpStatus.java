package com.gc.common.base.http;

import lombok.Getter;

/**
 * @author jackson
 * 2020/2/15 7:18 下午
 */
@Getter
public enum HttpStatus implements IHttpStatus {

    /**
     * 操作成功
     */
    OK(200, "成功","OK"),

    /**
     * {@code 500 Internal Server Error}.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.6.1">HTTP/1.1: Semantics and Content, section 6.6.1</a>
     */
    INTERNAL_SERVER_ERROR(500,"操作异常！", "Internal Server Error"),

    /**
     * 请求异常！
     */
    BAD_REQUEST(400, "请求异常！", "bad request"),

    /**
     * 参数不匹配！
     */
    PARAM_NOT_MATCH(400, "参数不匹配！", "parameter not match"),

    /**
     * 参数不能为空！
     */
    PARAM_NOT_NULL(400, "参数不能为空！", "parameter not null"),

    /**
     * {@code 401 Unauthorized}.
     * @see <a href="https://tools.ietf.org/html/rfc7235#section-3.1">HTTP/1.1: Authentication, section 3.1</a>
     */
    UNAUTHORIZED(401, "未登录", "Unauthorized"),

    /**
     * {@code 403 Forbidden}.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.3">HTTP/1.1: Semantics and Content, section 6.5.3</a>
     */
    FORBIDDEN(403, "权限不足","Forbidden"),

    /**
     * {@code 404 Not Found}.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.4">HTTP/1.1: Semantics and Content, section 6.5.4</a>
     */
    NOT_FOUND(404, "请求不存在", "Not Found"),

    /**
     * {@code 405 Method Not Allowed}.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.5">HTTP/1.1: Semantics and Content, section 6.5.5</a>
     */
    METHOD_NOT_ALLOWED(405, "请求方式不支持", "Method Not Allowed");



    /**
     * 状态码
     */
    private final Integer code;

    private final String zhCn;

    private final String en;



    HttpStatus(Integer code, String zhCn, String en) {
        this.code = code;
        this.zhCn = zhCn;
        this.en = en;
    }

    @Override
    public String getMessage() {
        return this.zhCn;
    }
}
