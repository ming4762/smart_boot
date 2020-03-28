package com.gc.starter.log.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gc.starter.crud.model.BaseModelCreateUserTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 日志实体类
 * @author jackson
 * 2020/1/22 2:19 下午
 */
@TableName("sys_log")
@Getter
@Setter
@Builder
public class SysLogPO extends BaseModelCreateUserTime {

    @TableField(exist = false)
    private static final long serialVersionUID = 7634433741019323407L;

    /**
     * 日志ID
     */
    @TableId(type = IdType.ASSIGN_ID)
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
    private Serializable params;

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

    private String type;

    private String platform;

    private String ident;
}
