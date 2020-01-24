package com.gc.module.code.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gc.starter.crud.model.BaseModelUserTime;
import lombok.Getter;
import lombok.Setter;

/**
 * 数据库连接信息
 * @author jackson
 * 2020/1/21 9:34 下午
 */
@TableName("db_connection")
@Getter
@Setter
public class DatabaseConnectionPO extends BaseModelUserTime {
    private static final long serialVersionUID = 8049827317664194034L;

    private Long id;

    private String databaseName;

    private String type;

    private String url;

    private String username;

    private String password;

    @TableField("table_schema")
    private String tableSchema;
}
