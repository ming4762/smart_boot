package com.gc.database.message.executor;

import com.gc.database.message.converter.DbJavaTypeConverter;
import org.springframework.stereotype.Component;

/**
 * @author shizhongming
 * 2020/1/19 8:18 下午
 */
public class OracleDatabaseExecutor extends AbstractDefaultDatabaseExecutor implements DatabaseExecutor {

    private final DbJavaTypeConverter dbJavaTypeConverter;

    public OracleDatabaseExecutor(DbJavaTypeConverter dbJavaTypeConverter) {
        super(dbJavaTypeConverter);
        this.dbJavaTypeConverter = dbJavaTypeConverter;
    }
}
