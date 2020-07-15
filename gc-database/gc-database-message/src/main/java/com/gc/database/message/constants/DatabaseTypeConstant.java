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
    SQL_SERVER("com.microsoft.sqlserver.jdbc.SQLServerDriver", SqlServerDatabaseExecutor.class),
    ORACLE("oracle.jdbc.driver.OracleDriver", OracleDatabaseExecutor.class);



    private final String driverClass;

    private final Class<? extends DatabaseExecutor> executerClass;

    DatabaseTypeConstant(String driverClass, Class<? extends DatabaseExecutor> executerClass) {
        this.driverClass = driverClass;
        this.executerClass = executerClass;
    }
}
