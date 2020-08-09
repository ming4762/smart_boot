package com.gc.database.message.executor;

import com.gc.database.message.converter.DbJavaTypeConverter;
import com.gc.database.message.pojo.bo.DatabaseConnectionBO;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author shizhongming
 * 2020/1/19 8:18 下午
 */
@Component
public class OracleDatabaseExecutor extends AbstractDefaultDatabaseExecutor implements DatabaseExecutor {

    private final DbJavaTypeConverter dbJavaTypeConverter;

    public OracleDatabaseExecutor(DbJavaTypeConverter dbJavaTypeConverter) {
        super(dbJavaTypeConverter);
        this.dbJavaTypeConverter = dbJavaTypeConverter;
    }
    @Override
    public String getUrl(@NonNull DatabaseConnectionBO databaseConnection) {
        return databaseConnection.getUrl();
    }
}
