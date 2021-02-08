package com.gc.starter.log.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 日志实体类
 * @author jackson
 * 2020/1/22 2:19 下午
 */
@Getter
@Setter
@Builder
public class SysLogPO {

    private static final long serialVersionUID = 7634433741019323407L;

    /**
     * 日志ID
     */
    private Long logId;

    /**
     * 操作
     */
    private String operation;

    /**
     * 用时
     */
    private Long useTime;

    /**
     * 方法
     */
    private String method;

    /**
     * 参数
     */
    private String params;

    /**
     * IP
     */
    private String ip;

    /**
     * 请求路径
     */
    private String requestPath;

    /**
     * 状态码
     */
    private int statusCode;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 返回值
     */
    private String result;

    private String type;

    private String platform;

    private String ident;
}
