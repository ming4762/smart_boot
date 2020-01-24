package com.gc.database.message.constants;

import com.gc.database.message.executor.DatabaseExecutor;
import com.gc.database.message.executor.MysqlDatabaseExecutor;
import com.gc.database.message.executor.OracleDatabaseExecutor;
import com.gc.database.message.executor.SqlServerDatabaseExecutor;
import lombok.Getter;

/**
 * 数据库类型
 */
@Getter
public enum DatabaseTypeConstant {

    Mysql("com.mysql.cj.jdbc.Driver", MysqlDatabaseExecutor.class),
    SqlServer("com.microsoft.jdbc.SqlServer.SQLServerDriver", SqlServerDatabaseExecutor.class),
    Oracle("Oracle.jdbc.driver.OracleDriver", OracleDatabaseExecutor.class);



    private String driverClass;

    private Class<? extends DatabaseExecutor> executerClass;

    DatabaseTypeConstant(String driverClass, Class<? extends DatabaseExecutor> executerClass) {
        this.driverClass = driverClass;
        this.executerClass = executerClass;
    };
}
