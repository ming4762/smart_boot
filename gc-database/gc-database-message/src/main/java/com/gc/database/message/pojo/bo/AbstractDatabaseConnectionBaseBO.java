package com.gc.database.message.pojo.bo;

import com.gc.database.message.constants.DatabaseTypeConstant;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public abstract class AbstractDatabaseConnectionBaseBO implements Serializable {
    private static final long serialVersionUID = -4906557187983706599L;

    private String databaseName;

    private DatabaseTypeConstant type;

    private String url;

    private String username;

    private String password;

    private String tableSchema;
}
