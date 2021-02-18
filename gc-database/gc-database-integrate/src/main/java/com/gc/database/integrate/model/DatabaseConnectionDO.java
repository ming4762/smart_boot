package com.gc.database.integrate.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gc.database.message.constants.DatabaseTypeConstant;
import com.gc.starter.crud.model.BaseModelUserTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author shizhongming
 * 2020/10/30 9:41 下午
 */
@ToString
@Getter
@Setter
@TableName("db_connection")
public class DatabaseConnectionDO extends BaseModelUserTime {
    private static final long serialVersionUID = -1126730714386529408L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String project;

    private String connectionName;

    private String databaseName;

    @TableField("database_type")
    private DatabaseTypeConstant type;

    private String url;

    private String username;

    private String password;

    private String tableSchema;

}
