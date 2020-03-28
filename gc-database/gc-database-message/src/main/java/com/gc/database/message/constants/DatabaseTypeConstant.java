package com.gc.database.message.constants;

import com.gc.database.message.executor.DatabaseExecutor;
import com.gc.database.message.executor.MysqlDatabaseExecutor;
import com.gc.database.message.executor.OracleDatabaseExecutor;
import com.gc.database.message.executor.SqlServerDatabaseExecutor;
import lombok.Getter;

/**
 * 数据库类型
 * @author jackson
 */
@Getter
public enum DatabaseTypeConstant {
    /**
     * mysql
     */
    MYSQL("com.mysql.cj.jdbc.Driver", MysqlDatabaseExecutor.class),
    SQL_SERVER("com.microsoft.jdbc.SqlServer.SQLServerDriver", SqlServerDatabaseExecutor.class),
    ORACLE("Oracle.jdbc.driver.OracleDriver", OracleDatabaseExecutor.class);



    private String driverClass;

    private Class<? extends DatabaseExecutor> executerClass;

    DatabaseTypeConstant(String driverClass, Class<? extends DatabaseExecutor> executerClass) {
        this.driverClass = driverClass;
        this.executerClass = executerClass;
    }
}
