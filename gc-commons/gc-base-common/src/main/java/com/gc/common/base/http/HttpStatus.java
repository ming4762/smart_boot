package com.gc.common.base.http;

import com.gc.common.base.message.I18nMessage;
import lombok.Getter;

/**
 * @author jackson
 * 2020/2/15 7:18 下午
 */
@Getter
public enum HttpStatus implements IHttpStatus, I18nMessage {

    /**
     * 操作成功
     */
    OK(200, "common.http.status.OK", "成功","OK"),

    /**
     * {@code 500 Internal Server Error}.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.6.1">HTTP/1.1: Semantics and Content, section 6.6.1</a>
     */
    INTERNAL_SERVER_ERROR(500, "common.http.status.INTERNAL_SERVER_ERROR", "操作异常！", "Internal Server Error"),

    /**
     * 请求异常！
     */
    BAD_REQUEST(400, "common.http.status.BAD_REQUEST",  "请求异常！", "bad request"),

    /**
     * 参数不匹配！
     */
    PARAM_NOT_MATCH(400, "common.http.status.PARAM_NOT_MATCH",  "参数不匹配！", "parameter not match"),

    UNSUPPORTED_MEDIA_TYPE(415, "common.http.status.UNSUPPORTED_MEDIA_TYPE",  "不支持的媒体类型", "Unsupported Media Type"),

    /**
     * 参数不能为空！
     */
    PARAM_NOT_NULL(400, "common.http.status.PARAM_NOT_NULL",  "参数不能为空！", "parameter not null"),

    /**
     * {@code 401 Unauthorized}.
     * @see <a href="https://tools.ietf.org/html/rfc7235#section-3.1">HTTP/1.1: Authentication, section 3.1</a>
     */
    UNAUTHORIZED(401, "common.http.status.UNAUTHORIZED",  "未登录", "Unauthorized"),

    /**
     * {@code 403 Forbidden}.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.3">HTTP/1.1: Semantics and Content, section 6.5.3</a>
     */
    FORBIDDEN(403, "common.http.status.FORBIDDEN",  "权限不足","Forbidden"),

    /**
     * {@code 404 Not Found}.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.4">HTTP/1.1: Semantics and Content, section 6.5.4</a>
     */
    NOT_FOUND(404, "common.http.status.NOT_FOUND",  "请求不存在", "Not Found"),

    /**
     * {@code 405 Method Not Allowed}.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.5">HTTP/1.1: Semantics and Content, section 6.5.5</a>
     */
    METHOD_NOT_ALLOWED(405, "common.http.status.METHOD_NOT_ALLOWED",  "请求方式不支持", "Method Not Allowed");



    /**
     * 状态码
     */
    private final Integer code;

    private final String zhCn;

    private final String en;

    private final String i18nCode;



    HttpStatus(Integer code, String i18nCode, String zhCn, String en) {
        this.code = code;
        this.zhCn = zhCn;
        this.en = en;
        this.i18nCode = i18nCode;
    }

    @Override
    public String getMessage() {
        return this.zhCn;
    }
}
