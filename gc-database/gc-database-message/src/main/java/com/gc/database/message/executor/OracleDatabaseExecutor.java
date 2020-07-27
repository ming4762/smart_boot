package com.gc.database.message.executor;

import com.gc.database.message.pojo.bo.DatabaseConnectionBO;
import org.springframework.lang.NonNull;

/**
 * @author shizhongming
 * 2020/1/19 8:18 下午
 */
public class OracleDatabaseExecutor extends AbstractDefaultDatabaseExecutor implements DatabaseExecutor {
    @Override
    public String getUrl(@NonNull DatabaseConnectionBO databaseConnection) {
        return databaseConnection.getUrl();
    }
}
