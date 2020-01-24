package com.gc.database.message.pojo.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author jackson
 * 2020/1/21 9:32 下午
 */
@ToString
@Getter
@Setter
public abstract class AbstractDatabaseConnectionBaseBO implements Serializable {
    private static final long serialVersionUID = -4906557187983706599L;

    private String databaseName;

    private String type;

    private String url;

    private String username;

    private String password;

    private String tableSchema;
}
